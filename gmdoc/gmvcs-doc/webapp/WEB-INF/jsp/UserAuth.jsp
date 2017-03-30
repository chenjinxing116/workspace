<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/content/uilib/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/content/css/org_tree.css"/>

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
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/content/uilib/artDialog/css/ui-dialog.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/dist/dialog-min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/util/dialogUtil.js"></script>
	<script src="${pageContext.request.contextPath}/content/uilib/zTree_v3/js/jquery.ztree.all.min.js" type="text/javascript" charset="utf-8"></script>
	
	<script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/js/auth/UserAuth.js"></script>
</head>
<body>

	<!-- 头部  -->
	<jsp:include page="./header.jsp"></jsp:include>

	<!-- 内容 -->
	<div id="swrap" class="container" style="margin-top: 15px;height: 100%; margin-top: -350px;padding-top: 370px;">
		<div class="row" style="height: 100%;">
			<div class="col-md-4" style="height: 100%;">
				<div style="height: 100%;background-color: #FFFFFF;border: 1px solid #DBDBDB; overflow: auto;min-height: 500px;">
					<ul id="tree_org" class="ztree ztree-org"></ul>
				</div>
			</div>
			<div class="col-md-8" style="height: 100%; padding-left: 10px;">
				<div style="min-height: 500px; height: 100%;background-color: #FFFFFF;border: 1px solid #DBDBDB; padding-left: 0px;padding-right: 0px;">
					<div class="row" style="width:500px; margin: 50px auto;">
						<div class="form-horizontal">
						   	<div class="form-group">
						      	<label class="col-sm-4  control-label">用户名称:</label>
						        <label id="label_name" class="col-sm-6 control-label" style="text-align: left;"></label>
						    </div>
						    <div class="form-group">
						      	<label class="col-sm-4  control-label">用户编号:</label>
						        <label id="label_code" class="col-sm-6 control-label" style="text-align: left;"></label>
						    </div>
						   	<!--<div class="form-group">
						      	<label class="col-sm-4  control-label">所属部门:</label>
						        <label class="col-sm-6 control-label" style="text-align: left;"></label>
						    </div>-->
						   	<div class="form-group">
						   		<label class="col-sm-4 control-label">用户权限: </label>
						    	<div class="col-sm-6">
						         	<select id="sel_lv" class="form-control">
							         	<option value="0">公开</option>
							         	<option value="1">敏感</option>
							         	<option value="2">秘密</option>
							         	<option value="3">机密</option>
							         	<option value="4">绝密</option>
							      	</select>
						      	</div>
						    </div>
						   	<div class="form-group">
						      	<div class="col-sm-offset-4 col-sm-6">
						         	<button id="btn_submit" class="btn btn-default form-control">提交</button>
						      	</div>
						   	</div>
						</div>
					</div>
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