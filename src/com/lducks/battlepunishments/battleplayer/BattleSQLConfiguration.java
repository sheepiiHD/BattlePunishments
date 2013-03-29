package com.lducks.battlepunishments.battleplayer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.sql.SQLInstance;
import com.lducks.battlepunishments.sql.SQLSerializer.RSCon;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.CoordsCon;

/**
 * 
 * @author lDucks
 * @see BattlePlayer
 * 
 */

public class BattleSQLConfiguration{

	SQLInstance sql = null;
	String sqltype = BattleSettings.getSQLOptions().getString("type");

	public BattleSQLConfiguration(SQLInstance sql) {
		this.sql = sql;
	}

	public static final String executeBan = "replace into bp_ban (player,banner,reason,time,timeofban,ipbanned) VALUES(?,?,?,?,?,?)";
	public void ban(String name, String reason, long time, String banner, boolean ipbanned) {

		if(sqltype.equalsIgnoreCase("sqlite"))
			executeBan.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(executeBan, name, banner, reason, time, System.currentTimeMillis(), ipbanned);
	}

	public static final String executeUnban = "delete from bp_ban where player=?";
	public void unban(String name) {
		sql.executeUpdate(executeUnban, name);
	}

	public static final String getBanReason = "select reason from bp_ban where player=?";
	public String getBanReason(String name) {
		return sql.getString(getBanReason, name);
	}

	public static final String getUnbanTime = "select time from bp_ban where player=?";
	public long getUnbanTime(String name) {
		return Long.parseLong(sql.getString(getUnbanTime, name));
	}

	public static final String getBanner = "select banner from bp_ban where player=?";
	public String getBanner(String name) {
		return sql.getString(getBanner, name);
	}

	public static final String getTimeOfBan = "select timeofban from bp_ban where player=?";
	public long getTimeOfBan(String name) {
		return sql.getLong(getTimeOfBan, name);
	}


	public boolean isBanned(String name) {
		if(sql.getBoolean("select count(*) from bp_ban where player=? limit 1", name)) {
			if(getUnbanTime(name) < System.currentTimeMillis() && getUnbanTime(name) != -1) {
				unban(name);
				return false;
			}
			return true;
		}
		return false;
	}

	public static final String executeMute = "replace into bp_mute (player,muter,reason,time,timeofmute) VALUES(?,?,?,?,?)";
	public void mute(String name, String muter, String reason, long time) {
		if(sqltype.equalsIgnoreCase("sqlite"))
			executeMute.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(executeMute, name, muter, reason, time, System.currentTimeMillis());
	}

	public static final String executeUnmute = "delete from bp_mute where player=?";
	public void unmute(String name) {
		sql.executeUpdate(executeUnmute, name);
	}

	public static final String getMuter = "select muter from bp_mute where player=?";
	public String getMuter(String name) {
		return sql.getString(getMuter, name);
	}

	public static final String getMuteReason = "select reason from bp_mute where player=?";
	public String getMuteReason(String name) {
		return sql.getString(getMuteReason, name);
	}

	public static final String getUnmuteTime = "select time from bp_mute where player=?";	
	public long getUnmuteTime(String name) {
		return Long.parseLong(sql.getString(getUnmuteTime, name));
	}

	public boolean isMuted(String name) {
		if(sql.getBoolean("select count(*) from bp_mute where player=? limit 1", name)) {
			if(getUnmuteTime(name) < System.currentTimeMillis() && getUnmuteTime(name) != -1) {
				unmute(name);
				return false;
			}
			return true;
		}
		return false;
	}

	public static final String getTimeOfMute = "select timeofmute from bp_mute where player=?";
	public long getTimeOfMute(String name) {
		return sql.getLong(getTimeOfMute, name);
	}

	public List<String> getIPList(String name) {
		RSCon resultSetAndConnection = sql.executeQuery("select ip from bp_ips where player=?", name);
		if (resultSetAndConnection == null || resultSetAndConnection.rs == null) return null;
		List<String> results = new ArrayList<String>();
		try{
			ResultSet rs = resultSetAndConnection.rs;
			while (rs.next()) {
				String ip = rs.getString("ip");
				ip = ip.replace("-",".");

				if(!results.contains(ip))
					results.add(ip);
			}
		} catch (Exception e) {
			new DumpFile("getIPList", e, "Error getting IP list");
			return null;
		} finally {
			sql.closeConnection(resultSetAndConnection);
		}

		return results;
	}

	public void removeIP(String name, String ip) {
		ip = ip.replace("/", "");
		sql.executeUpdate("delete from bp_ips where player=? and ip=?", name, ip);
	}

	public static final String executeAddIP = "replace into bp_ips(player,ip) VALUES(?,?)";
	public void addIP(String name, String ip) {
		if(sqltype.equalsIgnoreCase("sqlite"))
			executeAddIP.replace("replace into", "INSERT OR REPLACE INTO");

		ip = ip.replace("/", "");

		if(!getIPList(name).contains(ip))
			sql.executeUpdate(executeAddIP, name, ip);
	}

	public void clearIPs(String name) {
		sql.executeUpdate("delete from bp_ips where player=?", name);
	}

	public static final String executeBlockCommand = "replace into bp_commands (player,command) VALUES(?,?)";
	public void blockCommand(String name, String command) {
		if(sqltype.equalsIgnoreCase("sqlite"))
			executeBlockCommand.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(executeBlockCommand, name, command);
	}

	public void unblockCommand(String name, String command) {
		sql.executeUpdate("delete from bp_commands where player=? and command=?", name, command);
	}

	public boolean isBlockedCommand(String name, String command) {
		return sql.getBoolean("select count(*) from bp_commands where player=? and command=? limit 1", name, command);
	}

	public List<String> getBlockedCommandsList(String name) {
		RSCon resultSetAndConnection = sql.executeQuery("select command from bp_commands where player=?", name);
		if (resultSetAndConnection == null || resultSetAndConnection.rs == null) return null;
		List<String> results = new ArrayList<String>();
		try{
			ResultSet rs = resultSetAndConnection.rs;
			while (rs.next()) {
				String command = rs.getString("command");
				results.add(command);
			}
		} catch (Exception e) {
			new DumpFile("getBlockedCommandsList", e, "Error getting list");
			return null;
		} finally {
			sql.closeConnection(resultSetAndConnection);
		}

		return results;
	}

	public void clearBlockedCommands(String name) {
		sql.executeUpdate("delete from bp_commands where player=?", name);
	}

	public static final String addToWatchList = "replace into bp_wl (player) VALUES(?)";
	public void addToWatchList(String name) {
		if(sqltype.equalsIgnoreCase("sqlite"))
			addToWatchList.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(addToWatchList, name);
	}

	public static final String removeFromWatchList = "DELETE FROM bp_wl WHERE player=?";
	public void removeFromWatchList(String name) {

		new ConsoleMessage("Removing person from watchlist "+name);

		name = name.toLowerCase();
		sql.executeUpdate(removeFromWatchList, name);
	}

	public boolean isOnWatchList(String name) {
		return sql.getBoolean("select count(*) from bp_wl where player=? limit 1", name);
	}

	public static final String getStrikes = "select strikes from bp_strikes where player=?";
	public int getStrikes(String name) {
		if(sql.getInteger(getStrikes, name) != null)
			return sql.getInteger(getStrikes, name);
		else
			return 0;
	}

	public static final String updateStrikes = "replace into bp_strikes (player,strikes,laststrike) VALUES(?,?,?)";
	public void setStrikes(String name, int s) {

		new ConsoleMessage("int s ===      " + s);

		int strikes = 0;
		if(sql.getBoolean("select count(*) from bp_strikes where player=? limit 1", name)) {
			strikes = getStrikes(name);
		}

		strikes = strikes + s;

		if(s < 0)
			s = 0;
		else if(s > BattleSettings.getStrikesMax())
			s = BattleSettings.getStrikesMax();

		if(s >= BattleSettings.getStrikesCap() && BattleSettings.getStrikesAutoban()){
			ban(name, "You have too many strikes!", -1, "Server", false);
		}

		if(sqltype.equalsIgnoreCase("sqlite"))
			updateStrikes.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(updateStrikes, name, s, System.currentTimeMillis());
	}

	public static final String executeSetRealName = "replace into bp_info (player,realname,nickname) VALUES(?,?,?)";
	public void setRealName(String name, String realname) {
		String nickname = "";

		try {
			nickname = getNickname(name);
		}catch(Exception e) {}

		if(nickname == null)
			nickname = realname;

		new ConsoleMessage("name === "+ name+ " realname == "+realname+"   nickname == "+nickname);

		if(sqltype.equalsIgnoreCase("sqlite"))
			executeSetRealName.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(executeSetRealName, name, realname, nickname);
	}

	public static final String updateNickName = "replace into bp_info (player,realname,nickname) VALUES(?,?,?)";
	public void changeNickName(String name, String n) {
		String realname = getRealName(name);
		if(realname == null)
			realname = "";

		if(sqltype.equalsIgnoreCase("sqlite"))
			updateNickName.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(updateNickName, name, realname, n);
	}

	public static final String getNickname = "select nickname from bp_info where player=?";
	public String getNickname(String name) {
		try {
			return sql.getString(getNickname, name);
		}catch(Exception e) {
			return "";
		}
	}

	public String getRealName(String name) {
		return sql.getString("select realname from bp_info where player=?", name);
	}

	public static final String executeSetTime = 
			"replace into bp_times (player,firstseen,lastseen,logoutlocation) VALUES(?,?,?,?)";
	public void setTimes(String name, String first, String last, Location loc) {
		String logoutlocation = CoordsCon.getLocString(loc);
		new ConsoleMessage(name+": "+first+" "+last+" "+logoutlocation);

		if(first == null)
			first = "";

		if(last == null)
			last = "";

		if(sqltype.equalsIgnoreCase("sqlite"))
			executeSetTime.replace("replace into", "INSERT OR REPLACE INTO");

		try {
			sql.executeUpdate(executeSetTime, name, first, last, logoutlocation);
		}catch(Exception e) {
			new DumpFile("setTimes", e, "Error setting times for "+name);
		}
	}

	public String getLastSeen(String name) {
		return sql.getString("select lastseen from bp_times where player=?", name);
	}

	public String getFirstSeen(String name) {
		try {
			return sql.getString("select firstseen from bp_times where player=?", name);
		}catch(Exception e) {return null;}
	}


	public String getLogoutLocation(String name) {
		return sql.getString("select logoutlocation from bp_times where player=?", name);
	}

	public static final String executeAddToWatchList = "replace into bp_wl(player) VALUES(?)";
	public void addPlayerToWatchList(String name) {

		if(sqltype.equalsIgnoreCase("sqlite"))
			executeAddToWatchList.replace("replace into", "INSERT OR REPLACE INTO");

		sql.executeUpdate(executeAddToWatchList, name);
	}


	public String getShortHandName(String name) {
		name = name.toLowerCase()+"%";
		return sql.getString("select player from bp_info where player like ?", name);
	}


	public boolean isIPBanned(String name) {
		if(!isBanned(name))
			return false;

		return sql.getBoolean("select ipbanned from bp_ban where player=?", name);
	}


	public boolean hasPlayedBefore(String name) {
		return sql.getBoolean("select count(*) from bp_playedbefore where player=? limit 1", name);
	}


	public void setHasPlayedBefore(String name) {
		sql.executeUpdate("INSERT INTO bp_playedbefore(player) VALUES(?)", name);
	}


	public String getLastStrike(String name) {
		String getTimeOfBan = "select laststrike from bp_strikes where player=?";
		return sql.getString(getTimeOfBan, name);
	}
}
