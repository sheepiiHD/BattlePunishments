package com.lducks.battlepunishments.util.webrequests;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mc.battleplugins.webapi.object.ConnectionType;
import mc.battleplugins.webapi.object.WebURL;

import org.bukkit.Bukkit;

import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class UpdateDatabase {

	private static String updateURL = "http://BattlePunishments.net/grabbers/updategrabber.php";
	private final static String key = WebConnections.getConnectionCode();

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateBan(final BattlePlayer bp) throws Exception {
		if(!BattleSettings.useWebsite() || !bp.isBanned() || !Bukkit.getOnlineMode() || WebConnections.getServerIP() == null)
			return;

		WebURL url = new WebURL(new URL(updateURL));
		url.addData("type","ban");
		url.addData("key", key);
		url.addData("server", WebConnections.getServerIP());
		url.addData("player", bp.getRealName());
		url.addData("banner", bp.getBanner());
		url.addData("reason", bp.getBanReason());
		url.addData("time", ""+bp.getBanTime());
		url.setConnectionType(ConnectionType.POST);
		url.sendData();
		
	}

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateMute(final BattlePlayer bp) throws Exception {
		if(!BattleSettings.useWebsite() || !Bukkit.getOnlineMode() || !bp.isMuted() || WebConnections.getServerIP() == null)
			return;

		WebURL url = new WebURL(new URL(updateURL));
		url.addData("type","mute");
		url.addData("key", key);
		url.addData("server", WebConnections.getServerIP());
		url.addData("player", bp.getRealName());
		url.addData("muter", bp.getMuter());
		url.addData("reason", bp.getMuteReason());
		url.addData("time", ""+bp.getMuteTime());
		url.setConnectionType(ConnectionType.POST);
		url.sendData();
	}

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateStrikes(final BattlePlayer bp) throws Exception {
		if(!BattleSettings.useWebsite() || !Bukkit.getOnlineMode() || bp.getStrikes() == 0 || WebConnections.getServerIP() == null)
			return;

		WebURL url = new WebURL(new URL(updateURL));
		url.addData("type","strikes");
		url.addData("key", key);
		url.addData("server", WebConnections.getServerIP());
		url.addData("player", bp.getRealName());
		url.addData("strikes", ""+bp.getStrikes());
		url.addData("maxstrikes", ""+BattleSettings.getStrikesMax());
		url.setConnectionType(ConnectionType.POST);
		url.sendData();
	}

	private static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i=0; i<messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateIP(final BattlePlayer bp, String ipb) throws Exception {
		final String ip = md5(ipb);
		if(!BattleSettings.useWebsite() || !Bukkit.getOnlineMode() || WebConnections.getServerIP() == null)
			return;

		WebURL url = new WebURL(new URL(updateURL));
		url.addData("type","ip");
		url.addData("key", key);
		url.addData("server", WebConnections.getServerIP());
		url.addData("player", bp.getRealName());
		url.addData("ip", ip);
		url.setConnectionType(ConnectionType.POST);
		url.sendData();
	}
}