package com.lducks.battlepunishments.listeners;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.chat.ChatEditor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.TimeConverter;
import com.lducks.battlepunishments.util.webrequests.ConnectionCode;
import com.lducks.battlepunishments.util.webrequests.PluginUpdater;

/**
 * 
 * @author lDucks
 *
 */

public class LoginListener implements Listener{

	public static boolean checkvalid = false;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		if(p.isOp()) {
			if(BattleSettings.autoUpdate() && PluginUpdater.hasPluginUpdates(BattlePunishments.getPlugin()) != null) {
				p.sendMessage(RED + "It seems you are using an outdated version of BattlePunishments " +
						"(v"+BattlePunishments.getVersion()+")");
				p.sendMessage(RED + "You can get the latest version at " +
						YELLOW + "tiny.cc/BattlePunishments");
			}

			if(BattleSettings.useWebsite()&& !ConnectionCode.validConnectionCode(null) && checkvalid) {
				p.sendMessage(BLUE + "This server is not registered on http://BattlePunishments.net! " +
						"Check it out to see what features you can get by signing up!");
			}
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event){
		Player p = event.getPlayer();
		String ip = event.getAddress().toString();

		BattlePlayer bp = BattlePunishments.createBattlePlayer(p.getName(), true);

		if(bp.getFirstSeen() == null)
			bp.setFirstSeen(TimeConverter.convertToString(p.getFirstPlayed()));

		String nn = bp.getNickname();

		if(nn != null) {
			p.setDisplayName(ChatColor.RESET + nn + ChatColor.RESET);
			p.setPlayerListName(nn);
			new ConsoleMessage("Giving "+p.getName()+" a colored name.");
		}

		try {
			bp.addIP(ip);
		}catch(Exception e) {
			new DumpFile(bp.getName()+"login", e, bp.getName()+" failed to log IP.");
		}

		if(bp.isBanned() || bp.isIPBanned()){
			if(p.hasPermission(BattlePerms.BAN)){
				bp.unban();
			}else {

				String reason = bp.getBanReason();
				long t = bp.getBanTime();
				String banner = bp.getBanner();

				if(t == -1){
					if(BattleSettings.showBanner())
						event.disallow(null, "Reason: Permabanned by " + banner + "! "+reason);
					else
						event.disallow(null, "Reason: Permabanned! "+reason);
				}else{
					if(BattleSettings.showBanner())
						event.disallow(null, "Reason: Banned until " + TimeConverter.convertToString(t) + " by " + banner + "! "+reason);
					else
						event.disallow(null, "Reason: Banned until " + TimeConverter.convertToString(t) + "! "+reason);
				}

				return;
			}
		}

		if(BattleSettings.welcomeMessage()) {
			if(p.hasPlayedBefore()) {
				String message = BattleSettings.getWelcomeMessage();
				message = message.replace("{name}", p.getName());
				message = ChatEditor.colorChat(message);
				Bukkit.broadcastMessage(message);
			}
		}

		if(BattleSettings.wlMessages()) {
			if(bp.isOnWatchList()) {
				for(Player pl : Bukkit.getOnlinePlayers()) {
					if(pl.hasPermission(BattlePerms.WATCHLIST)){
						pl.sendMessage(DARK_RED + "[ATTN] "+ YELLOW + bp.getRealName()+" just logged in and is on the watchlist!");
					}
				}
			}
		}
	}
}