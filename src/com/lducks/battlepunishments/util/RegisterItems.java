package com.lducks.battlepunishments.util;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.commands.BroadcastExecutor;
import com.lducks.battlepunishments.commands.ChatStalkerExecutor;
import com.lducks.battlepunishments.commands.ForceExecutor;
import com.lducks.battlepunishments.commands.BattlePunishmentsExecutor;
import com.lducks.battlepunishments.commands.ItemInformationExecutor;
import com.lducks.battlepunishments.commands.KickExecutor;
import com.lducks.battlepunishments.commands.NickNameExecutor;
import com.lducks.battlepunishments.commands.PlayerInfoExecutor;
import com.lducks.battlepunishments.commands.ReloadExecutor;
import com.lducks.battlepunishments.commands.SneakExecutor;
import com.lducks.battlepunishments.commands.StrikesExecutor;
import com.lducks.battlepunishments.commands.TeleportLastExecutor;
import com.lducks.battlepunishments.commands.TeleportRandomExecutor;
import com.lducks.battlepunishments.commands.WatchListExecutor;
import com.lducks.battlepunishments.commands.ban.BanExecutor;
import com.lducks.battlepunishments.commands.ban.UnbanExecutor;
import com.lducks.battlepunishments.commands.blockcommands.BlockExecutor;
import com.lducks.battlepunishments.commands.blockcommands.BlockListExecutor;
import com.lducks.battlepunishments.commands.blockcommands.UnblockCommandExecutor;
import com.lducks.battlepunishments.commands.ip.ClearIpExecutor;
import com.lducks.battlepunishments.commands.ip.IpExecutor;
import com.lducks.battlepunishments.commands.ip.IpMoreExecutor;
import com.lducks.battlepunishments.commands.mute.FastMuteExecutor;
import com.lducks.battlepunishments.commands.mute.MuteAllExecutor;
import com.lducks.battlepunishments.commands.mute.MuteExecutor;
import com.lducks.battlepunishments.commands.mute.UnmuteExecutor;
import com.lducks.battlepunishments.commands.needhelp.NeedHelpExecutor;
import com.lducks.battlepunishments.commands.needhelp.RespondExecutor;
import com.lducks.battlepunishments.controllers.watchlist.WatchListController;
import com.lducks.battlepunishments.debugging.ConsoleMessage;

/**
 * 
 * @author lDucks
 *
 */

public class RegisterItems {

	public static Chat chat = null;
	
	/**
	 * Registers all the commands if enabled in config
	 */
	public static void registerCommands() {
		if(BattleSettings.getRegisteredCommands().contains("ban")) {
			BattlePunishments.getPlugin().getCommand("ban").setExecutor(new BanExecutor());
			BattlePunishments.getPlugin().getCommand("unban").setExecutor(new UnbanExecutor());
		}

		if(BattleSettings.getRegisteredCommands().contains("mute")) {
			BattlePunishments.getPlugin().getCommand("mute").setExecutor(new MuteExecutor());
			BattlePunishments.getPlugin().getCommand("unmute").setExecutor(new UnmuteExecutor());
			BattlePunishments.getPlugin().getCommand("fm").setExecutor(new FastMuteExecutor());
			BattlePunishments.getPlugin().getCommand("muteall").setExecutor(new MuteAllExecutor());
		}

		if(BattleSettings.getRegisteredCommands().contains("needhelp")) {
			BattlePunishments.getPlugin().getCommand("needhelp").setExecutor(new NeedHelpExecutor());
			BattlePunishments.getPlugin().getCommand("respond").setExecutor(new RespondExecutor());
		}

		if(BattleSettings.getRegisteredCommands().contains("block")) {
			BattlePunishments.getPlugin().getCommand("block").setExecutor(new BlockExecutor());
			BattlePunishments.getPlugin().getCommand("unblock").setExecutor(new UnblockCommandExecutor());
			BattlePunishments.getPlugin().getCommand("blocklist").setExecutor(new BlockListExecutor());
		}

		if(BattleSettings.getRegisteredCommands().contains("nick"))
			BattlePunishments.getPlugin().getCommand("nick").setExecutor(new NickNameExecutor());

		BattlePunishments.getPlugin().getCommand("BattlePunishments").setExecutor(new BattlePunishmentsExecutor());

		if(BattleSettings.getRegisteredCommands().contains("ip")) {
			BattlePunishments.getPlugin().getCommand("clearips").setExecutor(new ClearIpExecutor());
			BattlePunishments.getPlugin().getCommand("ip").setExecutor(new IpExecutor());
			BattlePunishments.getPlugin().getCommand("ipmore").setExecutor(new IpMoreExecutor());
		}

		if(BattleSettings.getRegisteredCommands().contains("kick"))
			BattlePunishments.getPlugin().getCommand("kick").setExecutor(new KickExecutor());

		if(BattleSettings.getRegisteredCommands().contains("bc"))
			BattlePunishments.getPlugin().getCommand("bc").setExecutor(new BroadcastExecutor());

		if(BattleSettings.getRegisteredCommands().contains("tpr"))
			BattlePunishments.getPlugin().getCommand("tpr").setExecutor(new TeleportRandomExecutor());

		if(BattleSettings.getRegisteredCommands().contains("watchlist")) {
			BattlePunishments.getPlugin().getCommand("watchlist").setExecutor(new WatchListExecutor());
			registerWatchList();
		}

		BattlePunishments.getPlugin().getCommand("editstk").setExecutor(new StrikesExecutor());

		if(BattleSettings.getRegisteredCommands().contains("playerinfo"))
			BattlePunishments.getPlugin().getCommand("playerinfo").setExecutor(new PlayerInfoExecutor());

		if(BattleSettings.getRegisteredCommands().contains("id"))
			BattlePunishments.getPlugin().getCommand("id").setExecutor(new ItemInformationExecutor());

		if(BattleSettings.getRegisteredCommands().contains("tplast"))
			BattlePunishments.getPlugin().getCommand("tplast").setExecutor(new TeleportLastExecutor());

		BattlePunishments.getPlugin().getCommand("bpreload").setExecutor(new ReloadExecutor());

		if(BattleSettings.getRegisteredCommands().contains("sneak"))
			BattlePunishments.getPlugin().getCommand("sneak").setExecutor(new SneakExecutor());

		if(BattleSettings.getRegisteredCommands().contains("force"))
			BattlePunishments.getPlugin().getCommand("force").setExecutor(new ForceExecutor());

		if(BattleSettings.getRegisteredCommands().contains("stalk"))
			BattlePunishments.getPlugin().getCommand("stalk").setExecutor(new ChatStalkerExecutor());
	}

	
	/**
	 * 
	 * Sets up the Chat if vault is installed
	 * 
	 */
	public static void setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
    }

	
	/**
	 * Registers the watch list
	 */
	public static void registerWatchList() {
		WatchListController wlc = BattlePunishments.getWatchListController();
		List<String> wl = new ArrayList<String>();

		if(wlc.getWatchList() != null)
			wl = wlc.getWatchList();

		BattlePunishments.setWatchList(wl);

		new ConsoleMessage("Default world = " + BattleSettings.getDefaultWorld());

	}
}
