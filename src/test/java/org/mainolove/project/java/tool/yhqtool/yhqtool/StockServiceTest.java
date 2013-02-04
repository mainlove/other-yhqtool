package org.mainolove.project.java.tool.yhqtool.yhqtool;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.mainolove.project.java.tool.yhqtool.yhqtool.db.H2Dbhelper;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.Page;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockCondition;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockRecord;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.StockService;

public class StockServiceTest extends TestCase {

	private StockService stockService = new StockService();

	public void testCreateRecord() throws Exception {
		Connection con = H2Dbhelper.getConnection("jdbc:h2:tcp://localhost/D:\\h2\\test");

		StockRecord record = new StockRecord(12345, "小排", "白痴", 1200, 1200, new Date(), "检查", null, null, null,
				1,
				"remark");
		stockService.createRecord(record, con);
		H2Dbhelper.closeConnection(con);
	}

	public void testUpdateRecord() throws Exception {

		Connection con = H2Dbhelper.getConnection("jdbc:h2:tcp://localhost/D:\\h2\\test");

		StockRecord record = new StockRecord(12345, "小排", "白痴", 1200, 1200, new Date(), "检查", null, null, null,
				1,
				"remark");

		stockService.createRecord(record, con);
		record.setOutStock(100);
		record.setFee(1.1f);
		record.setDepot(1);
		stockService.updateRecord(record, con);
		H2Dbhelper.closeConnection(con);

	}

	public void testQueryRecord() throws Exception {

		Connection con = H2Dbhelper.getConnection("jdbc:h2:tcp://localhost/D:\\h2\\test");

		Page page = new Page(1);
		page.setPageSize(4);

		StockCondition condition = new StockCondition();
		condition.setCustomer("白痴");
		for (int i = 1; i < 4; i++) {
			page.setCurPage(i);
			List<StockRecord> List = stockService.queryRecord(con, condition, page).getList();
			for (StockRecord stockRecord : List) {
				System.out.println(stockRecord);
			}
			System.out.println();
		}
	}

}
