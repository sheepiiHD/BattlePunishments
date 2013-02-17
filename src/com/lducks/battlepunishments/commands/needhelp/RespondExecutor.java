package com.lducks.battlepunishments.commands.needhelp;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.PluginLoader;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;


public class RespondExecutor extends CustomCommandExecutor{
	@MCCommand(perm=BattlePerms.RESPOND, inGame=true)
	public void onRespond(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(DARK_GRAY + "----- " + BLUE + "Help Request List" + DARK_GRAY + " -----");
			sender.sendMessage(YELLOW + "Usage: /respond <player>");
			sender.sendMessage(YELLOW + "Players who still need help:");

			if(NeedHelpExecutor.timeout.size() == 0){
				sender.sendMessage(GRAY + "Nobody needs help right now.");
				return;
			}

			for(String p : NeedHelpExecutor.timeout.keySet()){
				if(NeedHelpExecutor.timeout.get(p) < System.currentTimeMillis())
					NeedHelpExecutor.timeout.remove(p);
			}

			if(NeedHelpExecutor.timeout.size() == 0){
				sender.sendMessage(GRAY + "Nobody needs help right now.");
				return;
			}

			for(String p : NeedHelpExecutor.timeout.keySet()){
				sender.sendMessage(GREEN + p);
			}
			return;
		}else{
			String player = args[0];
			if(Bukkit.getPlayer(player) == null){
				sender.sendMessage(RED + "This player is not online.");
			}else if(!NeedHelpExecutor.timeout.containsKey(Bukkit.getPlayer(player).getName())){
				sender.sendMessage(RED + "Player didn't request help or the request has expired.");
			}else{
				Player p = Bukkit.getPlayer(player);
				p.sendMessage(GREEN + "[HELP] " + ((Player)sender).getDisplayName() + GREEN + " will help you shortly, thank you" +
						" for using our ingame support system.");

				for(Player pl : Bukkit.getOnlinePlayers()){
					if(pl.hasPermission(BattlePerms.RESPOND)){
						pl.sendMessage(GREEN + "[HELP] " + ((Player)sender).getDisplayName() + GREEN + " responded to " + 
								Bukkit.getPlayer(player).getDisplayName() + GREEN + "'s help request.");
					}
				}

				if(PluginLoader.herochatInstalled()){
					Bukkit.dispatchCommand(sender, "tell "+p.getName());
					p.sendMessage(GREEN + "[HELP] To reply to the admin helping you type /r after they send you a message.");
				}

				if(BattleSettings.useBattleLog())
					BattleLog.addMessage(sender.getName() + " responded to "+player+"'s request for help!");
				NeedHelpExecutor.timeout.remove(Bukkit.getPlayer(player).getName());	
			}
		}
	}
}
