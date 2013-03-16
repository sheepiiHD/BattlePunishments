package com.lducks.battlepunishments.sql;

import java.sql.Connection;

import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.util.BattleSettings;

/**
 * 
 * @author lDucks
 *
 */

public class SQLInstance extends SQLSerializer {
	public final String createBanTable =" create table if not exists bp_ban " +
			"(player VARCHAR(16), banner VARCHAR(16), reason VARCHAR(256), time VARCHAR(56), timeofban VARCHAR(56), " +
			"ipbanned BOOLEAN," +
			" primary key (player)) ";
	
	public final String createMuteTable =" create table if not exists bp_mute " +
			"(player VARCHAR(16), muter VARCHAR(16), reason VARCHAR(256), time VARCHAR(56), timeofmute VARCHAR(56), " +
			" primary key (player)) ";

	public final String createStrikesTable =" create table if not exists bp_strikes " +
			"(player VARCHAR(16), strikes INTEGER, laststrike VARCHAR(56)," +
			" primary key (player)) ";
	
	public final String createPlayerInfo =" create table if not exists bp_info " +
			"(player VARCHAR(16), realname VARCHAR(16), nickname VARCHAR(16), " +
			" primary key (player)) ";

	public final String createWatchList = "create table if not exists bp_wl " +
			"(player VARCHAR(32), " +
			"primary key (player))";

	public String createIPList = "create table if not exists bp_ips " +
			"(id INTEGER NOT NULL AUTO_INCREMENT, " + 
			"player VARCHAR(16), ip VARCHAR(16), " +
			"primary key (id))";
	
	public String createBlockedCommandsList = "create table if not exists bp_commands " +
			"(id INTEGER NOT NULL AUTO_INCREMENT, " + 
			"player VARCHAR(16), command VARCHAR(16), " +
			"primary key (id))";
	
	public final String createTimeInformation = "create table if not exists bp_times " +
			"(player VARCHAR(16), firstseen VARCHAR(256), lastseen VARCHAR(256), logoutlocation VARCHAR(256), " +
			"primary key (player))";
	
	public final String createIPBanTable = " create table if not exists bp_ipban " +
			"(ip VARCHAR(16)," +
			" primary key (ip)) ";

	public final String createPlayTable = " create table if not exists bp_playedbefore " +
			"(player VARCHAR(16)," +
			" primary key(player))";
	
	@Override
	public boolean init() {
		super.init();
		Connection con;
		
		try {
			con = this.getConnection();
		} catch (Exception e) {
			new DumpFile("SQLInstance", e, "Error creating connection.");
			return false;
		}
		
		if(BattleSettings.getSQLOptions().getString("type").equalsIgnoreCase("sqlite")) {
			createIPList = createIPList.replace("AUTO_INCREMENT", "");
			createBlockedCommandsList = createBlockedCommandsList.replace("AUTO_INCREMENT", "");
		}
		
		this.createTable(con, "bp_ban", createBanTable);
		this.createTable(con, "bp_mute", createMuteTable);
		this.createTable(con, "bp_strikes", createStrikesTable);
		this.createTable(con, "bp_info", createPlayerInfo);
		this.createTable(con, "bp_wl", createWatchList);
		this.createTable(con, "bp_ips", createIPList);
		this.createTable(con, "bp_commands", createBlockedCommandsList);
		this.createTable(con, "bp_lastseen", createTimeInformation);
		this.createTable(con, "bp_ipban", createIPBanTable);
		this.createTable(con, "bp_playedbefore", createPlayTable);
		return true;
	}
}
