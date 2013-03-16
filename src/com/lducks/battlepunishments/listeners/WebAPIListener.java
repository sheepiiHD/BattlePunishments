package com.lducks.battlepunishments.listeners;

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

import com.lducks.battlepunishments.BattlePunishments;

/**
 * @author lDucks
 *
 */
public class WebAPIListener implements Listener{

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

						if(BattlePunishments.getServerIP() != null)
							valid = BattlePunishments.getServerIP().equalsIgnoreCase(line);
						else
							valid = false;

						if(timerid != -2) {
							Bukkit.getScheduler().cancelTask(timerid);
							timerid = -2;

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
