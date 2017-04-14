var $table = $('.table');
var loadwnd = null;
$(function() {
	getDepartment("cj_department"); //部门信息
	initTable(); //初始化表格
	getManufactures(); //初始化厂商信息
	loadwnd = $('body').gmLoading({});

	$('#btnSearch').on('click', onSearch);
	$('#modify').on('click', modify);
	$('#add').on('click', addCjgzz);

	$('#tacticsConfig').on('click', tacticsConfig);
	$('#securityConfig').on('click', securityConfig);

	$('#pagePrev').on('click', pagePrev);
	$('#pageNext').on('click', pageNext);

	$table.on('click-row.bs.table', function(e, row, $element) {
		$('.success').removeClass('success');
		$($element).addClass('success');
		$("#modify").attr('disabled', false);
		$("#tacticsConfig").attr('disabled', false);
		$("#securityConfig").attr('disabled', false);
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

//组合负责人（负责人电话）
function matchAdminFormatter(value, row, index) {
	if(value) {
		return value + "(" + row.phoneNumber + ")";
	} else {
		return '-';
	}
}
//百分比数据组合（CPU和内存）
//function matchDataFormatter(value, row, index) {
//	if(value) {
//		return value + "%";
//	} else {
//		return '-';
//	}
//
//}
var manufactureObject = null;
//获取厂商型号
function getManufactures() {
	manufactureObject = [];
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
			if(ret.headers.ret === 0) {
				manufactureObject = ret.body;
			}
		}
	});
}

function workState(value, row, index) {
	if(value == 1) {
		return '在线';
	} else if(value == 2) {
		return '离线';
	} else {
		return '-';
	}
}
//组合厂商和厂商Code
function matchManufacturerFormatter(value, row, index) {
	for(var i = 0; i < manufactureObject.length; i++) {
		if(manufactureObject[i].code == value) {
			return manufactureObject[i].name;
		} else if(!value) {
			return '-';
		}
	}
	//	if(value) {
	//		return value + "(" + row.manufacturer + ")";
	//	} else {
	//		return '-';
	//	}
}
//转换单位为GB
//function sizeToGbFormatter(value, row, index) {
//	if(value) {
//		return mbToGb(value);
//	} else {
//		return '-';
//	}
//}
////转换单位为GB
//function useSizeToGbFormatter(value, row, index) {
//	if(value) {
//		return mbToGb(value);
//	} else {
//		return '-';
//	}
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

	$("#modify").attr('disabled', true);
	$("#tacticsConfig").attr('disabled', true);
	$("#securityConfig").attr('disabled', true);

	search();
}
var total = null;

function search() {
	var cj_department = $("#cj_department").data('selOrg'); //部门
	var orgId = cj_department.id;

	if(cj_department == null) {
		alert("请选择部门！");
		return;
	}

	params = {
		"orgId": orgId,
		"page": page,
		"pageSize": pageSize
	}

	loadwnd.show();

	$.ajax({
		url: '/gmvcsomm/resource/mgr/workstation/list.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("查询信息失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('查询信息失败');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				if(!body){initTable([]);}
				if(body) {
					infoArray.push(body.wss);
					initTable(body.wss);
					if(body.total) {
						total = body.total;
					}
					initpagination(body.total);

					if(ret.body.wss.length >= pageSize) {
						var p = (Math.floor(total / 20) + ((total % 20) ? 1 : 0));
						if(page + 1 < p) {
							$('#pageNext').attr('disabled', false);
						} else if(page + 1 == p) {
							$('#pageNext').attr('disabled', true);
						}
					} else {
						pageCount = params.page;
						$('#pageNext').attr('disabled', true);
					}
				}
			}
		},

		complete: function() {
			loadwnd.hide();
		}
	});
}

//初始化分页
function initpagination(total) {
	if(total == 0) {
		$("#prevPagination").text(0);
		$("#afterPagination").html('<span id="page"></span>');
	}
	if(total) {
		var p = (Math.floor(total / 20) + ((total % 20) ? 1 : 0));
		$("#prevPagination").text(total);
		$("#afterPagination").html('<span id="page">' + parseInt(page + 1) + '</span>/' + p);
	}
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
function addCjgzz() {
	$.gmModelDialog({
		title: "添加新采集工作站",
		dragEnabled: true,
		iframeUrl: "../dialog/cjgzzWnd/addWSInfo.html",
		width: 408,
		height: 475
	});
}

function modify() {
	$.gmModelDialog({
		title: "修改新采集工作站",
		dragEnabled: true,
		iframeUrl: "../dialog/cjgzzModify/modifyCjgzz-alter.html",
		width: 408,
		height: 475
	});
}

function securityConfig() {
	$.gmModelDialog({
		title: "安全配置",
		dragEnabled: true,
		iframeUrl: "../dialog/securityConfig.html",
		width: 460,
		height: 410
	});
}

function tacticsConfig() {
	$.gmModelDialog({
		title: "策略配置",
		dragEnabled: true,
		iframeUrl: "../dialog/tacticsConfig.html",
		width: 450,
		height: 210
	});
}
//关闭弹窗
function closeResigter() {
	$.gmModelDialog("destory");
}

function mbToGb(bytes) {
	if(bytes === 0) return '0';

	var k = 1024;

	sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

	i = Math.floor(Math.log(bytes) / Math.log(k));

	var result = (bytes / Math.pow(k, i));
	//toPrecision(3) 后面保留一位小数，如1.0GB      + ' ' + sizes[i+2]                                                                                                             //return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];  

	switch(sizes[i + 2]) {
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