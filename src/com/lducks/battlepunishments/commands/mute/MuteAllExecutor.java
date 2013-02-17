package com.lducks.battlepunishments.commands.mute;

import static org.bukkit.ChatColor.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class MuteAllExecutor extends CustomCommandExecutor {

	@MCCommand(perm=BattlePerms.MUTEALL)
	public void onMuteAllCommand(CommandSender sender) {
		if(BattlePunishments.muteall) {
			BattlePunishments.muteall = false;
			Bukkit.broadcastMessage(DARK_GREEN + sender.getName() + " has unmuted the chat.");
			
			if(BattleSettings.useBattleLog())
				BattleLog.addMessage(sender.getName() + " unmuted everyone.");
			
		}else {
			BattlePunishments.muteall = true;
			Bukkit.broadcastMessage(DARK_RED + sender.getName() + " has muted the chat.");

			if(BattleSettings.useBattleLog())
				BattleLog.addMessage(sender.getName() + " muted everyone");
		}
	}
	
}
