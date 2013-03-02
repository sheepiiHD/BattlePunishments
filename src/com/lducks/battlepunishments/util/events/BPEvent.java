package com.lducks.battlepunishments.util.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author lDucks
 *
 */
public class BPEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	public void callEvent(){
		Bukkit.getServer().getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
