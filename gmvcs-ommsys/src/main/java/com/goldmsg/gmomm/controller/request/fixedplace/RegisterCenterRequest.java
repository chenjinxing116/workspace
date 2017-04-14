package com.goldmsg.gmomm.controller.request.fixedplace;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 注册办案中心request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午2:34:05
 */
public class RegisterCenterRequest {

	@NotBlank
	private int orgId;	//部门id
	@NotBlank
	private String name;	//办案中心名称
	
	private String desc;	//描述
  
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
