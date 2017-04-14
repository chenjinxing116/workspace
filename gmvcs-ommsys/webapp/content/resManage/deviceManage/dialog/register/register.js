/**
 * Created by GM-PC-0235 on 2016/10/20.
 */

function getI18NStr(key) {
	return parent.getI18NStr(key);
}

$(function() {
	
	$('[data-i18n]').each(function() {
		var $this = $(this);
		$this.append(getI18NStr($this.attr('data-i18n')));
	});
	
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
	var regSimCode = $("#regSimCode").val();
	if(!userCode) {
		alert(getI18NStr('alert.selectPolice'));
		return;
	}
	if(!regSimCode) {
		alert(getI18NStr('alert.inputSIM'));
		return;
	}
	if(regSimCode.length<1){
		alert(getI18NStr('alert.SIMError'));
		return;
	}
	if(!deviceId) {
		alert(getI18NStr('alert.inputDeviceID'));
		return;
	}
	var params = {
		"deviceId": deviceId,
		"manufacturers": manufacturers,
		"model": model,
		"orgId": orgId,
		"userCode": userCode,
		"simCode": regSimCode
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
				alert(getI18NStr('alert.repeatDeviceID'));
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
			alert(getI18NStr('alert.getManufaError'));
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert(getI18NStr('alert.getManufaError'));
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
			alert(getI18NStr('alert.getZFYTypeError'));
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert(getI18NStr('alert.getZFYTypeError'));
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
	$('#orgCode').gmOrgSelector({
		'chkStyle': '',
		'chkboxType': {
			"Y": "ps",
			"N": "ps"
		},
		'children': 'children',
		'showPolice': false,
		'name': 'displayName',
		'onSelect': getUser
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
			alert(getI18NStr('alert.getPoliceError'));
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert(getI18NStr('alert.getPoliceError'));
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
    $("#regSimCode").keypress(function (event) {
        var eventObj = event || e;
        var keyCode = eventObj.keyCode || eventObj.which;
        if ((keyCode >= 48 && keyCode <= 57))
            return true;
        else
            return false;
    });/*.focus(function () {
    //禁用输入法
        this.style.imeMode = 'disabled';
    }).bind("paste", function () {
    //获取剪切板的内容
        var clipboard = window.clipboardData.getData("Text");
        if (/^\d+$/.test(clipboard))
            return true;
        else
            return false;
    });*/
};