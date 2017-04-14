var $table = $('.table');
var loadwnd = null;

$(function() {
	getDepartment("userDepartment"); //部门信息
	initTable(); //初始化表格

	loadwnd = $('body').gmLoading({});

	$('#btnSearch').on('click', onSearch);
	$('#edit').on('click', edit);
	$('#mangeScopeEdit').on('click', mangeScopeEdit);

	$('#pagePrev').on('click', pagePrev);
	$('#pageNext').on('click', pageNext);

	$table.on('click-row.bs.table', function(e, row, $element) {
		$('.success').removeClass('success');
		$($element).addClass('success');
		$("#edit").attr('disabled', false);
		$("#mangeScopeEdit").attr('disabled', false);
	});

});

//获取部门信息
function getDepartment(department) {
	//	部门信息 异步加载
	$.ajax({
		url: "/gmvcshomepage/system/org.action",
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: 'true',
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
			var id = '#' + department;
			$(id).gmOrgSelector({
				'chkStyle': '',
				'chkboxType': {
					"Y": "ps",
					"N": "ps"
				},
				'children': 'children',
				'name': 'text'
			}, zNodes);
		}
	});
}

//获取Table表中行信息
function getSelectedRow() {
	var index = $table.find('tr.success').data('index');
	return $table.bootstrapTable('getData')[index];
}

//组合岗位（岗位id）
//function postFormatter(value, row, index) {
//	if(!value) {
//		value = '-';
//	}
//	if(!row.postId) {
//		row.postId = '-'
//	}
//	return value + "(" + row.postId + ")";
//}
//组合角色（角色id）
//function roleFormatter(value, row, index) {
//	if(!value) {
//		value = '-';
//	}
//	if(!row.roleId) {
//		row.roleId = '-'
//	}
//	return value + "(" + row.roleId + ")";
//}
////组合警员类别（警员类别id）
//function typeFormatter(value, row, index) {
//	if(!value) {
//		value = '-';
//	}
//	if(!row.typeId) {
//		row.typeId = '-'
//	}
//	return value + "(" + row.typeId + ")";
//}
////组合业务名称（业务id）
//function businessFormatter(value, row, index) {
//	if(!value) {
//		value = '-';
//	}
//	if(!row.businessId) {
//		row.businessId = '-'
//	}
//	return value + "(" + row.businessId + ")";
//}

/**
 *处理表格排序从1开始
 * */
function runningFormatter(value, row, index) {
	return page * pageSize + index + 1;
}

function formatTime(value) {
	return new Date(parseInt(value) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}

//查询
var params = {};
var infoArray = [];
var page = 0;
var pageCount = -1;
var pageSize = 20;

function onSearch() {
	infoArray = [];
	page = 0;
	pageCount = -1;

	$('#pagePrev').attr('disabled', true);
	$('#pageNext').attr('disabled', true);

	$("#edit").attr('disabled', true);
	$("#mangeScopeEdit").attr('disabled', true);

	search();
}

function search() {

	loadwnd.show();

	var cj_department = $("#userDepartment").data('selOrg'); //部门
	var orgId = cj_department.id;
	var subOrg = document.getElementById("subOrg").checked;
	var kw = document.getElementById("kw").value;

	if(cj_department == null) {
		alert("请选择部门！");
		return;
	}

	params = {
		"orgId": orgId,
		"subOrg": subOrg,
		"kw": kw
	}

	$.ajax({
		url: '/gmvcsomm/user/mgr/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			console.log("查询失败！")
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				initTable([]);
				return;
			}
			if(ret.headers.ret == 0) {

				var total = ret.body.total;
				initpagination(total);
				var body = ret.body.users;
				if(body.length > 20) {
					$('#pageNext').attr('disabled', false);
				} else {
					pageCount = 1;
					$('#pageNext').attr('disabled', true);
				}
				var temp = [];
				for(; true;) {
					temp = body.splice(0, 20);

					infoArray.push(temp);
					pageCount++;

					if(body.length == 0) {
						break;
					}
				}

				initTable(infoArray[0]);

				//infoArray.push(body);
				//if(infoArray[infoArray.length-1].length >= 20) {
				//	$('#pageNext').attr('disabled', false);
				//} else {
				//	pageCount = params.page;
				//	$('#pageNext').attr('disabled', true);
				//}
			}
		},

		complete: function() {
			loadwnd.hide();
		}
	});
}

//初始化分页
function initpagination(total) {
	var p = (Math.floor(total / 20) + ((total % 20) ? 1 : 0));
	$("#prevPagination").text(total);
	$("#afterPagination").html('<span id="page">' + parseInt(page + 1) + '</span>/' + p);
}

function onSearchUserCode(policeId) {
	infoArray = [];
	page = 0;
	pageCount = -1;

	$('#pagePrev').attr('disabled', true);
	$('#pageNext').attr('disabled', true);

	$("#edit").attr('disabled', true);
	$("#mangeScopeEdit").attr('disabled', true);

	searchUserCode(policeId);
}

function searchUserCode(id) {
	var cj_department = $("#userDepartment").data('selOrg'); //部门
	var orgId = cj_department.id;
	var subOrg = document.getElementById("subOrg").checked;
	var kw = document.getElementById("kw").value;

	if(cj_department == null) {
		alert("请选择部门！");
		return;
	}

	params = {
		"orgId": orgId,
		"subOrg": subOrg,
		"kw": id
	}

	$.ajax({
		url: '/gmvcsomm/user/mgr/list.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			alert("失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('异常');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body.users;
				if(body) {
					initTable(body);
					initpagination(ret.body.total);
				}
			}
		}
	});
}
//初始化表格
function initTable(data) {
	//先销毁表格
	$('.table').bootstrapTable('destroy');

	$('.table').bootstrapTable({
		data: data,
		pagination: false,
		height: '600px'
	});
}

function pagePrev() {
	if(page == 0) {
		return;
	}

	if(page == 1) {
		$('#pagePrev').attr('disabled', true);
	}

	page -= 1;
	$("#page").text(parseInt(page + 1));
	initTable(infoArray[page]);

	$('#pageNext').attr('disabled', false);
	return;
}

function pageNext() {
	// 达到最大页数
	if(pageCount != -1 && page >= pageCount) {
		$('#pageNext').attr('disabled', true);
		return;
	}

	// 如果当前页未满条数，不处理
	if(infoArray[page].length < pageSize) {
		$('#pageNext').attr('disabled', true);
		return;
	}

	page++;
	$("#page").text(parseInt(page + 1));
	if(!infoArray[page + 1]) {
		$('#pageNext').attr('disabled', true);
	}
	// 如果当前页满数据条数，并且下一页未加载过数据，则请求下一页数据
	//	if(!infoArray[page]) {
	//		params.page = page;
	//
	//		search();
	//
	//		$('#pagePrev').attr('disabled', false);
	//		return;
	//	}

	// 如果下一页加载过数据
	if(infoArray[page]) {
		initTable(infoArray[page]);
		$('#pagePrev').attr('disabled', false);

		if(pageCount != -1 && page >= pageCount) {
			$('#pageNext').attr('disabled', true);
		}

		return;
	}
}

//弹窗JS代码
function edit() {
	$.gmModelDialog({
		title: "编辑",
		dragEnabled: true,
		iframeUrl: "../dialog/edit/edit.html",
		width: 550,
		height: 200
	});
}

function mangeScopeEdit() {
	$.gmModelDialog({
		title: "管理范围设置",
		dragEnabled: true,
		iframeUrl: "../dialog/mangeScopeEdit/mangeScopeEdit.html",
		width: 350,
		height: 400
	});
}

//关闭弹窗
function closeResigter() {
	$.gmModelDialog("destory");
}