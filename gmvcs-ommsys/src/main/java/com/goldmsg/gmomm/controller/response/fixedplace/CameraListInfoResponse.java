package com.goldmsg.gmomm.controller.response.fixedplace;

/***
 * 摄像头信息response
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 上午11:40:13
 */
public class CameraListInfoResponse {

	private String deviceId;	//摄像头编号
	private String name;	//摄像头名称 
	private String roomName;	//房间名称 
	private String roomId;	//房间名id
	private Integer type;	//摄像头类型
	private String typeStr;	//摄像头类型名
	private boolean checked;  //摄像头分配标识
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
