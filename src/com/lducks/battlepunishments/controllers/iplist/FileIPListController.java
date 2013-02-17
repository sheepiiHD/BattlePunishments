package com.lducks.battlepunishments.controllers.iplist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.FileBattlePlayer;

/**
 * 
 * @author lDucks
 *
 */

public class FileIPListController implements IPListController{

	public List<String> getPlayerList(String i) {
		List<String> playerlist = new ArrayList<String>();

		for(File nn : new File(BattlePunishments.getPath()+"/players").listFiles()){

			if(!nn.exists() || nn.length() == 0)
				return playerlist;

			for(File fn : nn.listFiles()){
				String n = fn.getName().replace(".yml", "");
				FileBattlePlayer bpn;
				try {
					bpn = new FileBattlePlayer(n);
				} catch (Exception e) {
					break;
				}

				for(String ip : bpn.getIPList()) {
					if(ip.equals(i)) {
						playerlist.add(bpn.getRealName());
						break;
					}
				}
			}
		}
		return playerlist;
	}
}
