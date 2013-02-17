package com.lducks.battlepunishments.debugging;

import com.lducks.battlepunishments.util.BattleSettings;
/**
 * 
 * @author lDucks
 *
 */
public class ConsoleMessage {
	public ConsoleMessage(String string) {
		if(BattleSettings.isDebugging())
			System.out.println("[BattlePunishments] " +string);
	}
}
