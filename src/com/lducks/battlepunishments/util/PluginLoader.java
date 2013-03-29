package com.lducks.battlepunishments.util;

import org.bukkit.Bukkit;

/**
 * 
 * @author lDucks
 *
 */

public class PluginLoader {
	/**
	 * 
	 * @return boolean If Herochat is installed
	 */
	public static boolean herochatInstalled(){
		return (Bukkit.getPluginManager().getPlugin("Herochat") != null);
	}

	/**
	 * 
	 * @return boolean If TagAPI is installed
	 */
	public static boolean tagAPIInstalled(){
		return (Bukkit.getPluginManager().getPlugin("TagAPI") != null);
	}
	
	/**
	 * 
	 * @return boolean If Vault is installed
	 */
	public static boolean vaultInstalled(){
		return (Bukkit.getPluginManager().getPlugin("Vault") != null);
	}

	/**
	 * @return boolean If Essentials is installed
	 */
	public static boolean essentialsInstalled() {
		return (Bukkit.getPluginManager().getPlugin("Essentials") != null);
	}

	/**
	 * @return boolean If CommandBook is installed
	 */
	public static boolean cmdBookInstalled() {
		return (Bukkit.getPluginManager().getPlugin("CommandBook") != null);
	}

	/**
	 * @return boolean If WebAPI is installed
	 */
	public static boolean webAPIInstalled() {
		return (Bukkit.getPluginManager().getPlugin("WebAPI") != null);
	}
}
