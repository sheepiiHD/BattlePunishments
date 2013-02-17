package com.lducks.battlepunishments.controllers.iplist;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.sql.SQLInstance;
import com.lducks.battlepunishments.sql.SQLSerializer.RSCon;

/**
 * 
 * @author lDucks
 *
 */

public class IPListConfiguration{

	SQLInstance sql = null;

	public IPListConfiguration(SQLInstance sql) {
		this.sql = sql;
	}

	public List<String> getPlayerList(String ip) {
		try {
			RSCon resultSetAndConnection = sql.executeQuery("select player from bp_ips where ip=?", ip);
			if (resultSetAndConnection == null || resultSetAndConnection.rs == null) return null;
			List<String> results = new ArrayList<String>();

			try{
				ResultSet rs = resultSetAndConnection.rs;
				while (rs.next()) {

					String name = rs.getString("player");

					if(!results.contains(name))
						results.add(name);

				}
			} catch (Exception e) {
				new DumpFile("getPlayerList", e, "Error getting Player List");
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
