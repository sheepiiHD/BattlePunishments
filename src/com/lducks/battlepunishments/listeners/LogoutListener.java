package com.lducks.battlepunishments.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lducks.battlepunishments.util.BattleSettings;

/**
 * @author lDucks
 *
 */
public class LogoutListener implements Listener {
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		if(!BattleSettings.logoutMessage())
			event.setQuitMessage(null);
	}
}
