package org.mainolove.project.java.tool.yhqtool.yhqtool;

import java.sql.Connection;
import java.util.Date;

import org.mainolove.project.java.tool.yhqtool.yhqtool.db.H2Dbhelper;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockRecord;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.DutyService;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.StockService;

public class loadtest {

	public static void main(String[] args) throws Exception {

		Connection con = H2Dbhelper.getConnection("jdbc:h2:record");
		DutyService dutyService = new DutyService();
		dutyService.ininTable(con);
		StockService stockService = new StockService();

		StockRecord record = null;
		for (int i = 1; i <= 10000; i++) {
			record = new StockRecord(i, "小排", "白痴", 1200, 1200, new Date(), "检查", null, null, null, i % 3 + 1, "remark");
			System.out.println(i);
			stockService.createRecord(record, con);

		}
	}

}
