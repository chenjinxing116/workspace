package com.goldmsg.gmomm.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gosun.service.entity.OrgRsp;
import com.gosun.service.entity.UserRsp;
import com.gosun.service.org.IOrgService;
import com.gosun.service.user.IUserService;

/***
 * 当前用户管理相关工具类
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年11月8日 : 下午4:14:53
 */
@Repository
public class UserSessions {

	public static IUserService userService;
	
	public static IOrgService iOrgService;
	
	/***
	 * 获取当前已经登录的用户信息
	 * @return 用户信息
	 */
	public static UserInfo getCurrentUser() {
		ServletRequestAttributes webrequest = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = webrequest.getRequest();
		String accountName = request.getRemoteUser();
		
		if (accountName == null) {
			return null;
		}
		
		UserRsp user = userService.getUserInfoByAccountName(accountName);
		OrgRsp org = iOrgService.getOrgInfoByOrgId(user.getOrgId());
		
		UserInfo results = new UserInfo();
		results.setUser(user);
		results.setOrg(org);
		
		return results;
	}
	
	/***
	 * 用户信息类
	 * @author QH
	 *
	 */
	public static class UserInfo {
		private UserRsp user;
		private OrgRsp org;
		
		public UserRsp getUser() {
			return user;
		}
		public void setUser(UserRsp user) {
			this.user = user;
		}
		public OrgRsp getOrg() {
			return org;
		}
		public void setOrg(OrgRsp org) {
			this.org = org;
		}
	}

	public static IUserService getUserService() {
		return userService;
	}

	public static void setUserService(IUserService userService) {
		UserSessions.userService = userService;
	}

	public static IOrgService getiOrgService() {
		return iOrgService;
	}

	public static void setiOrgService(IOrgService iOrgService) {
		UserSessions.iOrgService = iOrgService;
	}

}
