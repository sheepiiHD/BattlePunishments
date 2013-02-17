package com.lducks.battlepunishments.controllers.iplist;

import java.util.List;
/**
 * 
 * @author lDucks
 *
 */
public interface IPListController {
	public List<String> getPlayerList(String ip);
}
