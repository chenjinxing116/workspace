package com.goldmsg.gmdoc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.goldmsg.core.utils.JsonUtil;
import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.interceptor.auth.AuthorityType;
import com.goldmsg.gmdoc.interceptor.auth.FireAuthority;
import com.goldmsg.gmdoc.interceptor.auth.ResultTypeEnum;
import com.goldmsg.gmdoc.service.CatoService;
import com.goldmsg.gmdoc.service.GMUserService;

import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * 分类管理controller，提供在web端对文档分类进行增删改查的功能
 * 
 * @author xiangrandy
 *
 */
@Controller
@RequestMapping(value = "/cato")
public class CatoController {

	@Autowired
	private CatoService catoService;

	@Autowired
	private GMUserService userService;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	/**
	 * 获取分类管理界面方法
	 * 
	 * @return 如果用户存在且有权限，返回分类管理界面；如果用户不存在，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.CATOMANAGE }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/manage.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String manage() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		}
		TDistrictDict distInfo = userInfo.getDistInfo();
		int dist_id = distInfo.getDistId();
		List<Map<String, Object>> catoInfoList = catoService.getCatoInfoList(dist_id);
		request.setAttribute("distInfo", distInfo);
		request.setAttribute("catoList", catoInfoList);
		return "docClassify";
	}

	/**
	 * 添加分类方法，可新增分类
	 * 
	 * @param requestBody
	 *            请求体中以JSON格式传入cato_name和parent_id两个参数
	 * @return 以JSON格式返回添加成功或者失败信息
	 */
	@RequestMapping(value = "/add.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String addCato(@RequestBody String requestBody) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			int dist_id = distInfo.getDistId();
			Map<String, Object> map = JsonUtil.parseMap(requestBody);
			String cato_name = map.get("cato_name").toString();
			int parent_id = (int) map.get("parent_id");
			String cato_code = catoService.generateCatoCodeByParentId(parent_id);
			if (cato_code == null || cato_code.equals("")) {
				return ReturnInfo.genResultJson(ReturnInfo.FAILED);
			}
			TCatogory cato = new TCatogory();
			cato.setCatoCode(cato_code);
			cato.setCatoName(cato_name);
			cato.setParentId(parent_id);
			cato.setCatoStatus("1");
			TCatogory retCato = catoService.saveCatoAndRelInfo(cato, dist_id);
			if (retCato == null) {
				return ReturnInfo.genResultJson(ReturnInfo.FAILED);
			} else {
				return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, retCato);
			}
		}
	}

	/**
	 * 编辑分类方法,可修改分类名称
	 * 
	 * @param requestBody
	 *            请求体中以JSON格式传入cato_id和cato_name两个参数
	 * @return 以JSON格式返回编辑成功或者失败信息
	 */
	@RequestMapping(value = "/edit.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String saveCato(@RequestBody String requestBody) {
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		int cato_id = (int) map.get("cato_id");
		String cato_name = map.get("cato_name").toString();
		TCatogory cato = new TCatogory();
		cato.setCatoId(cato_id);
		cato.setCatoName(cato_name);
		TCatogory result = catoService.saveCatoName(cato);
		if (result == null) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("cato_id", result.getCatoId());
			retMap.put("cato_code", result.getCatoCode());
			retMap.put("cato_name", result.getCatoName());
			retMap.put("parent_id", result.getParentId());
			retMap.put("cato_status", result.getCatoStatus());
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, retMap);
		}
	}

	/**
	 * 删除分类方法，可删除分类，如果为二级分类，则需要该分类下没有文档，否则删除失败；如果为一级分类，则需要该分类下无二级分类，否则删除失败。
	 * 
	 * @param requestBody
	 *            请求体中以JSON格式传入cato_id
	 * @return 以JSON格式返回删除成功或者失败信息
	 */
	@RequestMapping(value = "/delete.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteCato(@RequestBody String requestBody) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			Map<String, Object> map = JsonUtil.parseMap(requestBody);
			int cato_id = (int) map.get("cato_id");
			TDistrictDict distInfo = userInfo.getDistInfo();
			int dist_id = distInfo.getDistId();
			boolean result = catoService.deleteCato(cato_id, dist_id);
			return ReturnInfo.genResultJson(result ? ReturnInfo.SUCCESS : ReturnInfo.FAILED);
		}
	}

	/**
	 * app端，根据用户信息，获取分类列表方法
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @return 以JSON格式返回分类信息列表
	 */
	@RequestMapping(value = "myCatoList.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getMyAppCatoList(@RequestParam(value = "token") String token) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> catoInfoList = catoService.getCatoInfoList(dist_id);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, catoInfoList);
		}
	}

	/**
	 * web端，根据用户信息，获取分类列表方法
	 * 
	 * @return 以JSON格式返回分类信息列表
	 */
	@RequestMapping(value = "myCatoList.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getMyCatoList() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOSESSION);
		} else {
			int dist_id = userInfo.getDistInfo().getDistId();
			List<Map<String, Object>> catoInfoList = catoService.getCatoInfoList(dist_id);
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, catoInfoList);
		}
	}
}
