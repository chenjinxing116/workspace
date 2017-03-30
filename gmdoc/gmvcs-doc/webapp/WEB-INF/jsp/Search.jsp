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
	
	<script>
		var basePath  = "${pageContext.request.contextPath}/";
		var keyword   = "${keyword}";
		var doc_type  = "${doc_type}";
		var cato_id   = "${catoInfo.getCatoId()}";
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/dist/dialog-min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/util/dialogUtil.js"></script>
    
    <script>
    	icon_base_path = "${pageContext.request.contextPath}/content/uilib/artDialog/";
    </script>

	<script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/content/js/search/search.js"></script>
</head>
<body>

	<!-- 头部  -->
	<jsp:include page="./header.jsp"></jsp:include>

	<!-- 内容 -->
	<div class="container" style="margin-top: 15px;min-height: 500px;">
	    <div class="row">
	        <div class="col-xs-12" style="border: 1px solid #949494; height: 40px; padding-top:8px;">
	            <h4 style="display:inline;">搜索结果:</h4>
			
	            <ol class="breadcrumb" style="display:inline;width:400px;">
	            <c:if test="${pCatoName!=null}">
	                <li class="active">${pCatoName}</li>
	                <li class="active">${catoInfo.getCatoName()}</li>
	            </c:if>
	            <c:if test="${pCatoName==null}">
	                <li class="active">所有分类</li>
	            </c:if>
	            </ol>
	            
	            <a data-toggle="collapse" href="#collapseOne" style="float:right;">更多分类</a>
	        </div>
	    </div>
	
	    <div class="container ci-menu panel-collapse collapse list-classify" id="collapseOne" style="margin-left: -15px;">
	    	<a href="../search/s.action?${not empty doc_type?'&doc_type=':''}${not empty doc_type?doc_type:''}${not empty keyword?'&keyword=':''}${not empty keyword?keyword:''}" class="${pCatoName==null?'classifyactive':''}"><i class="fa fa-square"></i><strong><c:out value=" 所有分类"></c:out></strong></a>
	        <div class="row" id="classfield" style="margin-top: 5px;">  
	        <c:forEach items="${catoList}" var="map" varStatus="i">
	        	<c:if test="${0==i.index%4}">
	        		<div class="clearfix"></div>
	        	</c:if>
	        	
	        	<div class="col-lg-3 col-md-4 col-sm-6">
	                <p><i class="fa fa-square"></i><strong><c:out value=" ${map.cato_name}"></c:out></strong></p>
	                <table class="table">
	                    <tbody>
	            <c:choose>
	            	<c:when test="${empty map.children}">
	            		</tbody></table></div>
	            	</c:when>
	            	<c:when test="${not empty map.children}">
	            		<c:set var="count" value="0" />
	            		<c:forEach items="${map.children}" var="child" step="1" varStatus="j">
	            			<c:if test="${count==0}">
	            				<tr>
	            			</c:if>
	            			<c:if test="${not empty child.cato_name}">
	                        	<td><a href="../search/s.action?cato_id=${child.cato_id}${not empty keyword?'&keyword=':''}${not empty keyword?keyword:''}${not empty doc_type?'&doc_type=':''}${not empty doc_type?doc_type:''}" class="${pCatoName!=null&&child.cato_id==catoInfo.getCatoId()?'classifyactive':''}">${child.cato_name}</a></td>
	                        	<c:set var="count" value="${count+1}" />
	                        </c:if>
	                        <c:if test="${j.index>=3}">
	            				</tr>
	            				<c:set var="count" value="0" />
	            			</c:if>
	            		</c:forEach> 
	            		</tbody></table></div>
	            	</c:when>
	            </c:choose>
	        </c:forEach>
			</div>
		</div>
	
	    <div class="row" style="margin-top: 10px;">
	        <div class="col-xs-12" style="border:1px solid #949494;min-height: 500px;padding-left: 0px;padding-right: 0px;">
	            <div id="result_search" class="list-group" style="margin-bottom: 0px;">
	            </div>
	        </div>
	    </div>
	
	    <div class="row">
	    	<div class="text-center">
		         <ul class="pager">
		            <li class="li-pre disabled"><a href="javascript:void(0);" id="pre-page">上一页</a></li>
		            <li class="li-next disabled"><a href="javascript:void(0);" id="next-page">下一页</a></li>
		        </ul>
		    </div>
	        <!--<div class="col-xs-12 text-right">
	            <ul class="pager">
	                <li><a id="pre-page" class="btn disabled">上一页</a></li>
	                <li><a id="next-page" class="btn disabled">下一页</a></li>
	            </ul>
	        </div>-->
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