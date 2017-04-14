package com.goldmsg.gmomm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goldmsg.gmomm.interceptor.auth.AuthorityType;
import com.goldmsg.gmomm.interceptor.auth.FireAuthority;
import com.goldmsg.gmomm.interceptor.auth.ResultTypeEnum;
import com.goldmsg.gmomm.utils.PrivUtils;

/***
 * 系统controller，做页面导航用
 * 
 * @author QH
 *
 */
@Controller
@RequestMapping("/system")
public class SystemController {

	/**
	 * 导航页
	 * 
	 * @return 返回首页页面，如果失败，返回404页面
	 */
	@RequestMapping(value = "/index.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String index(HttpServletRequest request, Model model) {
		List<String> privList = PrivUtils.getPrivilegeListByAccountName(request.getRemoteUser());
		if (privList.isEmpty()) {
			return "login";
		}
		model.addAttribute("privList", privList);
		return "resManage/home/index";
	}

	/**
	 * 设备管理页
	 * 
	 * @return 返回首页页面，如果失败，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.DEVICEMANAGE }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/deviceManage/index.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deviceManage(HttpServletRequest request, Model model) {

		List<String> privList = PrivUtils.getPrivilegeListByAccountName(request.getRemoteUser());
		if (privList.isEmpty()) {
			return "login";
		}
		model.addAttribute("privList", privList);
		return "resManage/deviceManage/index";
	}

	/**
	 * 用户管理页
	 * 
	 * @return 返回首页页面，如果失败，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.USERMANAGE }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/userManage/index.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String userConfig(HttpServletRequest request, Model model) {

		List<String> privList = PrivUtils.getPrivilegeListByAccountName(request.getRemoteUser());
		if (privList.isEmpty()) {
			return "login";
		}
		model.addAttribute("privList", privList);
		return "resManage/userManage/index";
	}

	/**
	 * 存储管理页
	 * 
	 * @return 返回首页页面，如果失败，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.STORAGEMANAGE }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping(value = "/storageManage/index.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String storageManage(HttpServletRequest request, Model model) {

		List<String> privList = PrivUtils.getPrivilegeListByAccountName(request.getRemoteUser());
		if (privList.isEmpty()) {
			return "login";
		}
		model.addAttribute("privList", privList);
		return "resManage/storageManage/index";
	}
}
