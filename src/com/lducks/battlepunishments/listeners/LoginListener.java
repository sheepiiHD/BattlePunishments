package com.lducks.battlepunishments.listeners;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

/**
 * 
 * @author lDucks
 *
 */

public class LoginListener implements Listener{

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!BattleSettings.loginMessage())
			event.setJoinMessage(null);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event){
		Player p = event.getPlayer();
		String ip = event.getAddress().toString();

		BattlePlayer bp = BattlePunishments.createBattlePlayer(p.getName(), true);

		if(bp.getFirstSeen() == null)
			bp.setFirstSeen(TimeConverter.convertLongToDate(p.getFirstPlayed()));

		String nn = bp.getNickname();

		if(nn != null && nn.length() <= 16) {
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
						event.disallow(null, "Reason: Banned until " + TimeConverter.convertLongToDate(t) + " by " + banner + "! "+reason);
					else
						event.disallow(null, "Reason: Banned until " + TimeConverter.convertLongToDate(t) + "! "+reason);
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