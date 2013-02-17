package com.lducks.battlepunishments.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;

/**
 * 
 * @author lDucks
 *
 */

public class TagAPIListener implements Listener {
	@EventHandler
	public void nameChange(PlayerReceiveNameTagEvent event){
		Player p = event.getNamedPlayer();
		
		if(event.isModified())
			return;

		BattlePlayer bp;
		
		try {
			bp = BattlePunishments.createBattlePlayer(p.getName());
		} catch (Exception e) {
			return;
		}
		
		String tag = bp.getNickname();
		
		if(tag == null || tag.length() > 16)
			return;

		event.setTag(tag);
	}
}
