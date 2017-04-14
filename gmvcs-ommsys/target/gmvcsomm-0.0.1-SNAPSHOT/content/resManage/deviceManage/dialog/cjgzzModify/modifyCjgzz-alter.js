$(function() {
	$("#modify").on('click', modify);
	$('#close').on('click', cancelDialog);
	initModify();
	getDepartment("alter_depart");
	getManufactures();
});

//初始化修改页面
function initModify() {
	var row = parent.getSelectedRow();
	var addr = row.addr;
	var storageId = row.storageId;
	var storageName = row.storageName;
	var wsName = row.wsName;
	var admin = row.admin;
	var phoneNumber = row.phoneNumber;
	$('#storageId').attr("value", storageName);
	$('#storageId').attr("storageId", storageId);
	$('#wsName').attr("value", wsName);
	$('#admin').attr("value", admin);
	$('#phone').attr("value", phoneNumber);
	$('#addr').attr("value", addr);
	var opt = '<option value=' + row.storageId + '>' + row.superStorage + '</option>';
	$("#storageId").append(opt);
}
//4.11.9	修改采集工作站信息
function modify() {
	var manufacturer = $("#manufacturers").val();
	var wsName = $("#wsName").val();
	var storageId = $("#storageId option:selected").attr("storageId");
	var phone = $("#phone").val();
	var addr = $("#addr").val();
	var admin = $("#admin").val();
	if(!wsName) {
		alert("工作站名称不能为空！");
		return;
	}
	if(!phone) {
		alert("负责人联系电话不能为空！");
		return;
	}
	if(!addr) {
		alert("工作站地址不能为空！");
		return;
	}
	if(!admin) {
		alert("负责人名称不能为空！");
		return;
	}
	if(!storageId) {
		//alert("请选择上级存储！");
		storageId = null;
		return;
	}
	var newDepartment = $("#alter_depart").data('selOrg'); //部门
	var orgId = newDepartment.id;

	var wsAddr = $("#addr").val();
	var wsId = parent.getSelectedRow().wsId;
	var params = {
		"orgId": orgId,
		"manufacturer": manufacturer,
		"wsName": wsName,
		"storageId": storageId,
		"admin": admin,
		"phoneNumber": phone,
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

//获取部门信息
function getDepartment(department) {
	//	部门信息 异步加载
	$.ajax({
		url: "/gmvcshomepage/system/org.action",
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			var zNodes = ret;
			var id = '#' + department;
			$(id).gmOrgSelector({
				'chkStyle': '',
				'chkboxType': {
					"Y": "ps",
					"N": "ps"
				},
				'children': 'children',
				'name': 'text',
				'onSelect': storagePre
			}, zNodes);
		}
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

//上级存储
function storagePre(tree) {
	var orgId = tree.id;
	params = {
		"orgId": orgId
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
	$("#storageId").empty().append(html);
}
//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}