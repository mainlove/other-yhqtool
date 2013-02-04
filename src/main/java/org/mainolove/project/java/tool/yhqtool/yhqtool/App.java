package org.mainolove.project.java.tool.yhqtool.yhqtool;

import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mainolove.project.java.tool.yhqtool.yhqtool.db.H2Dbhelper;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.DutyService;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.StockService;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.ViewApi;
import org.mainolove.project.java.tool.yhqtool.yhqtool.ui.View;

public class App {

	public static void main(String[] args) throws Exception {

		JFrame frame = new JFrame("Report");
		Connection con = H2Dbhelper.getConnection("jdbc:h2:db/record");
		StockService stockService = new StockService();
		DutyService dutyService = new DutyService();

		dutyService.ininTable(con);

		ViewApi api = new ViewApi(con, stockService, dutyService);
		View view = new View(api);
		JPanel panel = view.getView();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
