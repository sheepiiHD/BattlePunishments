package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.SneakListener;
import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class SneakExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.SNEAK, inGame=true)
	public void onSneakCommand(CommandSender sender) {
		Player p = (Player) sender;
		if(p.isSneaking()) {
			p.setSneaking(false);
			SneakListener.setSneaking(false, p.getName());
			p.sendMessage(GRAY + "You are no longer sneaking.");
		}else if(!p.isSneaking()) {
			p.setSneaking(true);
			SneakListener.setSneaking(true, p.getName());
			p.sendMessage(GRAY + "You are now sneaking.");
		}else {
			new DumpFile(p.getName()+"sneakerror",	new CommandException("Error while trying to sneak."), 
					"Error while "+p.getName()+" tried to sneak.");
			sender.sendMessage(RED + "Error! Contact server administrator.");
		}
	}

	@MCCommand(cmds="check", perm=BattlePerms.SNEAK, inGame=true)
	public void onSneakCheckCommand(CommandSender sender) {
		Player p = (Player) sender;

		if(p.isSneaking())
			p.sendMessage(GRAY + "You are sneaking.");
		else
			p.sendMessage(GRAY + "You are not sneaking");
	}
	
	@MCCommand(cmds="on", perm=BattlePerms.SNEAK, inGame=true)
	public void onSneakOnCommand(CommandSender sender) {
		Player p = (Player) sender;

		if(p.isSneaking())
			p.sendMessage(GRAY + "You are already sneaking.");
		else {
			SneakListener.setSneaking(true, p.getName());
			p.sendMessage(GRAY + "You are now sneaking");
			p.setSneaking(true);
		}
	}
	
	@MCCommand(cmds="off", perm=BattlePerms.SNEAK, inGame=true)
	public void onSneakOffCommand(CommandSender sender) {
		Player p = (Player) sender;

		if(!p.isSneaking())
			p.sendMessage(GRAY + "You are not sneaking.");
		else {
			SneakListener.setSneaking(false, p.getName());
			p.sendMessage(GRAY + "You are no longer sneaking");
			p.setSneaking(false);
		}
	}
	
}
