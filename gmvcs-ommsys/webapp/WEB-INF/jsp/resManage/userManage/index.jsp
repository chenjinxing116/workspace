<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="renderer" content="webkit">
	    <meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
	    <meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=EDGE;chrome=1" />
	    <meta http-equiv="Pragma" content="no-cache"> 
	    <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no,width=device-width,height=device-height">
		<title>用户管理</title>
		
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/content/resManage/userManage/userNav.css"/>
	</head>
	<body>
		<div class="left-nav">
			<ul>
				<c:if test="${privList.contains('omm0301')}">
					<li data-for="#iframeYHPZ">
						<a href="javascript:void(0);">用户配置</a>
					</li>
				</c:if>
				<c:if test="${privList.contains('omm0302')}">
					<li data-for="#iframeYWGWZD">
						<a href="javascript:void(0);">业务岗位字典</a>
					</li>
				</c:if>
			</ul>
		</div>
		<div class="wrap">
		</div>
		
		<script src="/front-resource/jquery/jquery-1.12.1.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath}/content/resManage/userManage/userNav.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>