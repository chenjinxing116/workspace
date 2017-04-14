package com.goldmsg.gmomm.controller.request.fixedplace;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 注册房间信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午1:47:57
 */
public class RegisterRoomRequest extends BaseRoomRequest {

	@NotBlank
	private String bazxId;	//办案中心id

	public String getBazxId() {
		return bazxId;
	}

	public void setBazxId(String bazxId) {
		this.bazxId = bazxId;
	}
	
}
