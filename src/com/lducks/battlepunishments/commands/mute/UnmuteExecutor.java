package com.lducks.battlepunishments.commands.mute;

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

public class UnmuteExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.MUTE)
	public void onUnmute(CommandSender sender, BattlePlayer bp) {
		bp.unmute();
		sender.sendMessage(RED + bp.getRealName() + " unmuted.");
		bp.getPlayer().sendMessage(RED + "Unmuted");
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " unmuted "+bp.getName());
	}
}
