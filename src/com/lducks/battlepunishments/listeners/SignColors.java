package com.lducks.battlepunishments.listeners;

import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class SignColors implements Listener{
	@EventHandler
	public void SignChange(SignChangeEvent event){
		Player p = event.getPlayer();
		if(p.hasPermission(BattlePerms.SIGNCOLORS)){
			for(int i=0; i<4;i++){
				String msg = event.getLine(i);
				msg = translateAlternateColorCodes('&', msg);
				event.setLine(i, msg);
			}
		}
	}
}
