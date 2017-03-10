package com.ml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlHelper {
	private static final Log log = LogFactory.getLog(SqlHelper.class);
	private static String url = null;
	private static String username = null;
	private static String password = null;
	public static final String JDBC_FILE_PATH = "jdbc.properties"; // 用户名

	static {
		try {
			url = CommonUtil.getProperty(JDBC_FILE_PATH, "url");
			username = CommonUtil.getProperty(JDBC_FILE_PATH, "username");
			password = CommonUtil.getProperty(JDBC_FILE_PATH, "password");
		} catch (Exception e) {
			log.error("#ERROR# :系统加载jdbc.properties配置文件异常，请检查！", e);
		}

		// 注册驱动类
		try {
			Class.forName(CommonUtil.getProperty(JDBC_FILE_PATH, "driverClassName"));
		} catch (ClassNotFoundException e) {
			log.error("#ERROR# :加载数据库驱动异常，请检查！", e);
		}
	}

	/**
	 * 创建一个数据库连接
	 * 
	 * @return 一个数据库连接
	 */
	public static Connection getConnection() {
		Connection conn = null;
		// 创建数据库连接
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			log.error("#ERROR# :创建数据库连接发生异常，请检查！", e);
		}
		return conn;
	}

	/**
	 * 在一个数据库连接上执行一个静态SQL语句查询
	 * 
	 * @param conn
	 *            数据库连接
	 * @param staticSql
	 *            静态SQL语句字符串
	 * @return 返回查询结果集ResultSet对象
	 * @throws SQLException
	 */
	public static ResultSet executeQuery(Connection conn, String staticSql) throws SQLException {
		ResultSet rs = null;

		// 创建执行SQL的对象
		Statement stmt = conn.createStatement();
		// 执行SQL，并获取返回结果
		rs = stmt.executeQuery(staticSql);

		return rs;
	}

	/**
	 * 在一个数据库连接上执行一带参数的SQL语句查询
	 * 
	 * @param conn
	 *            数据库连接
	 * @param Sql
	 *            SQL语句字符串
	 * @param params
	 *            参数
	 * @return 返回查询结果集ResultSet对象
	 * @throws SQLException
	 */
	public static int executeUpdate(Connection conn, String Sql, String[] params) throws SQLException {
		int iCnt = 1;

		// 创建执行SQL的对象
		PreparedStatement stmt = conn.prepareStatement(Sql);
		if (params != null && params.length > 0) {
			int i = 1;
			for (String param : params) {
				stmt.setString(i++, param);
			}
		} // 执行SQL，并获取返回结果
		iCnt = stmt.executeUpdate();
		stmt.close();

		return iCnt;
	}

	/**
	 * 在一个数据库连接上执行一个静态SQL语句查询
	 * 
	 * @param conn
	 *            数据库连接
	 * @param staticSql
	 *            静态SQL语句字符串
	 * @return 返回查询结果集ResultSet对象
	 * @throws SQLException
	 */
	public static ResultSet executeQuery(Connection conn, String Sql, String[] params) throws SQLException {
		ResultSet rs = null;

		// 创建执行SQL的对象
		PreparedStatement stmt = conn.prepareStatement(Sql);
		if (params != null && params.length > 0) {
			int i = 1;
			for (String param : params) {
				stmt.setString(i++, param);
			}
		} // 执行SQL，并获取返回结果
		rs = stmt.executeQuery();

		return rs;
	}

	/**
	 * 在一个数据库连接上执行一个静态SQL语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param staticSql
	 *            静态SQL语句字符串
	 * @throws SQLException
	 */
	public static void executeSQL(Connection conn, String staticSql) throws SQLException {

		// 创建执行SQL的对象
		Statement stmt = conn.createStatement();
		// 执行SQL，并获取返回结果
		stmt.execute(staticSql);

	}

	/**
	 * 在一个数据库连接上执行一批静态SQL语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sqlList
	 *            静态SQL语句字符串集合
	 * @throws SQLException
	 */
	public static void executeBatchSQL(Connection conn, List<String> sqlList) throws SQLException {

		// 创建执行SQL的对象
		Statement stmt = conn.createStatement();
		for (String sql : sqlList) {
			stmt.addBatch(sql);
		}
		// 执行SQL，并获取返回结果
		stmt.executeBatch();

	}

	public static void closeConnection(Connection conn) {
		if (conn == null)
			return;
		try {
			if (!conn.isClosed()) {
				// 关闭数据库连接
				conn.close();
			}
		} catch (SQLException e) {
			log.error("#ERROR# :关闭数据库连接发生异常，请检查！", e);
		}
	}
}
