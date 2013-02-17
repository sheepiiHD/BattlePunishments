package com.lducks.battlepunishments.controllers.watchlist;

import java.util.List;

/**
 * 
 * @author lDucks
 *
 */

public interface WatchListController {
	public List<String> getWatchList();

	public void clear();
}
