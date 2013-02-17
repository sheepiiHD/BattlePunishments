package com.lducks.battlepunishments.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * 
 * @author alkarinv and lDucks
 *
 */
public class CoordsCon {
	/**
	 * 
	 * @param l
	 * @return Location as String
	 */
	public static String getLocString(Location l){
		return l.getWorld().getName() +"," + l.getX() + "," + l.getY() + "," + l.getZ()+","+l.getYaw()+","+l.getPitch();
	}

	/**
	 * 
	 * @param locstr
	 * @return String as Location
	 */
	public static Location getLocation(String locstr) {
//		System.out.println("getLocation locstr=" +locstr);
		if (locstr == null)
			return null;
		String split[] = locstr.split(",");
		String w = split[0];
		Float x = Float.valueOf(split[1]);
		Float y = Float.valueOf(split[2]);
		Float z = Float.valueOf(split[3]);
		float yaw = 0, pitch = 0;
		if (split.length > 4){yaw = Float.valueOf(split[4]);}
		if (split.length > 5){pitch = Float.valueOf(split[5]);}
		World world = null;
		if (w != null)
			world = Bukkit.getServer().getWorld(w);
		if (world ==null || x == null || y== null || z== null){
			return null;}
		Location l =  new Location(world,x,y,z,yaw,pitch);
		return l;
		
	}
}