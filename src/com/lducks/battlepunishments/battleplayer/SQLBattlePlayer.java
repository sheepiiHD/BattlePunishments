package com.lducks.battlepunishments.battleplayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.sql.SQLInstance;
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

public class SQLBattlePlayer implements BattlePlayer {
	private String name;
	SQLInstance sql = null;
	private BattleSQLConfiguration config = null;

	public SQLBattlePlayer(String n, SQLInstance sql) throws Exception {
		this.sql = sql;
		config = new BattleSQLConfiguration(sql);

		name = n.toLowerCase();
		String closename = config.getShortHandName(name);

		if(closename != null && getPlayer() == null)
			name = closename;

		setRealName();
		name = getRealName().toLowerCase();
	}

	public String getName() {
		return name;
	}

	public void setRealName() {
		if(getPlayer() != null)
			config.setRealName(name, getPlayer().getName());
	}

	public String getRealName() {
		if(config.getRealName(name) != null)
			return config.getRealName(name);

		return name;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}

	public void kickPlayer(String r) {
		if(getPlayer() != null) {
			getPlayer().kickPlayer(r);
		}
	}

	public void setTimes() {

		if(getPlayer() == null)
			return;

		config.setTimes(name, TimeConverter.convertToString(getPlayer().getFirstPlayed()),
				TimeConverter.convertToString(getPlayer().getLastPlayed()), 
				getPlayer().getLocation());
	}

	public String getLastSeen() {
		return config.getLastSeen(name);
	}

	public String getFirstSeen() {
		try {
			return config.getFirstSeen(name);
		}catch(Exception e) {return null;}
	}

	public void setLogoutCoords(Location l) {
		setTimes();
	}

	public Location getLogoutCoords() {
		String loc = config.getLogoutLocation(name);
		return CoordsCon.getLocation(loc);
	}

	public void deletePlayer() {
		//do nothing
	}

	public String getDisplayName() {
		if(config.getNickname(name) != null)
			return config.getNickname(name);

		if(config.getRealName(name) != null)
			return config.getRealName(name);

		return name;
	}

	public void sendMessage(String string) {
		if(getPlayer() != null)
			getPlayer().sendMessage(string);
	}

	public boolean contains(String string) {
		return true;
	}

	public void removeField(String string) {
		//do nothing
	}

	public boolean exists() {
		if(config.getRealName(name) == null)
			return false;

		return true;
	}

	public void mute(String r, long i, String muter) {
		config.mute(name, muter, r, i);
	}

	public boolean isBanned() {
		return config.isBanned(name);
	}

	public List<String> getIPList() {
		return config.getIPList(name);
	}

	public boolean isMuted() {
		return config.isMuted(name);
	}

	public long getMuteTime() {
		return config.getUnmuteTime(name);
	}

	public String getMuteReason() {
		return config.getMuteReason(name);
	}

	public void setNickname(String n) {
		config.changeNickName(name, n);
	}

	public String getNickname() {
		return config.getNickname(name);
	}

	public int getStrikes() {

		int s = config.getStrikes(name);

		try {
			long laststrike = Long.parseLong(config.getLastStrike(name));
			if(BattleSettings.getCooldownTime() != "-1") {
				laststrike = TimeConverter.convertToLong(laststrike, BattleSettings.getCooldownTime());
				if(laststrike <= System.currentTimeMillis()) {
					s = s - BattleSettings.getCooldownDrop();
				}
			}
		} catch (Exception e) {
			new DumpFile("editStrikes", e, "Error converting laststrike to long");
		}

		return s;
	}

	public List<String> getBlockList() {
		return config.getBlockedCommandsList(name);
	}

	public String getMuter() {
		return config.getMuter(name);
	}

	public String getBanner() {
		return config.getBanner(name);
	}

	public String getBanReason() {
		return config.getBanReason(name);
	}

	public long getBanTime() {
		return config.getUnbanTime(name);
	}

	public String getTimeOfMute() {
		return config.getTimeOfMute(name);
	}

	public String getTimeOfBan() {
		return config.getTimeOfBan(name);
	}

	public void editStrikes(int s) {
		int strikes = config.getStrikes(name);
		long laststrike;

		try {
			laststrike = Long.parseLong(config.getLastStrike(name));
		}catch (Exception e) {
			new DumpFile("editStrikes",e,"Error parsing laststrike to long");
			return;
		}

		if(laststrike <= System.currentTimeMillis()) {
			s = s - BattleSettings.getCooldownDrop();
		}

		strikes = strikes + s;
		config.setStrikes(name, strikes);
	}

	public boolean isOnWatchList() {
		return config.isOnWatchList(name);
	}

	public void addPlayerToWatchList() {
		List<String> wl = BattlePunishments.getWatchList();
		if(!wl.contains("name")) {
			BattlePunishments.getWatchList().remove(getRealName());
			config.addPlayerToWatchList(name);
		}
	}

	public void removePlayerFromWatchList() {
		List<String> wl = BattlePunishments.getWatchList();

		new ConsoleMessage("Contains    ====   "+name);

		if(wl.contains(name)) {

			new ConsoleMessage("YES IT DOES CONTAIN              "+name);

			wl.remove(name);
			BattlePunishments.setWatchList(wl);
			config.removeFromWatchList(name);
		}
	}

	public void ban(String r, long time, String banner, boolean ipbanned) {
		config.ban(name, r, time, banner, ipbanned);
		Bukkit.getOfflinePlayer(name).setBanned(true);
	}

	public void unmute() {
		config.unmute(name);
	}

	public void unban() {
		config.unban(name);
		Bukkit.getOfflinePlayer(name).setBanned(false);
	}

	public void clearBlockList() {
		config.clearBlockedCommands(name);
	}

	public boolean isBlocked(String command) {
		return config.isBlockedCommand(name, command);
	}

	public void blockCommand(String command) {
		config.blockCommand(name, command);
	}

	public void unblockCommand(String command) {
		config.unblockCommand(name, command);
	}

	public void addIP(String ip) {
		if(ip == "127.0.0.1") return;

		if(!getIPList().contains(ip))
			config.addIP(name, ip);
		
		try {
			UpdateDatabase.updateIP(BattlePunishments.createBattlePlayer(name), ip);
		} catch (Exception e) {
			new DumpFile("addIP", e, "Error adding IP to website via flatfile");
		}
	}

	public void clearIPs() {
		config.clearIPs(name);
	}

	public void setLastSeen(String s) {
		setTimes();
	}

	public void setFirstSeen(String s) {
		setTimes();
	}

	@Override
	public boolean isIPBanned() {
		return config.isIPBanned(name);
	}

	@Deprecated
	public boolean hasPlayedBefore() {
		return false;
	}
}
