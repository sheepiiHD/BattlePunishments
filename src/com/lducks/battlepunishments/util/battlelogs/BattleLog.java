package com.lducks.battlepunishments.util.battlelogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.util.TimeConverter;

/**
 * @author lDucks
 *
 */
public class BattleLog {

	private static File f = new File(BattlePunishments.getPath()+"/battlelog.txt");
	
	/**
	 * 
	 * @param message What message you want to send to the custom log.
	 * 
	 */
	public static void addMessage(String message) {
		try {
			FileOutputStream output = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(output);
			ps.println("["+TimeConverter.convertToString(System.currentTimeMillis())+"] "+message);
		} catch (Exception e) {
			new DumpFile("addMessage", e, "com.lducks.battlepunishments.util.battlelogs.BattleLog.class");
		}
	}
	
}
