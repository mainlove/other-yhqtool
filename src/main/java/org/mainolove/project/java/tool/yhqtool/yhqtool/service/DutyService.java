package org.mainolove.project.java.tool.yhqtool.yhqtool.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.mainolove.project.java.tool.yhqtool.yhqtool.db.ContentHandler;
import org.mainolove.project.java.tool.yhqtool.yhqtool.db.DbTemplate;

public class DutyService {

	private Logger log;

	private Map<String, Integer> depotNameToIntMap = new HashMap<String, Integer>();
	private static Map<Integer, String> depotIntToNameMap = new HashMap<Integer, String>();

	public Map<String, Integer> getDepotNameToIntMap() {
		return depotNameToIntMap;
	}

	public static Map<Integer, String> getDepotIntToNameMap() {
		return depotIntToNameMap;
	}

	private static String[] inReasons = new String[] { "客户要求入库", "采样", "整改", "其他" };

	public String[] getInReasons() {
		return inReasons;
	}

	public DutyService() {
		// Logger log = Logger.getLogger("system");
		// log.setLevel(Level.INFO);
		// FileHandler fileHandler = null;
		// try {
		// fileHandler = new FileHandler("report.log");
		// fileHandler.setFormatter(new SimpleFormatter());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// fileHandler.setLevel(Level.INFO);
		// log.addHandler(fileHandler);

		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("/depot.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<Object> keys = properties.keySet();
		for (Object object : keys) {
			depotIntToNameMap.put(Integer.valueOf(object.toString()), properties.getProperty(object.toString()));
			depotNameToIntMap.put(properties.getProperty(object.toString()), Integer.valueOf(object.toString()));
		}

	}

	public void ininTable(Connection con) throws Exception {

		final String rsql = buildRecordTable();

		ContentHandler handler = new ContentHandler() {
			public void handle(Connection con) throws Exception {
				DbTemplate.doExecute(con, rsql, new ArrayList<Object>());
			}
		};
		DbTemplate.doTranscation(con, handler);
	}

	private String buildRecordTable() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS R (");
		sql.append(" KEY  	INT  PRIMARY KEY AUTO_INCREMENT,");
		sql.append(" ID    	INT  ,");
		sql.append(" BREED 	VARCHAR(20)  ,");
		sql.append(" CUSTOMER 	VARCHAR(20) ,");
		sql.append(" INSTOCK 	INT ,");
		sql.append(" INREALSTOCK 	INT ,");
		sql.append(" INDATE   DATE,");
		sql.append(" INREASON   VARCHAR(20),");
		sql.append(" NOTICEDATE   DATE,");
		sql.append(" OUTSTOCK   INT,");
		// sql.append(" OUTDETAIL 	VARCHAR(255),");
		sql.append(" FEE REAL,");
		sql.append(" DEPOT TINYINT,");
		sql.append(" REMARK VARCHAR(255),");
		sql.append(" OUTDETAIL ARRAY");

		sql.append(");");
		return sql.toString();
	}

	public void logException(String msg) {
		// log.severe(msg);
	}

}
