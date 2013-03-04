package com.lducks.battlepunishments.listeners.chat;

import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.Chatter.Result;
import com.dthielke.herochat.ConversationChannel;
import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.ChatStalkerExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class HeroChatListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(ChannelChatEvent event){
		Chatter player = event.getSender();
		String p = player.getName();
		BattlePlayer bp;
		try {
			bp = BattlePunishments.createBattlePlayer(p, true);
		} catch (Exception e) {
			return;
		}
		
		if(BattlePunishments.muteall && !bp.getPlayer().hasPermission(BattlePerms.MUTEALL)) {
			event.setResult(Result.MUTED);
			bp.getPlayer().sendMessage(DARK_RED + "The chat is muted!");
			return;
		}

		AntiSpam.checkTimer(p, event.getMessage());

		if(bp.isMuted()){
			if(bp.getPlayer().hasPermission(BattlePerms.MUTE)){
				bp.unmute();
				return;
			}

			String reason = bp.getMuteReason();

			long t = bp.getMuteTime();

			if(t != -1){
				t = t - System.currentTimeMillis();
			}

			Player pl = player.getPlayer();
			Result b = Result.MUTED;

			pl.sendMessage(RED + "You can not talk. Reason: " + reason);

			if(t == -1){
				pl.sendMessage(RED + "You are permamuted.");
			}else{
				long nt = t / 60000;
				pl.sendMessage(RED + "You will be unmuted in: ~" + nt + " minute(s).");
			}

			event.setResult(b);
			return;
		}

		if(!(event.getChannel() instanceof ConversationChannel)) {
			for(Chatter c : event.getChannel().getMembers()) {
				if(ChatStalkerExecutor.listen.containsKey(c.getName())) {
					CommandSender s = ChatStalkerExecutor.listen.get(p);
					s.sendMessage(BattleSettings.getStalkMesssageFormat(p, event.getMessage()));
					return;
				}
			}
		}
	}
}
