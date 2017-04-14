package com.goldmsg.gmomm.controller.request.dsj;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 修改执法仪信息request
 * @author QH
 *
 */
public class DSJModifyRequest {

	@NotBlank
	private String deviceId;	//设备id
	
	@NotNull
	private Integer orgId;	//部门编号
	
	@NotBlank
	private String userCode;	//警号
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}
