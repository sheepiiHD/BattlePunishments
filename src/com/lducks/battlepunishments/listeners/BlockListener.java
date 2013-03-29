package com.lducks.battlepunishments.listeners;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.lducks.battlepunishments.controllers.MineWatchController;
import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class BlockListener implements Listener {

	static HashMap<Player, Long> times = new HashMap<Player, Long>();

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled() || !MineWatchController.isEnabled())
			return;

		Player p = event.getPlayer();

		if(p.hasPermission(BattlePerms.BLOCKLOGGER))
			return;

		List<Material> blocks = MineWatchController.getList();

		if(blocks.contains(event.getBlock().getType())){
			long last = System.currentTimeMillis();
			if(times.containsKey(p)){
				last = times.get(p);
			}

			if(last <= System.currentTimeMillis()){
				Player[] players = Bukkit.getServer().getOnlinePlayers();
				String blockname = WordUtils.capitalize(event.getBlock().getType().toString().replace("_", " ").toLowerCase());
				for (Player player : players){
					if(player.hasPermission(BattlePerms.BLOCKLOGGER)){
						String x = Integer.toString(event.getBlock().getX());
						String y = Integer.toString(event.getBlock().getY());
						String z = Integer.toString(event.getBlock().getZ());
						String toprint = "(".concat(x).concat(",").concat(y).concat(",").concat(z).concat(")");
						player.sendMessage(DARK_RED + event.getPlayer().getName() + GOLD
								+ " found " + DARK_RED
								+ blockname
								+ GOLD + " at " + DARK_RED + toprint);
					}
				}
			}

			times.put(p, System.currentTimeMillis() + 10000);

		}
	}
}
