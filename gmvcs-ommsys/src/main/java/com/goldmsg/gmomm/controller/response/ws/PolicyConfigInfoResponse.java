package com.goldmsg.gmomm.controller.response.ws;

import javax.validation.constraints.NotNull;

/***
 * 工作站策略配置信息response
 * @author QH
 *
 */
public class PolicyConfigInfoResponse {
	
	@NotNull
	private Integer days;	//默认时间（天）

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
}
