$(function() {
	$("#modify").on('click', modify);
	$('#close').on('click', cancelDialog);
	initModify()
});

//初始化修改页面
function initModify() {
	var row = parent.getSelectedRow();
	var ipAddr = row.ipAddr;
	var orgName = row.orgName;
	var wsName = row.wsName;
	var admin = row.admin;
	var phoneNumber = row.phoneNumber;
	var addr = row.addr;
	$('#ipAddr').attr("value", ipAddr);
	$('#departmen').attr("value", orgName);
	$('#orgName').attr("value", orgName);
	$('#wsName').attr("value", wsName);
	$('#admin').attr("value", admin);
	$('#phoneNumber').attr("value", phoneNumber);
	$('#wsAddr').attr("value", addr);
}

var params = {};

function modify() {
	var wsName = $("#wsName").val();
	var admin = $("#admin").val();
	var phoneNumber = $("#phoneNumber").val();
	var wsAddr = $("#wsAddr").val();
	var wsId = parent.getSelectedRow().wsId;
	params = {
		"wsName": wsName,
		"admin": admin,
		"phoneNumber": phoneNumber,
		"wsAddr": wsAddr,
		"wsId": wsId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/workstation/modify.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("采集工作站‘修改数据’失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取设备信息失败，设备或不在线');
				return;
			}
			if(ret.headers.ret == 0) {
				parent.onSearch();
				cancelDialog();
			}
		}
	});
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}