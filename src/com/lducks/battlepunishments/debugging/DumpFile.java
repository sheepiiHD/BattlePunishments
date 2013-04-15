package com.lducks.battlepunishments.debugging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.TimeConverter;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;
import com.lducks.battlepunishments.util.webrequests.WebConnections;

/**
 * 
 * @author lDucks
 * Creates debugging dumb file.
 */

public class DumpFile {
	private static Logger log = Logger.getLogger("Minecraft");
	public DumpFile(String string, Exception e, String message) {
		log.severe("["+BattlePunishments.getPluginName()+"] Creating error file: '"+string+"'. " + message);
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage("["+BattlePunishments.getPluginName()+"] Creating error file: '"+string+"'. " + message);

		File f = new File(BattlePunishments.getPath()+"/errors/"+string+".txt");

		try {

			FileOutputStream output = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(output);

			ps.println("Version: BattlePunishments v"+BattlePunishments.getVersion());
			ps.println();
			ps.println("Day of Report: "+TimeConverter.convertLongToDate(System.currentTimeMillis()));
			ps.println("Reason: "+message);
			ps.println();
			e.printStackTrace(ps);
			ps.println();
			ps.println();
			output.close();

			if(BattleSettings.useWebsite())
				WebConnections.sendErrorReport(e);
		}catch(Exception ex) {
			if(BattleSettings.useBattleLog())
				BattleLog.addMessage("Error creating crash file for "+f.getName());

			System.out.println("["+BattlePunishments.getPluginName()+"] Error creating crash file for "+f.getName());
			ex.printStackTrace();

			if(BattleSettings.useWebsite())
				try {
					WebConnections.sendErrorReport(ex);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
	}
}
