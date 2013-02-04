package org.mainolove.project.java.tool.yhqtool.yhqtool.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mainolove.project.java.tool.yhqtool.yhqtool.db.H2Dbhelper;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockCondition;
import org.mainolove.project.java.tool.yhqtool.yhqtool.ui.View;

@SuppressWarnings("deprecation")
public class Excelhelper {

	public static String exportRet(ResultSet ret, StockCondition condition) throws Exception {

		Workbook wb = new HSSFWorkbook();

		CellStyle background = wb.createCellStyle();
		background.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		background.setFillPattern(CellStyle.SOLID_FOREGROUND);

		String fileName = new java.sql.Date(new Date().getTime()).toString();
		int i = 1;

		File file = new File("export");
		if (!file.exists()) {
			file.mkdirs();
		}
		String baseName = "export/report-" + fileName + ".xls";
		while (new File(baseName).exists()) {
			baseName = "export/report-" + fileName + "-" + (i++) + ".xls";
		}
		if (i > 1) {
			fileName = "export/report-" + fileName + "-" + (i - 1) + ".xls";
		} else {
			fileName = "export/report-" + fileName + ".xls";
		}

		FileOutputStream fileOut = new FileOutputStream(fileName);

		Sheet sheet = wb.createSheet("report");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
		Row rowCondition = sheet.createRow(0);
		if (condition != null) {
			rowCondition.createCell(0).setCellValue(condition.toString());
		}

		Row row = sheet.createRow(1);
		setHead(row);
		setBody(ret, sheet, background, wb);

		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(12);

		wb.write(fileOut);
		fileOut.close();

		return fileName;
	}

	private static void setHead(Row row) {

		int i = 0;
		Object[] c = View.columnNames;
		row.createCell(i++).setCellValue(c[1].toString());
		row.createCell(i++).setCellValue(c[2].toString());
		row.createCell(i++).setCellValue(c[3].toString());
		row.createCell(i++).setCellValue(c[4].toString());
		row.createCell(i++).setCellValue(c[5].toString());
		row.createCell(i++).setCellValue(c[6].toString());
		row.createCell(i++).setCellValue(c[7].toString());
		row.createCell(i++).setCellValue(c[8].toString());
		row.createCell(i++).setCellValue(c[9].toString());
		row.createCell(i++).setCellValue(c[10].toString());
		row.createCell(i++).setCellValue(c[11].toString());
		row.createCell(i++).setCellValue(c[12].toString());
		row.createCell(i++).setCellValue("出库记录");

	}

	public static void main(String[] args) throws Exception {
		StockService stockService = new StockService();
		Connection con = H2Dbhelper.getConnection("jdbc:h2:record");

		stockService.exportRecord(con, null);
	}

	private static void setBody(ResultSet ret, Sheet sheet, CellStyle background, Workbook wb)

	throws SQLException {

		Short i = 2;
		while (ret.next()) {

			Row row = sheet.createRow(i++);
			int j = 0;

			row.createCell(j++).setCellValue(ret.getInt(2));
			row.createCell(j++).setCellValue(ret.getString(3));
			row.createCell(j++).setCellValue(ret.getString(4));
			row.createCell(j++).setCellValue(ret.getInt(5));
			row.createCell(j++).setCellValue(ret.getInt(6));
			if (ret.getDate(7) != null) {
				row.createCell(j++).setCellValue(ret.getDate(7).toString());
			} else {
				row.createCell(j++).setCellValue("");
			}
			row.createCell(j++).setCellValue(ret.getString(8));
			if (ret.getDate(9) != null) {
				row.createCell(j++).setCellValue(ret.getDate(9).toString());
			} else {
				row.createCell(j++).setCellValue("");
			}

			row.createCell(j++).setCellValue(ret.getInt(10));
			row.createCell(j++).setCellValue(DutyService.getDepotIntToNameMap().get(ret.getInt(12)));
			row.createCell(j++).setCellValue(ret.getFloat(11));
			row.createCell(j++).setCellValue((ret.getString(13)));
			row.createCell(j++).setCellValue(ret.getString(14));

			if (ret.getFloat(11) > 0) {
				row.getCell(0).setCellStyle(background);
				row.getCell(1).setCellStyle(background);
				row.getCell(2).setCellStyle(background);
				row.getCell(3).setCellStyle(background);
				row.getCell(4).setCellStyle(background);
				row.getCell(5).setCellStyle(background);
				row.getCell(6).setCellStyle(background);
				row.getCell(7).setCellStyle(background);
				row.getCell(8).setCellStyle(background);
				row.getCell(9).setCellStyle(background);
				row.getCell(10).setCellStyle(background);
				row.getCell(11).setCellStyle(background);
				row.getCell(12).setCellStyle(background);
			}
		}
	}

}
