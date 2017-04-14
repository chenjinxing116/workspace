package com.goldmsg.gmomm.controller.request.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 修改用户信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午4:18:05
 */
public class ModifyUserRequest {

	@NotBlank
	private String bsCode;	//业务号
	@NotBlank
	private String userCode;	//警号
	@NotNull
	private Integer postId;	//岗位id
	@NotNull
	private Integer type;	//警员类别
	
	
	public String getBsCode() {
		return bsCode;
	}
	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
