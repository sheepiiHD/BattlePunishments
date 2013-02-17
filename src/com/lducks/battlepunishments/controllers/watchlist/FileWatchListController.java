package com.lducks.battlepunishments.controllers.watchlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.battleplayer.FileBattlePlayer;

/**
 * 
 * @author lDucks
 *
 */

public class FileWatchListController implements WatchListController{

	public List<String> getWatchList() {
		List<String> watchlist = new ArrayList<String>();

		for(File nn : new File(BattlePunishments.getPath()+"/players").listFiles()){
			
			if(!nn.exists() || nn.length() == 0)
				return watchlist;
			
			for(File fn : nn.listFiles()){
				String n = fn.getName().replace(".yml", "");
				FileBattlePlayer bpn;
				try {
					bpn = new FileBattlePlayer(n);
				} catch (Exception e) {
					break;
				}
				
				if(bpn.isOnWatchList()){
					if(bpn.getRealName() != null)
						watchlist.add(bpn.getRealName());
					else
						watchlist.add(bpn.getName());
				}
			}
		}
		return watchlist;
	}

	public void clear() {
		for(String n : BattlePunishments.getWatchList()) {
			BattlePlayer bp = BattlePunishments.createBattlePlayer(n);
			bp.removePlayerFromWatchList();
		}
		
		BattlePunishments.setWatchList(null);
	}
}
