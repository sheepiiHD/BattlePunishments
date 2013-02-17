package com.lducks.battlepunishments.debugging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.TimeConverter;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

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

			boolean x = false;

			if(!f.exists()) {
				f.createNewFile();
				x = true;
			}

			FileOutputStream output = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(output);
			if(x)
				ps.println("Please report this to http://dev.bukkit.org/server-mods/BattlePunishments");
			
			ps.println("Version: BattlePunishments v"+BattlePunishments.getVersion());
			ps.println();
			ps.println("Day of Report: "+TimeConverter.convertToString(System.currentTimeMillis()));
			ps.println("Reason: "+message);
			ps.println();
			e.printStackTrace(ps);
			ps.println();
			ps.println();
			output.close();
		}catch(Exception ex) {
			if(BattleSettings.useBattleLog())
				BattleLog.addMessage("Error creating crash file for "+f.getName());
						
			System.out.println("["+BattlePunishments.getPluginName()+"] Error creating crash file for "+f.getName());
			ex.printStackTrace();
		}
	}
}
