package org.mainolove.project.java.tool.yhqtool.yhqtool.model;

public class Page {

	private Integer pageSize = 10;

	private Integer curPage = 1;

	private long totalSize;

	public Page(Integer curPage) {
		super();
		this.curPage = curPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public Long getTotalPage() {

		if (totalSize % pageSize == 0) {
			return (totalSize / pageSize);
		} else {
			return (totalSize / pageSize) + 1;
		}

	}

}
