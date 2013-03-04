package com.lducks.battlepunishments.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.chat.ChatEditor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.PluginLoader;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class NickNameExecutor extends CustomCommandExecutor{

	@MCCommand(perm=BattlePerms.NICK)
	public void onNickCommand(CommandSender sender, String player, String[] args) {
		StringBuilder nn = new StringBuilder();

		Player p = Bukkit.getPlayer(player);
		if(p != null)
			player = p.getName();

		BattlePlayer bp;
		try {
			bp = BattlePunishments.createBattlePlayer(player);
		} catch (Exception e1) {
			sender.sendMessage("Player not found.");
			return;
		}

		int amt = args.length - 1;

		for (int i=1;i< args.length;i++){
			if(i != amt){
				nn.append(args[i]+" ");
			}else{
				nn.append(args[i]);
			}
		}

		String n = bp.getRealName();

		if(args.length == 1){
			bp.setNickname(null);
		}else{
			n = ChatEditor.colorChat(nn.toString());
			bp.setNickname(n);
		}

		sender.sendMessage(GREEN + "Player is now known as "+n);
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(bp.getRealName() + "'s nickname was changed by "+sender.getName());
		
		n = n.trim();

		if(bp.getPlayer() != null){
			bp.getPlayer().sendMessage("You are now known as " + n);
			bp.getPlayer().setDisplayName(ChatColor.RESET + n + ChatColor.RESET);
			bp.getPlayer().setPlayerListName(n);

			if(PluginLoader.tagAPIInstalled() && BattleSettings.useTagAPI() == true) {
				try {
					TagAPI.refreshPlayer(bp.getPlayer());
				}catch(Exception e) {
					new DumpFile("onNickCommand", e, "Error giving player a TagAPI nickname.");
				}
			}
		}
	}
}