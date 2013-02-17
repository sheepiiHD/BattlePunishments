package com.lducks.battlepunishments.controllers.iplist;

import java.util.List;

import com.lducks.battlepunishments.sql.SQLInstance;

/**
 * 
 * @author lDucks
 *
 */

public class SQLIPListController implements IPListController{
	IPListConfiguration config = null;

	public SQLIPListController(SQLInstance sql) {
		config = new IPListConfiguration(sql);
	}
		
	public List<String> getPlayerList(String ip) {
		return config.getPlayerList(ip);
	}
}
