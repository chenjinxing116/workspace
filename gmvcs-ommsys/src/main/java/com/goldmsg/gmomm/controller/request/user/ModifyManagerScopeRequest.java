package com.goldmsg.gmomm.controller.request.user;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/***
 * 修改用户管理范围request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午4:48:09
 */
public class ModifyManagerScopeRequest {

	@NotNull
	@NotBlank
	private String userCode;	//警号
	
	private List<Integer> addOrgIds;	//添加的部门id列表
	private List<Integer> deleteOrgIds;	//删除的部门id列表
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public List<Integer> getAddOrgIds() {
		return addOrgIds;
	}
	public void setAddOrgIds(List<Integer> addOrgIds) {
		this.addOrgIds = addOrgIds;
	}
	public List<Integer> getDeleteOrgIds() {
		return deleteOrgIds;
	}
	public void setDeleteOrgIds(List<Integer> deleteOrgIds) {
		this.deleteOrgIds = deleteOrgIds;
	}
}
