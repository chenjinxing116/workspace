package com.goldmsg.gmdoc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

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
import com.goldmsg.core.utils.HTTPParam;
import com.goldmsg.core.utils.JsonUtil;
import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.entity.RelUserColDoc;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.entity.RelUserReadDoc;
import com.goldmsg.gmdoc.entity.RelUserReadDocPK;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.interceptor.auth.AuthorityType;
import com.goldmsg.gmdoc.interceptor.auth.FireAuthority;
import com.goldmsg.gmdoc.interceptor.auth.ResultTypeEnum;
import com.goldmsg.gmdoc.service.CatoService;
import com.goldmsg.gmdoc.service.CollectionService;
import com.goldmsg.gmdoc.service.DistService;
import com.goldmsg.gmdoc.service.DocService;
import com.goldmsg.gmdoc.service.GMUserService;
import com.goldmsg.gmdoc.service.ReadService;
import com.goldmsg.gmdoc.service.RemoteCallService;
import com.gosun.service.entity.UserRsp;
import com.gosun.service.user.IUserService;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * 首页和个人中心controller
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年5月4日 下午5:08:54
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	CollectionService coleService;

	@Autowired
	ReadService readService;

	@Autowired
	CatoService catoService;

	@Autowired
	GMUserService userService;

	@Autowired
	DocService docService;

	@Autowired
	DistService distService;

	@Autowired
	IUserService iUserService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpSession session;

	@Autowired
	MemcachedClient memcachedClient;

	@Autowired
	RemoteCallService remoteCallService;

	/**
	 * web端-首页
	 * 
	 * @return 返回首页页面，如果失败，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.HOMEPAGE }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/index.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String index() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			int dist_id = distInfo.getDistId();
			List<Map<String, Object>> catoInfoList = catoService.getCatoInfoList(userInfo, dist_id);
			List<Map<String, Object>> pubInfoList = docService.getLatestPubDocs(userInfo, dist_id, 0, 8);
			List<Map<String, Object>> collectionBillboardList = docService.getHotColeDocs(userInfo, dist_id, 0, 6);
			List<Map<String, Object>> readBillboardList = docService.getHotReadDocs(userInfo, dist_id, 0, 6);
			request.setAttribute("distInfo", distInfo);
			request.setAttribute("catoList", catoInfoList);
			request.setAttribute("pubInfoList", pubInfoList);
			request.setAttribute("collectionBillboardList", collectionBillboardList);
			request.setAttribute("readBillboardList", readBillboardList);
			return "index";
		}

	}

	/**
	 * 个人中心
	 * 
	 * @return 返回个人中心页面，如果失败，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.USERCENTER }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/center.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String center() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			request.setAttribute("distInfo", distInfo);
			String userName = userInfo.getUserName();
			UserRsp userRsp = iUserService.getUserInfoByAccountName(userInfo.getUserCode());
			String orgName = userRsp.getOrgName();
			request.setAttribute("userName", userName);
			request.setAttribute("userGender", userRsp.getGender());
			request.setAttribute("orgName", orgName);
			return "UserCenter";
		}
	}

	/**
	 * 登录 提供app用户登录的功能
	 * 
	 * @param requestBody
	 *            请求体中加入username和password两个参数
	 * @return 以JSON格式返回用户登录成功或失败信息
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String applogin(@RequestBody String requestBody) {
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("username") || !map.containsKey("password")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		String accountName = map.get("username").toString();
		UserRsp rsp = iUserService.getUserInfoByAccountName(accountName);
		if (rsp == null) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		long accountId = rsp.getAccountId();
		String password = map.get("password").toString();
		// uap库tb_v2_user表中的accountName字段与gmdoc库中t_user_info表中的user_code字段对应
		TUserInfo userInfo = userService.getUserInfoByUserCode(accountName);
		if (userInfo == null) {
			// 如果userInfo还未创建，则创建
			String user_code = rsp.getAccountName();
			String user_name = rsp.getUserName();
			userInfo = userService.createUserInfo(user_code, user_name);
			if (userInfo == null) {
				// 创建用户失败，返回内部错误
				return ReturnInfo.genResultJson(ReturnInfo.INTERNALERROR);
			}
		}
		// 验证用户密码
		boolean check = iUserService.checkUserPassword(accountId, password);
		if (check) {
			String token = UUID.randomUUID().toString().replace("-", "");
			try {
				memcachedClient.set(token, 60 * 60 * 24 * 14, userInfo);
				memcachedClient.set("gmdoc" + userInfo.getUserCode(), 60 * 60 * 24 * 14, token);
			} catch (TimeoutException | InterruptedException | MemcachedException e) {
				return ReturnInfo.genResultJson(3);
			}
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("token", token);
			TDistrictDict dist = distService.findByDistId(userInfo.getDistInfo().getDistId());
			body.put("dist_id", dist.getDistId());
			body.put("dist_name", dist.getDistName());
			body.put("dist_code", dist.getDistCode());
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, body);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		}
	}

	/**
	 * 登出 提供用户登出app的功能
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @return 以JSON格式返回用户登出成功或失败信息
	 */
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appLogout(@RequestParam("token") String token) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		session.invalidate();
		try {
			TUserInfo userInfo = memcachedClient.get(token);
			memcachedClient.delete("gmdoc" + userInfo.getUserCode());
			memcachedClient.delete(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		return ReturnInfo.genResultJson(ReturnInfo.SUCCESS);
	}

	/**
	 * 登出 提供用户登出web的功能
	 * 
	 * @return 登出后跳转至登录界面
	 */
	@RequestMapping(value = "/logout.action")
	public String webLogout() {
		session.invalidate();
		return "redirect:/user/index.action";
	}

	/**
	 * 修改密码 修改用户的密码
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param requestBody
	 *            请求体中包括新密码newPwd和旧密码oldPwd2个参数
	 * @return 以JOSN格式返回修改密码成功或失败信息
	 */
	@RequestMapping(value = "/password.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appModUserPassword(@RequestParam(value = "token") String token, @RequestBody String requestBody) {
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (token == null || token.equals("") || !map.containsKey("newPwd") || !map.containsKey("oldPwd")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		String newPassword = (String) map.get("newPwd");
		String oldPassword = (String) map.get("oldPwd");
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		long accountId = iUserService.getUserInfoByAccountName(userInfo.getUserCode()).getAccountId();
		boolean check = iUserService.checkUserPassword(accountId, oldPassword);
		if (check) {
			// 验证密码成功，修改密码，返回成功信息
			boolean result = iUserService.modUserPassword(accountId, newPassword, oldPassword);
			return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
		} else {
			// 验证密码失败，不修改密码，返回失败信息
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		}
	}

	/**
	 * 修改密码 修改用户的密码
	 * 
	 * @param requestBody
	 *            请求体中包括新密码newPwd和旧密码oldPwd2个参数
	 * @return 以JOSN格式返回修改密码成功或失败信息
	 */
	@RequestMapping(value = "/password.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String modUserPassword(@RequestBody String requestBody) {
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		String newPassword = (String) map.get("newPwd");
		String oldPassword = (String) map.get("oldPwd");
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		long accountId = iUserService.getUserInfoByAccountName(userInfo.getUserCode()).getAccountId();
		boolean check = iUserService.checkUserPassword(accountId, oldPassword);
		if (check && !oldPassword.equals(newPassword)) {
			// 验证密码成功，修改密码，返回成功信息
			boolean result = iUserService.modUserPassword(accountId, newPassword, oldPassword);
			return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
		} else {
			// 验证密码失败，不修改密码，返回失败信息
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		}
	}

	/**
	 * 用户验证
	 * 
	 * @param requestBody
	 *            请求体中包括userName用户名和password密码两个参数
	 * @return 以JSON格式返回验证成功或失败信息
	 */
	@RequestMapping(value = "/check.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String checkUserCodeAndPassword(@RequestBody String requestBody) {
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("userName") || !map.containsKey("password")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		String security_url = PropertiesHelper.getProperty("security.url");
		String URL = security_url + "login/checkUserNameAndPassword.noCons";
		List<HTTPParam> params = new ArrayList<HTTPParam>();
		params.add(new HTTPParam("userName", map.get("userName").toString()));
		params.add(new HTTPParam("password", map.get("password").toString()));
		String result = "";
		try {
			result = remoteCallService.sendGet(URL, params);
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	/***
	 * 首页/搜索-app -获取区域列表 根据登录的用户权限，获取可选择的区域列表
	 * 
	 * 
	 */
	@RequestMapping(value = "/district.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appDistrict(@RequestParam(value = "token") String token) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(4);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			return ReturnInfo.genResultJson(3);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(3);
		}
		TDistrictDict dist = userInfo.getDistInfo();
		if (dist == null) {
			return ReturnInfo.genResultJson(0);
		} else {
			Map<String, Object> distMap = new HashMap<String, Object>();
			distMap.put("dist_id", dist.getDistId());
			distMap.put("dist_name", dist.getDistName());
			distMap.put("dist_code", dist.getDistCode());
			return ReturnInfo.genResultJson(1, distMap);
		}
	}

	/***
	 * 首页/搜索-获取区域列表 根据登录的用户权限，获取可选择的区域列表
	 * 
	 * 
	 */
	@RequestMapping(value = "/district.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String district() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(2);
		}
		TDistrictDict dist = userInfo.getDistInfo();
		if (dist == null) {
			return ReturnInfo.genResultJson(0);
		} else {
			Map<String, Object> distMap = new HashMap<String, Object>();
			distMap.put("dist_id", dist.getDistId());
			distMap.put("dist_name", dist.getDistName());
			distMap.put("dist_code", dist.getDistCode());
			return ReturnInfo.genResultJson(1, distMap);
		}
	}

	/**
	 * app端-个人中心-阅读记录 展示我收藏的文档记录
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回收藏的文档记录
	 */
	@RequestMapping(value = "/collected.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appCollection(@RequestParam(value = "token") String token,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		List<Map<String, Object>> docList = userService.getColeDocsByUserInfo(userInfo, page, pageSize);
		if (docList == null || docList.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docList);
		}
	}

	/**
	 * web端-个人中心-阅读记录 展示我收藏的文档记录
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回收藏的文档记录
	 */
	@RequestMapping(value = "/collected.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String collection(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		List<Map<String, Object>> docList = userService.getColeDocsByUserInfo(userInfo, page, pageSize);
		if (docList == null || docList.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docList);
		}
	}

	/**
	 * 取消收藏 取消收藏指定的文档
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param requestBody
	 *            请求体中传入参数doc_id
	 * @return 以JSON格式返回取消收藏文档成功或者失败信息
	 */
	@RequestMapping(value = "/collected/cancel.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appDisCollection(@RequestParam(value = "token") String token, @RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("doc_id") || token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		int user_id = userInfo.getUserId();
		int doc_id = (int) map.get("doc_id");
		RelUserColDocPK pk = new RelUserColDocPK(user_id, doc_id);
		RelUserColDoc col = new RelUserColDoc();
		col.setId(pk);
		boolean result = coleService.disCollectDoc(col);
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}

	/**
	 * 取消收藏 取消收藏指定的文档
	 * 
	 * @param requestBody
	 *            请求体中传入参数doc_id
	 * @return 以JSON格式返回取消收藏文档成功或者失败信息
	 */
	@RequestMapping(value = "/collected/cancel.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String disCollection(@RequestBody String requestBody) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		int user_id = userInfo.getUserId();
		int doc_id = (int) map.get("doc_id");
		RelUserColDocPK pk = new RelUserColDocPK(user_id, doc_id);
		RelUserColDoc col = new RelUserColDoc();
		col.setId(pk);
		boolean result = coleService.disCollectDoc(col);
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}

	/**
	 * app端-个人中心-阅读记录 展示我阅读过的文档记录
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回阅读过的文档记录
	 */
	@RequestMapping(value = "/read.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appRead(@RequestParam(value = "token") String token,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		List<Map<String, Object>> docList = userService.getReadDocByUserInfo(userInfo, page, pageSize);
		if (docList == null || docList.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docList);
		}
	}

	/**
	 * web端-个人中心-阅读记录 展示我阅读过的文档记录
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回阅读过的文档记录
	 */
	@RequestMapping(value = "/read.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String read(@RequestParam(value = "page") int page, @RequestParam(value = "pageSize") int pageSize) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		List<Map<String, Object>> docList = userService.getReadDocByUserInfo(userInfo, page, pageSize);
		if (docList == null || docList.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docList);
		}
	}

	/**
	 * app端-个人中心-阅读记录-app 删除阅读记录
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param requestBody
	 *            请求体中传入参数doc_id
	 * @return 以JSON格式返回删除阅读记录成功或者失败信息
	 */
	@RequestMapping(value = "/read/cancel.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appReadCancel(@RequestParam(value = "token") String token, @RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("doc_id") || token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		int user_id = userInfo.getUserId();
		int doc_id = (int) map.get("doc_id");
		RelUserReadDocPK pk = new RelUserReadDocPK(user_id, doc_id);
		RelUserReadDoc col = new RelUserReadDoc(pk);
		boolean result = readService.disReadDoc(col);
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}

	/**
	 * web端-个人中心-阅读记录 删除阅读记录
	 * 
	 * @param requestBody
	 *            请求体中传入参数doc_id
	 * @return 以JSON格式返回删除阅读记录成功或者失败信息
	 */
	@RequestMapping(value = "/read/cancel.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String readCancel(@RequestBody String requestBody) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		int doc_id = (int) map.get("doc_id");
		int user_id = userInfo.getUserId();
		RelUserReadDocPK pk = new RelUserReadDocPK(user_id, doc_id);
		RelUserReadDoc col = new RelUserReadDoc(pk);
		boolean result = readService.disReadDoc(col);
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}

	/**
	 * app端-个人中心-阅读记录 清空阅读记录
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @return 以JSON格式返回清空阅读记录成功或失败信息
	 */
	@RequestMapping(value = "/read/empty.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appReadEmpty(@RequestParam(value = "token") String token) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		boolean result = userService.deleteReadRecordsByUserInfo(userInfo);
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}

	/**
	 * web端-个人中心-阅读记录 清空阅读记录
	 * 
	 * @return 以JSON格式返回清空阅读记录成功或失败信息
	 */
	@RequestMapping(value = "/read/empty.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String readEmpty() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		boolean result = userService.deleteReadRecordsByUserInfo(userInfo);
		return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
	}
}
