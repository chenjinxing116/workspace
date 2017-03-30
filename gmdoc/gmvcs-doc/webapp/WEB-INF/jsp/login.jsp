<%@ page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="webkit" />

<title>消防文库-登录</title>

<meta name="viewport" content="width=device-width,initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,Chrome=1">

<!-- 引入 Bootstrap -->
<link href="<%=basePath%>content/css/bootstrap.min.css" rel="stylesheet">

<link href="<%=basePath%>content/css/login.css" rel="stylesheet">

<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="<%=basePath%>content/js/jquery-1.12.1.min.js"></script>

<!-- 包括所有已编译的插件 -->
<script src="<%=basePath%>content/js/bootstrap.min.js"></script>

<!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
<!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
<!--[if lt IE 9]>
        <script src="<%=basePath%>content/js/html5shiv.min.js"></script>
        <script src="<%=basePath%>content/js/respond.min.js"></script>
    <![endif]-->

<script type="text/javascript" src="<%=basePath%>content/js/security.js"></script>
<script type="text/javascript" src="<%=basePath%>content/js/md5.js"></script>
<script type="text/javascript"
	src="<%=basePath%>content/js/jquery.placeholder.js"></script>

<script>
        $(function () {
            $('input, textarea').placeholder();
        });

        function onlogin()
        {
            var userName = $("input[name='j_username']").val();
            var password = $("input[name='j_password']").val();
            
            if(userName.length==0 || password.length==0){
            	$("#login-msg").html(" " + "请输入用户名与密码");
				$("#alert-login").removeClass("hide");
				$("#input-pwd").addClass("has-error");
				
				if(userName.length==0){
					$("input[name='j_username']").focus();
					return;
				}
				
				if(password.length==0){
					$("input[name='j_password']").focus();
					return;
				}
	
				return;
            }
            
            var jsonStr = "";
        	var url     = "<%=basePath%>user/check.do";

			var pdata = {
				"userName" : userName,
				"password" : hex_md5(password)
			};
	
			$.ajax({
				url : url,
				type : 'post',
				contentType : "application/json",
				data : JSON.stringify(pdata),
				async : false,
				cache : false,
				success : function(result) {
					var code = result.code;
					var msg = result.msg;
					if (code != 200) {
						$("#login-msg").html(" " + msg);
						$("#alert-login").removeClass("hide");
						$("#input-pwd").addClass("has-error");
						return;
					}
	
					//如果有需要保存密码，可以引入jquery.cookie.js保存cookie
					//if(that.bSavePassword){
					//	$.cookie(userName , password , { expires: 7 });
					//}
					$("input[name='j_password']").val(hex_md5(password));
					$("form").submit(); //验证成功，直接提交表单，会自动跳转到index.jsp
					return;
				},
				error : function(result) {
					
				}
			});

		}

		window.onload = function() {
		$("#btn_login").on("click", function() {
			onlogin();
		});

		/*$("form input").change(function(){
		$("#alert-login").addClass("hide");
		$("#input-pwd").removeClass("has-error");
		return;
		});*/

		$("form input").keyup(function(event) {
			if (event.keyCode == 13)
				return;

			$("#alert-login").addClass("hide");
			$("#input-pwd").removeClass("has-error");
			return;
		});

		$("form input").keypress(function(event) {
			if (event.keyCode == 13) {
				onlogin();
				return;
			}
		});
	}
</script>
</head>

<body>
	<div class="container-fluid wrapper">
		<div class="hidden-sm hidden-xs bg">
			<img src="<%=basePath%>content/images/main_bg.png"
				class="img-responsive h-center">
		</div>
		<div class="container">
			<div class="row">
				<div class="col-xs-12 img_title">
					<!--<img src="<%=basePath%>content/images/login_title.png"
						class="img-responsive">-->
						<h1 style="font-size:50px;color: #D33A3A; font-weight: bold;text-align: center;">消 防 文 库</h1>
				</div>
			</div>

			<div class="row marigin-bottom">
				<div class="col-md-2 col-md-offset-4">
					<img src="<%=basePath%>content/images/login_u.png"
						class="img-responsive">
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 col-md-offset-3">
					<div class="row">
						<div class="col-md-8 col-md-offset-2">
							<form method="post" action="j_security_check">
								<div class="form-group">
									<div class="input-group marigin-bottom">
										<span class="input-group-addon" style="background-color: #B62015;"><span class="glyphicon glyphicon-user" style="color: #FFFFFF;"></span></span>
										<input name="j_username" type="text" class="form-control" placeholder="请输入用户名" />
									</div>

									<div class="input-group marigin-bottom">
										<span class="input-group-addon" style="background-color: #B62015;"><span class="glyphicon glyphicon-lock" style="color: #FFFFFF;"></span></span>
										<input name="j_password" type="password" class="form-control" placeholder="请输入密码" />
									</div>

									<div class="btn_sub marigin-bottom">
										<button id="btn_login" type="button" class="btn btn-danger btn-lg form-control" style="height: 39px; font-size: 15px;">登录</button>
									</div>

									<div class="alert alert-danger hide" id="alert-login">
										<strong>提示:</strong>
										<span id="login-msg">
											您输入的用户名或者密码错误!
										</span>
									</div>

									<div class="lab_concact">
										<label style="">忘记密码请联系管理员</label>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="push"></div>
	</div>

	<div class="footer container-fluid">
		<label>版权所有© 2016中华人民共和国公安局交通管理局</label><br /> <label>著作权号:
			中华人民共和国国家版权局2011SR012287</label><br /> <label>技术支持: 广州市国迈科技有限公司
			支持电话: 020-2839 8008</label><br /> <label>软件版本号: V2.2.0 发布日期:
			2016-01-01</label><br />
	</div>
</body>
</html>
