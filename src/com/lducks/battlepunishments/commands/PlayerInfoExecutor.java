package com.lducks.battlepunishments.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.command.CommandSender;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.commands.blockcommands.BlockListExecutor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.TimeConverter;

/**
 * 
 * @author lDucks
 *
 */

public class PlayerInfoExecutor extends CustomCommandExecutor{

	@MCCommand(cmds= {"me","self"}, perm=BattlePerms.PLAYERINFO_ME, inGame=true)
	public void onPlayerInfoMe(CommandSender sender) {
		BattlePlayer bp = BattlePunishments.createBattlePlayer(sender.getName());
		sendInfo(sender, bp);
	}

	@MCCommand(perm=BattlePerms.PLAYERINFO)
	public void onPlayerInfoCommand(CommandSender sender, BattlePlayer bp) {
		sendInfo(sender, bp);
		
		if(bp.isMuted()){
			String muter = bp.getMuter();
			String reason = bp.getMuteReason();

			long t = bp.getMuteTime();

			if(t == -1){
				sender.sendMessage(RED + "Player is Permamuted");
				sender.sendMessage(GREEN + "Muted By: " + YELLOW + muter);
				sender.sendMessage(GREEN + "Reason: " + YELLOW + reason);
				sender.sendMessage(GREEN + "Time Of Mute: "+YELLOW + TimeConverter.convertLongToDate(bp.getTimeOfMute()));
			}else{
				sender.sendMessage(RED + "Player is Muted");
				sender.sendMessage(GREEN + "Muted By: " + YELLOW + muter);
				sender.sendMessage(GREEN + "Reason: " + YELLOW + reason);
				sender.sendMessage(GREEN + "Unmute Time: " + YELLOW + TimeConverter.convertLongToDate(t));
				sender.sendMessage(GREEN + "Time Of Mute: "+YELLOW + TimeConverter.convertLongToDate(bp.getTimeOfMute()));
			}
		}

		if(bp.isBanned()){
			String banner = bp.getBanner();
			String reason = bp.getBanReason();

			long t = bp.getBanTime();

			if(t == -1){
				sender.sendMessage(RED + "Player is Permabanned");
				sender.sendMessage(GREEN + "Banner: " + YELLOW + banner);
				sender.sendMessage(GREEN + "Reason: " + YELLOW + reason);
				sender.sendMessage(GREEN + "Time Of Ban: "+YELLOW + TimeConverter.convertLongToDate(bp.getTimeOfBan()));
			}else{
				sender.sendMessage(RED + "Player is Banned");
				sender.sendMessage(GREEN + "Banner: " + YELLOW + banner);
				sender.sendMessage(GREEN + "Reason: " + YELLOW + reason);
				sender.sendMessage(GREEN + "Unban Time: " + YELLOW + TimeConverter.convertLongToDate(t));
				sender.sendMessage(GREEN + "Time Of Ban: "+YELLOW + TimeConverter.convertLongToDate(bp.getTimeOfBan()));
			}
		}
	}

	public void sendInfo(CommandSender sender, BattlePlayer bp) {
		String n = bp.getRealName();
		if(n == null)
			n = bp.getName();

		sender.sendMessage(DARK_GRAY + "----- " + BLUE + "Player Info For: " + ITALIC + n + DARK_GRAY + " -----");

		if(BattleSettings.useStrikes() && sender.hasPermission(BattlePerms.STRIKES)){
			int s = bp.getStrikes();
			sender.sendMessage(GREEN + "Strikes: " + YELLOW + s + "/" + 
					BattleSettings.getStrikesMax());
		}

		if(bp.getNickname() != null) {
			if(bp.getNickname().equalsIgnoreCase(bp.getRealName())) {
				bp.setNickname(null);
			}else {
				sender.sendMessage(GREEN + "Nickname: " + WHITE + bp.getNickname());
			}
		}

		if(bp.getFirstSeen() != null) {
			sender.sendMessage(GREEN + "First seen: " + YELLOW + bp.getFirstSeen());
		}
		
		if(bp.getPlayer() != null) {
			sender.sendMessage(GREEN + "Last seen: " + YELLOW + "Now");
		}else if(bp.getLastSeen() != null) {
			sender.sendMessage(GREEN + "Last seen: " + YELLOW + bp.getLastSeen());
		}

		if(BattleSettings.useWebsite()) {
			sender.sendMessage(GREEN + "Profile: " + YELLOW + "http://bcpvp.net/profile/"+bp.getRealName());
		}
		
		if(BattleSettings.containsBlockList()) {
			if(bp.getBlockList().size() > 0 && BattleSettings.getRegisteredCommands().contains("block")) {
				sender.sendMessage(GREEN + "Blocked Commands:");
				BlockListExecutor.sendList(sender, bp);
			}
		}
	}


}
