package com.lducks.battlepunishments.commands;

import java.util.HashMap;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class ChatStalkerExecutor extends CustomCommandExecutor{

	public static HashMap<String, CommandSender> listen = new HashMap<String, CommandSender>();
	
	@MCCommand(perm=BattlePerms.CHATSTALKER, cmds= {"stop"})
	public void onExecuteStop(CommandSender sender, Player p) {
		if(listen.containsKey(sender.getName())) {
			listen.remove(sender.getName().toLowerCase());
			sender.sendMessage(YELLOW + "You are no longer listening to "+ ITALIC + p.getName());
		}else {
			sender.sendMessage(GREEN + "You aren't even listening to "+ ITALIC + p.getName());
		}
	}
	
	@MCCommand(perm=BattlePerms.CHATSTALKER, cmds= {"start"})
	public void onExecuteStart(CommandSender sender, Player p) {
		if(listen.containsKey(sender.getName().toLowerCase())) {
			sender.sendMessage(YELLOW + "You are already listening to "+ ITALIC + p.getName());
		}else {
			listen.put(sender.getName().toLowerCase(), sender);
			sender.sendMessage(GREEN + "You are now listening to "+ ITALIC + p.getName());
		}
	}
}
