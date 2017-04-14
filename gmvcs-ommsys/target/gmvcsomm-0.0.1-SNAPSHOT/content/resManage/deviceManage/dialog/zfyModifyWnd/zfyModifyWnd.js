$(function() {
	$('#alter').on('click', alter);
	$('#close').on('click', cancelDialog);
	//部门信息初始化
	initDep();
});

function initDep() {
	$.ajax({
		url: '/gmvcshomepage/system/org.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: true,
		cache: false,
		error: function() {
			alert("获取执法记录仪‘部门信息’获取失败");
			return;
		},

		success: function(ret) {
			var zNodes = ret;
			$('#zf_department').gmOrgSelector({
				'chkStyle': '',
				'chkboxType': {
					"Y": "ps",
					"N": "ps"
				},
				'children': 'children',
				'name': 'text',
				'onSelect': getUser
			}, zNodes);
		}
	});
}

function alter() {
	var zf_department = $("#zf_department").data('selOrg'); //部门
	var orgId = zf_department.id;
	var userCode = $("#userCode").val();
	//	var userCode = parent.getSelectedRow().userCode;
	var tb_deviceId = parent.getSelectedRow().deviceId;
	var params = {
		"deviceId": tb_deviceId,
		"orgId": orgId,
		"userCode": userCode
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/modify.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("执法记录仪‘修改’失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('注册新设备失败！');
				return;
			}
			if(ret.headers.ret == 0) {
				parent.onSearch();
				cancelDialog();
			}
		}
	});
}

function getUser(tree) {
	var orgId = tree.id;
	params = {
		"org_id": orgId
	}
	$.ajax({
		url: '/gmvcshomepage/system/org/user.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		async: true,
		cache: false,
		error: function() {
			alert("获取执法记录仪‘部门人员信息’获取失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取部门人员信息异常！');
				return;
			}
			var html = "";
			for(var i = 0; i < ret.body.length; ++i) {
				html += '<option value=' + ret.body[i].userCode + '>' + ret.body[i].userName + '(' + ret.body[i].userCode + ')' + '</option>';
			}
			$('#userCode').empty();
			$('#userCode').append(html);
		}
	});
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}