package com.goldmsg.gmomm.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.goldmsg.gmomm.interceptor.auth.AuthorityType;
import com.goldmsg.gmomm.interceptor.auth.FireAuthority;
import com.goldmsg.gmomm.utils.PrivUtils;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.gosun.service.privilege.IPrivilegeService;
import com.gosun.service.user.IUserService;

/**
 * 权限验证拦截器
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年5月3日 上午9:09:18
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	IPrivilegeService iPrivilegeService;

	@Autowired
	IUserService iUserService;

	/**
	 * 权限拦截方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		FireAuthority fireAuthority = ((HandlerMethod) handler).getMethodAnnotation(FireAuthority.class);
		if (fireAuthority == null) {
			// 没有声明权限,放行
			return true;
		}
		List<String> privList = PrivUtils.getPrivilegeListByAccountName(request.getRemoteUser());
		for (AuthorityType authType : fireAuthority.authorityTypes()) {
			if (privList.contains(authType.getCode())) {
				// 有声明权限，有访问权限，放行
				return true;
			}
		}
		// 有声明权限，没有访问权限，不放行
		switch (fireAuthority.resultType()) {
		case PAGE:
			// PAGE类型的请求拦截提示
			try {
				response.sendRedirect("/gmvcshomepage/system/index.action");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case JSON:
			// ajax类型的请求拦截提示
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			try (OutputStream out = response.getOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
					PrintWriter pw = new PrintWriter(writer);) {
				pw.println(ReturnInfo.genResultJson(ReturnInfo.NOPRIVILEGE));
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		return false;
	}
}
