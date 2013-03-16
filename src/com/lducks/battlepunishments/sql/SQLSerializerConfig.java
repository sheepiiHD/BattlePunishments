package com.lducks.battlepunishments.sql;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.lducks.battlepunishments.debugging.ConsoleMessage;
import com.lducks.battlepunishments.debugging.DumpFile;
import com.lducks.battlepunishments.sql.SQLSerializer.SQLType;

/**
 * 
 * @author lDucks and alkarinv
 *
 */

public class SQLSerializerConfig {

	public static void configureSQL(JavaPlugin plugin, SQLSerializer sql, ConfigurationSection cs) {
		String type = cs.getString("type");
		String url = cs.getString("url");
		if (type != null && type.equalsIgnoreCase("sqlite")){
			url = plugin.getDataFolder().toString();
		} 
		configureSQL(sql, type, url, cs.getString("db"),
				cs.getString("port"), cs.getString("username"), cs.getString("password"));
	}

	public static void configureSQL(SQLSerializer sql, String type, String urlOrPath, 
			String db, String port, String user, String password) {
		new ConsoleMessage("wtf dude");
		try{
			if (db != null){
				sql.setDB(db);}
			if (type == null || type.equalsIgnoreCase("mysql")){
				sql.setType(SQLType.MYSQL);
				if (urlOrPath==null) urlOrPath = "localhost";
				if (port == null)  port = "3306";
				sql.setURL(urlOrPath);
				sql.setPort(port);
			} else { /// sqlite
				sql.setType(SQLType.SQLITE);
				sql.setURL(urlOrPath);
			}
			sql.setUsername(user);
			sql.setPassword(password);
			sql.init();
		} catch (Exception e){
			new DumpFile("sql", e, "Error configuring SQL");
		}
	}
}