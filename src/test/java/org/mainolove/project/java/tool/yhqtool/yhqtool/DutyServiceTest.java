package org.mainolove.project.java.tool.yhqtool.yhqtool;

import java.sql.Connection;

import junit.framework.TestCase;

import org.mainolove.project.java.tool.yhqtool.yhqtool.db.H2Dbhelper;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.DutyService;

public class DutyServiceTest extends TestCase {

	private DutyService dutyService = new DutyService();

	public void testInitTable() throws Exception {
		Connection con = H2Dbhelper.getConnection("jdbc:h2:tcp://localhost/D:\\h2\\test");
		dutyService.ininTable(con);
		H2Dbhelper.closeConnection(con);
		
	}


	
	

}
