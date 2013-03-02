/**
 *
 * @author lDucks
 *
 */
package com.lducks.battlepunishments.util.webrequests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.DumpFile;

/**
 * @author lDucks
 *
 */
public class WebAPI {

	private static String apiurl = "http://api.battlepunishments.net/";
	
	public static double getRatio(BattlePlayer bp) {
		double ratio;
		try {
			URL url = new URL(apiurl+"ratio.php?q="+bp.getRealName());
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String line = reader.readLine();
			
			try {
				ratio = Double.parseDouble(line);
			}catch(Exception e) {
				new DumpFile("getRatio", e, "Error parsing double.");
				return 0.00;
			}
			
			reader.close();
		}catch(Exception e) {
			ratio = 0.00;
		}
		
		return ratio;
	}
	
}
