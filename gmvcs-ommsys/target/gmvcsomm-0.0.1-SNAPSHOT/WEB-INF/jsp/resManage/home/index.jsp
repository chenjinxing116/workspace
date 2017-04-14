<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=EDGE;chrome=1" />
		<meta http-equiv="Pragma" content="no-cache">
		<meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no,width=device-width,height=device-height">
		<title>一体化指挥平台</title>

		<link rel="stylesheet" href="/front-resource/bootstrap-3.3.5/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/front-resource/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/content/resManage/home/index.css" />
	</head>

	<body>
		<!-- 顶部导航 -->
		<div class="header">
			<div class="container-fluid">
				<a class="ma-brand text-center" href="#">
					<img src="${pageContext.request.contextPath}/content/resManage/home/main_title.png" style="vertical-align: top;" />
				</a>
				<ul class="ma-nav" id="nav">
					<!--<li class="ma-active" data-url="/gmvcsomm/system/deviceManage/index.action">
						<a><i class="fa fa-video-camera" aria-hidden="true"></i> 设备管理</a>
					</li>

					<li data-url="/gmvcsomm/system/storageManage/index.action">
						<a><i class="fa fa-folder-open" aria-hidden="true"></i> 存储管理</a>
					</li>
					<li data-url="/gmvcsomm/system/taskDispatch/index.action">
						<a><i class="fa fa-calculator" aria-hidden="true"></i> 任务调度</a>
					</li>
					<li data-url="/gmvcsomm/system/troubleManage/index.action">
						<a><i class="fa fa-file" aria-hidden="true"></i> 报障管理</a>
					</li>
					<li data-url="/gmvcsomm/system/manageArea/index.action">
						<a><i class="fa fa-file" aria-hidden="true"></i> 管理范围</a>
					</li>
					<li data-url="/gmvcsomm/system/userManage/index.action">
						<a><i class="fa fa-users" aria-hidden="true"></i> 用户管理</a>
					</li>-->

					<c:if test="${privList.contains('omm0100')}">
						<li class="ma-active" data-url="/gmvcsomm/system/deviceManage/index.action">
							<a><i class="fa fa-video-camera" aria-hidden="true"></i> 设备管理</a>
						</li>
					</c:if>
					<c:if test="${privList.contains('omm0200')}">
						<li data-url="/gmvcsomm/system/storageManage/index.action">
							<a><i class="fa fa-folder-open" aria-hidden="true"></i> 存储管理</a>
						</li>
					</c:if>
					<c:if test="${privList.contains('omm0300')}">
						<li data-url="/gmvcsomm/system/userManage/index.action">
							<a><i class="fa fa-users" aria-hidden="true"></i> 用户管理</a>
						</li>
					</c:if>
				</ul>
				
				<!-- 右侧导航工具 -->
				<ul class="ma-nav ma-nav-clear" style="float: right;">
					<li class="">
						<a href="/gmvcshomepage/system/logout.action" title="退出" style="font-size: 20px;">
							<i class="fa fa-sign-out"></i>
						</a>
					</li>
				</ul>
			</div>
		</div>

		<!-- 内容 -->
		<div class="content">
		</div>

		<script src="/front-resource/jquery/jquery-1.12.1.min.js"></script>
		<script src="/front-resource/bootstrap-3.3.5/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/content/resManage/home/index.js" type="text/javascript" charset="utf-8"></script>
	</body>

</html>