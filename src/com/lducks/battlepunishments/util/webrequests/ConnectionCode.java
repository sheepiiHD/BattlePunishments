package com.lducks.battlepunishments.util.webrequests;

import java.io.File;
import java.net.URL;

import mc.battleplugins.webapi.object.WebURL;

import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;

/**
 * 
 * @author lDucks
 *
 */

public class ConnectionCode {

	private static YamlConfiguration config;
	private static File f;
	private static boolean valid;

	/**
	 * 
	 * @param f
	 * Set the key's file
	 */
	public void setConfig(File f){
		ConnectionCode.f = f;
		config = new YamlConfiguration();
		try {
			load();
		} catch (Exception e){
			e.printStackTrace();
		}

		new ConsoleMessage("BattleSettings setconfig");

	}

	private static void load(){
		try{
			config.load(f);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void save(){
		try{
			config.save(f);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return key
	 */
	public static String getConnectionCode() {
		return config.getString("connection");
	}

	/**
	 * 
	 * @param key
	 * Do not edit this key, this will break the plugin!
	 * 
	 */
	public static void setKey(String key) {
		config.set("connection", key);
		save();
	}

	/**
	 * @param caller Person calling the request
	 * @return boolean Is server's connection code is valid
	 */
	public static boolean validConnectionCode(String caller) {
		WebURL url;
		try {
			url = new WebURL(new URL("http://www.BattlePunishments.net/grabbers/register.php"));
		} catch (Exception e) {
			new DumpFile("validConnectionCode", e, "Error validating connection code");
			setValid(false);
			return false;
		}

		url.addData("server", BattlePunishments.getServerIP());
		url.addData("key", getConnectionCode());
		url.sendData();

		return valid;
	}

	/**
	 * @param valid
	 */
	public static void setValid(boolean v) {
		valid = v;
	}
}
