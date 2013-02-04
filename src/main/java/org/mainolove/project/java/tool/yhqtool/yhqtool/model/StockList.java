package org.mainolove.project.java.tool.yhqtool.yhqtool.model;

import java.util.List;

public class StockList {

	private Page page;

	private List<StockRecord> list;

	public StockList(Page page, List<StockRecord> list) {
		super();
		this.page = page;
		this.list = list;
	}

	public Page getPage() {
		return page;
	}

	public List<StockRecord> getList() {
		return list;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void setList(List<StockRecord> list) {
		this.list = list;
	}

}
