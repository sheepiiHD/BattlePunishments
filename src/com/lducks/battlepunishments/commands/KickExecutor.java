package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;


public class KickExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.KICK)
	public void onKickCommand(CommandSender sender, Player p, String args[]) {
		if(args.length < 2 && BattleSettings.requireKickReason()) {
			sender.sendMessage(RED + "You need to enter a reason to kick someone.");
			return;
		}

		String r = "";

		StringBuilder reason = new StringBuilder();
		for (int i=1;i< args.length;i++)
			reason.append(args[i]+" ");

		r = reason.toString();

		String pname = p.getName();
		BattlePlayer bp;
		
		try {
			bp = BattlePunishments.createBattlePlayer(pname);
		} catch (Exception e) {
			sender.sendMessage("Player not found.");
			return;
		}


		if(p != null && !p.hasPermission(BattlePerms.KICK))
			bp.kickPlayer(r);

		new ConsoleMessage("[" + BattlePunishments.getPluginName() + "] " + sender.getName() + " just kicked " + pname + "!");

		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " just kicked " + bp.getRealName()
					+ "! " + r);
		
		for(World world : Bukkit.getServer().getWorlds()){
			for(Player player1 : world.getPlayers()){
				if(player1.isOp() || player1.hasPermission(BattlePerms.KICK)){
					player1.sendMessage(RED + sender.getName() + " just kicked " + bp.getRealName()
							+ "! " + r);
				}
			}	
		}
	}

	@MCCommand(cmds="*", perm=BattlePerms.KICKALL)
	public void onKickAllCommand(CommandSender sender, String args[]) {
		if(args.length < 2 && BattleSettings.requireKickReason()) {
			sender.sendMessage(RED + "You need to enter a reason to kick someone.");
			return;
		}

		String r = "";

		StringBuilder reason = new StringBuilder();
		for (int i=1;i< args.length;i++)
			reason.append(args[i]+" ");

		r = reason.toString();

		new ConsoleMessage("[" + BattlePunishments.getPluginName() + "] " + sender.getName() + " just kicked everyone!");
		BattlePlayer bp;
		for(Player pl : Bukkit.getOnlinePlayers()) {
			try {
				bp = BattlePunishments.createBattlePlayer(pl.getName());
			} catch (Exception e) {
				break;
			}
			bp.kickPlayer(r);
		}

		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " just kicked everyone"
					+ "! " + r);
		
		for(World world : Bukkit.getServer().getWorlds()){
			for(Player player1 : world.getPlayers()){
				if(player1.isOp() || player1.hasPermission(BattlePerms.KICK)){
					player1.sendMessage(RED + sender.getName() + " just kicked everyone"
							+ "! " + r);
				}
			}	
		}
	}
}