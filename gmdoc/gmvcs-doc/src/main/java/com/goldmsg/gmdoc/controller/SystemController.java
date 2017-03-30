package com.goldmsg.gmdoc.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goldmsg.core.helpers.PropertiesHelper;
import com.goldmsg.core.utils.JsonUtil;
import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.entity.AppVersion;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.interceptor.auth.AuthorityType;
import com.goldmsg.gmdoc.interceptor.auth.FireAuthority;
import com.goldmsg.gmdoc.interceptor.auth.ResultTypeEnum;
import com.goldmsg.gmdoc.service.GMUserService;
import com.goldmsg.gmdoc.service.SystemService;
import com.gosun.service.user.IUserService;

/**
 * 系统设备controller
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年4月29日 上午9:45:42
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController {

	@Autowired
	IUserService iUserService;

	@Autowired
	GMUserService gmUserService;

	@Autowired
	SystemService systemService;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	/**
	 * 系统权限设置界面
	 * 
	 * @return 如果用户存在且有权限，返回系统权限设置界面；如果用户不存在，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.USERAUTH }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/auth.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String index() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			request.setAttribute("distInfo", distInfo);
			return "UserAuth";
		}
	}

	/**
	 * 获取部门树方法，系统会根据用户登录信息加载部门树
	 * 
	 * @return 以JSON格式返回部门树
	 */
	@RequestMapping(value = "/org.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getOrgTreeByOrgId() {
		return iUserService.getOrgTree().toString();
	}

	/**
	 * 根据部门id查询该部门下所有用户
	 * 
	 * @param org_id
	 *            部门id
	 * @return 以JSON格式返回该部门下所有用户信息
	 */
	@RequestMapping(value = "/org/user.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getUserListByOrgId(@RequestParam("org_id") int org_id) {
		List<Map<String, Object>> userList = gmUserService.getUserListByOrgId(org_id);
		return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, userList);
	}

	@RequestMapping(value = "/user/auth.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getUserAuthInfo(@RequestParam("user_code") String user_code) {
		TUserInfo curUserInfo = (TUserInfo) session.getAttribute("userInfo");
		if (curUserInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			Map<String, Object> userAuthMap = gmUserService.getUserAuthInfo(user_code);
			if (userAuthMap.isEmpty()) {
				return ReturnInfo.genResultJson(ReturnInfo.FAILED);
			} else {
				return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, userAuthMap);
			}
		}
	}

	/**
	 * 修改用户阅读等级方法
	 * 
	 * @param requestBody
	 *            以JSON格式传参如sec_level和user_code两个参数
	 * @return 以JSON格式返回修改成功或失败信息
	 */
	@RequestMapping(value = "/sec_level.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String setUserSec_level(@RequestBody String requestBody) {
		TUserInfo curUserInfo = (TUserInfo) session.getAttribute("userInfo");
		if (curUserInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("sec_level") || !map.containsKey("user_code")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		int sec_level = (int) map.get("sec_level");
		String user_code = map.get("user_code").toString();
		boolean result = gmUserService.updateUserSecLevel(sec_level, user_code);
		if (result) {
			if (curUserInfo.getUserCode().equals(user_code)) {
				curUserInfo.setSecLevel(sec_level);
				gmUserService.refreshUserInfoInMemory(curUserInfo);
			}
		}
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}

	/**
	 * 版本检查更新方法
	 * 
	 * @param token
	 *            用户token验证字符串
	 * @param op_type
	 *            用户所使用的操作系统类型
	 * @param op_version
	 *            用户所使用的操作系统版本
	 * @return 以JSON格式返回可更新到的app版本信息
	 */
	@RequestMapping(value = "/version/check.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getNewestVersion(@RequestParam(value = "token") String token, @RequestParam("op_type") String op_type,
			@RequestParam("op_version") String op_version) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = gmUserService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		AppVersion app = systemService.findNewestAvailableVersion(op_type, op_version);
		if (app == null) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			Map<String, Object> versionMap = new HashMap<String, Object>();
			versionMap.put("app_id", app.getId());
			versionMap.put("app_code", app.getAppCode());
			versionMap.put("app_name", app.getAppName());
			versionMap.put("app_version", app.getAppVersion());
			versionMap.put("app_op_type", app.getAppOpType());
			versionMap.put("app_op_version", app.getAppOpVersion());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			versionMap.put("publish_time", sdf.format(app.getPublishTime()));
			String nginx_url = PropertiesHelper.getProperty("nginx.url");
			String url = nginx_url + app.getApkName();
			versionMap.put("url", url);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, versionMap);
		}
	}

	@RequestMapping(value = "/app/url.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getNewestAppUrl() {
		AppVersion app = systemService.findNewestAppVersion();
		if (app == null) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			String nginx_url = PropertiesHelper.getProperty("nginx.url");
			String url = nginx_url + app.getApkName();
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, url);
		}
	}
}
