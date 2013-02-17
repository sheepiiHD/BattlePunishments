package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class TeleportLastExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.TPLAST, inGame=true)
	public void onTeleportLastCommand(CommandSender sender, BattlePlayer bp) {
		if(bp.getLogoutCoords() != null) {
			Player p = (Player) sender;
			p.teleport(bp.getLogoutCoords());
			sender.sendMessage(GREEN + "You have been teleported to where "
					+ YELLOW + bp.getRealName()+ GREEN + " last logged out.");
			sender.sendMessage(GREEN + "This location was logged on "+ YELLOW + bp.getLastSeen());
			
			return;
		}
		
		sender.sendMessage(RED + "There is no location saved for this player.");
	}
}
