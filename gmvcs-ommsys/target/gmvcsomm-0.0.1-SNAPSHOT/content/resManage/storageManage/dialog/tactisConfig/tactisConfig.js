$(function() {
	var row = parent.getSelectedRow();
	var id = row.id;
	getTactisInfo(id);

	$("#startTimeDisplay").datetimepicker({
		format: "yyyy-mm-dd hh:ii:ss",
		language: "zh-CN",
		autoclose: true,
		todayBtn: 'linked',
		todayHighlight: true,
		pickerPosition: "bottom-left"
	});
	$("#endTimeDisplay").datetimepicker({
		format: "yyyy-mm-dd hh:ii:ss",
		language: "zh-CN",
		autoclose: true,
		todayBtn: 'linked',
		todayHighlight: true,
		pickerPosition: "bottom-left"
	});

	$("#alter").on('click', modifyTactisInfo);
	$("#close").on('click', cancelDialog);
});

//获取配置策略信息
function getTactisInfo(id) {
	var params = {
		"storageId": id
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/storage/policy/configuration/info.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取设备信息失败，设备或不在线');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				initTactisInfo(body);
			}
		}
	});
}
//初始化配置信息
function initTactisInfo(body) {
	var storageId = body.storageId;
	var maxUploadInst = body.maxUploadInst;
	var iNetSpeed = body.iNetSpeed;
	var startTimeDisplay = body.startTimeDisplay;
	var endTimeDisplay = body.endTimeDisplay;
	var days = body.days;

	$("#maxUploadInst").attr('value', maxUploadInst);
	$("#iNetSpeed").attr('value', iNetSpeed);
	$("#startTimeDisplay").attr('value', startTimeDisplay);
	$("#endTimeDisplay").attr('value', endTimeDisplay);
	$("#days").attr('value', days);
}

//修改存储策略配置信息
function modifyTactisInfo() {
	var row = parent.getSelectedRow();
	var id = row.id;
	var maxUploadInst = $("#maxUploadInst").val();
	var iNetSpeed = $("#iNetSpeed").val();
	var startTimeDisplay = $("#startTimeDisplay").val();
	var endTimeDisplay = $("#endTimeDisplay").val();
	var days = $("#days").val();
	
	
	//test
	var oneTime = getTimeByDateStr(startTimeDisplay);

	var params = {
		"storageId": id,
		"maxUploadInst": maxUploadInst,
		"iNetSpeed": iNetSpeed,
		"startTime": getTimeByDateStr(startTimeDisplay),
		"endTime": getTimeByDateStr(endTimeDisplay),
		"days": days
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/storage/policy/configuration/modify.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert('修改失败');
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('修改异常');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				cancelDialog();
			}
		}
	});
}

function getTimeByDateStr(stringTime){
	//return Date.parse(new Date(stringTime));
	var dt = new Date(stringTime.replace(/-/,"/"));
	return dt.getTime();
}
//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}