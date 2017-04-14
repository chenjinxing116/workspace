$(function() {
	var row = parent.getSelectedRow();
	initStorageInfo(row);

	$("#modify").on('click', modifyStorageInfo);
	$("#close").on('click', cancelDialog);
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

//修改存储信息
function modifyStorageInfo() {
	var row = parent.getSelectedRow();
	var id = document.getElementById("id");
	var name = document.getElementById("name");
	var admin = document.getElementById("admin");
	var phone = document.getElementById("phone");
	var address = document.getElementById("address");
	var type = document.getElementById("type");
	var orgId = row.orgId;
	var orgName = document.getElementById("orgName");
	
	var params = {
		"id": id.value,
		"name": name.value,
		"admin": admin.value,
		"phone": phone.value,
		"address": address.value,
		"type": type.value,
		"orgId": orgId,
		"orgName": orgName.value

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
			
			parent.onSearch();
			cancelDialog();
			
		}
	});
}
//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}