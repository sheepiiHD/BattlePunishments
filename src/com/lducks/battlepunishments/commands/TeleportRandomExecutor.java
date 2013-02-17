package com.lducks.battlepunishments.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.util.BattlePerms;

/**
 * 
 * @author lDucks
 *
 */

public class TeleportRandomExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.TPR, inGame=true)
	public void onTeleportRandomCommand(Player p) {



		List<Player> players = Arrays.asList(Bukkit.getOnlinePlayers());

		Random rand = new Random();
		int num = rand.nextInt(players.size());

		Player player = players.get(num);

		if(player.hasPermission(BattlePerms.TPR)){
			Bukkit.dispatchCommand(p, "tpr");
			return;
		}

		p.teleport(player);

		p.sendMessage(GREEN + "You have been teleported to "+ RED + player.getName() + GREEN + " randomly.");
		new ConsoleMessage("Teleported "+p.getName()+" to "+player.getName()+" randomly.");
	}
}
