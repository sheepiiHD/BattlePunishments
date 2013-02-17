package com.lducks.battlepunishments.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;

/**
 * 
 * @author lDucks
 *
 */

public class SneakTimer {

	public SneakTimer() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(BattlePunishments.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for(String s : SneakListener.getPlayersSneaking()) {
					if(Bukkit.getPlayer(s) == null) {
						SneakListener.setSneaking(false, s);
					}else {
						Player p = Bukkit.getPlayer(s);
						p.setSneaking(true);
					}
				}
			}
			
		}, 0L, 10L);
	}
}
