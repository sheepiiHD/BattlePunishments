package com.lducks.battlepunishments.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 
 * @author lDucks
 * BlockListeningList config
 * 
 */
public class MineWatchController {

	private static YamlConfiguration config;
	private static File f;

	public void setConfig(File f){
		MineWatchController.f = f;
		config = new YamlConfiguration();
		try {
			config.load(f);
		} catch (Exception e){
			e.printStackTrace();
		}

		boolean x = false;

		if(!config.contains("enabled")){
			config.set("enabled", true);
			x = true;
		}
		
		if(!config.contains("blocks")){
			List<String> l = config.getStringList("blocks");
			l.add("DIAMOND_ORE");
			config.set("blocks", l);
			
			x = true;
		}

		if(x){
			try{
				config.save(MineWatchController.f);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	public static boolean isEnabled(){
		return getConfig().getBoolean("enabled");
	}
	
	public static List<Material> getList() {
		List<String> l = getConfig().getStringList("blocks");
		List<Material> mlist = new ArrayList<Material>();
		for(String n : l){
			mlist.add(Material.valueOf(n.toUpperCase()));
		}
		
		return mlist;
	}

	public static YamlConfiguration getConfig() {
		return config;
	}
}
