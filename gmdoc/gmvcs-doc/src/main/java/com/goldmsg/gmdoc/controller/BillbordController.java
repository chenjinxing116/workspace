package com.goldmsg.gmdoc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.service.DocService;
import com.goldmsg.gmdoc.service.GMUserService;

/**
 * 榜单controller，提供在app端和web端获取最新发布文档，收藏榜单接口，阅读榜单接口
 * 
 * @author xiangrandy
 *
 */
@Controller
@RequestMapping(value = "/billboard")
public class BillbordController {

	@Autowired
	DocService docService;

	@Autowired
	GMUserService userService;

	@Autowired
	HttpSession session;

	/**
	 * 首页-最新发布-app 显示最新发布的法律法规文档
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回最新发布的文档
	 */
	@RequestMapping(value = "/publish.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAppPublishBillboard(@RequestParam(value = "token") String token,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> docs = docService.getLatestPubDocs(userInfo, dist_id, page, pageSize);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docs);
		}
	}

	/**
	 * 首页-最新发布-web 显示最新发布的法律法规文档
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回最新发布的文档
	 */
	@RequestMapping(value = "/publish.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getWebPublishBillboard(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> docList = docService.getLatestPubDocs(userInfo, dist_id, page, pageSize);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docList);
		}
	}

	/**
	 * 首页-收藏榜单-app 显示某地区 收藏 人数最多的法律法规文档排行榜
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回最新发布的文档
	 */

	@RequestMapping(value = "/collection.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAppCollectionBillboard(@RequestParam(value = "token") String token,
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
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> body = docService.getHotColeDocs(userInfo, dist_id, page, pageSize);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, body);
		}
	}

	/**
	 * 首页-收藏榜单-web 显示某地区 收藏 人数最多的法律法规文档排行榜
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回最新发布的文档
	 */
	@RequestMapping(value = "/collection.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getWebCollectionBillboard(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> body = docService.getHotColeDocs(userInfo, dist_id, page, pageSize);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, body);
		}
	}

	/**
	 * 首页-阅读榜单-app 显示某地区 阅读 人数最多的法律法规文档排行榜
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回最新发布的文档
	 */
	@RequestMapping(value = "/read.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAppReadBillboard(@RequestParam(value = "token") String token,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		if (token == null || "".equals(token)) {
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
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> docs = docService.getHotReadDocs(userInfo, dist_id, page, pageSize);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docs);
		}
	}

	/**
	 * 首页-阅读榜单-web 显示某地区 阅读 人数最多的法律法规文档排行榜
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @return JSON格式返回最新发布的文档
	 */
	@RequestMapping(value = "/read.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getWebReadBillboard(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> docs = docService.getHotReadDocs(userInfo, dist_id, page, pageSize);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, docs);
		}
	}
}
