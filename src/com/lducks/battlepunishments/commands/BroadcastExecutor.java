package com.lducks.battlepunishments.commands;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.listeners.chat.ChatEditor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class BroadcastExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.BROADCAST)
	public void onBroadcastCommand(CommandSender sender, String[] args) {
		
		if(args.length == 0) {
			sender.sendMessage(RED + "You need to enter something to broadcast!");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i=0;i< args.length;i++)
			sb.append(args[i]+" ");

		String message = ChatEditor.colorChat(sb.toString());

		Bukkit.getServer().broadcastMessage(GOLD + "[SERVER MESSAGE] " + message);
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " just broadcasted: "+message);
	}
}
