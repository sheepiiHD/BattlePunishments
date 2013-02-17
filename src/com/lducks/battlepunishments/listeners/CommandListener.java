package com.lducks.battlepunishments.listeners;

import static org.bukkit.ChatColor.*;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class CommandListener implements Listener{
	
	private static Logger log = Logger.getLogger("Minecraft");
	
	@EventHandler
	public void playerUsesCommand(PlayerCommandPreprocessEvent event){
		if(event.isCancelled())
			return;

		Player p = event.getPlayer();
		String pname = p.getName();

		String commandsplit[] = event.getMessage().split(" ");
		String command = commandsplit[0];
		
		if(BattleSettings.logCommands() && event.getMessage().startsWith("/")){
			log.info(pname + " executed command: "+ event.getMessage());
		}
		
		BattlePlayer bp;
		try {
			bp = BattlePunishments.createBattlePlayer(pname);
		} catch (Exception e) {
			return;
		}

		if(bp.getBlockList().size() > 0) {
			new ConsoleMessage(bp.getRealName() + " has blocked commands");
			
			if(p.hasPermission(BattlePerms.BLOCKCOMMANDS)) {
				bp.clearBlockList();
				return;
			}
			
			if(bp.isBlocked(command)){
				new ConsoleMessage("Command is blocked");
				p.sendMessage(RED + "You have been blocked from using this command!");
				event.setCancelled(true);
			}
		}
	}
}