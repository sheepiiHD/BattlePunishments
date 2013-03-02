package com.lducks.battlepunishments.util.webrequests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.util.events.UrlCheckEvent;

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
	 * 
	 * @return boolean
	 * Checks servers connection code is valid
	 */
	public static boolean validConnectionCode() {
		runValidConnection(null);
		return valid;
	}
	
	public static void runValidConnection(final String name) {
		Bukkit.getScheduler().runTaskAsynchronously(BattlePunishments.getPlugin(), new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://www.BattlePunishments.net/grabbers/register.php?server="+BattlePunishments.getServerIP()
							+"&connection="
							+getConnectionCode());
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

					final String line = reader.readLine();

					reader.close();


					final boolean validconnection;
					
					if(BattlePunishments.getServerIP().equalsIgnoreCase(line)) {
						validconnection = true;
					}else {
						validconnection = false;
					}
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(BattlePunishments.getPlugin(), new Runnable() {
						@Override
						public void run() {
							UrlCheckEvent event = new UrlCheckEvent(validconnection, name);
							event.callEvent();
						}
					});

				}catch(Exception e) {return;}
			}
		});
	}

	/**
	 * @param valid
	 */
	public static void setValid(boolean v) {
		valid = v;
	}
}
