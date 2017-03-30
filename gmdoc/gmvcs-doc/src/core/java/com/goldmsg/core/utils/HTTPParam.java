package com.goldmsg.core.utils;

public class HTTPParam {
	// 请求参数
	private String key;
	// 参数值
	private String value;

	public HTTPParam() {

	}

	public HTTPParam(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
