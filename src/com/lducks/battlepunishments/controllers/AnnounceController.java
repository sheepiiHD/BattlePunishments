package com.lducks.battlepunishments.controllers;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
/**
 * 
 * @author lDucks
 * Announcement config
 * 
 */
public class AnnounceController {
	private static YamlConfiguration config;
	private static File f;

	public void setConfig(File f){
		AnnounceController.f = f;
		config = new YamlConfiguration();
		try {
			config.load(f);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void load(){
		try {
			config.load(f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean b = false;

		if(!config.contains("config.intervalinseconds")){
			config.set("config.intervalinseconds", 60);
			b = true;
		}

		if(!config.contains("config.enabled")){
			config.set("config.enabled", true);
			b = true;
		}

		if(!config.contains("config.tag")){
			config.set("config.tag", "&6[HAVOC]");
			b = true;
		}

		if(!config.contains("msg")){
			List<String> l = config.getStringList("msg");
			l.add("&2This Is A Test Message");
			l.add("&4This Is A Test Message2");
			config.set("msg", l);
			b = true;
		}

		if(b){
			save();
		}
	}

	public static void save(){
		try{
		config.save(f);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String getMessages(int i) {
		return config.getStringList("msg").get(i);
	}

	public static int getTime() {
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config.getInt("config.intervalinseconds");
	}

	public static String getTag() {
		return config.getString("config.tag");
	}

	public static boolean isEnabled() {
		return config.getBoolean("config.enabled");
	}

	public static int size() {
		return config.getStringList("msg").size();
	}
}
