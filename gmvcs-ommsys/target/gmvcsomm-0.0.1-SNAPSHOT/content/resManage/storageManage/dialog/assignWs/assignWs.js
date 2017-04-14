$(function() {
	var row = parent.getSelectedRow();
	var id = row.id;
	getWsInfo(id);
	$('#notListWS').on('dblclick', 'li', add);
	$('#listWS').on('dblclick', 'li', del);
	$("#sureAssign").on('click',assignWs);
	$("#close").on('click',cancelDialog);
});

var wsid = [];//工作站ID字符串列表

function add() {
	$(this).remove();
	var text = $(this).text();
	var value = $(this).attr("value");
	var html = "";
	html = "<li value=" + value + ">" + text + "</li>"
	wsid.push(value);
	$("#listWS").append(html);
	
}

function del() {
	$(this).remove();
	var text = $(this).text();
	var value = $(this).attr("value");
	var html = "";
	html = "<li value=" + value + ">" + text + "</li>"
	wsid.pop(value);
	$("#notListWS").append(html);
}
//获取工作站分配信息
function getWsInfo(id) {
	var param = {
		"id": id
	};
	$.ajax({
		url: "/gmvcsomm/resource/mgr/storage/listWs.action",
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: param,
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取工作站信息异常！');
				return;
			}
			var body = ret.body;
			var asigned = body.asigned;
			var unsigned = body.unsigned;
			initAsigned(asigned);
			initNotAsigned(unsigned);
		}
	});
}
//分配工作站
function assignWs() {
	var row = parent.getSelectedRow();
	var id = row.id;
	var params = {
		"storageId": id,
		"wsid":wsid
	};
	$.ajax({
		url: "/gmvcsomm/resource/mgr/storage/assignWs.action",
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
				alert('分配工作站异常！');
				return;
			}
			cancelDialog();
		}
	});
}

function initNotAsigned(notWsInfo) {
	var html = "";
	for(var i = 0; i < notWsInfo.length; i++) {
		var wsInfo = notWsInfo[i];
		html += "<li value=" + wsInfo.value + ">" + wsInfo.name + "</li>"
	}
	$("#notListWS").append(html);
}
function initAsigned(wsInfo) {
	var html = "";
	for(var i = 0; i < wsInfo.length; i++) {
		var assignwsInfo = wsInfo[i];
		html += "<li value=" + assignwsInfo.value + ">" + assignwsInfo.name + "</li>"
		wsid.push(assignwsInfo.value);
	}
	$("#listWS").append(html);
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}