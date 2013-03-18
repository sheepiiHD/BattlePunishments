package com.lducks.battlepunishments.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.lducks.battlepunishments.util.BattlePerms;


public class SneakListener implements Listener{
	private static List<String> sneaklist = new ArrayList<String>();

	/**
	 * 
	 * @param sneaking
	 * @param name
	 */
	public static void setSneaking(boolean sneaking, String name) {
		if(sneaking) {
			if(!sneaklist.contains(name))
				sneaklist.add(name);
		}else if(!sneaking) {
			if(sneaklist.contains(name))
				sneaklist.remove(name);
		}
	}

	/**
	 * 
	 * @return list of sneaking people
	 */
	public static List<String> getPlayersSneaking(){
		return sneaklist;
	}

	/**
	 * 
	 * @param name Name of player to check
	 * @return boolean isSneaking();
	 */
	public static boolean isSneaking(String name) {
		return sneaklist.contains(name);
	}

	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();

		if(!p.hasPermission(BattlePerms.SNEAK))
			return;

		if(isSneaking(p.getName())) {
			p.setSneaking(true);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = event.getPlayer();
			if(isSneaking(p.getName()) && p.getItemInHand().getType() != Material.AIR) {
				if(p.getTargetBlock(null, 5).getState() instanceof Chest) {
					event.setCancelled(true);
					Chest c = (Chest) p.getTargetBlock(null, 5).getState();
					p.openInventory(c.getBlockInventory());
				}
			}
		}
	}
}
