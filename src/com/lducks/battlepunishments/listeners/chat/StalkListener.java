package com.lducks.battlepunishments.listeners.chat;

import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RED;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lducks.battlepunishments.commands.ChatStalkerExecutor;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class StalkListener implements Listener{

	@EventHandler()
	public void onCommand(PlayerCommandPreprocessEvent event) {
		try {
		String cmd = event.getMessage().toLowerCase().replace("/", "").toLowerCase();
		String[] split = cmd.split(" ");

		if(split.length < 2) return;

		if(BattleSettings.getChatStalkerList().contains(split[0])) {
			String p = split[1];
			if(ChatStalkerExecutor.listen.containsKey(p)) {
				CommandSender s = ChatStalkerExecutor.listen.get(p);
				String newcmd = cmd.replace(p, "").replace(split[0], "").trim();
				s.sendMessage(BattleSettings.getStalkMesssageFormat(event.getPlayer().getName(), newcmd));
			}
		}
		}catch(Exception e) {
			new DumpFile("onCommand_StalkListener", e, "Error with arguments");
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
