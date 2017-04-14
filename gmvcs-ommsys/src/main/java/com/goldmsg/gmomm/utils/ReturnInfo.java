package com.goldmsg.gmomm.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.goldmsg.core.utils.JsonUtil;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.system.SystemCode;

/**
 * 返回JSON信息组装类，将需要返回的信息按照固定的格式组装成json
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年4月28日 下午4:59:03
 */
@Repository
public class ReturnInfo {

	public static SystemCode errorCode;

	public static void setErrorCode(SystemCode errorCode) {
		ReturnInfo.errorCode = errorCode;
	}

	// 失败时传入此常量
	public static final int FAILED = 0;
	// 成功时传入此常量
	public static final int SUCCESS = 1;
	// 未获取到用户session时传入此常量
	public static final int NOSESSION = 2;
	// 未获取到用户token时传入此常量
	public static final int NOTOKEN = 3;
	// 传入参数错误时传入此常量
	public static final int PARAMERROR = 4;
	// 无权限时传入此常量
	public static final int NOPRIVILEGE = 5;
	// 系统内部错误时传入此常量
	public static final int INTERNALERROR = 6;
	// 找不到用户
	public static final int ERROR_ACOUNT = 7;
	// 不存在的任务
	public static final int TASK_NOT_EXISTS = 8;
	// 录像结果还未生成
	public static final int TASK_RESULTS_NOT_EXISTS = 9;
	// 执法仪不存在
	public static final int DSJ_NOT_EXISTS = 10;
	// 配置文件格式错误
	public static final int CONFIG_FORMAT_ERROR = 11;
	// 对象已存在
	public static final int OBJECT_EXISTS_ERROR = 12;
	// 执法仪已经存在
	public static final int DSJ_ALREADY_EXISTS = 13;
	// 错误的部门
	public static final int ERROR_ORG_INFO = 14;
	// 不存在的工作站
	public static final int WS_NOT_EXISTS = 15;

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

	/***
	 * 生成响应实体
	 * 
	 * @param result
	 *            返回值
	 * @return
	 */
	public static <T> BaseResponse<T> genResponseEntity(int result) {
		return genResponseEntity(result, null);
	}

	/***
	 * 生成响应实体
	 * 
	 * @param result
	 *            返回值
	 * @param body
	 *            实体内容
	 * @return
	 */
	public static <T> BaseResponse<T> genResponseEntity(int result, T body) {
		BaseResponse<T> response = new BaseResponse<T>();
		BaseResponse<T>.ResponseHeader headers = response.new ResponseHeader();

		switch (result) {
		case FAILED:
			headers.setRet(1000);
			headers.setMsg(errorCode.getFAILED());
			break;
		case SUCCESS:
			headers.setRet(0);
			headers.setMsg(errorCode.getSUCCESS());
			break;
		case NOSESSION:
			headers.setRet(1002);
			headers.setMsg(errorCode.getNOSESSION());
			break;
		case NOTOKEN:
			headers.setRet(1003);
			headers.setMsg(errorCode.getNOTOKEN());
			break;
		case PARAMERROR:
			headers.setRet(2001);
			headers.setMsg(errorCode.getPARAMERROR());
			break;
		case NOPRIVILEGE:
			headers.setRet(2002);
			headers.setMsg(errorCode.getNOPRIVILEGE());
			break;
		case INTERNALERROR:
			headers.setRet(1001);
			headers.setMsg(errorCode.getINTERNALERROR());
			break;
		case ERROR_ACOUNT:
			headers.setRet(7);
			headers.setMsg(errorCode.getERROR_ACOUNT());
			break;
		case TASK_NOT_EXISTS:
			headers.setRet(TASK_NOT_EXISTS);
			headers.setMsg(errorCode.getTASK_NOT_EXISTS());
			break;
		case TASK_RESULTS_NOT_EXISTS:
			headers.setRet(TASK_RESULTS_NOT_EXISTS);
			headers.setMsg(errorCode.getTASK_RESULTS_NOT_EXISTS());
			break;
		case DSJ_NOT_EXISTS:
			headers.setRet(DSJ_NOT_EXISTS);
			headers.setMsg(errorCode.getDSJ_NOT_EXISTS());
			break;
		case CONFIG_FORMAT_ERROR:
			headers.setRet(CONFIG_FORMAT_ERROR);
			headers.setMsg(errorCode.getCONFIG_FORMAT_ERROR());
			break;
		case OBJECT_EXISTS_ERROR:
			headers.setRet(OBJECT_EXISTS_ERROR);
			headers.setMsg(errorCode.getOBJECT_EXISTS_ERROR());
			break;
		case DSJ_ALREADY_EXISTS:
			headers.setRet(DSJ_ALREADY_EXISTS);
			headers.setMsg(errorCode.getDSJ_ALREADY_EXISTS());
			break;
		case ERROR_ORG_INFO:
			headers.setRet(ERROR_ORG_INFO);
			headers.setMsg(errorCode.getERROR_ORG_INFO());
			break;
		case WS_NOT_EXISTS:
			headers.setRet(WS_NOT_EXISTS);
			headers.setMsg(errorCode.getWS_NOT_EXISTS());
			break;
		default:
			headers.setRet(9999);
			headers.setMsg(errorCode.getUNKNOWERROR());
		}

		response.setHeaders(headers);
		response.setBody(body);

		return response;
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
		Map<String, Object> obj = new HashMap<String, Object>();
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
		return JsonUtil.toJsonString(obj);
	}

}
