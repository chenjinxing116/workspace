package com.goldmsg.gmomm.controller.request.ws;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 修改工作站信息request
 * @author QH
 *
 */
public class WSModifyRequest {

	@NotNull
	private Integer orgId;	//部门id
	
	@NotBlank
	private String manufacturer;	//厂商code
	
	@NotNull
	private String wsId;
	
	@NotNull
	private String wsName;	//工作站名称
	
	@NotNull
	private String admin;	//负责人
	
	@NotNull
	private String phoneNumber;	//负责人电话
	
	@NotNull
	private String wsAddr;	//工作站地址
	
	@NotBlank
	private String ip; //工作站ip
	
	private String storageId;	//存储id

	
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getWsId() {
		return wsId;
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	public String getWsName() {
		return wsName;
	}

	public void setWsName(String wsName) {
		this.wsName = wsName;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
