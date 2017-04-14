$(function() {
	var row = parent.getSelectedRow();
	//	initStorageInfo(row);
	getDepartment("ws_depart");
	getManufactures();
	$("#add").on('click', addNewWSInfo);
	$("#close").on('click', cancelDialog);
});
//获取部门信息
function getDepartment(department) {
	//	部门信息 异步加载
	
	$('#' + department).gmOrgSelector({
		'chkStyle': '',
		'chkboxType': {
			"Y": "ps",
			"N": "ps"
		},
		'children': 'children',
		'showPolice': false,
		'name': 'displayName',
		'onSelect': storagePre
	});
}
//获取厂商型号
function getManufactures() {
	$.ajax({
		url: '/gmvcsomm/resource/mgr/workstation/listManufacturer.action',
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
//添加新采集工作站
function addNewWSInfo() {
	var newDepartment = $("#ws_depart").data('selOrg'); //部门
	var orgId = newDepartment.id;
	var row = parent.getSelectedRow();

	var manufacturer = $("#manufacturers").val();
	var wsName = document.getElementById("wsName");
	var storageId = $("#storageId option:selected").attr("storageId");
	var phone = document.getElementById("phone");
	var addr = $("#addr").val();
	var addWorkstationIP = $("#addWorkstationIP").val();
	var admin = document.getElementById("admin");
	
	if(!wsName.value) {
		alert("工作站名称不能为空！");
		return;
	}
	
	if(!phone.value) {
		alert("负责人联系电话不能为空！");
		return;
	}
	
	if(!addr) {
		alert("工作站地址不能为空！");
		return;
	}
	
	if(!addWorkstationIP) {
		alert("工作站IP不能为空！");
		return;
	}
	
	if(!admin.value) {
		alert("负责人名称不能为空！");
		return;
	}
	
	var params = {
		"orgId": orgId,
		"manufacturer": manufacturer,
		"wsName": wsName.value,
		"storageId": storageId,
		"admin": admin.value,
		"phone": phone.value,
		"addr": addr,
		"ip":addWorkstationIP

	}
	
	$.ajax({
		url: "/gmvcsomm/resource/mgr/workstation/add.action",
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert('失败！');
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('添加异常！');
				return;
			}
			if(ret.headers.ret == 0) {
				parent.onSearch();
				cancelDialog();
			}
		}
	});
}
//初始化上级存储
function storageInit() {
	var department = $("#ws_depart").data('selOrg'); //部门
	var orgId = department.id;
	params = {
		"orgId": orgId,
		"assign":1
	}

	$.ajax({
		url: '/gmvcsomm/resource/mgr/org/storage/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			$("#storageId").empty();
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				initTable([]);
				alert('获取设备信息失败，设备或不在线');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				var storageList = body.storageList;
				storageHTML(storageList);
			}
		}
	});
}

//上级存储
var parentId = null;
function storagePre(tree) {
	if(!parentId) {
		parentId = tree.id;
	} else {
		return;
	}
	//	var orgId = tree.id;
	params = {
		//		"orgId": orgId
		"orgId": parentId,
		"assign":1
	}

	$.ajax({
		url: '/gmvcsomm/resource/mgr/org/storage/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			$("#storageId").empty();
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				initTable([]);
				alert('获取设备信息失败，设备或不在线');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				var storageList = body.storageList;
				storageHTML(storageList);
			}
		}
	});
}

function storageHTML(storageList) {
	var html = '';
	for(var i = 0; i < storageList.length; i++) {
		var name = storageList[i].name;
		var id = storageList[i].id;
		html += '<option storageId=' + id + '>' + name + '</option>';
	}
	$("#storageId").empty();
	$("#storageId").empty().append('<option>无</option>');
	$("#storageId").append(html);
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}