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

        <link rel="shortcut ico" href="">

        <!-- 引入bootstrap样式 -->
        <link href="${pageContext.request.contextPath}/content/css/bootstrap.min.css" rel="stylesheet" >
        <!-- 引入字体库 -->
        <link href="${pageContext.request.contextPath}/content/css/font-awesome.min.css" rel="stylesheet" >
        	
        <!-- 自定义样式 -->
        <link href="${pageContext.request.contextPath}/content/css/sy.css" rel="stylesheet" >
        <link href="${pageContext.request.contextPath}/content/css/themes/pill.css" id="scrollUpTheme" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/content/uilib/artDialog/css/ui-dialog.css" rel="stylesheet">

        <!-- 引入jquery -->
        <script src="${pageContext.request.contextPath}/content/js/jquery-1.12.1.min.js"></script>

        <!-- 引入bootstrap脚本 -->
        <script src="${pageContext.request.contextPath}/content/js/bootstrap.js"></script>
        
        <script src="${pageContext.request.contextPath}/content/js/jquery.qrcode.min.js" type="text/javascript" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/content/js/qiao.js" type="text/javascript" charset="utf-8"></script>
        
        <!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
        <!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
        <!--[if lt IE 9]>
            <script src="${pageContext.request.contextPath}/content/js/html5shiv.min.js"></script>
            <script src="${pageContext.request.contextPath}/content/js/respond.min.js"></script>
        <![endif]-->
        
        <script src="${pageContext.request.contextPath}/content/js/jquery.scrollUp1.min.js"></script>
        
        <!-- IE8 实现输入框未输入时显示提示语句 -->
        <script src="${pageContext.request.contextPath}/content/js/jquery.placeholder.js"></script>
        
        <script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/dist/dialog-min.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/content/uilib/artDialog/util/dialogUtil.js"></script>
	    
	    <script>
	    	icon_base_path = "${pageContext.request.contextPath}/content/uilib/artDialog/";
	    </script>

		<script src="${pageContext.request.contextPath}/content/js/header/header.js" type="text/javascript" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/content/js/index/index.js"></script>
    </head>

    <body>
        <!-- 头部  -->
		<jsp:include page="./header.jsp"></jsp:include>
        
		<!-- 分类 -->
        <div class="container ci-menu">
            <div class="row" id="classfield">  
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
	                        	<td><a href="../search/s.action?cato_id=${child.cato_id}"><c:out value="${child.cato_name}" /></a></td>
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
            
		<!-- 最新发布 -->
        <div class="container" style="margin-top: 10px; padding: 10px 10px 0 10px;">
            <div class="row">
                <div class="col-sm-6 col-md-6 col-lg-8">
                    <div class="row">
                        <div class="col-xs-6">
                            <p style="font-size: 20px;"><strong>最新发布</strong></p>
                        </div>
                        <div class="col-xs-6 text-right">
                            <a href="../search/s.action">查看更多>></a>
                        </div>
                    </div>

                    <div class="row">
                    	<c:forEach var="item" items="${pubInfoList}" varStatus="i">
                        <div class="col-sm-6 col-md-4 col-lg-3">
                            <div class="thumbnail">
                                <a class="a_doc" docId="${item.doc_id}" readabled="${item.readable}" href="javascript:void(0);"><img class="lazy" src="${pageContext.request.contextPath}//content/images/jt.jpg" width="113" height="150"></a>
                                <div class="caption text-center">
                                    <a class="a_doc" docId="${item.doc_id}" readabled="${item.readable}" href="javascript:void(0);" style="color: #000; font-size: 12px;text-align: center;"><c:out value="${item.doc_title}"></c:out></a>
                                </div>
                            </div>
                        </div> 
                        <c:if test="${i.index!=0 && 0==(i.index+1)%4}">
		            		<div class="clearfix visible-lg"></div>
		            	</c:if>
                        </c:forEach>
                    </div>
                </div>

                <div class="col-sm-6 col-md-6 col-lg-4">
                    <!-- 阅读榜单 -->
                    <div class="row">
                        <div class="col-xs-6">
                            <p style="font-size: 20px;"><strong>阅读榜单</strong></p>
                        </div>
                        <div class="col-xs-6 text-right hide">
                            <a href="#">查看更多>></a>
                        </div>
                    </div>
					
					<c:forEach var="item" items="${readBillboardList}" varStatus="x">
						<div class="row" style="padding-left: 15px;">
	                        <a class="a_doc" docId="${item.doc_id}" readabled="${item.readable}" href="javascript:void(0);" style="font-size: 15px;display: block; margin-bottom: 10px;overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
	                            <c:choose>
	                            	<c:when test="${x.index==0}">
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #fd8c84;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>
					            	</c:when>	
					            	<c:when test="${x.index==1}">
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #7fd75a;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>
					            	</c:when>
					            	<c:when test="${x.index==2}">
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #60c4fd;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>
					            	</c:when>
					            	<c:otherwise>
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #999999;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>	
					            	</c:otherwise>
	                            </c:choose>
	                            <c:out value="${item.doc_title}"></c:out>
	                        </a>
	                    </div>
					</c:forEach>
					
					<!-- 收藏榜单 -->
                    <div class="row">
                        <div class="col-xs-6">
                            <p style="font-size: 20px;"><strong>收藏榜单</strong></p>
                        </div>
                        <div class="col-xs-6 text-right hide">
                            <a href="#">查看更多>></a>
                        </div>
                    </div>

                    <c:forEach var="item" items="${collectionBillboardList}" varStatus="x">
						<div class="row" style="padding-left: 15px;">
	                        <a class="a_doc" docId="${item.doc_id}" readabled="${item.readable}" href="javascript:void(0);" style="font-size: 15px;display: block; margin-bottom: 10px;overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
	                        	<c:choose>
	                            	<c:when test="${x.index==0}">
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #fd8c84;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>
					            	</c:when>	
					            	<c:when test="${x.index==1}">
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #7fd75a;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>
					            	</c:when>
					            	<c:when test="${x.index==2}">
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #60c4fd;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>
					            	</c:when>
					            	<c:otherwise>
					            		<span style="font-size: 18px; width: 25px; height: 25px; display: inline-block; background-color: #999999;color: #FFF; text-align: center;"><strong><c:out value="${x.index+1}"></c:out></strong></span>	
					            	</c:otherwise>
	                            </c:choose>
	                            <c:out value="${item.doc_title}"></c:out>
	                        </a>
	                    </div>
					</c:forEach>
                </div>
            </div>
        </div>

        <div class="container" style="border-top: 1px solid #ddd;height: 1px;"></div>
		
		<!-- 分类文档展示 -->
		<c:forEach var="item" items="${catoList}" varStatus="i">
				<div class="container" style="margin-top: 10px;">
		            <h2 style="color: #d33a3a;"><strong><c:out value="${item.cato_name}"></c:out></strong></h2>       
		            <div class="row">
		                <div class="col-sm-12">
		                    <ul id="myTab" class="nav nav-tabs">
		                    <c:forEach items="${item.children}" var="child" step="1" varStatus="j">
		                    	<c:choose>
					            	<c:when test="${j.index==0}">
					            		<li class="active"><input value="${child.cato_id}" class="hide"><a href="#classify${i.index}${j.index}" data-toggle="tab"><c:out value="${child.cato_name}"></c:out></a></li>
					            	</c:when>
			                    	<c:otherwise>
			                    		<li><input value="${child.cato_id}" class="hide"><a href="#classify${i.index}${j.index}" data-toggle="tab"><c:out value="${child.cato_name}"></c:out></a></li>
			                    	</c:otherwise>
		                    	</c:choose>
		                    </c:forEach>
			                    <div class="text-right">
									<a href="javascript:void(0);" class="more_cato">查看更多>></a>
			                    </div>
		                    </ul>
		                   	
		                   	<div id="myTabContent" class="container tab-content" style="background-color:#FFF; border:1px solid #ddd; border-top: transparent; min-height: 250px; padding-top: 20px;">
		                   		<c:forEach items="${item.children}" var="child" step="1" varStatus="x">
		                   			<c:choose>
						            	<c:when test="${x.index==0}">
						            		<div class="row tab-pane fade in active" id="classify${i.index}${x.index}">
						            	</c:when>
				                    	<c:otherwise>
				                    		<div class="row tab-pane fade" id="classify${i.index}${x.index}">
				                    	</c:otherwise>
			                    	</c:choose>
		                   			
		                   			<c:if test="${not empty child.docs}">
		                   				<c:forEach var="doc" items="${child.docs}" begin="0" end="6">
		                   					<div class="col-sm-4 col-md-3 col-lg-2">
				                                <div class="thumbnail">
				                                    <a class="a_doc" docId="${doc.doc_id}" readabled="${doc.readable}" href="javascript:void(0);"><img class="lazy" src="${pageContext.request.contextPath}//content/images/jt.jpg" width="113" height="150"></a>
				                                    <div class="caption text-center">
				                                        <a class="a_doc" docId="${doc.doc_id}" readabled="${doc.readable}" href="javascript:void(0);" style="color: #000; font-size: 12px;text-align: center;"><c:out value="${doc.doc_title}"></c:out></a>
				                                    </div>
				                                </div>
				                            </div>		
		                   				</c:forEach>
		                   			</c:if>
		                   			<c:if test="${empty child.docs}">
	                   					<p style="text-align: center;vertical-align: middle;line-height: 250px;">该分类暂无文档</p>
	                   				</c:if>
		                   			</div>
		                   		</c:forEach>	
		                   	</div>
		               	</div>
					</div>
				</div>

		</c:forEach>
                    	
        <div class="footer container-fluid" style="margin-top: 20px;padding-top:20px; padding-bottom: 20px; border-top: 5px solid #d33a3a;">
            <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">版权所有© 2016中华人民共和国公安局交通管理局</label><br/>
            <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">著作权号: 中华人民共和国国家版权局2011SR012287</label><br/>
            <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">技术支持: 广州市国迈科技有限公司 支持电话: 020-2839 8008</label><br/>
            <label style="width: 100%; color: #9d9d9d; font-size: 13px; text-align: center;">软件版本号: V2.2.0 发布日期: 2016-01-01</label><br/>
        </div>
    </body>
</html>