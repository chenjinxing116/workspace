<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.goldmsg.gmdoc.entity.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,Chrome=1">

    <title>消防文库</title>

    <link href="${pageContext.request.contextPath}/content/css/bootstrap.min.css" rel="stylesheet" >
    <link href="${pageContext.request.contextPath}/content/css/font-awesome.min.css" rel="stylesheet" >
    <link href="${pageContext.request.contextPath}/content/css/sy.css" rel="stylesheet" >
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/content/reader/css/flexpaper.css" />

    <!--[if lt IE 9]>
    	<script src="${pageContext.request.contextPath}/content/js/html5shiv.min.js"></script>
    	<script src="${pageContext.request.contextPath}/content/js/respond.min.js"></script>
    <![endif]-->

    <!-- 引入jquery -->
    <script src="${pageContext.request.contextPath}/content/js/jquery-1.12.1.min.js"></script>

    <!-- 引入bootstrap脚本 -->
    <script src="${pageContext.request.contextPath}/content/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/content/js/jquery.qrcode.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/content/js/qiao.js" type="text/javascript" charset="utf-8"></script>

    <!-- IE8 实现输入框未输入时显示提示语句 -->
    <script src="${pageContext.request.contextPath}/content/js/jquery.placeholder.js"></script>
	
	<script type="text/javascript">
		var fileUrl = "${url}";
		var basepath= "${pageContext.request.contextPath}/";
		var docInfo = {};
		docInfo.doc_id = ${docInfo.doc_id};
		docInfo.is_collected = ${docInfo.is_collected};
	</script>

	<script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/reader/js/flexpaper.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/reader/js/flexpaper_handlers.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/js/docmanage/docReader.js"></script>
</head>
<body>

	<!-- 头部  -->
	<jsp:include page="./header.jsp"></jsp:include>

	<!-- 内容 -->
	<div class="container" style="margin-top: 15px;min-height: 500px;">
	    <div class="row">
	        <div class="col-xs-12">
	            <ol class="breadcrumb" style="width:400px;">
	                <li><a href="manage.action">文档管理</a></li>
	                <li class="active">文档阅读</li>
	            </ol>
	        </div>
	
	        <div class="col-xs-10 col-xs-offset-1">
	            <h3>
	            <c:choose>
	            	<c:when test="${docInfo.doc_type=='doc'|| docInfo.doc_type=='docx'}">
	            		<i style="color:#0068b7;" class="fa fa-file-word-o"></i>
	            	</c:when>
	            	<c:when test="${docInfo.doc_type=='xls'|| docInfo.doc_type=='xlsx'}">
	            		<i style="color:#22ac38;" class="fa fa-file-excel-o"></i>
	            	</c:when>
	            	<c:when test="${docInfo.doc_type=='pdf'}">
	            		<i style="color:#cd0a0a;" class="fa fa-file-pdf-o"></i>
	            	</c:when>
	            	<c:when test="${docInfo.doc_type=='ppt'|| docInfo.doc_type=='pptx'}">
	            		<i style="color:#eb5c06;" class="fa fa-file-powerpoint-o"></i>
	            	</c:when>
	            	<c:when test="${docInfo.doc_type=='txt'}">
	            		<i class="fa fa-file-text-o"></i>
	            	</c:when>
	            	<c:otherwise>
	            		<i class="fa fa-file"></i>
	            	</c:otherwise>
	            </c:choose>
	             ${docInfo.doc_title}</h2>
	            <div style="margin: 5px auto 10px;">
	                <p style="display: inline;">发布时间: ${docInfo.pub_dateline}    格式: ${docInfo.doc_type}</p>
	                <a class="a-collect" style="margin-right: 2px; margin-left: 5px;cursor: pointer;">
	                <c:if test="${docInfo.is_collected}">
	                	<i class="fa fa-star"></i>
	                </c:if>
	                <c:if test="${!docInfo.is_collected}">
	                	<i class="fa fa-star-o"></i>
	                </c:if>
	                </i>收藏</a>
	                <a href="../doc/download.action?doc_id=${docInfo.doc_id}" class="btn btn-success btn-sm pull-right" style="margin-top: -5px;"><i class="fa fa-download"></i>下载</a>
	            </div>
	        </div>
	        <div class="col-xs-10 col-xs-offset-1">
	            <div id="documentViewer" class="flexpaper_viewer" style="width:100%px;height:800px;margin:0px auto;"></div>
	        </div>
	    </div>
	</div>
	
	<div class="footer container-fluid" style="margin-top: 20px;padding-top:20px; padding-bottom: 20px; border-top: 5px solid #d33a3a;">
	    <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">版权所有© 2016中华人民共和国公安局交通管理局</label><br/>
	    <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">著作权号: 中华人民共和国国家版权局2011SR012287</label><br/>
	    <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">技术支持: 广州市国迈科技有限公司 支持电话: 020-2839 8008</label><br/>
	    <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">软件版本号: V2.2.0 发布日期: 2016-01-01</label><br/>
	</div>

</body>
</html>