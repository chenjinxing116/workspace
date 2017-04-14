package com.goldmsg.gmomm.controller.response.ws;

/***
 * 工作站基础信息response
 * @author QH
 *
 */
public class WSBaseInfoResponse {

	private String wsName;	//工作站名称
	private String orgCode;	//部门编号
	private String ipAddr;	//ip地址
	private String admin;	//负责人
	private String phoneNumber;	//负责人联系电话
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
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
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
