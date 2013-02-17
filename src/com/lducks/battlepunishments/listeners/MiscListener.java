package com.lducks.battlepunishments.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.util.TimeConverter;

/**
 * 
 * @author lDucks
 *
 */

public class MiscListener implements Listener{
	@EventHandler
	public void onLogout(PlayerQuitEvent event){
		Player p = event.getPlayer();
		BattlePlayer bp;
		try {
			bp = BattlePunishments.createBattlePlayer(p.getName());
		} catch (Exception e) {
			return;
		}
		
		bp.setLastSeen(TimeConverter.convertToString(System.currentTimeMillis()));
		bp.setLogoutCoords(p.getLocation());
	}
}
