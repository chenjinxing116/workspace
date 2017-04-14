package com.goldmsg.gmomm.controller.request.platform;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 摄像头信息修改request
 * @author QH
 *
 */
public class CameraInfoModifyRequest {

	@NotBlank
	private String name;	//名称
	private String channel;	//频道
	@NotNull
	private Integer type;	//摄像头类型
	private String desc;	//描述
	@NotBlank
	private String sid;	//摄像头id
	private String vrSid;	//虚拟摄像头id
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getVrSid() {
		return vrSid;
	}
	public void setVrSid(String vrSid) {
		this.vrSid = vrSid;
	}
}
