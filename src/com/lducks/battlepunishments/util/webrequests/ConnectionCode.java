package com.lducks.battlepunishments.util.webrequests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.ConsoleMessage;

/**
 * 
 * @author lDucks
 *
 */

public class ConnectionCode {

	private static YamlConfiguration config;
	private static File f;

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
	 * 
	 * @return boolean
	 * Checks servers connection code is valid
	 */
	public static boolean validConnectionCode() {
		try {
			URL url = new URL("http://www.BattlePunishments.net/grabbers/register.php?server="+BattlePunishments.getServerIP()
					+"&connection="
					+getConnectionCode());
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String line = reader.readLine();

			reader.close();
			
			if(BattlePunishments.getServerIP().equalsIgnoreCase(line)) {
				return true;
			}

			return false;
		}catch(Exception e) {
			return false;
		}
	}

}
