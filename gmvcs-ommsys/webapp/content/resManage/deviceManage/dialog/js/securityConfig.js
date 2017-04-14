$(function() {
	$('#modify').on('click', modifySecurity);
	$('#close').on('click', cancelDialog);

	getSecurity();
});

//获取安全配置信息
function getSecurity() {
	var wsId = parent.getSelectedRow().wsId;
	var params = {
		'wsId': wsId
	};
	$.ajax({
		url: '/gmvcsomm/resource/mgr/ws/security/configuration/info.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		async: true,
		cache: false,
		error: function() {
			alert("获取安全配置信息失败！");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取安全配置信息异常！');
				return;
			}
			if(ret.headers.ret == 0) {
				setSecurity(ret)
				return;
			}
		}
	});
}
//修改安全配置信息
function modifySecurity() {
	var params = {};
	var bluetooth = document.getElementById("bluetooth");
	var cdDriver = document.getElementById("cdDriver");
	var keyboard = document.getElementById("keyboard");
	var modem = document.getElementById("modem");
	var usb = document.getElementById("usb");
	var nUSBExport = document.getElementById("nUSBExport");
	var sUSBExport = document.getElementById("sUSBExport");
	var whiteList = document.getElementById("whiteList").value;
	var enable = false;

	if(document.getElementById("start").checked === true) {
		enable = true;
	}
	if(document.getElementById("ban").checked === true) {
		enable = false;
	}

	var wsId = parent.getSelectedRow().wsId;
	
	var deviceControlPolicy = {
		"bluetooth": bluetooth.checked,
		"cdDriver": cdDriver.checked,
		"keyboard": keyboard.checked,
		"modem": modem.checked,
		"usb": usb.checked
	};
	var exportPolicy = {
		"nUSBExport": nUSBExport.checked,
		"sUSBExport": sUSBExport.checked
	};
	var netControlPolicy = {
		"enable": enable
	};
	params = {
		"deviceControlPolicy": deviceControlPolicy,
		"exportPolicy": exportPolicy,
		"netControlPolicy": netControlPolicy,
		"whiteList": whiteList,
		"wsId": wsId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/ws/security/configuration/modify.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("修改‘安全配置’失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('修改安全配置‘异常');
				return;
			}
			if(ret.headers.ret == 0) {
				parent.onSearch();
				cancelDialog();
				return;
			}
		}
	});
}

function setSecurity(ret) {
	var deviceControlPolicy = ret.body.deviceControlPolicy;
	var exportPolicy = ret.body.exportPolicy;
	var netControlPolicy = ret.body.netControlPolicy;
	var whiteList = ret.body.whiteList;
	//设备控制策略
	if(deviceControlPolicy.bluetooth == true) {
		$("#bluetooth").attr('checked', true); //蓝牙
	}
	if(deviceControlPolicy.cdDriver == true) {
		$("#cdDriver").attr('checked', true); //光驱
	}
	if(deviceControlPolicy.keyboard == true) {
		$("#keyboard").attr('checked', true); //USB键盘
	}
	if(deviceControlPolicy.modem == true) {
		$("#modem").attr('checked', true); //调制解调器
	}
	if(deviceControlPolicy.usb == true) {
		$("#usb").attr('checked', true); //USB存储设备
	}
	//导出策略
	if(exportPolicy.nUSBExport == true) {
		$("#nUSBExport").attr('checked', true);
	}
	if(exportPolicy.sUSBExport == true) {
		$("#sUSBExport").attr('checked', true);
	}
	//网络控制策略
	if(netControlPolicy.enable == true) {
		$("#start").attr('checked', true);
	} else {
		$("#ban").attr('checked', true);
	}
	$("#whiteList").attr('value',whiteList);
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}