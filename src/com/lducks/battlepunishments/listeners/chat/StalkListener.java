package com.lducks.battlepunishments.listeners.chat;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lducks.battlepunishments.commands.ChatStalkerExecutor;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class StalkListener implements Listener{

	@EventHandler()
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String cmd = event.getMessage().toLowerCase().replace("/", "").toLowerCase();
		new ConsoleMessage(cmd.split(" ")[0]);
		new ConsoleMessage(BattleSettings.getChatStalkerList().contains(cmd.split(" ")[0])+"");
		if(BattleSettings.getChatStalkerList().contains(cmd.split(" ")[0])) {
			String p = cmd.split(" ")[1];
			if(ChatStalkerExecutor.listen.containsKey(p)) {
				CommandSender s = ChatStalkerExecutor.listen.get(p);
				String newcmd = cmd.replace(p, "").replace(cmd.split(" ")[0], "").trim();
				s.sendMessage(BattleSettings.getStalkMesssageFormat(event.getPlayer().getName(), newcmd));
			}
		}
	}
	
	@EventHandler()
	public void onPlayerLogout(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if(ChatStalkerExecutor.listen.containsKey(p.getName())) {
			CommandSender s = ChatStalkerExecutor.listen.get(p.getName());
			s.sendMessage(DARK_RED + "" + ITALIC + p.getName() + RED + 
					" has logged out, you are no longer listening to them!");
			ChatStalkerExecutor.listen.remove(p.getName());
		}
	}
	
}
