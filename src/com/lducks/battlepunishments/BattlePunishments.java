package com.lducks.battlepunishments;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import mc.battleplugins.webapi.object.WebURL;
import mc.battleplugins.webapi.object.callbacks.URLResponseHandler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.battleplayer.FileBattlePlayer;
import com.lducks.battlepunishments.battleplayer.SQLBattlePlayer;
import com.lducks.battlepunishments.controllers.MineWatchController;
import com.lducks.battlepunishments.controllers.iplist.FileIPListController;
import com.lducks.battlepunishments.controllers.iplist.IPListController;
import com.lducks.battlepunishments.controllers.iplist.SQLIPListController;
import com.lducks.battlepunishments.controllers.watchlist.FileWatchListController;
import com.lducks.battlepunishments.controllers.watchlist.SQLWatchListController;
import com.lducks.battlepunishments.controllers.watchlist.WatchListController;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.BlockListener;
import com.lducks.battlepunishments.listeners.CommandListener;
import com.lducks.battlepunishments.listeners.LoginListener;
import com.lducks.battlepunishments.listeners.LogoutListener;
import com.lducks.battlepunishments.listeners.MiscListener;
import com.lducks.battlepunishments.listeners.SignColors;
import com.lducks.battlepunishments.listeners.SneakListener;
import com.lducks.battlepunishments.listeners.TagAPIListener;
import com.lducks.battlepunishments.listeners.WebAPIListener;
import com.lducks.battlepunishments.listeners.chat.ChatListener;
import com.lducks.battlepunishments.listeners.chat.HeroChatListener;
import com.lducks.battlepunishments.listeners.chat.StalkListener;
import com.lducks.battlepunishments.sql.SQLInstance;
import com.lducks.battlepunishments.sql.SQLSerializerConfig;
import com.lducks.battlepunishments.util.Announcement;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.FileMaker;
import com.lducks.battlepunishments.util.PluginLoader;
import com.lducks.battlepunishments.util.RegisterItems;
import com.lducks.battlepunishments.util.webrequests.ConnectionCode;
import com.lducks.battlepunishments.util.webrequests.PluginUpdater;

/**
 * 
 * @author lDucks
 *
 */

public class BattlePunishments extends JavaPlugin{

	private static String serverip = null;
	private static Logger log = Logger.getLogger("Minecraft");
	private static String name, version;
	private static BattlePunishments plugin;
	private static String path = "plugins/BattlePunishments";
	private static List<String> watchlist;

	/**
	 * If muteall is enabled or disabled
	 */
	public static boolean muteall;

	/**
	 * The current SQL instance (if any)
	 */
	public static SQLInstance sql = null;

	public void onEnable(){
		plugin = this;
		PluginDescriptionFile pdfFile = this.getDescription();
		name = pdfFile.getName();
		version = pdfFile.getVersion();

		this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
		this.getServer().getPluginManager().registerEvents(new LogoutListener(), this);

		if(!PluginLoader.herochatInstalled())
			this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		else
			this.getServer().getPluginManager().registerEvents(new HeroChatListener(), this);

		this.getServer().getPluginManager().registerEvents(new MiscListener(), this);
		this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getServer().getPluginManager().registerEvents(new SneakListener(), this);

		new FileMaker();
		saveDefaultConfig();

		BattleSettings bs = new BattleSettings();
		bs.setConfig(new File(getPath()+"/config.yml"));

		if(BattleSettings.useWebsite()) {
			getServerIP();
			this.getServer().getPluginManager().registerEvents(new WebAPIListener(), this);
			
			try {
				ConnectionCode cc = new ConnectionCode();
				cc.setConfig(new File(getPath()+"/private.key"));
			}catch(Exception e) {
				new DumpFile("ConnectionCode", e, "Error in private.key file or file does not exist");
			}
			
			log.info("[BattlePunishments] Website enabled!");
		}

		try {
			Metrics.runScripts();
		} catch (Exception e) {
			new DumpFile("onEnable", e, "Error enabling Metrics");
		}

		if(BattleSettings.sqlIsEnabled()) {
			sql = new SQLInstance();
			SQLSerializerConfig.configureSQL(this, sql, BattleSettings.getSQLOptions());
		}

		MineWatchController mw = new MineWatchController();
		mw.setConfig(new File(getPath()+"/blocklogger.yml"));

		try {
			MineWatchController.getConfig().load(getPath()+"/blocklogger.yml");
		} catch (Exception e) {
			new DumpFile("MineWatchController", e, "Error loading controller");
		}

		if(BattleSettings.useTagAPI())
			this.getServer().getPluginManager().registerEvents(new TagAPIListener(), this);

		new Announcement();

		RegisterItems.registerCommands();

		if(PluginLoader.vaultInstalled())
			RegisterItems.setupChat();

		if(BattleSettings.getRegisteredCommands().contains("stalk"))
			this.getServer().getPluginManager().registerEvents(new StalkListener(), this);

		this.getServer().getPluginManager().registerEvents(new SignColors(), this);

		if(BattleSettings.autoUpdate())
			PluginUpdater.downloadPluginUpdates(this);

		muteall = false;
	}

	public void onDisable(){
		if(BattleSettings.autoUpdate())
			PluginUpdater.updatePlugin(this);
	}

	/**
	 * 
	 * @return plugin Bukkit Plugin object for BattlePunishments
	 */
	public static BattlePunishments getPlugin() {
		return plugin;
	}

	/**
	 * 
	 * @return plugin Name of plugin (BattlePunishments)
	 */
	public static String getPluginName() {
		return name;
	}

	/**
	 * 
	 * @return plugin Version of plugin 
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * 
	 * @return watchlist Watchlist full of watched players
	 */
	public static List<String> getWatchList() {
		return watchlist;
	}

	/**
	 * 
	 * @param watchlist Watchlist full of watched players
	 */
	public static void setWatchList(List<String> watchlist) {
		BattlePunishments.watchlist = watchlist;
	}

	/**
	 * 
	 * @return config Path to configuration folder
	 */
	public static String getPath() {
		return path;
	}

	/**
	 * 
	 * @param playername Name of the player
	 * @return BattlePlayer BattlePunishments version of playername
	 */
	public static BattlePlayer createBattlePlayer(String playername) {
		if (BattleSettings.sqlIsEnabled()) {
			try {
				return new SQLBattlePlayer(playername, sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				return new FileBattlePlayer(playername);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param playername Name of the player
	 * @param create Creates player if not exists (FlatFile only)
	 * @return BattlePlayer BattlePunishments version of playername
	 */
	public static BattlePlayer createBattlePlayer(String playername, boolean create) {
		if (BattleSettings.sqlIsEnabled()) {
			try {
				return new SQLBattlePlayer(playername, sql);
			} catch (Exception e) {
				new DumpFile("createBattlePlayer", e, "Error creating SQLBattlePlayer!");
				return null;
			}
		} else {
			return new FileBattlePlayer(playername, create);
		}
	}

	/**
	 * 
	 * @return WatchListController SQL or FlatFile version
	 */
	public static WatchListController getWatchListController() {
		if(BattleSettings.sqlIsEnabled())
			return new SQLWatchListController(sql);
		else
			return new FileWatchListController();
	}

	/**
	 * 
	 * @return IPListController SQL or FlatFile version
	 */
	public static IPListController getIPListController() {
		if(BattleSettings.sqlIsEnabled())
			return new SQLIPListController(sql);
		else
			return new FileIPListController();
	}

	/**
	 * 
	 * @param reason Reason for abort
	 * Shuts down the plugin
	 */
	public static void Abort(String reason) {
		log.severe("[" + getPluginName() + "] "+reason+" | ERROR - PLUGIN ABORTING");
		Bukkit.getPluginManager().disablePlugin(plugin);
	}

	/**
	 * 
	 * @return server IP of current server
	 */
	public static String getServerIP(){
		if(serverip == null) {
			final int port = Bukkit.getPort();

			if(Bukkit.getServer().getIp() == null){
				serverip = Bukkit.getServer().getIp();
				if (port != 25565)
					serverip += ":"+port;
			} else {
				URL whatismyip;
				try {
					whatismyip = new URL("http://BattlePunishments.net/grabbers/ip.php");
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

				WebURL url = new WebURL(whatismyip);

				url.getPage(new URLResponseHandler() {
					public void validResponse(final BufferedReader br) throws IOException {
						String ip = br.readLine();
						if(ip == null)
							throw new NullPointerException();
						serverip = ip;
						if (port != 25565)
							serverip += ":"+port;
					}

					public void invalidResponse(Exception e) {
						e.printStackTrace();
					}
				});
			}
		}

		return serverip;
	}
}
