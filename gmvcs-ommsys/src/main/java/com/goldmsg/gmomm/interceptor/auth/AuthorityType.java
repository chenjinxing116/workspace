package com.goldmsg.gmomm.interceptor.auth;

public enum AuthorityType {
	// 包含了枚举的中文名称, 枚举的索引值
	DEVICEMANAGE("设备管理", "omm0100"), 
	DSJMANAGE("执法记录仪管理", "omm0101"), 
	
	WORKSTATIONMANAGE("采集工作站管理", "omm0102"), 
	
	STORAGEMANAGE("存储管理", "omm0200"), 
	
	USERMANAGE("用户管理", "omm0300"),
	CONFIGMANAGE("用户配置", "omm0301"),
	DICMANAGE("业务岗位字典", "omm0302");
	private String name;
	private String code;

	private AuthorityType(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}
}
