/**
 * Created by GM-PC-0235 on 2016/10/20.
 */
$(function() {
	$('#register').on('click', register);
	$('#close').on('click', cancelDialog);
});

function register() {
	var params = {
		"deviceId": '',
		"manufacturers": '',
		"model": '',
		"orgCode": '',
		"userCode": ''
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/register.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("执法记录仪‘查询数据’失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('注册新设备失败！');
				return;
			}
			if(ret.headers.ret == 0) {
				//				var body = ret.body;
				//				infoArray.push(body);
				//				initTable(body);
				//				if(ret.body.length >= pageSize) {
				//					$('#pageNext').attr('disabled', false);
				//				} else {
				//					pageCount = params.page;
				//					$('#pageNext').attr('disabled', true);
				//				}
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
				html += '<option value=' + ret.body[i].id + '>' + ret.body[i].name + '</option>';
			}

			$('#manufactures').append(html);
		}
	});
}
//获取执法记录仪产品型号
function getDsj(){
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsj/list.action',
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
				html += '<option value=' + ret.body[i].id + '>' + ret.body[i].name + '</option>';
			}

			$('#dsj').append(html);
		}
	});
}
