package com.lducks.battlepunishments.util;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.chat.ChatEditor;

/**
 * 
 * @author lDucks
 * All the settings in the config
 */
public class BattleSettings {
	private static YamlConfiguration config;
	private static File f;

	/**
	 * 
	 * @param f
	 * Sets config file
	 */
	public void setConfig(File f){
		BattleSettings.f = f;
		config = new YamlConfiguration();
		try {
			config.load(f);
		} catch (Exception e){
			e.printStackTrace();
		}

		new ConsoleMessage("BattleSettings setconfig");

	}

	public static boolean useStrikes() {
		return config.getBoolean("strikes.use");
	}

	public static boolean requireKickReason() {
		return config.getBoolean("kick.requirereason");
	}

	public static boolean requiresHelpReason() {
		return config.getBoolean("needhelp.requirereason");
	}

	public static int getMaxStrikes(){
		return config.getInt("strikes.max");
	}

	public static boolean getSmartBans(){
		return config.getBoolean("bans.ipban.smartban");
	}

	public static boolean logCommands() {
		return config.getBoolean("logcommands");
	}

	public static boolean getStrikesAutoban() {
		return config.getBoolean("strikes.autoban");
	}

	/**
	 * @return boolean Whether or not to change the player's "tabbed" name if he has a nickname
	 */
	public static boolean changedTabName() {
		return config.getBoolean("nicknames.changetabname");
	}
	
	public static boolean useTagAPI(){
		return config.getBoolean("nicknames.usetagapi");
	}

	public static boolean wlRemoveOnBan() {
		return config.getBoolean("watchlist.removewhenbanned");
	}

	public static boolean dataSaverCaching() {
		return config.getBoolean("datasaver.cache");
	}

	public static boolean wlMessages() {

		if(!BattleSettings.getRegisteredCommands().contains("watchlist")) {
			config.set("watchlist.loginmessage", false);
			try {
				config.save(f);
			}catch(Exception e) {
				new DumpFile("watchlist", e, "Error with watchlist!");
			}
		}

		return config.getBoolean("watchlist.loginmessage");
	}

	public static long getNeedHelpTimeout() {
		return (long) config.getInt("needhelp.cooldown") * 1000;
	}

	public static int getIdSize() {
		return config.getInt("maxidsize");
	}

	public static String getDefaultWorld() {
		return config.getString("defaultworld");
	}

	public static boolean isDebugging() {
		return config.getBoolean("debugging");
	}

	public static List<String> getRegisteredCommands() {
		return config.getStringList("activecommands");
	}

	public static boolean containsBlockList() {
		return config.getBoolean("playerinfo.containsblocklist");
	}

	public static boolean isAntiSpam() {
		return config.getBoolean("antispam.enabled");
	}

	public static long getAntiSpamTime() {
		return config.getLong("antispam.time") * 1000;
	}

	public static int getAntiSpamAmount() {
		return config.getInt("antispam.amount");
	}

	public static ConfigurationSection getSQLOptions() {
		return config.getConfigurationSection("sqloptions");
	}

	public static boolean sqlIsEnabled() {
		return config.getBoolean("sqloptions.enabled");
	}

	public static boolean autoUpdate() {
		return config.getBoolean("autoupdate");
	}

	public static boolean customChat() {
		return config.getBoolean("customchat.enabled");
	}

	public static String getCustomChatFormat() {
		return config.getString("customchat.format");
	}

	public static boolean welcomeMessage() {
		return config.getBoolean("welcomemessage.enabled");
	}

	public static String getWelcomeMessage() {
		return config.getString("welcomemessage.format");
	}

	public static List<String> getChatStalkerList() {
		return config.getStringList("stalkcommand.stalklist");
	}

	public static String getStalkMesssageFormat(String name, String message) {
		String format = config.getString("stalkcommand.format");
		format = format.replace("{sender}", name);
		format = ChatEditor.colorChat(format);
		format = format.replace("{message}", message);
		return format;
	}

	public static boolean cannotForceSameLevel() {
		return config.getBoolean("force.samelevel");
	}
	
	public static boolean showBanner() {
		return config.getBoolean("bans.showbanner");
	}
	
	public static boolean useBattleLog() {
		return config.getBoolean("battlelog");
	}

	/**
	 * @return boolean If bans.notifyserver.enabled is set to true or false in the config.
	 */
	public static boolean notifyOnBan() {
		return config.getBoolean("bans.notifyserver.enabled");
	}

	/**
	 * 
	 * @param banner Name of banner (String)
	 * @param player Name of player (String)
	 * @param unbantime The time (long) that the player will be unbanned
	 * @param reason Reason player is banned
	 * @return format Format of the message in the config under bans.notifyserver.format
	 */
	public static String getNotifyOnBanMessage(String banner, String player, long unbantime, String reason) {
		String s = config.getString("bans.notifyserver.format");
		s.replace("{banner}", banner);
		s.replace("{player}", player);
		s.replace("{unbantime}", TimeConverter.convertToString(unbantime));
		s.replace("{reason}", reason);
		
		s = ChatEditor.colorChat(s);
		
		return s;
	}

	/**
	 * @return boolean Whether the website option is true or false
	 */
	public static boolean useWebsite() {
		return config.getBoolean("website");
	}
}
