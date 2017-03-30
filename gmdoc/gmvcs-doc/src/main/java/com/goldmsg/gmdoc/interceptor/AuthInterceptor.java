package com.goldmsg.gmdoc.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.interceptor.auth.AuthorityType;
import com.goldmsg.gmdoc.interceptor.auth.FireAuthority;
import com.goldmsg.gmdoc.interceptor.auth.ResultTypeEnum;
import com.gosun.service.privilege.IPrivilegeService;

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
	HttpSession session;

	/**
	 * 权限拦截方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		FireAuthority fireAuthority = ((HandlerMethod) handler).getMethodAnnotation(FireAuthority.class);
		if (fireAuthority == null) {
			// 没有声明权限,放行
			return true;
		} else {
			@SuppressWarnings("unchecked")
			List<String> privilegeList = (List<String>) session.getAttribute("privileges");
			boolean aflag = false;
			for (AuthorityType at : fireAuthority.authorityTypes()) {
				if (privilegeList.contains(at.getCode())) {
					aflag = true;
					break;
				}
			}
			if (!aflag) {
				if (fireAuthority.resultType() == ResultTypeEnum.PAGE) {
					// PAGE类型的请求拦截提示
					try {
						response.sendRedirect("/gmdoc/404");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (fireAuthority.resultType() == ResultTypeEnum.JSON) {
					// ajax类型的请求拦截提示
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/html;charset=UTF-8");
					try (OutputStream out = response.getOutputStream();
							OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");) {
						PrintWriter pw = new PrintWriter(writer);
						String returnInfo = ReturnInfo.genResultJson(ReturnInfo.NOPRIVILEGE);
						pw.println(returnInfo);
						pw.flush();
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return false;
			} else {
				return true;
			}
		}
	}
}
