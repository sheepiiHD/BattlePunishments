package com.lducks.battlepunishments.battleplayer;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 
 * @author lDucks
 * 
 */
public interface BattlePlayer {

	/**
	 * 
	 * @return The name of the person. This name will be all lower case.
	 */
	public String getName();
	
	/**
	 * 
	 * @deprecated Method is no longer needed, the plugin sets the real name on its own.
	 * 
	 */
	@Deprecated
	public void setRealName();

	/**
	 * 
	 * @return The "real" name of a player. This is their name with correct case.
	 */
	public String getRealName();

	/**
	 * 
	 * 
	 * @return org.bukkit.entity.Player;
	 */
	public Player getPlayer();

	/**
	 * 
	 * @param reason Reason for kicking a player
	 */
	public void kickPlayer(String reason);

	/**
	 * 
	 * @param lastseen Set last time the player logged out
	 */
	public void setLastSeen(String lastseen);

	/**
	 * 
	 * @return lastseen
	 */
	public String getLastSeen();

	/**
	 * 
	 * @param firstseen 
	 */
	public void setFirstSeen(String firstseen);

	/**
	 * 
	 * @return firstseen
	 */
	public String getFirstSeen();

	/**
	 * 
	 * @param location org.bukkit.Location
	 * The location that the player was logged out at
	 */
	public void setLogoutCoords(Location location);

	/**
	 * 
	 * @return org.bukkit.Location;
	 * The location that the player logged out at.
	 */
	public Location getLogoutCoords();

	/**
	 * 
	 * @deprecated Method no longer needed due to plugin handling bad players on its own
	 * 
	 */
	@Deprecated
	public void deletePlayer();

	/**
	 * 
	 * @return String - Display name of the player
	 */
	public String getDisplayName();

	/**
	 * 
	 * @deprecated Use getPlayer().sendMessage();
	 */
	@Deprecated
	public void sendMessage(String string);

	/**
	 * 
	 * @deprecated Method invalid
	 */
	@Deprecated
	public boolean contains(String string);

	/**
	 * 
	 * @deprecated Method invalid
	 */
	@Deprecated
	public void removeField(String string);

	/**
	 * 
	 * @param reason for ban
	 * @param time of unmute
	 * @param banner of player
	 */
	public void mute(String reason, long time, String banner);

	/**
	 * 
	 * @return if player is banned or not
	 */
	public boolean isBanned();

	/**
	 * 
	 * @return list of IPs attached to the player
	 */
	public List<String> getIPList();

	/**
	 * 
	 * @return if player is muted or not
	 */
	public boolean isMuted();

	/**
	 * 
	 * @return long the time that the player was muted
	 */
	public long getMuteTime();

	/**
	 * 
	 * @return the reason the player was muted
	 */
	public String getMuteReason();

	/**
	 * 
	 * @return muter
	 */
	public String getMuter();

	/**
	 * 
	 * @return the time the player was muted
	 */
	public long getTimeOfMute();
	
	/**
	 * 
	 * @param nickname
	 */
	public void setNickname(String nickname);

	/**
	 * 
	 * @return nickname
	 */
	public String getNickname();

	/**
	 * 
	 * @return strikes
	 */
	public int getStrikes();

	/**
	 * 
	 * @param strikes to add/subtract
	 */
	public void editStrikes(int strikes);
	
	/**
	 * 
	 * @param reason
	 * @param time Time of unban
	 * @param banner
	 * @param ipban Whether the ban is an IP ban or not
	 */
	public void ban(String reason, long time, String banner, boolean ipban);
	
	/**
	 * 
	 * @return banner Player who banned the person
	 */
	public String getBanner();

	/**
	 * 
	 * @return reason of ban
	 */
	public String getBanReason();

	/**
	 * 
	 * @return time of unban
	 */
	public long getBanTime();

	/**
	 * 
	 * @return time Time that player was banned
	 */
	public long getTimeOfBan();

	/**
	 * 
	 * @return boolean
	 */
	public boolean isIPBanned();
	
	/**
	 * 
	 * @return player If player is on watchlist or not
	 */
	public boolean isOnWatchList();

	/**
	 * 
	 * Adds the player to the watchlist
	 * 
	 */
	public void addPlayerToWatchList();

	/**
	 * Removes the player from the watchlist
	 */
	public void removePlayerFromWatchList();

	/**
	 * Unmutes the player
	 */
	public void unmute();

	/**
	 * Unbans the player
	 */
	public void unban();

	/**
	 * 
	 * @return list of blocked commands
	 */
	public List<String> getBlockList();

	/**
	 * Clears the blocked commands list
	 */
	public void clearBlockList();
	
	/**
	 * 
	 * @param command
	 * @return boolean
	 */
	public boolean isBlocked(String command);

	/**
	 * 
	 * @param command
	 */
	public void blockCommand(String command);

	/**
	 * 
	 * @param command
	 */
	public void unblockCommand(String command);

	/**
	 * 
	 * @param ip
	 */
	public void addIP(String ip);

	/**
	 * Clears the IPs
	 */
	public void clearIPs();

	/**
	 * 
	 * @return boolean is player a registered BattlePlayer
	 */
	public boolean exists();

	/**
	 * 
	 * @deprecated Use .getPlayer().hasPlayedBefore();
	 */
	@Deprecated
	public boolean hasPlayedBefore();
}
