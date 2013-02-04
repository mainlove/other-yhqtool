package org.mainolove.project.java.tool.yhqtool.yhqtool.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mainolove.project.java.tool.yhqtool.yhqtool.db.ContentHandler;
import org.mainolove.project.java.tool.yhqtool.yhqtool.db.DbTemplate;
import org.mainolove.project.java.tool.yhqtool.yhqtool.db.ResultHandler;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.Page;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockCondition;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockList;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockRecord;

public class StockService {

	public void createRecord(final StockRecord record, Connection con) throws Exception {

		final StringBuilder insertSql = new StringBuilder();
		insertSql
				.append("INSERT INTO R (ID,BREED,CUSTOMER,INSTOCK,INREALSTOCK,INDATE ,INREASON,NOTICEDATE,OUTSTOCK,FEE,DEPOT,REMARK) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

		final List<Object> paramList = fillparamList(record);

		DbTemplate.doTranscation(con, new ContentHandler() {
			@Override
			public void handle(Connection con) throws Exception {
				Integer key = DbTemplate.doExecuteAndgetKey(con, insertSql.toString(), paramList);
				record.setKey(key);
			}
		});

	}

	public void updateRecord(StockRecord record, Connection con) throws Exception {

		final StringBuilder updateSql = new StringBuilder();
		updateSql
				.append("UPDATE R SET ID=?,BREED=?,CUSTOMER=?,INSTOCK=?,INREALSTOCK=?,INDATE=? ,INREASON=? ,NOTICEDATE=? ,OUTSTOCK=? ,Fee=?,DEPOT=?,REMARK=?");
		final List<Object> paramList = fillparamList(record);

		updateSql.append(" WHERE KEY= ?");
		paramList.add(record.getKey());

		DbTemplate.doTranscation(con, new ContentHandler() {
			@Override
			public void handle(Connection con) throws Exception {
				DbTemplate.doExecute(con, updateSql.toString(), paramList);
			}
		});
	}

	public List<Object> fillparamList(StockRecord record) {

		List<Object> paramList = new ArrayList<Object>();

		paramList.add(record.getId());
		paramList.add(record.getBreed());
		paramList.add(record.getCustomer());
		paramList.add(record.getInStock());
		paramList.add(record.getInRealStock());
		paramList.add(record.getInDate());
		paramList.add(record.getInReason());
		paramList.add(record.getNoticeDate());
		paramList.add(record.getOutStock());
		paramList.add(record.getFee());
		paramList.add(record.getDepot());
		paramList.add(record.getRemark());

		return paramList;

	}

	public String exportRecord(Connection connection, final StockCondition condition) throws Exception {
		StringBuilder totalSql = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		prepareQuery(connection, totalSql, paramList, null, condition);

		ResultHandler<String> resultHandler = new ResultHandler<String>() {
			@Override
			public String handle(ResultSet ret) throws Exception {
				return Excelhelper.exportRet(ret, condition);
			}
		};
		return DbTemplate.doQuery(connection, totalSql.toString(), paramList, resultHandler);

	}

	public StockList queryRecord(Connection connection, StockCondition condition, Page page) throws Exception {

		StringBuilder totalSql = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();

		prepareQuery(connection, totalSql, paramList, page, condition);

		ResultHandler<List<StockRecord>> resultHandler = new ResultHandler<List<StockRecord>>() {
			@Override
			public List<StockRecord> handle(ResultSet ret) throws SQLException {

				List<StockRecord> lists = new ArrayList<StockRecord>();
				while (ret.next()) {
					int i = 1;
					StockRecord record = new StockRecord(ret.getInt(i++), ret.getInt(i++), ret.getString(i++),
							ret.getString(i++), ret.getInt(i++), ret.getInt(i++), ret.getDate(i++), ret.getString(i++),
							ret.getDate(i++), ret.getInt(i++), ret.getFloat(i++), ret.getInt(i++), ret.getString(i++),
							(Object[]) (ret.getObject(i++)));
					lists.add(record);
				}
				return lists;
			}
		};
		List<StockRecord> list = DbTemplate.doQuery(connection, totalSql.toString(), paramList, resultHandler);

		StockList stockList = new StockList(page, list);
		return stockList;
	}

	public void removeRecord(Connection con, StockRecord record) throws Exception {

		final StringBuilder deleteSql = new StringBuilder();
		deleteSql.append("delete from R where key = ?");
		final List<Object> paramList = new ArrayList<Object>();
		paramList.add(record.getKey());

		DbTemplate.doTranscation(con, new ContentHandler() {
			@Override
			public void handle(Connection con) throws Exception {
				DbTemplate.doExecute(con, deleteSql.toString(), paramList);
			}
		});
	}

	public void prepareQuery(Connection connection, StringBuilder totalSql, List<Object> paramList, Page page,
			StockCondition condition) throws Exception {
		StringBuilder querySql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder(" where 1=1 ");
		StringBuilder limitSql = new StringBuilder();
		StringBuilder orderSql = new StringBuilder(" ORDER BY KEY DESC");

		querySql.append("SELECT * FROM R ");

		if (condition != null) {
			if (StringUtils.isNotBlank(condition.getId())) {
				whereSql.append(" and ID=? ");
				paramList.add(condition.getId());
			}
			if (StringUtils.isNotBlank(condition.getCustomer())) {
				whereSql.append(" and CUSTOMER=? ");
				paramList.add(condition.getCustomer());
			}

			if (StringUtils.isNotBlank(condition.getNoticeMaxDate())) {
				whereSql.append(" and NOTICEDATE<=?");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dateFormat.parse(condition.getNoticeMaxDate());
				paramList.add(date);
			}

			if (condition.getIsAllOut() != null) {
				if (condition.getIsAllOut()) {
					whereSql.append(" and INREALSTOCK-OUTSTOCK = 0");
				} else {
					whereSql.append(" and INREALSTOCK-OUTSTOCK > 0");
				}
			}

			if (condition.getIsfee() != null) {
				if (condition.getIsfee()) {
					whereSql.append(" and FEE>0");
				} else {
					whereSql.append(" and FEE=0");
				}
			}
			if (condition.getDepot() != null && condition.getDepot() > 0) {
				whereSql.append(" and DEPOT=?");
				paramList.add(condition.getDepot());
			}

		}

		if (page != null) {
			long count = DbTemplate.doQuery(connection, "SELECT COUNT(1) FROM R" + whereSql, paramList,
					new ResultHandler<Long>() {

						@Override
						public Long handle(ResultSet ret) throws SQLException {
							ret.next();
							return ret.getLong(1);
						}
					});

			page.setTotalSize(count);
			int begin = (page.getCurPage() - 1) * page.getPageSize();
			limitSql.append(" LIMIT " + begin + "," + page.getPageSize());
		}
		totalSql.append(querySql.toString() + whereSql + orderSql + limitSql);
	}

	public StockRecord addOutStock(Connection connection, final Integer key, final Integer out,
 final String dateField)
			throws Exception {

		final StockRecord nstock = new StockRecord();
		DbTemplate.doTranscation(connection, new ContentHandler() {

			@Override
			public void handle(Connection con) throws Exception {

				StockRecord stock = get(con, key);

				List<Object> l = new ArrayList<Object>();
				if (stock.getOutDetail() != null) {
					l.addAll(Arrays.asList(stock.getOutDetail()));
				}
				// SimpleDateFormat dateFormat = new
				// SimpleDateFormat("yyyy-MM-dd");
				l.add(out + ";" + dateField);

				int nout = stock.getOutStock() + out;
				List<Object> paramList = new ArrayList<Object>();
				String sql = " UPDATE R SET OUTSTOCK =? ,OUTDETAIL= ? WHERE KEY = ?";
				paramList.add(nout);
				paramList.add(l.toArray(new Object[l.size()]));
				paramList.add(key);
				DbTemplate.doExecute(con, sql, paramList);

				nstock.setOutStock(nout);
				nstock.setOutDetail(l.toArray(new Object[l.size()]));
			}
		});

		return nstock;
	}

	public StockRecord get(Connection connection, Integer key) throws Exception {

		String sql = "SELECT * FROM R WHERE KEY=?";
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(key);
		return DbTemplate.doQuery(connection, sql, paramList, new ResultHandler<StockRecord>() {

			@Override
			public StockRecord handle(ResultSet ret) throws SQLException {
				ret.next();
				int i = 1;
				StockRecord record = new StockRecord(ret.getInt(i++), ret.getInt(i++), ret.getString(i++), ret
						.getString(i++), ret.getInt(i++), ret.getInt(i++), ret.getDate(i++), ret.getString(i++), ret
						.getDate(i++), ret.getInt(i++), ret.getFloat(i++), ret.getInt(i++), ret.getString(i++),
						(Object[]) (ret.getObject(i++)));
				return record;
			}
		});
	}

}
