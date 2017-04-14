package com.goldmsg.gmomm.controller.request;

import javax.validation.constraints.NotNull;

/***
 * 基础分页请求request
 * @author QH
 *
 */
public class BasePagingRequest {

	// 当前页数
	@NotNull
	private Integer page;
	
	// 每页数据量
	@NotNull
	private Integer pageSize;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
