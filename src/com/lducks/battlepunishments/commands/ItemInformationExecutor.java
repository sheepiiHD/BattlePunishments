package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class ItemInformationExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.ITEMID, inGame=true)
	public void onItemIdCommand(CommandSender sender) {
		Player p = (Player) sender;
		ItemStack is = p.getItemInHand();
		
		int id = is.getTypeId();
		int dur = is.getDurability();
		
		sender.sendMessage(GREEN + "Item: " + is.getType() + ", ID: "+id+":"+dur);
	}
}
