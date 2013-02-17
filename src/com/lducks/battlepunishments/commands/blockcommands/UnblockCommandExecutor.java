package com.lducks.battlepunishments.commands.blockcommands;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class UnblockCommandExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.BLOCKCOMMANDS)
	public void onUnblockCommand(CommandSender sender, BattlePlayer bp, String command) {
		if(!bp.isBlocked(command)){
			sender.sendMessage(RED + "That command is not blocked for this player.");
			return;
		}
							
		bp.unblockCommand(command);
		
		bp.getPlayer().sendMessage(GREEN + "You have just been unblocked from using the command: " + YELLOW + command);
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " has unblocked "+command+" from "+bp.getRealName());
		
		sender.sendMessage(GREEN + "You have unblocked that command for this player.");
	}
}
