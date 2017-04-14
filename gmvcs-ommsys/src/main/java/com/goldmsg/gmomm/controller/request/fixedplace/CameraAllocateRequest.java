package com.goldmsg.gmomm.controller.request.fixedplace;

import java.util.List;

/***
 * 摄像头分配信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午2:58:58
 */
public class CameraAllocateRequest {

	private List<String> deviceIds;	//摄像头id列表
	private String roomId;	//房间id
	
	public List<String> getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
