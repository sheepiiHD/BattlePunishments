package com.lducks.battlepunishments.battleplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.chat.ChatEditor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.CoordsCon;
import com.lducks.battlepunishments.util.TimeConverter;
import com.lducks.battlepunishments.util.webrequests.UpdateDatabase;

/**
 * 
 * @author lDucks
 * @see BattlePlayer.class
 *
 */

public class FileBattlePlayer implements BattlePlayer {
	private YamlConfiguration config;
	private File f;
	private String name;
	private String path = "plugins/BattlePunishments/players";

	public FileBattlePlayer(String n, boolean create){
		n = n.toLowerCase();
		config = new YamlConfiguration();
		f = new File(path+"/"+n.charAt(0)+"/"+n+".yml");

		try {
			config.load(f);
		} catch (Exception e){
			if(getPlayer() != null)
				create(n);
			else {
				try {
					if(create)
						create(n);
				}catch(Exception e1) {
					new DumpFile("FileBattlePlayer", e1, "Error creating FileBattlePlayer.");
				}
			}
		}

		name = n;
		setRealName();
	}

	public FileBattlePlayer(String n) throws Exception {
		n = n.toLowerCase();

		config = new YamlConfiguration();
		File newf = new File(path+"/"+n.charAt(0)+"/"+n+".yml");

		try {
			config.load(newf);
		} catch (Exception e){
			if(getPlayer() != null)
				create(n);
			else {
				new ConsoleMessage("Got "+n);

				try {
					newf = new File(path+"/"+n.charAt(0)+"/"+n+".yml");
					config.load(newf);
				}catch(Exception e1) {
					throw new Exception ("Player not found");
				}
			}
		}

		f = newf;
		name = n;
		setRealName();
	}

	public String findPlayer(String n) {
		for(File ff : new File (path+"/"+n.charAt(0)).listFiles()) {
			if(ff.exists()) {
				String nn = ff.getName().replace(".yml", "");
				nn = nn.toLowerCase();
				if(nn.startsWith(n)) {
					new ConsoleMessage(nn);
					return nn;
				}
			}
		}

		return null;
	}

	public void create(String n) {
		if(!f.exists()){
			if(!new File(path+"/"+n.charAt(0)).exists())
				new File(path+"/"+n.charAt(0)).mkdirs();

			try{
				f.createNewFile();
			}catch(Exception e){
				new DumpFile("creating"+f.getName(), e, "Error creating player "+getName());
			}

			try {
				config.load(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			new ConsoleMessage("[BattlePunishments] Created player "+n);
		}
	}

	public void save(){
		try{
			config.save(getConfig());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void load(){
		try{
			config.load(getConfig());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public File getConfig() {
		return f;
	}

	public String getName() {
		return name;
	}

	public void setRealName() {
		if(getPlayerExact() != null) {
			new ConsoleMessage(getPlayerExact().getName());
			config.set("casename", getPlayerExact().getName());
		}
		save();
	}

	public Player getPlayerExact() {
		return Bukkit.getPlayerExact(name);
	}

	public String getRealName() {
		if(!config.contains("casename"))
			return name;

		return config.getString("casename");
	}

	public Player getPlayer() {
		try {
			return Bukkit.getPlayer(name);
		}catch(Exception e) {
			return null;
		}
	}

	public void kickPlayer(String reason) {
		if(getPlayer() != null) {
			getPlayer().kickPlayer(reason);
		}
	}

	public void setLastSeen(String s) {
		config.set("lastseen", s);
		save();
	}

	public String getLastSeen() {
		return config.getString("lastseen");
	}

	public void setFirstSeen(String s) {
		config.set("firstseen", s);
		save();
	}

	public String getFirstSeen() {
		return config.getString("firstseen");
	}

	public void setLogoutCoords(Location l) {
		config.set("logoutlocation", CoordsCon.getLocString(l));
		save();
	}

	public Location getLogoutCoords() {
		return CoordsCon.getLocation(config.getString("logoutlocation"));
	}

	public void deletePlayer() {
		f.delete();

		if(getPlayer() != null) {
			getPlayer().setDisplayName(ChatColor.RESET + getPlayer().getName());

			if(BattleSettings.changedTabName())
				getPlayer().setPlayerListName(getPlayer().getName());
		}
	}

	public String getDisplayName() {
		if(getNickname() != null)
			return getNickname();

		return getRealName();
	}

	public void sendMessage(String string) {
		if(getPlayer() != null)
			getPlayer().sendMessage(string);
	}

	public boolean contains(String string) {
		return config.contains(string);
	}

	public void removeField(String string) {
		if(contains(string)) {
			config.set(string, null);	
			save();		}
	}

	public boolean exists() {
		return getConfig().exists();
	}

	public void ban(String reason, long time, String string, boolean ipbanned){
		config.set("ban.reason", reason);
		config.set("ban.time", time);
		config.set("ban.by", string);
		config.set("ban.timeofban", TimeConverter.convertLongToDate(System.currentTimeMillis()));

		if(ipbanned)
			IPBan();

		if(BattleSettings.wlRemoveOnBan() && isOnWatchList()){
			removePlayerFromWatchList();
		}

		save();
	}

	public void unban(){
		config.set("ban", null);
		config.set("ipbanned", null);
		save();
	}

	public boolean isBanned(){
		if(config.contains("ban")){
			if(getBanTime() < System.currentTimeMillis() && getBanTime() != -1){
				unban();
				save();
			}
		}

		if(config.contains("ban"))
			return true;

		return false;
	}

	public String getBanReason(){
		String reason = config.getString("ban.reason");
		return reason;
	}

	public long getBanTime(){
		return config.getLong("ban.time");
	}

	public String getBanner(){
		String t = config.getString("ban.by");
		return t;
	}

	public long getTimeOfBan() {
		return config.getLong("ban.timeofban");
	}

	public void blockCommand(String command){
		List<String> l = getBlockList();
		l.add(command.replace("/", "").toLowerCase());

		config.set("blockedcommands", l);

		save();
	}

	public void unblockCommand(String command){
		List<String> l = getBlockList();
		l.remove(command.replace("/", ""));

		if(l.size() > 0)
			config.set("blockedcommands", l);
		else
			config.set("blockedcommands", null);

		save();
	}

	public boolean isBlocked(String command){
		List<String> l = getBlockList();

		if(l == null)
			return false;

		if(l.contains(command.replace("/", "")))
			return true;

		return false;
	}

	public List<String> getBlockList() {
		if(!config.contains("blockedcommands"))
			return new ArrayList<String>();

		return config.getStringList("blockedcommands");
	}

	public void clearBlockList() {
		if(config.contains("blockedcommands")){
			config.set("blockedcommands", null);

			save();
		}
	}

	public void addPlayerToWatchList() {
		config.set("watchlist", true);
		BattlePunishments.getWatchList().add(getRealName());

		save();
	}

	public void removePlayerFromWatchList() {
		config.set("watchlist", false);
		BattlePunishments.getWatchList().remove(getRealName());
		save();
	}

	public boolean isOnWatchList() {
		if(!config.contains("watchlist"))
			config.set("watchlist", false);

		return config.getBoolean("watchlist");
	}

	public void addIP(String ip){
		ip = ip.replace(".", "-").replace("/", "");
		
		if(ip == "127.0.0.1") return;

		List<String> ips = new ArrayList<String>();

		if(config.contains("ips"))
			ips = getIPList();

		if(ips.contains(ip))
			return;

		ips.add(ip);

		config.set("ips", ips);

		save();

		for(String aip : ips)
			new ConsoleMessage("Adding IP "+aip+" to player "+getRealName());
		
		try {
			UpdateDatabase.updateIP(BattlePunishments.createBattlePlayer(name), ip);
		} catch (Exception e) {
			new DumpFile("addIP", e, "Error adding IP to website via flatfile");
		}
	}

	public List<String> getIPList(){
		return config.getStringList("ips");
	}

	@SuppressWarnings("unused")
	private void remove(String ip) {
		List<String> l = config.getStringList("ips");

		if(l.contains(ip)) {
			l.remove(ip);
			new ConsoleMessage("Removing IP: "+ip+" from Player: "+getRealName()+" for Reason: Dupe IP");
		}

		config.set("ips", ip);

		save();
	}

	public void clearIPs() {
		config.set("ips", null);

		save();
	}

	public void IPBan() {
		config.set("ipbanned", true);
		save();
	}

	public void IPunban() {
		config.set("ipbanned", false);
		save();
	}

	public boolean isIPBanned() {
		return config.getBoolean("ipbanned");
	}

	public String getMuter(){
		String t = config.getString("mute.by");
		return t;
	}

	public String getMuteReason(){
		String reason = config.getString("mute.reason");
		return reason;
	}

	public long getMuteTime(){
		long t = config.getLong("mute.time");
		return t;
	}

	public void mute(String reason, long time, String who){

		if(getPlayer() != null && getPlayer().hasPermission(BattlePerms.MUTE))
			return;

		config.set("mute.reason", reason);
		config.set("mute.time", time);
		config.set("mute.by", who);
		config.set("mute.timeofmute", TimeConverter.convertLongToDate(System.currentTimeMillis()));

		save();
	}

	public void unmute(){
		config.set("mute", null);
		save();
	}

	public boolean isMuted(){

		boolean t = false;

		if(config.contains("mute")){

			t = true;

			if(getMuteTime() < System.currentTimeMillis() && getMuteTime() != -1){
				unmute();

				save();
			}

			if(config.contains("mute")){
				t = true;
			}else{
				t = false;
			}
		}

		return t;
	}

	public long getTimeOfMute() {
		return config.getLong("mute.timeofmute");
	}

	public void setNickname(String n){
		n = ChatEditor.colorChat(n);
		config.set("nickname", n);

		save();
	}

	public String getNickname() {
		return config.getString("nickname");
	}

	public void editStrikes(int strikes){
		if(!BattleSettings.useStrikes())
			return;
		
		int s = config.getInt("strikes");

		long laststrike = config.getInt("laststrike");


		try {
			if(BattleSettings.getCooldownTime() != "-1") {
				laststrike = TimeConverter.convertToLong(laststrike, BattleSettings.getCooldownTime());
				if(laststrike <= System.currentTimeMillis()) {
					s = s - BattleSettings.getCooldownDrop();
				}
			}
		} catch (Exception e) {
			new DumpFile("editStrikes", e, "Error converting laststrike to long");
			return;
		}

		s = strikes + s;

		if(s < 0)
			s = 0;

		if(s > BattleSettings.getStrikesCap() && BattleSettings.getStrikesAutoban()){
			ban("You have too many strikes!", -1, "Server", false);
		}

		if(s > BattleSettings.getStrikesMax()) s = BattleSettings.getStrikesMax();

		if(s > 0)
			config.set("strikes", s);
		else
			config.set("strikes", null);

		save();
	}

	public int getStrikes(){
		if(!config.contains("strikes"))
			return 0;

		long laststrike = config.getInt("laststrike");

		try {
			laststrike = TimeConverter.convertToLong(laststrike, BattleSettings.getCooldownTime());
		} catch (Exception e) {
			new DumpFile("editStrikes", e, "Error converting laststrike to long");
			return 0;
		}

		if(laststrike <= System.currentTimeMillis()) {
			config.set("strikes", config.getInt("strikes") - BattleSettings.getCooldownDrop());
			save();
		}

		return config.getInt("strikes");
	}

	@Deprecated
	public boolean hasPlayedBefore() {
		return false;
	}
}
