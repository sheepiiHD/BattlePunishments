package com.lducks.battlepunishments.commands.ban;

import static org.bukkit.ChatColor.RED;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.TimeConverter;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;
import com.lducks.battlepunishments.util.webrequests.UpdateDatabase;


public class BanExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.BAN)
	public void onBan(CommandSender sender, String[] args) {
		if(args.length < 4 && BattleSettings.useStrikes()){
			sender.sendMessage(RED + "Usage: /ban <player|IP> <strikes> <time> <reason>");
			sender.sendMessage(RED + "Example: /ban "+sender.getName()+" 1 2d,3h Fly hacking");
			sender.sendMessage(RED+ "Use -1 for the time to permaban a player.");
		}else if(args.length < 3 && !BattleSettings.useStrikes()){
			sender.sendMessage(RED + "Usage: /ban <player|IP> <time> <reason>");
			sender.sendMessage(RED + "Example: /ban "+sender.getName()+" 2d,3h Fly hacking");
			sender.sendMessage(RED + "Use -1 for the time to permaban a player.");
		}else{

			String strikes = "0";
			if(BattleSettings.useStrikes())
				strikes = args[1];

			String time;

			if(BattleSettings.useStrikes())
				time = args[2].trim();
			else
				time = args[1].trim();

			long t = -1;

			if(!time.contains("-1")) {
				try {
					t = TimeConverter.convertToLong(System.currentTimeMillis(), time);
				}catch(Exception e) {
					sender.sendMessage(RED + "Invalid time.");
					return;
				}
			}

			if(t == 0) {
				sender.sendMessage(RED + "Invalid time");
				return;
			}

			int s = 0;

			try {
				s = Integer.parseInt(strikes);
			}
			catch(NumberFormatException nFE) {
				sender.sendMessage(RED + "You need to enter a valid time and/or strike amount.");
				return;
			}


			if(s < 0 && BattleSettings.useStrikes())
				sender.sendMessage(RED + "You need to enter a valid strike amount.");
			else{
				String player = args[0].toLowerCase();

				final Pattern ip = Pattern.compile(
						"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
				Matcher matcher = ip.matcher(player);

				StringBuilder reason = new StringBuilder();

				int i;

				if(BattleSettings.useStrikes())
					i = 3;
				else
					i = 2;

				for (;i < args.length;i++)
					reason.append(args[i]+" ");

				String r = reason.toString();

				if(matcher.matches()){ //tis an ip
					List<String> l = BattlePunishments.getIPListController().getPlayerList(player);

					if(l.size() != 0){
						for(String n : l){
							BattlePlayer bp = BattlePunishments.createBattlePlayer(n, true);
							banPlayer(bp, r, t, sender, s, true);
							if(sender.hasPermission(BattlePerms.STRIKES))
								bp.editStrikes(s);

							if(bp.getPlayer() != null && bp.getPlayer().hasPermission(BattlePerms.BAN)){
								if(BattleSettings.showBanner())
									bp.kickPlayer("You have been banned by " + sender.getName() + ": " + r);
								else
									bp.kickPlayer("You have been banned: " + r);
								sender.sendMessage(RED + player + " was kicked.");
							}
						}
					}
				}else{
					BattlePlayer bp = BattlePunishments.createBattlePlayer(player, true);
					banPlayer(bp, r, t, sender, s, false);
					if(sender.hasPermission(BattlePerms.STRIKES))
						bp.editStrikes(s);

					if(bp.getPlayer() != null)
						sender.sendMessage(RED + player + " was kicked.");

					if(BattleSettings.showBanner())
						bp.kickPlayer("You have been banned by " + sender.getName() + ": " + r);
					else
						bp.kickPlayer("You have been banned: " + r);
				}

				new ConsoleMessage(sender.getName() + " just banned " + player + "!");

				if(BattleSettings.useBattleLog())
					BattleLog.addMessage(sender.getName() + " just banned " + player);

				if(BattleSettings.notifyOnBan())
					Bukkit.broadcastMessage(BattleSettings.getNotifyOnBanMessage(sender.getName(), player, t, r));

				for(World world : Bukkit.getServer().getWorlds()){
					for(Player player1 : world.getPlayers()){
						if(player1.isOp() || player1.hasPermission(BattlePerms.BAN)){
							if(t == -1)
								player1.sendMessage(RED + sender.getName() + " just permabanned " + player + ": " + r);
							else
								player1.sendMessage(RED + sender.getName() + " just banned " + player + " until " + TimeConverter.convertLongToDate(t) + ": " + r);
						}
					}
				}
			}
		}
	}

	public static void banPlayer(BattlePlayer bp, String r, long time, CommandSender sender, int s, boolean ipbanned){
		bp.ban(r, time, sender.getName(), ipbanned);
		bp.editStrikes(s);

		if(BattleSettings.useWebsite()) {
			try {
				UpdateDatabase.updateStrikes(bp);
				UpdateDatabase.updateBan(bp);
			} catch (Exception e) {
				new DumpFile("banPlayer", e, "Updating BP website");
			}
		}

		if(bp.isMuted() && time == -1)
			bp.unmute();
	}
}
