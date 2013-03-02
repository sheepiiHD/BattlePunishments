package com.lducks.battlepunishments.util.events;

/**
 * @author lDucks
 *
 */

public class UrlCheckEvent extends BPEvent{
	final boolean valid;
	final String name;

	public UrlCheckEvent(boolean valid, String name) {
		this.valid = valid;
		this.name = name;
	}

	public boolean getValid(){
		return valid;
	}
	
	public String getPlayerName(){
		return name;
	}
}