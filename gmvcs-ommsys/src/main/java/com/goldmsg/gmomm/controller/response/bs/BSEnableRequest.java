package com.goldmsg.gmomm.controller.response.bs;

import javax.validation.constraints.NotNull;

/***
 * 启用或者停用业务类别request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午5:19:40
 */
public class BSEnableRequest {

	@NotNull
	private Integer id;	//业务类别id
	@NotNull
	private Boolean enable;	//是否启用
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
