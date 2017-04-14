package com.goldmsg.gmomm.controller.response.bs;

import javax.validation.constraints.NotNull;

/***
 * 移除岗位信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午5:25:10
 */
public class RemovePostTypeRequest {

	@NotNull
	private Integer postId;	//岗位id
	@NotNull
	private Integer bsId;	//业务id
	
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	public Integer getBsId() {
		return bsId;
	}
	public void setBsId(Integer bsId) {
		this.bsId = bsId;
	}
}
