package com.goldmsg.gmomm.controller.response.bs;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 添加岗位request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午5:23:09
 */
public class AddPostTypeRequest {

	@NotBlank
	private String postName;	//岗位名称
	@NotNull
	private Integer bsId;	//业务id
	
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Integer getBsId() {
		return bsId;
	}
	public void setBsId(Integer bsId) {
		this.bsId = bsId;
	}
}
