package com.lducks.battlepunishments.commands.mute;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class FastMuteExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.MUTE)
	public void onFastMute(CommandSender sender, Player p) {
		if(BattleSettings.useStrikes())
			Bukkit.dispatchCommand(sender, "mute "+p.getName()+" 0 1h Causing issues in chat");
		else
			Bukkit.dispatchCommand(sender, "mute "+p.getName()+" 1h Causing issues in chat");
	}
}
