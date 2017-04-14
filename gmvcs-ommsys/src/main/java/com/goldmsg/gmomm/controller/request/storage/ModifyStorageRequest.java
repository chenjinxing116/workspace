package com.goldmsg.gmomm.controller.request.storage;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 修改存储信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午3:28:46
 */
public class ModifyStorageRequest {

	@NotBlank
	private String id;	//存储服务器id
	@NotBlank
	private String name;	//存储名称
	@NotBlank
	private String admin;	//负责人
	@NotBlank
	private String phone;	//联系电话
	@NotBlank
	private String address;	//服务器地址
	@NotNull
	private Integer orgId;	//部门id
	@NotBlank
	private String orgName;	//部门名称
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
