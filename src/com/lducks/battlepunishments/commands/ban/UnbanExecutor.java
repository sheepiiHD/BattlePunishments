package com.lducks.battlepunishments.commands.ban;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class UnbanExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.BAN)
	public void onUnban(CommandSender sender, String p) {
		final Pattern ip = Pattern.compile(
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher matcher = ip.matcher(p);

		if(matcher.matches()){ //tis an ip
			List<String> l1 = BattlePunishments.getIPListController().getPlayerList(p);
			if(l1.size() == 0){
				sender.sendMessage(RED + "Sorry, but that IP has never logged into the server. ");
				return;
			}

			for(String n : l1){
				BattlePlayer bp;
				try {
					bp = BattlePunishments.createBattlePlayer(n);
				} catch (Exception e) {
					sender.sendMessage("Player not found.");
					return;
				}
				unbanPlayer(bp, sender);
			}

		}else{
			BattlePlayer bp;
			try {
				bp = BattlePunishments.createBattlePlayer(p);
			} catch (Exception e) {
				sender.sendMessage("Player not found.");
				return;
			}
			unbanPlayer(bp, sender);
		}
	}

	private static void unbanPlayer(BattlePlayer bp, CommandSender sender) {
		if(bp.isBanned()){
			bp.unban();
			sender.sendMessage(RED + bp.getName() + " unbanned.");
			if(BattleSettings.useBattleLog())
				BattleLog.addMessage(sender.getName() +" has unbanned "+bp.getRealName());
		}else{
			sender.sendMessage(RED + bp.getName() + " is not banned.");
		}
	}
}
