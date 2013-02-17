package com.lducks.battlepunishments.controllers.watchlist;

import java.util.List;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.sql.SQLInstance;

/**
 * 
 * @author lDucks
 * @see WatchListController.class
 */

public class SQLWatchListController implements WatchListController{
	WatchListConfiguration config = null;

	public SQLWatchListController(SQLInstance sql) {
		config = new WatchListConfiguration(sql);
	}
		
	public List<String> getWatchList() {
		if(config.getWatchList() == null || config.getWatchList().size() == 0) {
			return null;
		}
		
		return config.getWatchList();
	}
	
	public void clear() {
		for(String n : BattlePunishments.getWatchList()) {
			BattlePlayer bp = BattlePunishments.createBattlePlayer(n);
			bp.removePlayerFromWatchList();
		}
		
		BattlePunishments.setWatchList(null);
	}
}
