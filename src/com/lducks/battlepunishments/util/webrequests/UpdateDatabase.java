package com.lducks.battlepunishments.util.webrequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.bukkit.Bukkit;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.battleplayer.BattlePlayer;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class UpdateDatabase {
	
	private static String updateURL = "http://BattlePunishments.net/grabbers/updategrabber.php";
	private final static String key = ConnectionCode.getConnectionCode();
	private final static String server = BattlePunishments.getServerIP();

	private static void encodeDataPair(final StringBuilder buffer, final String key, final String value) throws UnsupportedEncodingException {
		buffer.append('&').append(encode(key)).append('=').append(encode(value));
	}

	private static String encode(final String text) throws UnsupportedEncodingException {
		return URLEncoder.encode(text, "UTF-8");
	}

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateBan(BattlePlayer bp) throws IOException {
		if(!BattleSettings.useWebsite() || !ConnectionCode.validConnectionCode() || !bp.isBanned() || !Bukkit.getOnlineMode())
			return;

		// Construct the post data
		final StringBuilder data = new StringBuilder();

		data.append(encode("type")).append('=').append(encode("ban"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "banner", bp.getBanner());
		encodeDataPair(data, "reason", bp.getBanReason());
		encodeDataPair(data, "time", ""+bp.getBanTime());

		// Create the url
		URL url = new URL(updateURL+"?"+data.toString());
		URLConnection connection = url.openConnection();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		reader.read();
		
		reader.close();
	}
	
	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateMute(BattlePlayer bp) throws IOException {
		if(!BattleSettings.useWebsite() || !ConnectionCode.validConnectionCode() || !Bukkit.getOnlineMode() || !bp.isMuted())
			return;

		// Construct the post data
		final StringBuilder data = new StringBuilder();

		data.append(encode("type")).append('=').append(encode("mute"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "muter", bp.getMuter());
		encodeDataPair(data, "reason", bp.getMuteReason());
		encodeDataPair(data, "time", ""+bp.getMuteTime());

		// Create the url		
		URL url = new URL(updateURL+"?"+data.toString());
		URLConnection connection = url.openConnection();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		reader.read();
		
		reader.close();
	}
	
	

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateStrikes(BattlePlayer bp) throws IOException {
		if(!BattleSettings.useWebsite() || !ConnectionCode.validConnectionCode() || !Bukkit.getOnlineMode() || bp.getStrikes() == 0)
			return;

		// Construct the post data
		final StringBuilder data = new StringBuilder();

		data.append(encode("type")).append('=').append(encode("strikes"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "strikes", ""+bp.getStrikes());
		encodeDataPair(data, "maxstrikes", ""+BattleSettings.getStrikesMax());


		URL url = new URL(updateURL+"?"+data.toString());
		URLConnection connection = url.openConnection();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		reader.read();
		
		reader.close();
	}
	
	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateIP(BattlePlayer bp, String ip) throws IOException {
		if(!BattleSettings.useWebsite() || !ConnectionCode.validConnectionCode() || !Bukkit.getOnlineMode() || bp.getStrikes() == 0)
			return;

		// Construct the post data
		final StringBuilder data = new StringBuilder();

		data.append(encode("type")).append('=').append(encode("ip"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "ip", ip);


		URL url = new URL(updateURL+"?"+data.toString());
		URLConnection connection = url.openConnection();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		reader.read();
		
		reader.close();
	}
}