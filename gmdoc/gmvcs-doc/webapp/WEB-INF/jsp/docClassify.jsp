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
    <link href="${pageContext.request.contextPath}/content/uilib/artDialog/css/ui-dialog.css" rel="stylesheet">

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

    <script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/dist/dialog-min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/util/dialogUtil.js"></script>
    
    <script>
    	icon_base_path = "${pageContext.request.contextPath}/content/uilib/artDialog/";
    </script>
    
    <script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/js/docmanage/docClassify.js"></script>
</head>
<body>
	<!-- 头部  -->
	<jsp:include page="./header.jsp"></jsp:include>

	<!-- 内容 -->
	<div class="container" style="margin-top: 15px;min-height: 500px;">
		<div class="row" style="margin-bottom: 10px;">
	        <div class="col-lg-6">
	        	<div class="row">
	        		<div class="col-xs-9">
	        			<input maxlength="16" id="input_one_name" type="text" class="form-control input-lg" placeholder="请输入分类名称"/>
	    			</div>
	    			<div class="col-xs-3">
	        			<button id="btn_add_one" class="btn btn-default btn-lg"><i class="fa fa-plus"></i> 添加分类</button>
	       			</div>
	       		</div>
	        </div>
	    </div>
	
	    <div class="row">
	        <div class="col-lg-12">
	        	<ul id="ul_one" class="list-inline">
	        	<c:forEach items="${catoList}" var="map" varStatus="i">
	        		<c:if test="${0==i.index%2&&i.index!=0}">
	            		<div class="clearfix"></div>
	            	</c:if>
	        		<li class="col-lg-6 col-xs-12 li-one" style="min-height: 200px;">
						<div class="panel panel-default">
					      	<div class="panel-heading">
					         	<h4 class="panel-title">
					            	<div class="list-group-item" style="background-color: #F5F5F5;border-color: #F5F5F5;">
					            		<p class="hidden p-oneInfo">${map.cato_id}</p>
					            		<%-- <span style="font-size: 24px;width: 300px;" class="span-name"><strong>${map.cato_name}</strong></span> --%>
					            		<input readOnly="true" style="font-size: 24px;background-color:#f5f5f5;display:inline; width: 420px;color: #D33A3A;margin:-2px auto -5px -4px;border:none;" type="text" class="list-group-item-heading span-name" value="${map.cato_name}">
					                	<button class="btn btn-danger badge btn-close btn-one-close" style="background-color: #d33a3a;"><i class="fa fa-times"></i></button>
					                	<button class="btn btn-danger badge btn-check hide" style="background-color: #d33a3a;"><i class="fa fa-check"></i></button>
					                	<button class="btn btn-danger badge btn-edit" style="background-color: #d33a3a;"><i class="fa fa-pencil"></i></button>
					                	<input maxlength="16" style="width: 300px;color: #000;margin-top:-11px;margin-bottom:-9px;" type="text" class="list-group-item-heading form-control input-name hide input-lg" placeholder="请输入名称">
					            	</div>
					         	</h4>
					      	</div>
					      	<div id="collapseOne" class="panel-collapse collapse in">
					         	<div class="panel-body">
						            <ul class="list-inline ul-two-classify">
					            	<c:forEach items="${map.children}" var="child">
					            		<li class="col-lg-6">
					            			<div class="list-group-item">
					            				<p class="hidden p-twoInfo">${child.cato_id}</p>
					            				<input readOnly="true" style="display:inline; width: 170px;color: #000;margin:-5px;border:none;" type="text" class="list-group-item-heading span-name" value="${child.cato_name}">
							                	<%-- <span class="span-name" style="width: 150px;">${child.cato_name}</span> --%>
							                	<input maxlength="16" style="display:inline; width: 170px;color: #000;margin:-10px auto -10px -12px;" type="text" class="list-group-item-heading form-control input-name hide" placeholder="请输入名称">
							                	<button class="btn btn-danger badge btn-close"><i class="fa fa-times"></i></button>
							                	<button class="btn btn-danger badge btn-check hide"><i class="fa fa-check"></i></button>
							                	<button class="btn btn-danger badge btn-edit"><i class="fa fa-pencil"></i></button>
					                		</div>
					            		</li>
				            		</c:forEach>
					            	</ul>
					            	<div class="clearfix"></div>
					            	<div style="margin-top:5px;">
					            		<div class="row">
							        		<div class="col-xs-10">
							        			<input maxlength="16" type="text" class="form-control input-two" placeholder="请输入分类名称"/>
							    			</div>
							    			<div class="col-xs-2">
							        			<button class="btn btn-default btn-two"><i class="fa fa-plus"></i></button>
							       			</div>
							       		</div>
						            </div>
				            	</div>
				            </div>
			            </div>
		            </li>
				</c:forEach>
				</ul>
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