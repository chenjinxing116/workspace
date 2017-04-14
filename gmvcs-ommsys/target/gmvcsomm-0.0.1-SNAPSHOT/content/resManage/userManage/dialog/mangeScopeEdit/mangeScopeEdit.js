$(function() {
	$('#alter').on('click', alter);
	$('#close').on('click', cancelDialog);
	initDepartment();
});

var orgIds = [] //原始部门id列表
var addOrgIds = []; //需要新增的部门id列表
//var deleteOrgIds = []; //需要移除的部门id列表

function getOrgIds() {
	return orgIds;
}

function getAddOrgIds() {
	return addOrgIds;
}

//function getDeleteOrgIds() {
//	return deleteOrgIds;
//}
//初始化部门信息
function initDepartment() {
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
			//			if(ret.headers.ret != 0) {
			//				alert('获取信息异常！');
			//				return;
			//			}
			var zNodes = ret;

			//4.12.6	获取用户管理范围信息列表
			var userCode = parent.getSelectedRow().userCode;
			var params = {
				'userCode': userCode
			}
			$.ajax({
				url: '/gmvcsomm/user/mgr/scope/list.action',
				type: "get",
				data: params,
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
						for(var i = 0; i < body.length; ++i) {
							orgIds.push(body[i].orgId);
						}
						initTree(zNodes, orgIds);
					}
				}
			});
		}
	});
}

//初始化部门树
function initTree(data, orgIds) {
	var setting = {
		check: {
			enable: true,
			chkboxType: {
				'Y': 's',
				'N': 's'
			}
		},
		data: {
			key: {
				name: 'text',
				children: 'children'
			}
		}
//		callback: {
//			onCheck: initDelOrgIds
//		}
	};
	handleTreeData(data, orgIds);
	$.fn.zTree.init($(".ztree"), setting, data);
}

function handleTreeData(data, orgIds) {
	data.forEach(function(value) {
		for(var i=0; i<orgIds.length; i++){
			if(orgIds[i] == value.id){
				value.checked = true;
				break;
			}
		}

		if(value.children) {
			handleTreeData(value.children, orgIds);
		}
	});
}

// 找出arr2数组中不存在于arr1数组中的值 ， 返回这些值的数组
function getDifferent(arr1, arr2){
	var ret = [];
	var find = false;
	for(var i=0; i<arr2.length; i++){
		find = false;
		for(var j=0; j<arr1.length; j++){
			if(arr1[j] == arr2[i]){
				find = true;
				break;
			}
		}
		
		if(!find){
			ret.push(arr2[i]);
		}
	}
	
	return ret;
}

function alter() {
	var zTree = $.fn.zTree.getZTreeObj("ztree");
	var nodes = zTree.getCheckedNodes(true);
	nodes.forEach(function(value) {
		var orgId = value.id;
		addOrgIds.push(orgId);
	});
	var oldIds = getOrgIds();
	var newIds = getAddOrgIds();
	
	var deleteOrgIds = getDifferent(newIds, oldIds);
	var userCode = parent.getSelectedRow().userCode;
	var params = {
			'userCode': userCode,
			'addOrgIds': newIds,
			'deleteOrgIds': deleteOrgIds
		}
			$.ajax({
				url: '/gmvcsomm/user/mgr/scope/modify.action',
				type: "post",
				contentType: "application/json",
				dataType: 'json',
				data: JSON.stringify(params),
				cache: false,
				error: function() {
					alert("失败");
					newIds = [];
					deleteOrgIds = [];
					return;
				},
		
				success: function(ret) {
					if(ret.headers.ret != 0) {
						alert('‘异常’！');
						return;
					}
					if(ret.headers.ret == 0) {
//						parent.onSearchUserCode(userCode);
						alert("管理范围配置成功！")
						cancelDialog();
					}
				}
			});
}

//关闭弹出窗口
function cancelDialog() {
	parent.closeResigter();
}