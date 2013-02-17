package com.lducks.battlepunishments.commands.blockcommands;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class BlockListExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.BLOCKCOMMANDS)
	public void onBlockListCommand(CommandSender sender, BattlePlayer bp) {
		sender.sendMessage(DARK_GRAY + "---- " + BLUE + "Blocked Commands For Player: " + bp.getRealName()
				 + DARK_GRAY + " ----");
		sendList(sender, bp);
	}
	
	public static void sendList(CommandSender sender, BattlePlayer bp) {
		if(bp.getBlockList() == null || bp.getBlockList().size() == 0){
			sender.sendMessage(RED + "There are no commands blocked for this player.");
		}else{
			for(String s : bp.getBlockList()){
				sender.sendMessage(YELLOW + "/" + s);
			}
		}
	}

	@MCCommand(cmds="clear", perm=BattlePerms.BLOCKCOMMANDS)
	public void onBlockListClearCommand(CommandSender sender, BattlePlayer bp) {
		if(bp.getBlockList() == null || bp.getBlockList().size() == 0){
			sender.sendMessage(RED + "There are no commands blocked for this player.");
		}else{
			for(String s : bp.getBlockList()) {
				bp.unblockCommand(s);
				sender.sendMessage(RED + "Unblocked "+s+" from "+bp.getRealName());
			}
		}
	}
}
