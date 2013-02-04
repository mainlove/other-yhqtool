package org.mainolove.project.java.tool.yhqtool.yhqtool.model;

import org.mainolove.project.java.tool.yhqtool.yhqtool.service.DutyService;

public class StockCondition {

	private String id;
	private String customer;
	private String noticeMaxDate;
	// private Integer outin;
	private Boolean isAllOut;
	private Boolean isfee;
	private Integer depot;

	public String getId() {
		return id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getNoticeMaxDate() {
		return noticeMaxDate;
	}

	public void setNoticeMaxDate(String noticeMaxDate) {
		this.noticeMaxDate = noticeMaxDate;
	}

	//
	// public Integer getOutin() {
	// return outin;
	// }

	public Boolean getIsfee() {
		return isfee;
	}

	public Integer getDepot() {
		return depot;
	}

	// public void setOutin(Integer outin) {
	// this.outin = outin;
	// }

	public void setIsfee(Boolean isfee) {
		this.isfee = isfee;
	}

	public void setDepot(Integer depot) {
		this.depot = depot;
	}

	public Boolean getIsAllOut() {
		return isAllOut;
	}

	public void setIsAllOut(Boolean isAllOut) {
		this.isAllOut = isAllOut;
	}

	@Override
	public String toString() {
		return "条件 [单号=" + id + ", 名称=" + customer + ", 到期日期=" + noticeMaxDate + ", 是否出完" + isAllOut + ", 是否产生费用="
				+ isfee + ", 仓库=" + DutyService.getDepotIntToNameMap().get(depot) + "]";
	}

}
