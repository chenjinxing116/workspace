package com.goldmsg.gmomm.controller.request.ws;

import javax.validation.constraints.NotNull;


import com.goldmsg.gmomm.controller.request.BasePagingRequest;

/***
 * 查询采集工作站信息request
 * @author QH
 *
 */
public class WSListInfoRequest extends BasePagingRequest {

	@NotNull
	private Integer orgId;	//部门id

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
}
