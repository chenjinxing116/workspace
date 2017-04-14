package com.goldmsg.gmomm.controller.response.platform;

import java.util.Date;

/***
 * 摄像头信息列表response
 * @author QH
 *
 */
public class CameraInfoListResponse {

	private String channel;	//频道
	private String desc;	//描述
	private String name;	//名称
	private String roomId;	//房间id
	private String sid;	//摄像头id
	private Date synTime;	//同步时间
	private Integer type;	//类型
	private String typeStr;	//类型名称
	private String vrSid;	//模拟摄像头id
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Date getSynTime() {
		return synTime;
	}
	public void setSynTime(Date synTime) {
		this.synTime = synTime;
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
	public String getVrSid() {
		return vrSid;
	}
	public void setVrSid(String vrSid) {
		this.vrSid = vrSid;
	}
}
