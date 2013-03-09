package com.lducks.battlepunishments.util.webrequests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	public static void updateBan(final BattlePlayer bp) throws Exception {
		if(!BattleSettings.useWebsite() || !bp.isBanned() || !Bukkit.getOnlineMode())
			return;

		final StringBuilder data = new StringBuilder();
		data.append(encode("type")).append('=').append(encode("ban"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "banner", bp.getBanner());
		encodeDataPair(data, "reason", bp.getBanReason());
		encodeDataPair(data, "time", ""+bp.getBanTime());

		openUrl(updateURL+"?"+data.toString());
	}

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateMute(final BattlePlayer bp) throws Exception {
		if(!BattleSettings.useWebsite() || !Bukkit.getOnlineMode() || !bp.isMuted())
			return;

		StringBuilder data = new StringBuilder();
		data.append(encode("type")).append('=').append(encode("mute"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "muter", bp.getMuter());
		encodeDataPair(data, "reason", bp.getMuteReason());
		encodeDataPair(data, "time", ""+bp.getMuteTime());

		// Create the url		
		openUrl(updateURL+"?"+data.toString());
	}

	/**
	 * @param bp BattlePlayer object
	 */
	public static void updateStrikes(final BattlePlayer bp) throws Exception {
		if(!BattleSettings.useWebsite() || !Bukkit.getOnlineMode() || bp.getStrikes() == 0)
			return;

		// Construct the post data
		final StringBuilder data = new StringBuilder();

		data.append(encode("type")).append('=').append(encode("strikes"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "strikes", ""+bp.getStrikes());
		encodeDataPair(data, "maxstrikes", ""+BattleSettings.getStrikesMax());

		openUrl(updateURL+"?"+data.toString());
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
		if(!BattleSettings.useWebsite() || !Bukkit.getOnlineMode())
			return;

		// Construct the post data
		final StringBuilder data = new StringBuilder();

		data.append(encode("type")).append('=').append(encode("ip"));
		encodeDataPair(data, "key", key);
		encodeDataPair(data, "server", server);
		encodeDataPair(data, "player", bp.getRealName());
		encodeDataPair(data, "ip", ip);

		openUrl(updateURL+"?"+data.toString());
	}

	private static void openUrl(final String urls) throws Exception {
		Bukkit.getScheduler().runTaskAsynchronously(BattlePunishments.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urls);
					URLConnection connection = url.openConnection();

					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					reader.read();

					reader.close();
				}catch(Exception e) {}
			}
		});
	}
}