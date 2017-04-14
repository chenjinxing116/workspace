package com.goldmsg.gmomm.controller.request.platform;

import javax.validation.constraints.NotNull;

/***
 * 修改平台信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 上午10:25:28
 */
public class ModifyPlatformRequest extends RegisterPlatformRequest {

	@NotNull
	private String sid;	//平台id

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
