package com.lducks.battlepunishments.commands.ip;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;


public class ClearIpExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.IPCHECK)
	public void onClearIpsCommand(CommandSender sender, BattlePlayer bp) {
		bp.clearIPs();
		sender.sendMessage(GREEN + "Ips have been cleared for "+bp.getRealName());
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " cleared all the IPs for "+bp.getRealName());
	}
}
