package com.lducks.battlepunishments.util;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.sql.SQLInstance;
import com.lducks.battlepunishments.sql.SQLSerializer.RSCon;

/**
 * 
 * @author lDucks
 *
 */

public class PlayerList {
	
	/**
	 * 
	 * @return list List of BattlePlayers
	 */
	public static List<BattlePlayer> getList() {
		List<BattlePlayer> playerlist = new ArrayList<BattlePlayer>();
		if(BattleSettings.sqlIsEnabled()) {
			SQLInstance sql = BattlePunishments.sql;
			
			RSCon resultSetAndConnection = sql.executeQuery("select player from bp_info");
			if (resultSetAndConnection == null || resultSetAndConnection.rs == null) return null;

			try{
				ResultSet rs = resultSetAndConnection.rs;
				while (rs.next()) {

					String name = rs.getString("player");

					BattlePlayer bp = BattlePunishments.createBattlePlayer(name);
					
					if(!playerlist.contains(bp))
						playerlist.add(bp);

				}
			} catch (Exception e) {
				new DumpFile("getAll", e, "Error getting Player List");
				return null;
			} finally {
				sql.closeConnection(resultSetAndConnection);
			}
		}else {
			for(File nn : new File(BattlePunishments.getPath()+"/players").listFiles()){
				for(File fn : nn.listFiles()){
					String n = fn.getName().replace(".yml", "");
					BattlePlayer bp = BattlePunishments.createBattlePlayer(n);
					new ConsoleMessage(bp.getName());
					if(!playerlist.contains(bp))
						playerlist.add(bp);
				}
			}
		}
		
		return playerlist;
	}
	
	/**
	 * @return int Total banned players on the current server
	 * 
	 */
	public static int getTotalBans() {
		int i = 0;
		for(BattlePlayer bp : getList()) {
			if(bp.isBanned()) {
				i++;
			}
		}
		
		new ConsoleMessage("Bans" + i);
		return i;
	}
	
	/**
	 * @return int Total muted players on the current server
	 * 
	 */
	public static int getTotalMutes() {
		int i = 0;
		for(BattlePlayer bp : getList()) {
			if(bp.isMuted()) {
				i++;
			}
		}
		
		new ConsoleMessage("Mutes" + i);
		return i;
	}
}