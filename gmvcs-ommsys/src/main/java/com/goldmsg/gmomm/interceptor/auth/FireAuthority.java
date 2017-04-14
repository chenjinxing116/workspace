package com.goldmsg.gmomm.interceptor.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义权限验证注解
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年5月3日 上午9:07:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FireAuthority {

	AuthorityType[] authorityTypes();

	ResultTypeEnum resultType() default ResultTypeEnum.PAGE;
}
