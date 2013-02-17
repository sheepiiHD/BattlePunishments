package com.lducks.battlepunishments.listeners.chat;

import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.RegisterItems;

/**
 * 
 * @author lDucks
 *
 */

public class ChatListener implements Listener{

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		String p = player.getName().toLowerCase();
		BattlePlayer bp;
		try {
			bp = BattlePunishments.createBattlePlayer(p, true);
		} catch (Exception e) {
			return;
		}

		if(BattlePunishments.muteall) {
			event.setCancelled(true);
			player.sendMessage(DARK_RED + "The chat is muted!");
			return;
		}
		
		AntiSpam.checkTimer(p, event.getMessage());

		if(bp.isMuted() && !Bukkit.getServer().getPlayer(p).hasPermission(BattlePerms.MUTE)){
			String reason = bp.getMuteReason();

			long t = bp.getMuteTime();

			if(t != -1){
				t = t - System.currentTimeMillis();
			}

			Player pl = player.getPlayer();

			pl.sendMessage(RED + "You can not talk. Reason: " + reason);

			if(t == -1){
				pl.sendMessage(RED + "You are permamuted.");
			}else{
				long nt = t / 60000;
				pl.sendMessage(RED + "You will be unmuted in: ~" + nt + " minute(s).");
			}

			event.setCancelled(true);
			return;
		}
		
		if(BattleSettings.customChat()) {
			event.setCancelled(true);
			String message = BattleSettings.getCustomChatFormat();
			message = message.replace("{name}", player.getDisplayName());
			
			if(RegisterItems.chat != null) {
				Chat chat = RegisterItems.chat;
				message = message.replace("{prefix}", chat.getPlayerPrefix(player));
				message = message.replace("{suffix}", chat.getPlayerSuffix(player));
			}
			
			message = message.replace("{message}", event.getMessage());
			
			message = ChatEditor.colorChat(message);
			Bukkit.broadcastMessage(message);
		}
	}
}
