package com.goldmsg.gmomm.controller.request.ws;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 注册采集工作站request
 * @author QH
 *
 */
public class WSRegisterRequest {

	@NotBlank
	private String wsName;	//名称
	@NotBlank
	private String orgCode;	//部门编号
	@NotBlank
	private String ipAddr;	//IP地址 
	@NotBlank
	private String fzr;	//负责人
	@NotBlank
	private String phoneNumber;	//负责人电话
	@NotBlank
	private String wsAddr;	//工作站地址
	
	public String getWsName() {
		return wsName;
	}
	public void setWsName(String wsName) {
		this.wsName = wsName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getFzr() {
		return fzr;
	}
	public void setFzr(String fzr) {
		this.fzr = fzr;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getWsAddr() {
		return wsAddr;
	}
	public void setWsAddr(String wsAddr) {
		this.wsAddr = wsAddr;
	}
}
