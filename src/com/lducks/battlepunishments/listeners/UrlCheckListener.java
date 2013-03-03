/**
 *
 * @author lDucks
 *
 */
package com.lducks.battlepunishments.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.lducks.battlepunishments.util.events.UrlCheckEvent;
import com.lducks.battlepunishments.util.webrequests.ConnectionCode;

/**
 * @author lDucks
 *
 */
public class UrlCheckListener implements Listener{

	public static int timerid = -2;

	@EventHandler
	public void onUrlCheckEvent(UrlCheckEvent event) {
		boolean valid = event.getValid();
		if(timerid != -2) {
			Bukkit.getScheduler().cancelTask(timerid);
			timerid = -2;

			Player p = Bukkit.getPlayer(event.getPlayerName());
			if(p != null) {
				if(valid) {
					p.sendMessage(ChatColor.GREEN + "Connection verified");
					
					long time = System.currentTimeMillis() - event.getStart();
					p.sendMessage(ChatColor.GREEN + "System took "+ ChatColor.YELLOW + time 
							+ ChatColor.GREEN + " millisecond(s) to connect.");
					
				}else
					p.sendMessage(ChatColor.RED + "Connection could not be verified");
			}
		}

		ConnectionCode.setValid(valid);

		if(valid)
			LoginListener.checkvalid = true;
	}

}
