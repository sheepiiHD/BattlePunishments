package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.GREEN;

import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class ReloadExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.BRELOAD)
	public void onReloadCommand(CommandSender sender) {
		BattlePunishments.getPlugin().reloadConfig();
		sender.sendMessage(GREEN + "Reloaded");
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " has reloaded the plugin.");
	}
}
