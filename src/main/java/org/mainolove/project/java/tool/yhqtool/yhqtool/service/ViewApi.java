package org.mainolove.project.java.tool.yhqtool.yhqtool.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.mainolove.project.java.tool.yhqtool.yhqtool.model.Page;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockCondition;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockList;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockRecord;
import org.mainolove.project.java.tool.yhqtool.yhqtool.ui.View;

public class ViewApi {

	private Connection connection;
	private DutyService dutyService;
	private StockService stockService;

	public ViewApi(Connection connection, StockService stockService, DutyService dutyService) {
		this.connection = connection;
		this.stockService = stockService;
		this.dutyService = dutyService;
	}

	public StockList getTableList(StockCondition condition, Page page) {
		try {
			return stockService.queryRecord(connection, condition, page);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			dutyService.logException(e.getMessage());
			return null;
		}
	}

	public void saveTable(StockRecord record) {
		try {
			if (record.getKey() == null) {
				stockService.createRecord(record, connection);
				View.lastName = record.getCustomer();
				JOptionPane.showMessageDialog(null, "新增成功");
			} else {
				stockService.updateRecord(record, connection);
				JOptionPane.showMessageDialog(null, "更新成功");
			}
		} catch (Exception e) {
			dutyService.logException(e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	public void removeTable(StockRecord record) {

		if (record.getKey() == null) {
			return;
		} else {
			try {
				stockService.removeRecord(connection, record);
				JOptionPane.showMessageDialog(null, "删除成功");
			} catch (Exception e) {
				dutyService.logException(e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

	}

	public String getDepotName(Integer i) {
		return dutyService.getDepotIntToNameMap().get(i);
	}

	public Integer getDepotValue(String s) {
		return dutyService.getDepotNameToIntMap().get(s);
	}

	public String[] getAllDepotNames() {

		Set<String> keyz = dutyService.getDepotNameToIntMap().keySet();
		List<String> keys = new ArrayList<String>();
		keys.add(null);
		keys.addAll(keyz);

		return keys.toArray(new String[keys.size()]);
	}

	public StockRecord addOutStock(Integer key, Integer out, String dateField) {
		try {
			StockRecord s = stockService.addOutStock(connection, key, out, dateField);
			JOptionPane.showMessageDialog(null, "出库成功");
			return s;
		} catch (Exception e) {
			dutyService.logException(e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	public void doExport(StockCondition condition) {
		try {
			String fileName = stockService.exportRecord(connection, condition);
			JOptionPane.showMessageDialog(null, "导出成功 文件名：" + fileName);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public String[] getInReasons() {
		return dutyService.getInReasons();
	}
}
