package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.convertplugins.ConvertCommandBook;
import com.lducks.battlepunishments.convertplugins.ConvertEssentials;
import com.lducks.battlepunishments.convertplugins.ConvertFlatFile;
import com.lducks.battlepunishments.convertplugins.ConvertVanilla;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.WebAPIListener;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.PluginLoader;
import com.lducks.battlepunishments.util.webrequests.ConnectionCode;

/**
 * 
 * @author lDucks
 *
 */

public class BattlePunishmentsExecutor extends CustomCommandExecutor {
	@MCCommand(perm=BattlePerms.HELP, cmds="help")
	public void onHelpCommand(CommandSender sender, Integer page) {
		help(page,sender);
	}

	@MCCommand(cmds="version")
	public void onVersionCommand(CommandSender sender) {
		sender.sendMessage(YELLOW + BattlePunishments.getPluginName() + " " + BattlePunishments.getVersion());
	}

	@MCCommand(op=true, cmds="check")
	public void onCheck(final CommandSender sender) {
		if(!BattleSettings.useWebsite()) {
			sender.sendMessage(RED + "You have website set to false in the config file, meaning you can not use the syncing abilities.");
			return;
		}

		if(BattlePunishments.getServerIP() == null) {
			sender.sendMessage(RED + "Your server IP has not yet registered.");
			BattlePunishments.getServerIP();
			return;
		}

		String ip = null;
		try {
			ip = BattlePunishments.getServerIP();

			if(ip == null) {
				sender.sendMessage(ChatColor.RED + "Error");
				return;
			}
		}catch(Exception e) {
			sender.sendMessage(ChatColor.RED + "Error");
			return;
		}

		ConnectionCode.validConnectionCode(sender.getName());

		WebAPIListener.timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattlePunishments.getPlugin(), new Runnable() {

			int i = 0;

			@Override
			public void run() {

				if(i > 5) {
					sender.sendMessage(RED + "Connection timed out");
					cancelThis();
					return;
				}

				sender.sendMessage(YELLOW + "Checking....");

				i++;
			}
		}, 0L, 60L);
	}

	@MCCommand(op=true, cmds={"verify"})
	public void onVerifyExecute(final CommandSender sender, String key) {

		if(!BattleSettings.useWebsite()) {
			sender.sendMessage(RED + "You have website set to false in the config file, meaning you can not use the syncing abilities.");
			return;
		}

		String ip = null;
		try {
			ip = BattlePunishments.getServerIP();
			sender.sendMessage(ChatColor.RED + "Error");	

			if(ip == null) {
				sender.sendMessage(ChatColor.RED + "Error");
				return;
			}
		}catch(Exception e) {
			sender.sendMessage(DARK_RED + "There was an error registering this server.");
			new DumpFile("onVerifyExecute", e, "Error registering server");
			return;
		}

		ConnectionCode.setKey(key);
		ConnectionCode.validConnectionCode(sender.getName());

		WebAPIListener.timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattlePunishments.getPlugin(), new Runnable() {

			int i;

			@Override
			public void run() {

				if(i > 5) {
					sender.sendMessage(RED + "Connection timed out");
					cancelThis();
					return;
				}

				sender.sendMessage(YELLOW + "Verifying....");
				i++;
			}
		}, 0L, 60L);

	}

	private void cancelThis() {
		Bukkit.getScheduler().cancelTask(WebAPIListener.timerid);
	}

	@MCCommand(op=true, cmds={"convert"})
	public void onConvert(CommandSender sender, String type, String plugin) {
		if(type.equalsIgnoreCase("ban")) {
			if(plugin.equalsIgnoreCase("essentials")) {
				if(PluginLoader.essentialsInstalled()) {
					ConvertEssentials.runBans();
					sender.sendMessage(GREEN + "Essentials bans have been converted.");
					//					sender.sendMessage(RED + "This feature is currently disabled.");
				}else {
					sender.sendMessage(RED + "Essentials is not installed!");
				}
			}else if(plugin.equalsIgnoreCase("commandbook")) {
				if(PluginLoader.cmdBookInstalled()) {
					ConvertCommandBook.runBans();
					sender.sendMessage(RED + "This feature is currently disabled.");
				}else {
					sender.sendMessage(RED + "CommandBook is not installed!");
				}
			}else if(type.equalsIgnoreCase("vanilla")){
				ConvertVanilla.runBans();				
				sender.sendMessage(GREEN + "Vanilla bans have been converted.");
			}else if(type.equalsIgnoreCase("flatfile")){
				ConvertFlatFile.runBans();				
				sender.sendMessage(GREEN + "Flatfile bans have been converted.");
			}else {
				sender.sendMessage(RED + "Unknown plugin!");
			}
		}else {
			sender.sendMessage(RED + "Invalid type!");
		}
	}

	private static void help(int i, CommandSender sender) {
		sender.sendMessage(DARK_GRAY + "----- " + BLUE + "BattlePunishments v"+BattlePunishments.getVersion() +
				" ("+i+"/7)" + DARK_GRAY + " -----");
		switch(i){
		case 1:	
			sender.sendMessage(YELLOW + "/kick - Kicks the player, do /kick for help");
			sender.sendMessage(YELLOW + "/ban - Bans the player, do /ban for help.");
			sender.sendMessage(YELLOW + "/unban - Unbans the player, do /unban for help.");
			break;
		case 2:
			sender.sendMessage(YELLOW + "/gmute - Mutes the player, do /gmute for help.");
			sender.sendMessage(YELLOW + "/unmute - Unmutes the player, do /unmute for help.");
			sender.sendMessage(YELLOW + "/fm - Mutes a player for 60 minutes, do /fm for help.");
			sender.sendMessage(YELLOW + "/muteall - Mutes the entire chat (does not persist through reloads).");
			break;
		case 3:
			sender.sendMessage(YELLOW + "/playerinfo - Tells you any availible information about the player" +
					" including bans, mutes, and strikes. Do /playerinfo for help.");
			sender.sendMessage(YELLOW + "/editstk - Edits the amount of strikes a player has, do /editstk for help.");
			break;
		case 4:
			sender.sendMessage(YELLOW + "/watchlist - Shows you the watchlist. /watchlist [add|del|tp|tpr] to add or remove players.");
			sender.sendMessage(YELLOW + "/tpr - Teleports to a random online player.");
			sender.sendMessage(YELLOW + "/respond <player>, tells the player you are helping them (if they just used /needhelp).");
			break;
		case 5:
			sender.sendMessage(YELLOW + "/block - Blocks a player from using a command, do /block for help.");
			sender.sendMessage(YELLOW + "/unblock - Unblocks a player from using a command, do /unblock for help.");
			sender.sendMessage(YELLOW + "/blocklist - Shows you what commands are blocked for a specific " +
					"player, do /blocklist for help.");
			sender.sendMessage(YELLOW + "/nick - Gives a nickname to a player, do /nick for help.");
			break;
		case 6:
			sender.sendMessage(YELLOW + "/ip - Shows you the information for a player/ip, do /ip for help.");
			sender.sendMessage(YELLOW + "/clearips - Clears a player's IP addresses, do /ip for help.");
			sender.sendMessage(YELLOW + "/id - Tells you information about the item in your hand.");
			sender.sendMessage(YELLOW + "/announce - Shows you all the announcements that the server has.");
			break;
		case 7:
			sender.sendMessage(YELLOW + "/tplast - Teleports you to the player's last location.");
			sender.sendMessage(YELLOW + "/bpreload - Relaods the config.");
			sender.sendMessage(YELLOW + "/force - Forces a player to enter a command.");
			sender.sendMessage(YELLOW + "/stalk - Listens to all private messages being sent to a player.");
			break;
		default:
			sender.sendMessage(RED + "Page number out of reach!");
			break;
		}
	}
}