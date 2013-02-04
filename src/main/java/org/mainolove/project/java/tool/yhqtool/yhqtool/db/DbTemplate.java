package org.mainolove.project.java.tool.yhqtool.yhqtool.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DbTemplate {

	public static <T> T doQuery(Connection connection, String sql, List<Object> paramList,
			ResultHandler<T> resultHandler) throws Exception {
		ResultSet ret = null;
		try {

			PreparedStatement ps = connection.prepareStatement(sql);
			int i = 1;
			for (Object value : paramList) {
				ps.setObject(i++, value);
			}
			ret = ps.executeQuery();
		} catch (Exception e) {
			throw new Exception(" Db query exception -> " + sql + " -->" + e.getMessage());
		}
		T t;
		try {
			t = resultHandler.handle(ret);
		} catch (Exception e) {
			throw new Exception(" Db handle data exception -> " + sql + " -->" + e.getMessage());
		}
		return t;
	}

	public static void doExecute(Connection connection, String sql, List<Object> paramList) throws Exception {

		try {

			PreparedStatement ps = connection.prepareStatement(sql);
			int i = 1;
			for (Object value : paramList) {
				ps.setObject(i++, value);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			throw new Exception(" Db execute exception -> " + sql + " -->" + e.getMessage());
		}
	}

	public static Integer doExecuteAndgetKey(Connection connection, String sql, List<Object> paramList)
			throws Exception {

		try {

			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			for (Object value : paramList) {
				ps.setObject(i++, value);
			}
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				Integer id = rs.getInt(1);
				return id;
			} else {
				throw new Exception(" can not get the auto_incremant key by db");

			}
		} catch (Exception e) {
			throw new Exception(" Db execute exception -> " + sql + " -->" + e.getMessage());
		}
	}

	public static void doTranscation(Connection con, ContentHandler handler) throws Exception {

		try {
			con.setAutoCommit(false);
			handler.handle(con);
			con.commit();
		} catch (SQLException e) {
			throw new Exception(" doTranscation wrong:" + e.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

}
