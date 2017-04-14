/**
 * Created by GM-PC-0235 on 2016/10/20.
 */
$(function() {
	$('#register').on('click', register);
	$('#close').on('click', cancelDialog);

	getManufactures();
	getDsj();

	//部门信息初始化
	initDep();
	onlyNumAlpha();//限制设备编号不能输入特殊字符
	onlyNum();//限制sim卡只能输入数字
});

function register() {
	var deviceId = $("#deviceId").val();
	var manufacturers = $("#manufacturers").val();
	var model = $("#model").val();
	var treeObj = $("#orgCode").data('selOrg'); //部门
	var orgId = treeObj.id;
	var userCode = $("#userCode").val();
	var simCode = $("#simCode").val();
	if(!userCode) {
		alert("请选择配发警员！");
		return;
	}
	if(!simCode) {
		alert("请填写sim卡号！");
		return;
	}
	if(simCode.length<1){
		alert("sim卡号格式不正确！");
		return;
	}
	if(!deviceId) {
		alert("请填写设备编号！");
		return;
	}
	var params = {
		"deviceId": deviceId,
		"manufacturers": manufacturers,
		"model": model,
		"orgId": orgId,
		"userCode": userCode,
		"simCode": simCode
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/register.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('注册新设备失败！');
				return;
			}
			if(ret.headers.ret == 0) {
				parent.onSearch();
				cancelDialog();
			}
		}
	});
}
//获取厂商型号
function getManufactures() {
	$.ajax({
		url: '/gmvcsomm/resource/mgr/manufactures/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: true,
		cache: false,
		error: function() {
			alert("获取厂商型号失败！");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取厂商信息异常！');
				return;
			}
			var html = "";
			for(var i = 0; i < ret.body.length; ++i) {
				html += '<option value=' + ret.body[i].code + '>' + ret.body[i].name + '</option>';
			}
			$('#manufacturers').empty();
			$('#manufacturers').append(html);
		}
	});
}
//获取执法记录仪产品型号
function getDsj() {
	$.ajax({
		url: '/gmvcsomm/resource/mgr/model/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: true,
		cache: false,
		error: function() {
			alert("获取执法仪型号失败！");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取执法仪信息异常！');
				return;
			}
			var html = "";
			for(var i = 0; i < ret.body.length; ++i) {
				html += '<option value=' + ret.body[i].code + '>' + ret.body[i].name + '</option>';
			}
			$('#model').empty();
			$('#model').append(html);
		}
	});
}

function initDep() {
	$.ajax({
		url: '/gmvcshomepage/system/org.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: true,
		cache: false,
		error: function() {
			alert("获取执法记录仪‘部门信息’获取失败");
			return;
		},

		success: function(ret) {
			var zNodes = ret;
			$('#orgCode').gmOrgSelector({
				'chkStyle': '',
				'chkboxType': {
					"Y": "ps",
					"N": "ps"
				},
				'children': 'children',
				'name': 'text',
				'onSelect': getUser
			}, zNodes);
		}
	});
}

function getUser(tree) {
	var orgId = tree.id;
	params = {
		"org_id": orgId
	}
	$.ajax({
		url: '/gmvcshomepage/system/org/user.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		async: true,
		cache: false,
		error: function() {
			alert("获取执法记录仪‘部门人员信息’获取失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取部门人员信息异常！');
				return;
			}
			var html = "";
			for(var i = 0; i < ret.body.length; ++i) {
				html += '<option value=' + ret.body[i].userCode + '>' + ret.body[i].userName + '(' + ret.body[i].userCode + ')' + '</option>';
			}
			$('#userCode').empty();
			$('#userCode').append(html);
		}
	});
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}
// <summary>
// 限制只能输入数字、字母、-和_
// </summary>
function onlyNumAlpha() {
    $("#deviceId").keypress(function (event) {
        var eventObj = event || e;
        var keyCode = eventObj.keyCode || eventObj.which;
        if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122)||(keyCode == 45 || keyCode == 95))
            return true;
        else
            return false;
    });/*.focus(function () {
      this.style.imeMode = 'disabled';
    }).bind("paste", function () {
        var clipboard = window.clipboardData.getData("Text");
        if (/^(\d|[a-zA-Z])+$/.test(clipboard))
            return true;
        else
            return false;
    });*/
};
function onlyNum() {
    $("#simCode").keypress(function (event) {
        var eventObj = event || e;
        var keyCode = eventObj.keyCode || eventObj.which;
        if ((keyCode >= 48 && keyCode <= 57))
            return true;
        else
            return false;
    }).focus(function () {
    //禁用输入法
        this.style.imeMode = 'disabled';
    }).bind("paste", function () {
    //获取剪切板的内容
        var clipboard = window.clipboardData.getData("Text");
        if (/^\d+$/.test(clipboard))
            return true;
        else
            return false;
    });
};