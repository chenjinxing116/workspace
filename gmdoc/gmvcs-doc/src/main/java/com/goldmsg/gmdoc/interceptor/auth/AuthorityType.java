package com.goldmsg.gmdoc.interceptor.auth;

public enum AuthorityType {
	// 包含了枚举的中文名称, 枚举的索引值
	HOMEPAGE("首页", "0001"),
	DOCMANAGE("文档管理", "0002"),
	CATOMANAGE("分类管理", "0003"),
	USERCENTER("个人中心","0004"), 
	USERAUTH("用户权限", "0005"),
	SYSMANAGE("系统管理","0006");
	
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
