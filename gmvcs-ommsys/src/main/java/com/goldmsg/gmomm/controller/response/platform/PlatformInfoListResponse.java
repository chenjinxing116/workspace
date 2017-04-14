package com.goldmsg.gmomm.controller.response.platform;

/***
 * 办案区平台消息列表response
 * @author QH
 *
 */
public class PlatformInfoListResponse {

	private Integer port;	//端口
	private String sid;		//平台id
	private String status;		//状态
	private String model;		//型号
	private String csid;		//平台id名称
	private String facturer;		//制造商
	private String facturerChs;		//制造商中文名称
	private String ip;		 //ip地址
	private String name;		//名称
	private String pwd;		//密码
	private String typeStr;		//类型名称
	private Integer type;		//类型代号
	private String username;		//用户名
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid) {
		this.csid = csid;
	}
	public String getFacturer() {
		return facturer;
	}
	public void setFacturer(String facturer) {
		this.facturer = facturer;
	}
	public String getFacturerChs() {
		return facturerChs;
	}
	public void setFacturerChs(String facturerChs) {
		this.facturerChs = facturerChs;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
