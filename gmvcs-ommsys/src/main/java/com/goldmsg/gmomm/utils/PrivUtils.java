package com.goldmsg.gmomm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.goldmsg.gmomm.system.ApplicationProperties;
import com.gosun.service.entity.UserRsp;
import com.gosun.service.privilege.IPrivilegeService;
import com.gosun.service.user.IUserService;

/***
 * 权限信息相关查询工具类
 * 
 * @author QH Email: qhs_dream@163.com 2016年10月8日 : 下午3:35:13
 */
@Repository
public class PrivUtils {

	public static ApplicationProperties props;

	public static IUserService userService;

	public static IPrivilegeService privilegeService;

	public static List<String> getPrivilegeListByAccountName(String accountName) {
		UserRsp userRsp = userService.getUserInfoByAccountName(accountName);
		if (userRsp == null) {
			// 用户不存在，返回空的权限信息列表
			return new ArrayList<>(0);
		}
		Map<String, String> privMap = privilegeService.getPrivilegesByAccountId(userRsp.getAccountId());
		String privStr = privMap.get(props.getProjectCode());
		if (privStr == null || "".equals(privStr)) {
			// 没有找到对应权限信息，返回空的權限信息列表
			return new ArrayList<>(0);
		}
		String[] priArr = privStr.split(",");
		return Arrays.asList(priArr);
	}

	public static ApplicationProperties getProps() {
		return props;
	}

	public static void setProps(ApplicationProperties props) {
		PrivUtils.props = props;
	}

	public static IUserService getUserService() {
		return userService;
	}

	public static void setUserService(IUserService userService) {
		PrivUtils.userService = userService;
	}

	public static IPrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	public static void setPrivilegeService(IPrivilegeService privilegeService) {
		PrivUtils.privilegeService = privilegeService;
	}
}
