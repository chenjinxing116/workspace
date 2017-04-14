$(function() {
	initBusTypeList(); //初始化岗位类型
	getPoliceType(); //初始化警员类别
	$("#type").change(function() {
		var option = $("option:selected");
		var val = option[0].innerHTML;
		var id = option[0].id;
		for(var i = 0; i < busTypeValue.length; i++) {
			if(val == busTypeValue[i]) { //获取下拉列表选中值的数据，如’车架管‘的所有数据
				getBusTypeDataList(id);
				break;
			}
		}
	});

	$('#alter').on('click', alter);
	$('#close').on('click', cancelDialog);
});

function onChange() {
	$("#type").change(function() {
		var option = $("option:selected");
		var val = option[0].innerHTML;
		var id = option[0].id;
		for(var i = 0; i < busTypeValue.length; i++) {
			if(val == busTypeValue[i]) { //获取下拉列表选中值的数据，如’车架管‘的所有数据
				getBusTypeDataList(id);
				break;
			}
		}
	});
}

//4.12.2	编辑用户信息
function alter() {
	var userCode = parent.getSelectedRow().userCode;
	var postId = $("#typeData option:selected").attr('id');
	var type = $("#policeType option:selected").attr('id');
	var bsCode=$("#type option:selected").attr('code');
	var params = {
		"userCode": userCode,
		"postId": postId,
		"bsCode": bsCode,
		"type": type
	}
	$.ajax({
		url: '/gmvcsomm/user/mgr/modify.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("编辑失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('编辑‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
//				parent.onSearchUserCode(userCode);
				alert("编辑成功！");
				cancelDialog();
			}
		}
	});
}


var busTypeValue = []; //业务类型名字
//4.12.3	获取业务类别类型列表
function initBusTypeList() {
	$.ajax({
		url: '/gmvcsomm/bs/mgr/type/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('编辑‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				var html = "";
				for(var i = 0; i < body.length; i++) {
					html += "<option id=" + body[i].id + " code=" + body[i].code + ">" + body[i].name + "</option>";
					busTypeValue.push(body[i].name);
				}
				$("#type").empty()
				$("#type").append(html);

				getBusTypeDataList(body[0].id);
			}
		}
	});
}

//4.13.5	获取业务类别下的岗位列表
function getBusTypeDataList(id) {
	var params = {
		"id": id
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/job/list.action',
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
				alert('获取‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				var html = "";
				for(var i = 0; i < body.length; i++) {
					html += "<option id=" + body[i].id + " code=" + body[i].code + ">" + body[i].name + "</option>"
				}
				$("#typeData").empty();
				$("#typeData").append(html);
			}
		}
	});
}

//4.12.5	获取警员类别列表
function getPoliceType() {
	$.ajax({
		url: '/gmvcsomm/user/mgr/type/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('类别获取‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				var html = "";
				for(var i = 0; i < body.length; i++) {
					html += "<option id=" + body[i].id + " code=" + body[i].code + ">" + body[i].name + "</option>"
				}
				$("#policeType").append(html);
			}
		}
	});
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}