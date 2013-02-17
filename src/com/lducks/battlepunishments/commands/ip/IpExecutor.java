package com.lducks.battlepunishments.commands.ip;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;


public class IpExecutor extends CustomCommandExecutor{

	public static HashMap<Integer, String> ipmore = new HashMap<Integer, String>();

	@MCCommand(perm=BattlePerms.IPCHECK)
	public void onIpCheck(CommandSender sender, String player) {
		final Pattern ip = Pattern.compile(
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher matcher = ip.matcher(player);

		if(matcher.matches()){ //tis an ip

			List<String> l = BattlePunishments.getIPListController().getPlayerList(player);

			sender.sendMessage(DARK_GRAY + "----- " + BLUE + " Players that have used IP: " + ITALIC
					+ player + DARK_GRAY + " -----");

			if(l == null || l.size() == 0){
				sender.sendMessage(RED + "There is no information for this IP");
				return;
			}

			int i = 1;

			ipmore.clear();

			for(String s : l){
				BattlePlayer bp;
				try {
					bp = BattlePunishments.createBattlePlayer(s);
				} catch (Exception e) {
					break;
				}
				sender.sendMessage(BLUE + "["+i+"] " + GREEN + bp.getRealName());
				ipmore.put(i, s);
				i++;
			}
		}else{ //tis a player
			BattlePlayer bp;
			try {
				bp = BattlePunishments.createBattlePlayer(player);
			} catch (Exception e) {
				return;
			}
			sender.sendMessage(DARK_GRAY + "----- " + BLUE + " IPs for User: " + ITALIC
					+ bp.getRealName() + DARK_GRAY + " -----");

			List<String> l = bp.getIPList();

			if(l.size() == 0){
				sender.sendMessage(RED + "There are no IPs for this player.");
				return;
			}

			if(bp.getPlayer() != null)
				sender.sendMessage(GREEN + "Current IP: "+ YELLOW + bp.getPlayer().getAddress().getAddress());

			int i = 1;

			ipmore.clear();

			for(String s : l){
				sender.sendMessage(BLUE + "["+i+"] " + GREEN + s.replace("-", "."));
				ipmore.put(i, s);
				i++;
			}
		}
		sender.sendMessage(DARK_GRAY + "Use /ipmore <number> to get information on the ip/player");
	}
}
