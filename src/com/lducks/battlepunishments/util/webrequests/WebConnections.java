package com.lducks.battlepunishments.util.webrequests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import mc.battleplugins.webapi.object.WebURL;
import mc.battleplugins.webapi.object.callbacks.URLResponseHandler;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;

/**
 * 
 * @author lDucks
 *
 */

public class WebConnections {

	private static YamlConfiguration config;
	private static File f;
	private static boolean valid;
	private static String serverip = null;

	/**
	 * 
	 * @param f
	 * Set the key's file
	 */
	public void setConfig(File f){
		WebConnections.f = f;
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

		url.addData("server", getServerIP());
		url.addData("key", getConnectionCode());
		url.sendData(caller);

		return valid;
	}

	/**
	 * @param valid
	 */
	public static void setValid(boolean v) {
		valid = v;
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
