/**
 *
 * @author lDucks
 *
 */
package com.lducks.battlepunishments.convertplugins;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.webrequests.UpdateDatabase;

/**
 * @author lDucks
 * 
 */
public class ConvertEssentials {

	/**
	 * 
	 * Converts Essentials bans to a BattlePunishments bans file.
	 * 
	 */
	public static void runBans() {
		if(!new File("plugins/Essentials/userdata").exists()) return;

		for(File f : new File("plugins/Essentials/userdata").listFiles()) {
			YamlConfiguration config = new YamlConfiguration();
			try {
				config.load(f);
				if(config.contains("ban")) {
					long timeout = config.getLong("ban.timeout");
					if(timeout == 0) timeout = -1;
					BattlePlayer bp = BattlePunishments.createBattlePlayer(f.getName().replace(".yml", ""), true);
					bp.ban(config.getString("ban.reason"), 
							timeout, "Essentials", false);
					config.set("battlepunishments.converted", true);
					config.save(f);

					if(BattleSettings.useWebsite()) 
						UpdateDatabase.updateBan(bp);
				}
			}catch(Exception e) {
				new DumpFile("ConvertEssentials", e, "Error converting essential bans to BP bans");
				return;
			}
		}
	}
}
