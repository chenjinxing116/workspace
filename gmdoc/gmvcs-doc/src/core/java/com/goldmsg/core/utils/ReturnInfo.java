package com.goldmsg.core.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 返回JSON信息组装类，将需要返回的信息按照固定的格式组装成json
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年4月28日 下午4:59:03
 */
public class ReturnInfo {

	/** 失败时传入此常量 */
	public static final int FAILED = 0;
	/** 成功时传入此常量 */
	public static final int SUCCESS = 1;
	/** 未获取到用户session时传入此常量 */
	public static final int NOSESSION = 2;
	/** 未获取到用户token时传入此常量 */
	public static final int NOTOKEN = 3;
	/** 传入参数错误时传入此常量 */
	public static final int PARAMERROR = 4;
	/** 无权限时传入此常量 */
	public static final int NOPRIVILEGE = 5;
	/** 系统内部错误时传入此常量 */
	public static final int INTERNALERROR = 6;

	/**
	 * 生成JSON格式的返回信息
	 * 
	 * @param result
	 *            传入的常量值，根据不同情况进行选择
	 * @return 返回json格式信息
	 */
	public static String genResultJson(int result) {
		return genResultJson(result, null);
	}

	/**
	 * 生成JSON格式的返回信息
	 * 
	 * @param result
	 *            传入的常量值，根据不同情况进行选择
	 * @param body
	 *            需要返回的主体信息
	 * @return 返回json格式信息
	 */
	public static String genResultJson(int result, Object body) {
		Map<String, Object> headers = new HashMap<String, Object>();
		JSONObject obj = new JSONObject();
		if (result == 0) {
			headers.put("ret", 1000);
			headers.put("msg", "failed");
		} else if (result == 1) {
			headers.put("ret", 0);
			headers.put("msg", "success");
		} else if (result == 2) {
			headers.put("ret", 1002);
			headers.put("msg", "session invalid");
		} else if (result == 3) {
			headers.put("ret", 1003);
			headers.put("msg", "token invalid");
		} else if (result == 4) {
			headers.put("ret", 2001);
			headers.put("msg", "parameter error");
		} else if (result == 5) {
			headers.put("ret", 2002);
			headers.put("msg", "no privilege");
		} else if (result == 6) {
			headers.put("ret", 1001);
			headers.put("msg", "internal error");
		} else {
			headers.put("ret", 9999);
			headers.put("msg", "unknown error");
		}
		obj.put("headers", headers);
		obj.put("body", body);
		return obj.toString();
	}
}
