package com.goldmsg.gmomm.controller.request.ws;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.goldmsg.gmomm.controller.response.ws.PolicyConfigInfoResponse;

/***
 * 修改工作站策略配置request
 * @author QH
 *
 */
public class PolicyConfigModifyRequest extends PolicyConfigInfoResponse {

	@NotBlank
	private String wsId;	//工作站id

	public String getWsId() {
		return wsId;
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

}
