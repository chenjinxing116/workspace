package com.goldmsg.gmomm.controller.request.fixedplace;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 基础房间信息请求request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午2:25:56
 */
public class BaseRoomRequest {

	@NotBlank
	private String name;	//房间名
	private List<String> roomType;  //房间类型
	private String desc;	//描述
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getRoomType() {
		return roomType;
	}
	public void setRoomType(List<String> roomType) {
		this.roomType = roomType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
