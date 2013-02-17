/**
 *
 * @author lDucks
 *
 */
package com.lducks.battlepunishments.convertplugins;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;

/**
 * @author lDucks
 *
 */
public class ConvertVanilla {

	/**
	 * 
	 * Converts Vanilla bans to BattlePunishments bans
	 * 
	 */
	public static void runBans() {
		for(OfflinePlayer p : Bukkit.getBannedPlayers()) {
			BattlePlayer bp = BattlePunishments.createBattlePlayer(p.getName());
			bp.ban("Vanilla banned", -1, "Operator", false);
		}
	}

}
