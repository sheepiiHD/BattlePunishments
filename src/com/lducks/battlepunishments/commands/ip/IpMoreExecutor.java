package com.lducks.battlepunishments.commands.ip;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class IpMoreExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.IPCHECK)
	public void onIpMoreCommand(CommandSender sender, Integer key) {
		String ip;

		if(IpExecutor.ipmore.containsKey(key)){
			ip = IpExecutor.ipmore.get(key).replace("-", ".");
		}else{
			sender.sendMessage(DARK_GRAY + "----- " + BLUE + "BattlePunishments v"+
					BattlePunishments.getVersion() + DARK_GRAY + " -----");
			sender.sendMessage(RED + "Key out of bounds.");
			return;
		}

		sender.getServer().dispatchCommand(sender, "ip "+ip);
	}
}
