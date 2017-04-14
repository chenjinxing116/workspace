package com.goldmsg.gmomm.controller.request.platform;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 注册平台信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 上午10:11:13
 */
public class RegisterPlatformRequest {

	@NotBlank
	private String username;	//用户名
	@NotBlank
	private String pwd;	//密码
	@NotNull
	private String facturer;	//厂商 
	@NotBlank
	private String name;	//平台名称
	@NotNull
	private Integer type;	//平台类型
	@NotBlank
	private String model;	//平台型号
	@NotBlank
	private String ip;	//平台ip
	@NotNull
	private Integer port;	//平台端口
	@NotBlank
	private String csid;	//存储服务器序号
	
	private Integer orgId;  //部门id


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getFacturer() {
		return facturer;
	}

	public void setFacturer(String facturer) {
		this.facturer = facturer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
}
