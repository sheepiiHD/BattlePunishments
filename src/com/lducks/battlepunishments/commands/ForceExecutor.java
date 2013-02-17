package com.lducks.battlepunishments.commands;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class ForceExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.FORCE)
	public void onForceCommand(CommandSender sender, Player p, String[] args) {
		if(args.length < 2) {
			sender.sendMessage(RED + "You did not enter a correct command.");
			return;
		}

		if(p.hasPermission(BattlePerms.FORCE) && BattleSettings.cannotForceSameLevel()) {
			sender.sendMessage(RED + "You can't force this person to do that!");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.length; i++)
			sb.append(args[i]+" ");

		String s = sb.toString();

		Bukkit.dispatchCommand((CommandSender) p, s);
		sender.sendMessage(YELLOW + p.getName() + GREEN + " executed command " + YELLOW + s);
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " forced "+p.getName() + " to use the command: "+s);
	}
}
