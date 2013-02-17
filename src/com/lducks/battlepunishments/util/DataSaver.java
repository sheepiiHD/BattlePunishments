package com.lducks.battlepunishments.util;

import java.io.File;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.battleplayer.FileBattlePlayer;
import com.lducks.battlepunishments.debugging.ConsoleMessage;

/**
 * 
 * @author lDucks
 * DataSaver is not needed with SQL integration in v2.8.0
 */
@Deprecated
public class DataSaver {
	public static void convertFromFlatFileToSQL() {
		if(!BattleSettings.sqlIsEnabled())
			return;
		
		File f = new File(BattlePunishments.getPath()+"/players/");

		if(!f.exists()) return;

		if(f.listFiles().length > 0) {
			for(File nf : f.listFiles()) {

				if(nf.listFiles().length == 0)
					nf.delete();
				else {

					for(File ff : nf.listFiles()) {
						String name = ff.getName().replace(".yml", "");
						FileBattlePlayer fbp = null;

						try {
							fbp = new FileBattlePlayer(name);
						} catch (Exception e) {
							nf.delete();
						}

						if(fbp != null) {

							BattlePlayer bp = BattlePunishments.createBattlePlayer(name);

							if(fbp.isBanned()) {
								bp.ban(fbp.getBanReason(), fbp.getBanTime(), fbp.getBanner(), fbp.isIPBanned());
							}

							for(String ip : fbp.getIPList()) {
								if(!bp.getIPList().contains(ip))
									bp.addIP(ip);
							}

							if(fbp.isOnWatchList())
								bp.addPlayerToWatchList();

							ff.delete();

							new ConsoleMessage(name);
						}
					}
				}
			}
		}
	}
}
