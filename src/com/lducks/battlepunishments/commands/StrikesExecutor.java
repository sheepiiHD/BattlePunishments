package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class StrikesExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.STRIKES)
	public void onEditStrikesCommand(CommandSender sender, BattlePlayer bp, Integer s) {
		bp.editStrikes(s);
		sender.sendMessage(RED + bp.getRealName() + " now has " + 
				bp.getStrikes() + "/"+ BattleSettings.getMaxStrikes() +" strikes.");
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(bp.getRealName() + "'s strikes were changed by " + sender.getName() + ". "+bp.getRealName() 
					+ " now has " + bp.getStrikes() + "/"+ BattleSettings.getMaxStrikes() +" strikes.");
	}
}
