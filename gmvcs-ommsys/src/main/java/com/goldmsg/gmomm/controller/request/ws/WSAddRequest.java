package com.goldmsg.gmomm.controller.request.ws;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 添加工作站request
 * 
 * @author QH
 * @Email: qhs_dream@163.com
 * @Date: 2016年11月22日: 下午4:19:03
 */
public class WSAddRequest {

	@NotNull
	private Integer orgId; // 部门id
	@NotBlank
	private String manufacturer; // 厂商
	@NotBlank
	private String wsName; // 工作站名称

	private String storageId; // 上级存储

	@NotBlank
	private String admin; // 管理员
	@NotBlank
	private String phone; // 联系电话
	@NotBlank
	private String addr; // 工作站地址
	@NotBlank
	private String ip; // 工作站ip

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

	public String getWsName() {
		return wsName;
	}

	public void setWsName(String wsName) {
		this.wsName = wsName;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
