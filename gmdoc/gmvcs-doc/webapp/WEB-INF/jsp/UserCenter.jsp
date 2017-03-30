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

    <!--[if lt IE 9]>
    	<script src="${pageContext.request.contextPath}/content/js/html5shiv.min.js"></script>
    	<script src="${pageContext.request.contextPath}/content/js/respond.min.js"></script>
    <![endif]-->

    <script src="${pageContext.request.contextPath}/content/js/jquery-1.12.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/content/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/content/js/jquery.placeholder.js"></script>
    
	<script src="${pageContext.request.contextPath}/content/js/jquery.qrcode.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/content/js/qiao.js" type="text/javascript" charset="utf-8"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/js/security.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/js/md5.js"></script>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/content/uilib/artDialog/css/ui-dialog.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/dist/dialog-min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/util/dialogUtil.js"></script>
	
	<script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/js/usercenter/usercenter.js"></script>
	
	<style>
		
	</style>
</head>
<body>

	<!-- 头部  -->
	<jsp:include page="./header.jsp"></jsp:include>

	<!-- 内容 -->
	<div class="container" style="margin-top: 15px;min-height: 500px;">
	    <div class="row">
	        <div class="col-xs-12">
	            <div style="margin-right:-15px; border: 1px solid #e1e1e1; background-color:#FFF;">
	                <div class="row" style="padding: 25px;">
	                    <div class="col-xs-1" style="min-width: 150px;">
	                        <!--<img class="" src="${pageContext.request.contextPath}/content/images/userbg.png" width="150" height="150"></img>-->
	                        <c:if test="${userGender==1}">
	                        	<img class="" src="${pageContext.request.contextPath}/content/images/man.png" width="150" height="150"></img>
	                        </c:if>
	                        <c:if test="${userGender==2}">
	                        	<img class="" src="${pageContext.request.contextPath}/content/images/woman.png" width="150" height="150"></img>
	                        </c:if>
	                    </div>
	                    <div class="col-xs-4" style="margin-top: 45px; color: #d33a3a;">
	                        <h2 style="margin: 0px;"><strong>${userName}</strong></h2>
	                        <p>所属部门: ${orgName}</p>
	                    </div>
	                    <div class="col-xs-4 pull-right" style="margin-top: 55px; min-width: 150px;">
	                        <div class="pull-right" style="margin-right: 50px;">
	                            <button id="btn-modifyPwd" class="btn btn-danger" style="width: 150px; margin-bottom: 5px; display: block;">修改密码</button>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	
	    <div class="row" style="margin-top: 5px;">
	        <div class="col-md-6">
	            <div style="border-bottom: 2px solid #DDDDDD; margin-bottom: -2px;">
	                <a class="a-title">我的收藏&nbsp;&nbsp;&nbsp;&nbsp;</a>
	                <a class="pull-right btn btn-link disabled" style="color: #949494;" id="next-page">下一页</a>
	                <a class="pull-right btn btn-link disabled" style="color: #949494;" id="pre-page">上一页</a>
	            </div>
	            <div class="table-responsive" style="margin-top: 2px;clear: both;">
	                <table class="table table-striped table-hover">
	                    <thead>
	                        <th style="color: #949494;">文档名称</th>
	                        <th class="text-center" style="color: #949494;">收藏时间</th>
	                        <th class="text-center" style="color: #949494;">操作</th>
	                    </thead>
	                    <tbody id="tbody-collect">  
	                    </tbody>
	                </table>
	            </div>
	            <!--<div>
	                <ul class="pager">
	                    <li class="li-pre disabled"><a href="javascript:void(0);" id="pre-page">上一页</a></li>
	                    <li class="li-next disabled"><a href="javascript:void(0);" id="next-page">下一页</a></li>
	                </ul>
	            </div>-->
	        </div>
	
	        <div class="col-md-6">
	            <div style="border-bottom: 2px solid #DDDDDD; margin-bottom: -2px;">
	                <a class="a-title">阅读记录&nbsp;&nbsp;&nbsp;&nbsp;</a>
	                <a id="empty-read" class="pull-right btn btn-link" style="color: #949494;">清空</a>
	                <a class="pull-right btn btn-link disabled" style="color: #949494;" id="read-next-page">下一页</a>
	                <a class="pull-right btn btn-link disabled" style="color: #949494;" id="read-pre-page">上一页</a>
	            </div>
	            <div class="table-responsive" style="margin-top: 2px; clear: both;">
	                <table class="table table-striped table-hover">
	                    <thead>
		                    <th style="color: #949494;">文档名称</th>
		                    <th class="text-center" style="color: #949494;">阅读时间</th>
		                    <th class="text-center" style="color: #949494;">操作</th>
	                    </thead>
	                    <tbody id="tbody-read">
	                    </tbody>
	                </table>
	            </div>
	        </div>
	    </div>
	</div>
	
	<!-- 修改密码弹窗 -->
	<div class="modal fade" id="changePWDModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close"
	                        data-dismiss="modal" aria-hidden="true">
	                    &times;
	                </button>
	                <h4 class="modal-title" id="myModalLabel">
	                    修改密码
	                </h4>
	            </div>
	            <div class="modal-body">
	                <form role="form" class="form-horizontal">
	                    <div class="form-group">
	                        <label for="old_pwd" class="col-sm-2 control-label">旧密码:</label>
	                        <div class="col-sm-10">
	                        	<input type="password" class="form-control" id="old_pwd" placeholder="请输入旧密码">
	                       	</div>
	                    </div>
	                    <div class="form-group">
	                        <label for="new_pwd1" class="col-sm-2 control-label">新密码:</label>
	                        <div class="col-sm-10">
	                        	<input type="password" class="form-control" id="new_pwd1" placeholder="请输入新密码">
	                       	</div>
	                    </div>
	                    <div class="form-group">
	                        <label for="new_pwd2" class="col-sm-2 control-label">新密码:</label>
	                        <div class="col-sm-10">
	                        	<input type="password" class="form-control" id="new_pwd2" placeholder="请再次输入新密码">
	                       	</div>
	                    </div>
	                </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-primary" onclick="onChangePwdClick()">修改密码</button>
	                <button type="button" class="btn btn-default" data-dismiss="modal">取  消</button>
	            </div>
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