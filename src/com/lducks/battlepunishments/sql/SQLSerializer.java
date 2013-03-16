package com.lducks.battlepunishments.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.bukkit.configuration.ConfigurationSection;

import com.lducks.battlepunishments.util.BattleSettings;


/**
 *
 * @author Alkarin
 *
 */
public abstract class SQLSerializer{
	static public final String version = "1.3";

	static protected final boolean DEBUG = BattleSettings.isDebugging();
	static final boolean DEBUG_UPDATE = false;
//	public static class SQLException
	/**
	 * Valid SQL Types
	 * @author alkarin
	 *
	 */
	public static enum SQLType{
		MYSQL("MySQL","com.mysql.jdbc.Driver"), SQLITE("SQLite","org.sqlite.JDBC");
		String name, driver;
		SQLType(String name, String driver){
			this.name = name;
			this.driver = driver;
		}
		public String getName(){return name;}
		public String getDriver(){return driver;}
	};


	static public final int MAX_NAME_LENGTH = 16;

	private DataSource ds ;

	ConfigurationSection cs = BattleSettings.getSQLOptions();
	
	protected String DB = "minecraft";
	protected SQLType TYPE = SQLType.valueOf(cs.getString("type").toUpperCase());

	protected String URL = cs.getString("url");
	protected String PORT = cs.getString("port");
	protected String USERNAME = cs.getString("username");
	protected String PASSWORD = cs.getString("password");

	private String create_database = "CREATE DATABASE IF NOT EXISTS `" + DB+"`";

	public String getURL() {return URL;}
	public void setURL(String url) {URL = url;}

	public String getPort() {return PORT;}
	public void setPort(String port) {PORT = port;}

	public String getUsername() {return USERNAME;}
	public void setUsername(String username) {USERNAME = username;}

	public String getPassword() {return PASSWORD;}
	public void setPassword(String password) {PASSWORD = password;}
	public void setType(SQLType type) {
		this.TYPE = type;
	}
	public SQLType getType(){return TYPE;}
	public String getDB() {return DB;}
	public void setDB(String dB) {
		DB = dB;
		create_database = "CREATE DATABASE IF NOT EXISTS `" + DB+"`";
	}

	public class RSCon{
		public ResultSet rs;
		public Connection con;
	}

	public Connection getConnection() throws SQLException{
		return getConnection(true);
	}
	public Connection getConnection(boolean autoCommit) throws SQLException{
		if (ds == null){
			throw new java.sql.SQLException("Connection is null.  Did you intiliaze your SQL connection?");}
		try {
			Connection con = ds.getConnection();
			con.setAutoCommit(autoCommit);
			return con;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public void closeConnection(RSCon rscon) {
		if (rscon == null || rscon.con == null)
			return;
		try {rscon.con.close();} catch (SQLException e) {}
	}
	public void closeConnection(Connection con) {
		if (con ==null)
			return;
		try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}

	protected boolean init(){
		Connection con = null;  /// Our database connection
		try {
			Class.forName(TYPE.getDriver());
			if (DEBUG) System.out.println("Got Driver " + TYPE.getDriver());
		} catch (ClassNotFoundException e1) {
			System.err.println("Failed getting driver " + TYPE.getDriver());
			e1.printStackTrace();
			return false;
		}
		String connectionString = null,datasourceString = null;
		switch(TYPE){
		case SQLITE:
			datasourceString = connectionString = "jdbc:sqlite:"+URL+"/"+DB+".sqlite";
			break;
		case MYSQL:
		default:
			datasourceString = "jdbc:mysql://"+URL+":"+PORT+"/"+DB+"?autoReconnect=true";
			connectionString = "jdbc:mysql://"+URL+":" + PORT;
			break;
		}

		try {
			ds = setupDataSource(datasourceString,USERNAME,PASSWORD,10,20 );
			//            LOG.debug("Connection attempt to database succeeded.");
		} catch(Exception e) {
			e.printStackTrace();
		}

		String strStmt = create_database;
		if (TYPE == SQLType.MYSQL){
			try {
				con = DriverManager.getConnection(connectionString, USERNAME,PASSWORD);
				Statement st = con.createStatement();
				st.executeUpdate(strStmt);
				if (DEBUG) System.out.println("Creating db");
			} catch (SQLException e) {
				System.err.println("Failed creating db: "  + strStmt);
				e.printStackTrace();
				return false;
			} finally{
				closeConnection(con);
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DataSource setupDataSource(String connectURI, String username, String password,
			int minIdle, int maxActive) throws Exception {
		GenericObjectPool connectionPool = new GenericObjectPool(null);

		connectionPool.setMinIdle( minIdle );
		connectionPool.setMaxActive( maxActive );

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,username, password);

		final boolean defaultReadOnly = false;
		final boolean defaultAutoCommit = true;
		final String validationQuery = null;
		KeyedObjectPoolFactory statementPool = new GenericKeyedObjectPoolFactory(null);
		new PoolableConnectionFactory(connectionFactory, connectionPool, statementPool,
				validationQuery, defaultReadOnly, defaultAutoCommit);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		return dataSource;
	}

	protected boolean createTable(Connection con, String tableName, String sql_create_table,String... sql_updates) {
		/// Check to see if our table exists;
		Boolean exists;
		if (TYPE == SQLType.SQLITE){
			exists = getBoolean("SELECT count(name) FROM sqlite_master WHERE type='table' AND name='"+tableName+"';");
		} else {
			List<Object> objs = getObjects("SHOW TABLES LIKE '"+tableName+"';");
			exists = objs!=null && objs.size() == 1;
		}
		if (DEBUG) System.out.println("table " + tableName +" exists =" + exists);

		if (exists != null && exists) {
			
			// Lets check if a new column needs to be added
			return true; /// Nothing left to do
		}

		/// Create our table and index
		String strStmt = sql_create_table;
		Statement st = null;
		int result =0;
		try {
			st = con.createStatement();
			result = st.executeUpdate(strStmt);
			if (DEBUG) System.out.println("Created Table with stmt=" + strStmt);
		} catch (SQLException e) {
			System.err.println("Failed in creating Table " +strStmt + " result=" + result);
			e.printStackTrace();
			return false;
		}
		/// Updates and indexes
		if (sql_updates != null){
			for (String sql_update: sql_updates){
				if (sql_update == null)
					continue;
				strStmt = sql_update;
				try {
					st = con.createStatement();
					result = st.executeUpdate(strStmt);
					if (DEBUG) System.out.println("Update Table with stmt=" + strStmt);
				} catch (SQLException e) {
					System.err.println("Failed in updating Table " +strStmt + " result=" + result);
					e.printStackTrace();
					return false;
				}
			}

		}

		return true;
	}

	public RSCon executeQuery(String strRawStmt, Object... varArgs){
		Connection con = null;
		try {
			con = getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		PreparedStatement ps = null;
		RSCon rscon = null;

		try {
			ps = getStatement(strRawStmt,con,varArgs);
			if (DEBUG) System.out.println("Executing   =" + ps.toString());
			ResultSet rs = ps.executeQuery();
			rscon = new RSCon();
			rscon.con = con;
			rscon.rs = rs;
		} catch (SQLException e) {
			System.err.println("Couldnt execute query "  + ps);
			e.printStackTrace();
		}
		return rscon;
	}


	public void executeBatch(String updateStatement, List<List<Object>> batch) {
		Connection con = null;
		try {
			con = getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		PreparedStatement ps = null;
//		System.out.println("executingBatch = " + updateStatement + "  batch=" + batch);
		try{con.setAutoCommit(false);} catch(Exception e){e.printStackTrace();}
		try{ps = con.prepareStatement(updateStatement);} catch(Exception e){e.printStackTrace();}
		for (List<Object> update: batch){
			try{
				for (int i=0;i<update.size();i++){
					if (DEBUG_UPDATE) System.out.println(i+" = " + update.get(i));
					ps.setObject(i+1, update.get(i));
				}
				if (DEBUG) System.out.println("Executing   =" + ps);
				ps.addBatch();
			} catch(Exception e){
				System.err.println("statement = " + ps);
				e.printStackTrace();
			}
		}
		try{
			ps.executeBatch();
			con.commit();
		} catch(Exception e){
			System.err.println("statement = " + updateStatement +" preparedStatement="+ps);
			for (List<Object> objs : batch){
				for(Object o: objs){
					System.err.print(o+",");}
				System.err.println();
			}
			e.printStackTrace();
		} finally {
			closeConnection(con);
		}

	}

	protected PreparedStatement getStatement(String strRawStmt, Connection con, Object... varArgs){
		PreparedStatement ps = null;
		try{
			ps = con.prepareStatement(strRawStmt);
			for (int i=0;i<varArgs.length;i++){
				if (DEBUG_UPDATE) System.out.println(i+" = " + varArgs[i]);
				ps.setObject(i+1, varArgs[i]);
			}
		} catch (Exception e){
			System.err.println("Couldnt prepare statment "  + ps);
			e.printStackTrace();
		}
		return ps;
	}

	public int executeTableUpdate(String strRawStmt, Object... varArgs){
		int result= -1;
		Connection con = null;
		try {
			con = getConnection();
		} catch (SQLException e) {
			return -1;
		}

		PreparedStatement ps = null;
		try {
			ps = getStatement(strRawStmt,con,varArgs);
			if (DEBUG) System.out.println("Executing   =" + ps.toString());
			result = ps.executeUpdate();
		} catch (Exception e) {} finally {
			closeConnection(con);
		}
		
		return result;
	}
	
	public int executeUpdate(String strRawStmt, Object... varArgs){
		int result= -1;
		Connection con = null;
		try {
			con = getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		PreparedStatement ps = null;
		try {
			ps = getStatement(strRawStmt,con,varArgs);
			if (DEBUG) System.out.println("Executing   =" + ps.toString());
			result = ps.executeUpdate();
		} catch (Exception e) {
			System.err.println("Couldnt execute update "  + ps);
			e.printStackTrace();
		} finally {
			closeConnection(con);
		}
		return result;
	}

	public Double getDouble(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

	public Integer getInteger(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

	public Short getShort(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				return rs.getShort(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

	public Long getLong(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

	public Boolean getBoolean(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				Integer i = rs.getInt(1);
				if (i==null)
					return null;
				return i > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

	public String getString(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

	public List<Object> getObjects(String query, Object... varArgs){
		RSCon rscon = executeQuery(query,varArgs);
		if (rscon == null || rscon.con == null)
			return null;
		try {
			ResultSet rs = rscon.rs;
			while (rs.next()){
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int nCol = rsmd.getColumnCount();
				List<Object> objs = new ArrayList<Object>(nCol);
				for (int i=0;i<nCol;i++){
					objs.add(rs.getObject(i+1));
				}
				return objs;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{rscon.con.close();}catch(Exception e){}
		}
		return null;
	}

}