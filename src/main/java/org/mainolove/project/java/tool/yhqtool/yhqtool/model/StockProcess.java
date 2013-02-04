package org.mainolove.project.java.tool.yhqtool.yhqtool.model;

import java.util.Date;

public class StockProcess {

	private Integer key;

	private Integer id;
	private Date outDate;
	private Integer outStock;
	private Date opDate;
	private String remark;

	public Integer getKey() {
		return key;
	}

	public Integer getId() {
		return id;
	}

	public Date getOutDate() {
		return outDate;
	}

	public Integer getOutStock() {
		return outStock;
	}

	public Date getOpDate() {
		return opDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public void setOutStock(Integer outStock) {
		this.outStock = outStock;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
