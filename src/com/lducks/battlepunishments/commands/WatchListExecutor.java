package com.lducks.battlepunishments.commands;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.controllers.watchlist.WatchListController;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class WatchListExecutor extends CustomCommandExecutor{

	WatchListController wlc = BattlePunishments.getWatchListController();
	
	@MCCommand(perm=BattlePerms.WATCHLIST, inGame=false)
	public void onWatchListCommand(CommandSender sender, String[] args) {
		sendWatchlist(sender);
	}
	
	@MCCommand(cmds="clear", perm=BattlePerms.WATCHLIST, inGame=false)
	public void onWatchListClear(CommandSender sender) {
		wlc.clear();
		sender.sendMessage(RED + "Watch list has been cleared.");
	}

	@MCCommand(cmds="add", perm=BattlePerms.WATCHLIST)
	public void onWatchListCommandAdd(CommandSender sender, BattlePlayer bp) {
		if(bp.isOnWatchList()){
			sender.sendMessage(RED + "That player is already on the watch list!");
			return;
		}

		bp.addPlayerToWatchList();
		BattlePunishments.setWatchList(wlc.getWatchList());
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(bp.getRealName() + " was just added to the watchlist by " + sender.getName() + ".");
		
		for(Player pl : Bukkit.getOnlinePlayers()){
			if(pl.hasPermission(BattlePerms.WATCHLIST)){
				pl.sendMessage(DARK_RED + bp.getRealName() + YELLOW + " was just added to the watchlist by " + GREEN + sender.getName() + ".");
			}
		}
	}

	@MCCommand(cmds="del", perm=BattlePerms.WATCHLIST)
	public void onWatchListCommandDel(CommandSender sender, BattlePlayer bp) {
		if(!bp.isOnWatchList()){
			sender.sendMessage(RED + "That player is not on the watch list!");
			return;
		}

		bp.removePlayerFromWatchList();
		BattlePunishments.setWatchList(wlc.getWatchList());
		
		sender.sendMessage(RED + "Player was removed.");
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(bp.getRealName() + " was just removed from the watchlist by " + sender.getName() + ".");
	}

	@MCCommand(cmds="tp", perm=BattlePerms.WATCHLIST, inGame=true)
	public void onWatchListCommandTP(CommandSender sender, Integer i) {
		i = i -1;
		Player p = (Player) sender;

		List<String> watchlist = BattlePunishments.getWatchList();

		if(i > watchlist.size()){
			sender.sendMessage(RED + "Number unknown");
			return;
		}

		String name = watchlist.get(i);

		if(Bukkit.getPlayer(name) != null){
			p.teleport(Bukkit.getPlayer(name).getLocation());
			sender.sendMessage(GREEN + "You were teleported!");
		}else{
			sender.sendMessage(RED + "Player is offline!");
		}
	}

	@MCCommand(cmds="tpr", perm=BattlePerms.WATCHLIST, inGame=true)
	public void onWatchListCommandTPR(CommandSender sender) {
		Player p = (Player) sender;
		List<String> pl = BattlePunishments.getWatchList();
		for(String s : wlc.getWatchList()){
			if(Bukkit.getPlayer(s) == null)
				pl.remove(s);
		}

		if(pl.size() == 0){
			sender.sendMessage(RED + "Nobody on the watchlist is online!");
		}else{
			Random rand = new Random();
			int num = rand.nextInt(pl.size());

			Player player = Bukkit.getPlayer(pl.get(num));
			p.teleport(player);

			sender.sendMessage(GREEN + "You have been teleported to "+ RED + player.getName() + GREEN + " randomly.");
		}
	}
	
	@MCCommand(cmds= {"reload","rl"}, perm=BattlePerms.WATCHLIST, inGame=false)
	public void onWatchListReloadCommand(CommandSender sender) {
		BattlePunishments.setWatchList(wlc.getWatchList());
		sender.sendMessage(GREEN + "Watch list reloaded.");
	}

	private static void sendWatchlist(CommandSender p) {
		p.sendMessage(DARK_GRAY + "----- " + BLUE + "Watchlist" + DARK_GRAY + " -----");
		p.sendMessage(YELLOW + "If the player is " + GREEN + "green" + YELLOW + " they are online.");
		p.sendMessage(YELLOW + "Use " + GREEN + " /watchlist tp <number>" + YELLOW 
				+ " to tp to the player.");

		List<String> watchlist = BattlePunishments.getWatchList();

		int i = 1;

		if(watchlist.size() == 0) {
			p.sendMessage(RED + "The watch list is empty.");
			return;
		}

		for(String s : watchlist){
			if(Bukkit.getPlayer(s) != null){
				p.sendMessage(BLUE + "[" + i + "] " + GREEN + s);
			}else{

				BattlePlayer bp;
				try {
					bp = BattlePunishments.createBattlePlayer(s);
				} catch (Exception e) {
					break;
				}

				if(bp.isBanned()) {
					if(BattleSettings.wlRemoveOnBan()) {
						bp.removePlayerFromWatchList();
					}else
						p.sendMessage(BLUE + "[" + i + "] " + RED +  STRIKETHROUGH + s);
				}else
					p.sendMessage(BLUE + "[" + i + "] " + RED + s);
			}

			i++;
		}
	}
}
