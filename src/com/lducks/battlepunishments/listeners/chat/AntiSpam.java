package com.lducks.battlepunishments.listeners.chat;

import java.util.HashMap;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class AntiSpam {

	private static HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	private static HashMap<String, String> messages = new HashMap<String, String>();
	private static HashMap<String, Integer> max = new HashMap<String, Integer>();

	public static void checkTimer(String bp, String message) {
		if(!BattleSettings.isAntiSpam())
			return;

		if(!containsPlayer(bp))
			editPlayer(bp, System.currentTimeMillis() + BattleSettings.getAntiSpamTime());

		if(!messagesContainsPlayer(bp))
			addMessage(bp, message);
		else if(getMessage(bp).equalsIgnoreCase(message)) {
			run(bp);
			removeMessage(bp);
		}

		if(getPlayer(bp) > System.currentTimeMillis()) {
			new ConsoleMessage("Time is greater");
			run(bp);
		}
	}

	private static void removeMessage(String bp) {
		messages.remove(bp);
	}

	private static void run(String bp) {
		if(getMax(bp) < BattleSettings.getAntiSpamAmount()) {
			new ConsoleMessage("Max is greater" + getMax(bp));
			editMax(bp, 1);
		}else {
			new ConsoleMessage("Muting");
			BattlePlayer b;
			try {
				b = BattlePunishments.createBattlePlayer(bp);
			} catch (Exception e) {
				return;
			}
			b.mute("Muted for spamming", -1, "System");

			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission(BattlePerms.MUTE))
					p.sendMessage(RED + "System just permamuted "+bp+" due to spamming.");
			}
		}
	}

	private static String getMessage(String bp) {
		return messages.get(bp);
	}

	private static void addMessage(String bp, String message) {
		messages.put(bp, message);
	}

	private static boolean messagesContainsPlayer(String bp) {
		return messages.containsKey(bp);
	}

	private static void editMax(String bp, int i) {
		max.put(bp, getMax(bp) + i);
	}

	private static int getMax(String bp) {
		if(!max.containsKey(bp)) {
			max.put(bp, 1);
			return 0;
		}else
			return max.get(bp);
	}

	public static void editPlayer(String bp, long l) {
		cooldowns.put(bp, l);
	}

	public static boolean containsPlayer(String bp) {
		return cooldowns.containsKey(bp);
	}

	public static long getPlayer(String bp) {
		return cooldowns.get(bp);
	}
}
