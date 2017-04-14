$(function() {
	$('#modify').on('click', modifyTactis);
	$('#close').on('click', cancelDialog);
	
	getTactis();
});

//获取策略配置
function getTactis() {
	var wsId = parent.getSelectedRow().wsId;
	var params = {
		'wsId':wsId
	};
	$.ajax({
		url: '/gmvcsomm/resource/mgr/ws/policy/configuration/info.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		async: true,
		cache: false,
		error: function() {
			alert("获取策略配置信息失败！");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取策略配置信息异常！');
				return;
			}
			if(ret.headers.ret == 0) {
//				var nFile = ret.body.n;
//				var kFile = ret.body.k;
//				var fFile = ret.body.f;
//				if(nFile===null){
//					nFile = false;
//				}
//				if(kFile===null){
//					kFile = false;
//				}
//				if(fFile===null){
//					fFile = false;
//				}
//				
//				$("#nFile").attr("checked",nFile);
//				$("#kFile").attr("checked",kFile);
//				$("#fFile").attr("checked",fFile);
				$("#days").attr("value",ret.body.days);
				return;
			}
		}
	});
}
//修改策略配置
function modifyTactis() {
	var params = {};
//	var nFile = document.getElementById("nFile");
//	var kFile = document.getElementById("kFile");
//	var fFile = document.getElementById("fFile");
	var days = $("#days").val();
	var wsId = parent.getSelectedRow().wsId;
	params = {
//		"n": nFile.checked,
//		"k": kFile.checked,
//		"f": fFile.checked,
		"days": days,
		"wsId": wsId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/ws/policy/configuration/modify.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("修改失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('修改策略配置‘异常');
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

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}