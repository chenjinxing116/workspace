package com.goldmsg.gmomm.controller.response.fixedplace;

public class RoomTypeListResponse {

	private String code; //房间类型编码
	
	private String name; //房间类型名称
	
	private String  ntType;  //房间对应笔录类型

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNtType() {
		return ntType;
	}

	public void setNtType(String ntType) {
		this.ntType = ntType;
	}
	
	
	
}
