package com.lducks.battlepunishments.util.webrequests;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import mc.battleplugins.webapi.controllers.encoding.EncodingType;
import mc.battleplugins.webapi.object.URLData;
import mc.battleplugins.webapi.object.WebURL;
import mc.battleplugins.webapi.object.callbacks.URLResponseHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.WebAPIListener;

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

	public static void sendErrorReport(Exception e) throws Exception{
		sendErrorReport(e.toString());
	}

	public static void sendErrorReport(Throwable exception) throws Exception {
		sendErrorReport(exception.getMessage());
	}

	public static void sendErrorReport(String message) throws Exception {
		WebURL err = new WebURL(new URL("http://battleplugins.com/grabber/error.php"));
		err.addData("server", serverip);
		err.addData("plugin", "BattlePunishments");
		err.addData(new URLData("error", message, EncodingType.HEX));

		System.out.println(err.getURLString());

		err.getPage(new URLResponseHandler() {

			@Override
			public void validResponse(BufferedReader br) throws IOException {
				System.out.println(br.readLine());
			}

			@Override
			public void invalidResponse(Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void runCheckTimer(final CommandSender sender) {
		WebAPIListener.timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattlePunishments.getPlugin(), new Runnable() {

			int i;

			@Override
			public void run() {
				if(i > 5) {
					sender.sendMessage(RED + "Connection timed out");
					cancelThis();
					return;
				}

				sender.sendMessage(YELLOW + "Checking....");
				i++;
			}
		}, 0L, 60L);
	}

	private static void cancelThis() {
		Bukkit.getScheduler().cancelTask(WebAPIListener.timerid);
	}
}