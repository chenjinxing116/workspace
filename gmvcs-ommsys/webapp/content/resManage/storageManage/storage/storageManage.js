var $table = $('.table');
var loadwnd = null;
$(function() {
	getDepartment("cj_department"); //部门信息
	initTable(); //初始化表格
	loadwnd = $('body').gmLoading({});

	$('#btnSearch').on('click', onSearch);
	$('#modify').on('click', modify);

	$('#assignWs').on('click', assignWs);
	$('#tacticsConfig').on('click', tacticsConfig);

	$('#pagePrev').on('click', pagePrev);
	$('#pageNext').on('click', pageNext);

	$table.on('click-row.bs.table', function(e, row, $element) {
		$('.success').removeClass('success');
		$($element).addClass('success');
		$("#modify").attr('disabled', false);
		$("#delete").attr('disabled', true);
		$("#assignWs").attr('disabled', false);
		$("#tacticsConfig").attr('disabled', false);
	});

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
		'name': 'displayName'
	});
}

//获取Table表中行信息
function getSelectedRow() {
	var index = $table.find('tr.success').data('index');
	return $table.bootstrapTable('getData')[index];
}

//组合警员（警号）
function matchUserCodeFormatter(value, row, index) {
	return value + "(" + row.userCode + ")";
}
//组合负责人（负责人电话）
function matchAdminFormatter(value, row, index) {
	return value + "(" + row.phone + ")";
}
//百分比数据组合（CPU和内存）
function matchDataFormatter(value, row, index) {
	return value + "%";
}
//转换单位为GB
function sizeToGbFormatter(value, row, index) {
	if(value) {
		return bytesToGb(value);
	} else {
		return '-';
	}
}

function bytesToGb(bytes) {
	if(bytes === 0) return '0';

	var k = 1024;

	sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

	i = Math.floor(Math.log(bytes) / Math.log(k));

	var result = (bytes / Math.pow(k, i));
	//toPrecision(3) 后面保留一位小数，如1.0GB      + ' ' + sizes[i+2]                                                                                                             //return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];  

	switch(sizes[i]) {
		case "B":
			result = result / (k*k*k);
			break;
		case "KB":
			result = result / (k*k);
			break;
		case "MB":
			result = result / k;
			break;
		case "TB":
			result = result * k;
			break;
		case "PB":
			result = result * k * k;
			break;
		case "EB":
			result = result * k * k * k;
			break;
		case "ZB":
			result = result * k * k * k * k;
			break;
		case "YB":
			result = result * k * k * k * k * k;
			break;
	}

	return result.toFixed(2);

}

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

	$("#modify").attr('disabled', true);
	$("#delete").attr('disabled', true);
	$("#assignWs").attr('disabled', true);
	$("#tacticsConfig").attr('disabled', true);

	search();
}

function search() {
	loadwnd.show();
	var cj_department = $("#cj_department").data('selOrg'); //部门
	var orgId = cj_department.id;

	if(cj_department == null) {
		alert("请选择部门！");
		return;
	}
	var assign = $("#assign").val();
//	if($("#assign").val() == 0) {
//		assign = null;
//	} else if($("#assign").val() == 1) {
//		assign = false;
//	} else {
//		assign = true;
//	}

	params = {
		"orgId": orgId,
		'assign':parseInt(assign)
	}

	$.ajax({
		url: '/gmvcsomm/resource/mgr/org/storage/list.action',
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
				initTable([]);
				alert('获取设备信息失败，设备或不在线');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				var storageList = body.storageList;
				var total = body.total;
				initTable(storageList);
			}
		},

		complete: function() {
			loadwnd.hide();
		}
	});
}
//初始化表格
function initTable(data) {
	//先销毁表格
	$('.table').bootstrapTable('destroy');

	$('.table').bootstrapTable({
		data: data,
		pageSize: 999,
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

	// 如果当前页满数据条数，并且下一页未加载过数据，则请求下一页数据
	if(!infoArray[page]) {
		params.page = page;

		search();

		$('#pagePrev').attr('disabled', false);
		return;
	}

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
function assignWs() {
	$.gmModelDialog({
		title: "分配工作站",
		dragEnabled: true,
		iframeUrl: "../dialog/assignWs/assignWs.html",
		width: 600,
		height: 400
	});
}

function modify() {
	$.gmModelDialog({
		title: "修改服务器信息",
		dragEnabled: true,
		iframeUrl: "../dialog/modifyWSInfo/modifyWSInfo.html",
		width: 700,
		height: 300
	});
}

function tacticsConfig() {
	$.gmModelDialog({
		title: "策略配置",
		dragEnabled: true,
		iframeUrl: "../dialog/tactisConfig/tactisConfig.html",
		width: 750,
		height: 485
	});
}


function mbToGb(bytes) {
	if(bytes === 0) return '0 GB';

	var k = 1024;

	sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

	i = Math.floor(Math.log(bytes) / Math.log(k));

	var result = (bytes / Math.pow(k, i));
	//toPrecision(3) 后面保留一位小数，如1.0GB      + ' ' + sizes[i+2]                                                                                                             //return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];  

	switch(sizes[i + 2]) {
		case "TB":
		result=result*k;
			break;
		case "PB":
		result=result*k*k;
			break;
		case "EB":
		result=result*k*k*k;
			break;
		case "ZB":
		result=result*k*k*k*k;
			break;
		case "YB":
		result=result*k*k*k*k*k;
			break;
	}

	return result.toFixed(2);

}

//关闭弹窗
function closeResigter() {
	$.gmModelDialog("destory");
}