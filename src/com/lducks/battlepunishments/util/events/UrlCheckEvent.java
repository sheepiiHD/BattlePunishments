package com.lducks.battlepunishments.util.events;

/**
 * @author lDucks
 *
 */

public class UrlCheckEvent extends BPEvent{
	final boolean valid;
	final String name;
	final long start;

	public UrlCheckEvent(boolean valid, String name) {
		this.valid = valid;
		this.name = name;
		this.start = System.currentTimeMillis();
	}

	public boolean getValid(){
		return valid;
	}
	
	public String getPlayerName(){
		return name;
	}
	
	public long getStart() {
		return start;
	}
}