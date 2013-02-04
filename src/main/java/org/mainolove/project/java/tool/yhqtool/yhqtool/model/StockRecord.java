package org.mainolove.project.java.tool.yhqtool.yhqtool.model;

import java.util.Arrays;
import java.util.Date;

public class StockRecord {

	private Integer key;

	private Integer id = 0;
	private String breed;
	private String customer;

	private Integer inStock = 0;
	private Integer inRealStock = 0;
	private Date inDate;
	private String inReason;
	private Date noticeDate;

	private Integer outStock = 0;

	private Float fee = 0f;
	private Integer depot;
	private String remark;

	private Object[] outDetail = new Object[] {};

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Integer getInStock() {
		return inStock;
	}

	public void setInStock(Integer inStock) {
		this.inStock = inStock;
	}

	public Integer getInRealStock() {
		return inRealStock;
	}

	public void setInRealStock(Integer inRealStock) {
		this.inRealStock = inRealStock;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public String getInReason() {
		return inReason;
	}

	public void setInReason(String inReason) {
		this.inReason = inReason;
	}

	public Date getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	public Integer getOutStock() {
		return outStock;
	}

	public void setOutStock(Integer outStock) {
		this.outStock = outStock;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Float getFee() {
		return fee;
	}

	public void setFee(Float fee) {
		this.fee = fee;
	}

	public Integer getDepot() {
		return depot;
	}

	public void setDepot(Integer depot) {
		this.depot = depot;
	}

	public Object[] getOutDetail() {
		return outDetail;
	}

	public void setOutDetail(Object[] outDetail) {
		this.outDetail = outDetail;
	}

	public StockRecord() {
		super();
	}


	@Override
	public String toString() {
		return "StockRecord [key=" + key + ", id=" + id + ", breed=" + breed + ", customer=" + customer + ", inStock="
				+ inStock + ", inRealStock=" + inRealStock + ", inDate=" + inDate + ", inReason=" + inReason
				+ ", noticeDate=" + noticeDate + ", outStock=" + outStock + ", fee=" + fee + ", depot=" + depot
				+ ", remark=" + remark + ", outDetail=" + Arrays.toString(outDetail) + "]";
	}

	public StockRecord(Integer key, Integer id, String breed, String customer, Integer inStock, Integer inRealStock,
			Date inDate, String inReason, Date noticeDate, Integer outStock, Float fee, Integer depot, String remark,
			Object[] outDetail) {
		super();
		this.key = key;
		this.id = id;
		this.breed = breed;
		this.customer = customer;
		this.inStock = inStock;
		this.inRealStock = inRealStock;
		this.inDate = inDate;
		this.inReason = inReason;
		this.noticeDate = noticeDate;
		this.outStock = outStock;
		this.fee = fee;
		this.depot = depot;
		this.remark = remark;
		this.outDetail = outDetail;
	}

	public StockRecord(Integer id, String breed, String customer, Integer inStock, Integer inRealStock, Date inDate,
			String inReason, Date noticeDate, Integer outStock, Float fee, Integer depot, String remark) {
		super();
		this.id = id;
		this.breed = breed;
		this.customer = customer;
		this.inStock = inStock;
		this.inRealStock = inRealStock;
		this.inDate = inDate;
		this.inReason = inReason;
		this.noticeDate = noticeDate;
		this.outStock = outStock;
		this.fee = fee;
		this.depot = depot;
		this.remark = remark;
	}



}
