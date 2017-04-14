$(function() {
	var row = parent.getSelectedRow();
	initStorageInfo(row);
	getDepartment("cj_department");
	$("#modify").on('click', modifyStorageInfo);
	$("#close").on('click', cancelDialog);
//	$("#orgName").on('click',parent.getDepartment("orgName"));
});

//初始化存储信息
function initStorageInfo(row){
	$("#name").attr('value',row.name);
	$("#orgName").attr('value',row.orgId);
	$("#ipAddr").attr('value',row.ip);
	$("#phone").attr('value',row.phone);
	$("#type").attr('value',row.typeDisplay);
	$("#address").attr('value',row.address);
	$("#admin").attr('value',row.admin);
	$("#id").attr('value',row.id);
	
}
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
		'name': 'displayName'
	});
}

//修改存储信息
function modifyStorageInfo() {
	var row = parent.getSelectedRow();
	var id = document.getElementById("id");
	var name = document.getElementById("name");
	var admin = document.getElementById("admin");
	var phone = document.getElementById("phone");
	var address = document.getElementById("address");
	var type = document.getElementById("type");
	
	var cj_department = $("#cj_department").data('selOrg'); //部门
	var orgId = cj_department.id;
	
	var orgName = cj_department.displayName;
	
	if(!address.value){
		alert('服务器地址不能为空');
		return;
	}
	if(!phone.value){
		alert('联系电话不能为空');
		return;
	}
	if(!admin.value){
		alert('负责人不能为空');
		return;
	}
	if(!name.value){
		alert('服务器名称不能为空');
		return;
	}
	
	var params = {
		"id": id.value,
		"name": name.value,
		"admin": admin.value,
		"phone": phone.value,
		"address": address.value,
		"type": type.value,
		"orgId": orgId,
		"orgName": orgName

	}
	$.ajax({
		url: "/gmvcsomm/resource/mgr/storage/modify.action",
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
				alert('存储异常！');
				return;
			}
			alert('修改成功');
			parent.onSearch();
			cancelDialog();
		}
	});
}
//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}