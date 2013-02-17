/**
 *
 * @author lDucks
 *
 */
package com.lducks.battlepunishments.convertplugins;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.DumpFile;

/**
 * @author lDucks
 * 
 */
public class ConvertFlatFile {

	final static Charset ENCODING = StandardCharsets.UTF_8;

	/**
	 * 
	 * Converts bans.txt to a BattlePunishments bans file.
	 * 
	 * File format:
	 * 
	 * # Player Name | Reason | Banner | Time of Unban 
	 * Test | Xray | Console | 1358721256
	 * 
	 */
	public static void runBans() {
		File f = new File(BattlePunishments.getPath() + "/bans.txt");
		
		try {
			Scanner scanner =  new Scanner(f, ENCODING.name());
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				String[] split = line.split("|");
				
				BattlePlayer bp = BattlePunishments.createBattlePlayer(split[0], true);
				bp.ban(split[1], Long.parseLong(split[3]), split[2], false);
				
			}
		} catch (Exception e) {
			new DumpFile("ConvertFlatFile.runBans()", e, "Error converting flat file bans");
		} 
	}
}
