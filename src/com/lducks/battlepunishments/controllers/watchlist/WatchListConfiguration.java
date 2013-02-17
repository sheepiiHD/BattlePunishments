package com.lducks.battlepunishments.controllers.watchlist;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.sql.SQLInstance;
import com.lducks.battlepunishments.sql.SQLSerializer.RSCon;

/**
 * 
 * @author lDucks
 * @see WatchListController.class
 */

public class WatchListConfiguration{

	SQLInstance sql = null;
	
	public WatchListConfiguration(SQLInstance sql) {
		this.sql = sql;
	}
	
	public List<String> getWatchList(){
		try {
			RSCon resultSetAndConnection = sql.executeQuery("select player from bp_wl");
			if (resultSetAndConnection == null || resultSetAndConnection.rs == null) return null;
			List<String> results = new ArrayList<String>();

			try{
				ResultSet rs = resultSetAndConnection.rs;
				while (rs.next()) {
					String name = rs.getString("player");
					results.add(name);
				}
			} catch (Exception e) {
				new DumpFile("getWatchList", e, "Error getting Watch List");
				return null;
			} finally {
				sql.closeConnection(resultSetAndConnection);
			}

			return results;
		}catch(Exception e) {
			return null;
		}
	}

}
