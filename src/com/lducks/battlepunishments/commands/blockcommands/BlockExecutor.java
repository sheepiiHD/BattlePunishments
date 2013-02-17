package com.lducks.battlepunishments.commands.blockcommands;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class BlockExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.BLOCKCOMMANDS)
	public void onBlockCommand(CommandSender sender, BattlePlayer bp, String command) {
		if(bp.isBlocked(command)){
			sender.sendMessage(RED + "That command is already blocked for that player.");
			return;
		}

		try {
			bp.blockCommand(command);
		}catch(Exception e) {
			sender.sendMessage(RED + "There was an error! Check error folder!");
			new DumpFile("blockcommand", e, "There was an error! Check error folder!");
		}

		bp.getPlayer().sendMessage(GREEN + "You have been blocked from using the command "+ YELLOW + command + GREEN +
				" by "+ bp.getDisplayName());
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " blocked "+bp.getName()+" from using "+command);
		
		sender.sendMessage(GREEN + "You have blocked that command for this player.");
	}
}