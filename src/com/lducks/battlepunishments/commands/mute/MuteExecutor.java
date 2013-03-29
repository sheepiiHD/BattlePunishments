package com.lducks.battlepunishments.commands.mute;

import static org.bukkit.ChatColor.RED;

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

/**
 * 
 * @author lDucks
 *
 */

public class MuteExecutor extends CustomCommandExecutor{
	
	/**
	 * 
	 * Mute command
	 * 
	 */
	@MCCommand(perm=BattlePerms.MUTE)
	public void onMute(CommandSender sender, String[] args) {
		if(args.length < 4 && BattleSettings.useStrikes()){
			sender.sendMessage(RED + "Usage: /mute <player> <strikes> <time> <reason>");
			sender.sendMessage(RED + "Example: /mute "+sender.getName()+" 1 2d,3h Cursing in chat");
			sender.sendMessage(RED+ "Use -1 for the time to permamute a player.");
		}else if(args.length < 3 && !BattleSettings.useStrikes()){
			sender.sendMessage(RED + "Usage: /mute <player> <time> <reason>");
			sender.sendMessage(RED + "Example: /mute "+sender.getName()+" 2d,3h Cursing in chat");
			sender.sendMessage(RED + "Use -1 for the time to permamute a player.");
		}else{

			String time;
			String strikes = "0";
			if(BattleSettings.useStrikes()) {
				strikes = args[1];
				time = args[2].trim();
			}else
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

				StringBuilder reason = new StringBuilder();

				int i;

				if(BattleSettings.useStrikes())
					i = 3;
				else
					i = 2;

				for (;i < args.length;i++)
					reason.append(args[i]+" ");

				String r = reason.toString();
				BattlePlayer bp;
				try {
					bp = BattlePunishments.createBattlePlayer(player, true);
				} catch (Exception e) {
					sender.sendMessage(RED + "Player not found.");
					return;
				}
				bp.mute(r, t, sender.getName());

				if(sender.hasPermission(BattlePerms.STRIKES))
					bp.editStrikes(s);

				new ConsoleMessage(sender.getName() + " just muted " + player + "!");

				if(BattleSettings.useBattleLog())
					BattleLog.addMessage(sender.getName() + " muted "+bp.getRealName());
				
				try {
					UpdateDatabase.updateMute(bp);
					UpdateDatabase.updateStrikes(bp);
				} catch (Exception e) {
					new DumpFile("banPlayer", e, "Updating BP website");
				}
				
				for(World world : Bukkit.getServer().getWorlds()){
					for(Player player1 : world.getPlayers()){
						if(player1.isOp() || player1.hasPermission(BattlePerms.MUTE)){
							if(t == -1)
								player1.sendMessage(RED + sender.getName() + " just permamuted " + player + ": " + reason);
							else
								player1.sendMessage(RED + sender.getName() + " just muted " + player + " until " + TimeConverter.convertLongToDate(t) + ": " + reason);
						}
					}
				}
			}
		}
	}
}
