<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.goldmsg.gmdoc.entity.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="container-fluid">
    <div class="container">
        <div class="row" style="height: 118px;">
            <div class="col-lg-3 visible-lg" style="height: 100%; line-height:118px;">
                <p class="text-danger visible-lg" style="width:100%; font-size: 32px;"><strong>消防文库</strong><small style="font-size:20px;"><c:out value=" [${distInfo.distName}]" escapeXml="false" default="无"/></small></p>
                <p class="text-danger text-center hidden-lg" style="width:100%; font-size: 34px;"><strong>消防文库</strong><small style="font-size:20px;"><c:out value=" [${distInfo.distName}]" escapeXml="false" default="无"/></small></p>
            </div>
			
            <div class="col-lg-7 col-xs-12" style="height: 100%;">
									<form id="form_search" method="get" action="../search/s.action">
                    <div class="col-lg-10" style="margin-top: 30px;">
                        <div class="input-group">
                            <input id="input_search" autocomplete="off" value="${empty keyword?'':keyword}" name="keyword" type="text" class="form-control dropdown-toggle" data-toggle="dropdown" placeholder="请输入关键字">
                            <ul id='ui_menu' class="dropdown-menu hidden" role="menu">
															</ul>
                            <span class="input-group-btn">
                                <button id="btn_search" class="btn btn-default"> 搜索</button>
                            </span>
                        </div>
                        <div style="margin-top: 5px; margin-left: -20px;">
                            <label class="checkbox-inline">
                                <input class="myradio" type="radio" name="doc_type" id="optionsRadios1" value="all" <c:out value="${(empty doc_type||doc_type=='all')?'checked':''}"></c:out>/> 全部
                            </label>

                            <label class="checkbox-inline">
                                <input type="radio" name="doc_type" id="optionsRadios2" value="doc" <c:out value="${doc_type=='doc'?'checked':''}"></c:out>/> DOC
                            </label>
                            
                            <label class="checkbox-inline">
                                <input type="radio" name="doc_type" id="optionsRadios2" value="xls" <c:out value="${doc_type=='xls'?'checked':''}"></c:out>/> XLS
                            </label>
                            
                            <label class="checkbox-inline">
                                <input type="radio" name="doc_type" id="optionsRadios5" value="pdf" <c:out value="${doc_type=='pdf'?'checked':''}"></c:out>/> PDF
                            </label>

                            <label class="checkbox-inline">
                                <input type="radio" name="doc_type" id="optionsRadios3" value="ppt" <c:out value="${doc_type=='ppt'?'checked':''}"></c:out>/> PPT
                            </label>

                            <label class="checkbox-inline">
                                <input type="radio" name="doc_type" id="optionsRadios4" value="txt" <c:out value="${doc_type=='txt'?'checked':''}"></c:out>/> TXT
                            </label>
                        </div>
                    </div>
									</form>
            </div>

            <div id="btn_exit" class="col-lg-2 pull-right visible-lg" style="height: 100%;position:relative;">
	            <a href="javascript:void(0);" class="app-link">
		            <span style="display: block;"><i class="fa fa-qrcode" aria-hidden="true"></i>下载文库APP</span>

					<span>
						<span style="margin: 5px auto;text-align: center;">扫一扫二维码下载</span>
				    	<span id="div_ewm" style="display: block;"></span>
			    	</span>
		    	</a>
            </div>
        </div>
    </div>
</div>
   
<div class="clearfix visible-xs"></div>

<!-- 导航 -->
<div class="container-fluid" style="background-color: #d33a3a;">
    <div class="container">
        <nav class="navbar navbar-inverse" role="navigation" style="border: none; background-color: #d33a3a;">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar-collapse">
                    <span class="sr-only">切换导航</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand visible-xs" href="#">消防文库</a>
            </div>
            <div class="collapse navbar-collapse" id="main-navbar-collapse" style="margin: auto -15px auto -15px;">
                <ul id="nar_mian" class="nav navbar-nav" style="width: 100%;">
                	<c:if test="${sessionScope.privileges.contains('0001')}">
                    	<li class="nav-current"><a href="../user/index.action">首页</a></li>
                    </c:if>
                    <c:if test="${sessionScope.privileges.contains('0002')}">
                    	<li><a href="../doc/manage.action">文档管理</a></li>
                    </c:if>	
                    <c:if test="${sessionScope.privileges.contains('0003')}">
                    	<li><a href="../cato/manage.action">分类管理</a></li>
                   	</c:if>		
                    <c:if test="${sessionScope.privileges.contains('0004')}">
                    	<li><a href="../user/center.action">个人中心</a></li>
                   	</c:if>	
                    <c:if test="${sessionScope.privileges.contains('0005')}">
                    	<li><a href="../system/auth.action">用户权限</a></li>
                   	</c:if>	
                    <li id="li_auto" class="invisible"><a>auto</a></li>
                    <c:if test="${sessionScope.privileges.contains('0006')}">
                    	<li><a style="font-size: 14px;" href="http://localhost:8080/sysmgr/sysmgr!index.action">系统管理</a></li>
                   	</c:if>
                    <li><a style="font-size: 14px;" href="../user/logout.action">退出系统</a></li>
                </ul>
            </div>
        </nav>
    </div>
</div>
