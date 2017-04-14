package com.goldmsg.gmomm.controller.request.fixedplace;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 修改房间信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午2:27:05
 */
public class ModifyRoomRequest extends BaseRoomRequest {

	@NotBlank
	private String roomId;	//房间id
	
	private String bazxId;	//办案中心id

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getBazxId() {
		return bazxId;
	}

	public void setBazxId(String bazxId) {
		this.bazxId = bazxId;
	}
	
}
