package org.mainolove.project.java.tool.yhqtool.yhqtool.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Dbhelper {

	public static Connection getConnection(String name) throws Exception {
		Connection conn;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(name);
		} catch (Exception e) {
			throw new Exception("can not connect to db -->" + e.getMessage());
		}
		return conn;
	}

	public static void closeConnection(Connection connection) throws Exception {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new Exception("can not close to db" + e.getMessage());
		}
	}

}
