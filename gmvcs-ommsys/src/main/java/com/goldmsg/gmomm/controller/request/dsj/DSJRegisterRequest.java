package com.goldmsg.gmomm.controller.request.dsj;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 注册执法仪request
 * @author QH
 *
 */
public class DSJRegisterRequest {

	
	@NotBlank
	private String manufacturers;	//厂商
	@NotBlank
	private String model;	//产品型号
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9]+")
	private String deviceId;	//设备id
	@NotBlank
	@Pattern(regexp = "^\\d+$")
	private String simCode;	//sim卡号，就是sim卡的号码（手机号），至少1位以上的数字
	
	private Integer orgId;	//部门编号
	private String userCode;	//警号
	
	public String getSimCode() {
		return simCode;
	}
	public void setSimCode(String simCode) {
		this.simCode = simCode;
	}
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
	public String getManufacturers() {
		return manufacturers;
	}
	public void setManufacturers(String manufacturers) {
		this.manufacturers = manufacturers;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
}
