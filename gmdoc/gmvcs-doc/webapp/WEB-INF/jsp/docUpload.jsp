<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.goldmsg.gmdoc.entity.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" charset="utf-8" >
    <meta name="renderer" content="webkit" >
    <meta name="viewport" content="width=device-width,initial-scale=1.0" >
    <meta http-equiv="x-ua-compatible" content="IE=Edge,Chrome=1" >

    <title>消防文库</title>

    <link href="${pageContext.request.contextPath}/content/css/bootstrap.min.css" rel="stylesheet" >
    <link href="${pageContext.request.contextPath}/content/css/font-awesome.min.css" rel="stylesheet" >
	<link href="${pageContext.request.contextPath}/content/uilib/webupload/webuploader.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/content/css/sy.css" rel="stylesheet" >
    <link href="${pageContext.request.contextPath}/content/css/maneger.css" rel="stylesheet" >

    <script type="text/javascript" src="${pageContext.request.contextPath}/content/js/jquery-1.12.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/content/js/bootstrap.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/webupload/webuploader.min.js"></script>
	<script src="${pageContext.request.contextPath}/content/js/jquery.placeholder.js"></script>
	<script src="${pageContext.request.contextPath}/content/js/jquery.qrcode.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/content/js/qiao.js" type="text/javascript" charset="utf-8"></script>

    <!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
    <!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
    <!--[if lt IE 9]>
    	<script src="${pageContext.request.contextPath}/content/js/html5shiv.min.js"></script>
    	<script src="${pageContext.request.contextPath}/content/js/respond.min.js"></script>
    <![endif]-->
    
	<script>
    	var contextPath = '${pageContext.request.contextPath}';
		var basePath    = "${pageContext.request.contextPath}/";
		var seesionID   = "${pageContext.session.id}";
		var state 		= 'pending';
		var uploader;
	</script>
		
	<script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/content/js/docmanage/docupfile_web.js" type="text/javascript" charset="utf-8"></script>
</head>

<body>
    <!-- 头部  -->
	<jsp:include page="./header.jsp"></jsp:include>
    
    <!-- 内容 -->
    <div class="container" style="margin-top: 15px;">
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb" style="width:400px;">
                    <li><a href="manage.action">文档管理</a></li>
                    <li><a href="upload.action" class="active">文档上传</a></li>
                </ol>
            </div>
            <div class="col-xs-12">
                <h2 style="color:#1aa79b;margin-top: 10px;">上传文档<small> 完成两步, 即可上传文档</small></h2>
            </div>

            <div class="col-xs-12" style="border:1px #cdcdcd solid;min-height: 500px;margin-left: 15px; margin-right: 15px;background-color: #fff;">
                <!--第一步-->
                <div class="container" id="onePage">
                    <div class="row">
                        <div class="col-xs-12 text-center upload-steps">
                            <div class="step-num" style="margin: 10px auto 0;">1</div>
                            <span class="steptip">选择文档</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6 col-lg-offset-3 text-center col-sm-8 col-sm-offset-2" style="border: 1px dashed #a1a1a1;background-color: #fafafa;height: 200px;padding-top: 50px;">
                            <form enctype="multipart/form-data;charset=utf-8">
                                <div id="queue" style="display: none;"></div>
                                <!--<input id="file_upload" name="file_upload" type="file" multiple="true">-->
                                <div id="picker"><i class="fa fa-sign-out"></i>选择文件</div>	
                            </form>
                            <p style="margin-top: 5px; font-size: 18px;">支持的文件格式: 
                            	<i style="color:#0068b7;" class="fa fa-file-word-o"></i>doc   
                            	<i style="color:#22ac38;" class="fa fa-file-excel-o"></i>xls   
                            	<i style="color:#cd0a0a;" class="fa fa-file-pdf-o"></i></i>pdf   
                            	<i style="color:#eb5c06;" class="fa fa-file-powerpoint-o"></i>ppt   
                            	<i class="fa fa-file-text-o"></i>txt
                            </p>
                        </div>
                    </div>
                </div>

                <!--第二步-->
                <div class="container pagehide" id="twoPage">
                    <div class="row">
                        <div class="col-xs-12 text-center upload-steps">
                            <div class="step-num" style="margin: 10px auto 0;">2</div>
                            <span class="steptip">补充信息</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12" style="margin-left:-16px;border: 1px #cdcdcd solid;background-color: #fafafa; height: 90px;">
                            <p id="tip-updating" style="float: left;margin-top: 30px;">
                                <span id="span_upding" style="font-size: 20px;"><strong>正在上传文档, 请稍后...</strong><br></span>
                            </p>
                            <p id="tip-upsucess" style="float: left;margin-top: 20px;" class="pagehide">
                                <span style="font-size: 20px;"><strong>请补充文档信息, 完成上传</strong><br></span>
                                <span style="font-size: 15px;">审核通过后, 文档将显示在文档平台中</span>
                            </p>
                            <button id="btn_UpfileInfo" class="btn btn-info btn-lg" style="float: right; margin-top: 22px;"><i class="fa fa-sign-out"></i>确认上传</button>
                        </div>
                    </div>

                    <div class="row" style="padding-top: 30px;padding-bottom: 30px;">
                        <div class="col-md-8 col-md-offset-2">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label for="doc_title" class="col-sm-2 control-label">* 标题</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="doc_title" placeholder="请输入文档标题">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="doc_mark" class="col-sm-2 control-label">  标签</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="doc_mark" placeholder="多个标签用逗号隔开">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="doc_select" class="col-sm-2 control-label">* 分类</label>
                                    <div class="col-sm-5">
                                        <select class="form-control" id="mainselect" name="mainselect"></select>
                                    </div>
                                    <div class="col-sm-5">
                                        <select class="form-control" id="midselect" name="midselect"></select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="doc_secrict" class="col-sm-2 control-label">保密等级</label>
                                    <div class="col-sm-5">
                                        <select class="form-control" id="doc_secrict" name="mainselect">
                                        	<option value="0" >公开</option>
                                        	<option value="1" >敏感</option>
                                        	<option value="2" >秘密</option>
                                        	<option value="3" >机密</option>
                                        	<option value="4" >绝密</option>                                        	
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!--第三步-->
                <div class="container pagehide" id="threePage">
                    <div class="row">
                        <div class="col-xs-12 text-center upload-steps">
                            <div class="step-num" style="margin: 10px auto 0;">3</div>
                            <span class="steptip">完成上传</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 text-center" style="margin-left:-16px;border: 1px #cdcdcd solid;background-color: #fafafa; padding: 30px;">
                            <h3 style="font-size: 22px; margin-top: 0px;"><i class="fa fa-check-circle fa-lg"></i><strong> 文档上传成功~^_^~</strong><br></h3>
                            <h3 style="font-size: 15px;">审核通过后, 文档将显示在文档平台中</h3>
                            <a href="upload.action" id="btn_goUpfile" class="btn btn-info btn-lg"><i class="fa fa-sign-out"></i>继续上传</a>
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