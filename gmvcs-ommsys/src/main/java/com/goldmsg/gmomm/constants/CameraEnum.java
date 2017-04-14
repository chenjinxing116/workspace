package com.goldmsg.gmomm.constants;

public enum CameraEnum {

	PC_CAMERA(1, "网络摄像头"), SIMULATION_CAMERA(2, "模拟摄像头");

	private int type;

	private String typeName;

	CameraEnum(int type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}

	public int getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

}
