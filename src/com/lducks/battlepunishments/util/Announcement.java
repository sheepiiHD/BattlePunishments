package com.lducks.battlepunishments.util;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.controllers.AnnounceController;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.listeners.chat.ChatEditor;

/**
 * 
 * @author lDucks
 * Starts the announcement rotation
 */

public class Announcement {
	public Announcement() {

		final HashMap<Integer, Integer> task = new HashMap<Integer, Integer>();

		AnnounceController an = new AnnounceController();
		an.setConfig(new File(BattlePunishments.getPath()+"/automessage.yml"));

		try {
			AnnounceController.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if(!AnnounceController.isEnabled())
			return;

		int t = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattlePunishments.getPlugin(), new Runnable(){

			int i = 0;

			public void run() {
				try {
					AnnounceController.load();
				} catch (Exception e) {
					new DumpFile("announce", e, "Error loading announcements.");
					return;
				}

				if(!AnnounceController.isEnabled()){
					Bukkit.getServer().getScheduler().cancelTask(task.get(1));
					return;
				}

				String tag = AnnounceController.getTag();
				if(tag == null)
					tag = "";

				if(i >= AnnounceController.size())
					i = 0;

				String msg = tag+" "+AnnounceController.getMessages(i);
				msg = ChatEditor.toChar(msg);
				msg = msg.replaceAll("(&([a-f0-9]))", "\u00A7$2");

				Bukkit.broadcastMessage(msg);
				i++;
			}

		}, 60, 20 * AnnounceController.getTime());

		task.put(1, t);
	}
}