package com.lducks.battlepunishments.listeners;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import java.io.BufferedReader;
import java.io.IOException;

import mc.battleplugins.webapi.controllers.timers.Scheduler;
import mc.battleplugins.webapi.event.SendDataEvent;
import mc.battleplugins.webapi.object.callbacks.URLResponseHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.webrequests.PluginUpdater;
import com.lducks.battlepunishments.util.webrequests.WebConnections;

/**
 * @author lDucks
 *
 */
public class WebAPIListener implements Listener{

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();

		if(p.isOp()) {
			if(BattleSettings.autoUpdate() && PluginUpdater.hasPluginUpdates(BattlePunishments.getPlugin()) != null) {
				p.sendMessage(RED + "It seems you are using an outdated version of BattlePunishments " +
						"(v"+BattlePunishments.getVersion()+")");
				p.sendMessage(RED + "You can get the latest version at " +
						YELLOW + "tiny.cc/BattlePunishments");
			}

			if(BattleSettings.useWebsite() && !WebConnections.validConnectionCode(null)) {
				p.sendMessage(BLUE + "This server is not registered on http://BattlePunishments.net! " +
						"Check it out to see what features you can get by signing up!");
			}
		}
	}

	public static int timerid = -2;

	@EventHandler
	public void onUrlCheckEvent(final SendDataEvent event) {
		event.getURL().getPage(new URLResponseHandler() {
			public void validResponse(BufferedReader br) throws IOException {
				final String line;
				try {
					line = br.readLine();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				Scheduler.scheduleSynchrounousTask(new Runnable() {
					public void run() {
						boolean valid = false;

						if(WebConnections.getServerIP() != null)
							valid = WebConnections.getServerIP().equalsIgnoreCase(line);
						else
							valid = false;

						WebConnections.setValid(valid);

						if(timerid != -2) {
							Bukkit.getScheduler().cancelTask(timerid);
							timerid = -2;

							if(event.getCaller() == null)
								return;

							Player p = Bukkit.getPlayer(event.getCaller());
							if(p != null) {
								if(valid) {
									p.sendMessage(ChatColor.GREEN + "Connection verified");

									long time = event.getDuration();
									p.sendMessage(ChatColor.GREEN + "System took "+ ChatColor.YELLOW + time 
											+ ChatColor.GREEN + " millisecond(s) to connect.");

								}else
									p.sendMessage(ChatColor.RED + "Connection could not be verified");
							}
						}
					}
				});
			}

			public void invalidResponse(Exception e) {
				Scheduler.scheduleSynchrounousTask(new Runnable() {
					public void run() {
						if(timerid != -2) {
							Bukkit.getScheduler().cancelTask(timerid);
							timerid = -2;
							Player p = Bukkit.getPlayer(event.getCaller());
							if(p != null)
								p.sendMessage(ChatColor.RED + "Connection could not be verified");
						}
					}
				});
			}
		});
	}
}
