var loadwnd = null;

function getI18NStr(key) {
	return parent.getI18NStr(key);
}

$(function() {

	$('[data-i18n]').each(function() {
		var $this = $(this);
		$this.append(getI18NStr($this.attr('data-i18n')));
	});

	$('#bPageInfo').html(getI18NStr('html.pageInfo1') + ' <span id="prevPagination">0</span> ' + getI18NStr('html.pageInfo2'));

	$('#timeStart').val(new Date().Format("yyyy-MM-dd"));
	$('#timeEnd').val(new Date().Format("yyyy-MM-dd"));

	//部门信息异步加载
	getDepartment("zf_department");
	initTime();
	initTable();

	loadwnd = $('body').gmLoading({});

	$('#selectSJFW').change(function() {
		if($(this).val() == "3") {
			$('.time-area').removeClass('hidden');
			initTime();
		} else {
			$('.time-area').addClass('hidden');
		}
	});

	$('#btnSearch').on('click', onSearch);
	$('#pagePrev').on('click', pagePrev);
	$('#pageNext').on('click', pageNext);
	$('#btnModify').on('click', onBtnModifyClick);
	$('#register').on('click', register);

	$('.table').on('click-row.bs.table', function(e, row, $element) {
		$element.addClass('success').siblings().removeClass('success');
		$("#btnModify").attr('disabled', false);
	});

});

function initTime() {
	//时间范围
	$(".form_datetime").datetimepicker({
		format: "yyyy-mm-dd", // hh:ii:ss
		language: "zh-CN",
		autoclose: true,
		todayBtn: 'linked',
		todayHighlight: true,
		pickerPosition: 'bottom-left',
		minView: 'month'
	});
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

//获取Table表中行信息
function getSelectedRow() {
	var $table = $('.table');
	var index = $table.find('tr.success').data('index');
	return $table.bootstrapTable('getData')[index];
}

//组合警员（警号）
function matchUserCodeFormatter(value, row, index) {
	return value + "(" + row.userCode + ")";
}
/**
 *处理表格排序从1开始
 * */
function runningFormatter(value, row, index) {
	return page * pageSize + index + 1;
}

//查询
var params = {};
var infoArray = [];
var page = 0;
var pageCount = -1;
var pageSize = 20;

function onSearch() {
	params = {};
	infoArray = [];
	page = 0;
	pageCount = -1;
	$("#btnModify").attr('disabled', true);

	var orgId = $("#zf_department").data('selOrg').id;
	var status = $("#selectSBZT").val(); //设备状态
	var userName = $("#inputPoliceName").val(); //配发警员名称
	var userCode = $("#inputPoliceCode").val(); //配发警员编号
	var deviceId = $("#inputDeviceId").val(); //设备编号
	var dsjType = $("#selectZFYLX").val(); //执法记录仪类型
	var simCode = $("#simCode").val(); //新增sim卡
	var domain = $('#inputDomain').val(); //区域id
	if(simCode == '') {
		simCode = null;
	}
	if(domain == '') {
		domain = null;
	}

	var beginTime = null;
	var endTime = null;
	switch($('#selectSJFW').val()) {
		case "1":
			var now = new Date();
			endTime = Math.floor(now.getTime());
			beginTime = Math.floor(new Date(now.getTime() - 7 * 24 * 3600 * 1000).getTime());
			break;
		case "2":
			var now = new Date();
			endTime = Math.floor(now.getTime());
			beginTime = Math.floor(now.setMonth(now.getMonth() - 1));
			break;
		case "3":
			var timeS = $('#timeStart').val() + ' 00:00:00';
			var timeE = $('#timeEnd').val() + ' 23:59:59';
			beginTime = getTimeByDateStr(timeS);
			endTime = getTimeByDateStr(timeE);
			break;
	}

	params = {
		"orgId": orgId,
		"beginTime": beginTime,
		"endTime": endTime,
		"status": status,
		"dsjType": dsjType,
		"simCode": simCode,
		"domain": domain,
		"page": page,
		"pageSize": pageSize
	}

	if(deviceId) {
		params.deviceId = deviceId;
	}

	if(userName) {
		params.userName = userName;
	}

	if(userCode) {
		params.userCode = userCode;
	}

	$('#pagePrev').attr('disabled', true);
	$('#pageNext').attr('disabled', true);

	$("#modify").attr('disabled', true);

	search();
}
var total = null;

function search() {

	loadwnd.show();

	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/list.action',
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
				alert(getI18NStr('alert.getInfoFail'));
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				if(!body){initTable([]);}
				if(body) {
					infoArray.push(body.dsjs);
					var dsjs = body.dsjs;
					if(body.total) {
						total = body.total;
					}
					initTable(dsjs);
					initpagination(total);
					if(dsjs.length >= pageSize) {
						var p = (Math.floor(total / 20) + ((total % 20) ? 1 : 0));
						if(page + 1 <= p) {
							$('#pageNext').attr('disabled', false);
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
	$("#btnModify").attr('disabled', true);
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
	$("#btnModify").attr('disabled', true);

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

//弹窗的JS代码块
function onBtnModifyClick() {
	$.gmModelDialog({
		title: getI18NStr('msg.modifyInof'),
		dragEnabled: true,
		iframeUrl: "../dialog/zfyModifyWnd/zfyModifyWnd.html",
		width: 460,
		height: 225
	});
}

function register() {
	$.gmModelDialog({
		title: getI18NStr('msg.registerNewDevice'),
		dragEnabled: true,
		iframeUrl: "../dialog/register/register.html",
		width: 836,
		height: 360
	});
}

function closeResigter() {
	$.gmModelDialog("destory");
}
//4.11.5	执法仪远程开机
function startUp(id) {
	var deviceId = id;
	var params = {
		'deviceId': deviceId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/powerOn.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			alert("命令执行失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('命令执行失败');
				return;
			}
			if(ret.headers.ret == 0) {
				alert("执法仪远程开机成功");
			}
		}
	});
}
//4.11.6 执法仪远程关机
function powerOff(id) {
	var deviceId = id;
	var params = {
		'deviceId': deviceId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/powerOff.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			alert("命令执行失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('命令执行失败');
				return;
			}
			if(ret.headers.ret == 0) {
				alert("执法仪远程关机成功");
			}
		}
	});
}
//4.11.7	执法仪远程锁定
function lock(id) {
	var deviceId = id;
	var params = {
		'deviceId': deviceId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/deviceLock.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			alert("命令执行失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('命令执行失败');
				return;
			}
			if(ret.headers.ret == 0) {
				alert("执法仪远程锁定成功");
			}
		}
	});
}
//4.11.8	执法仪远程解锁
function deblock(id) {
	var deviceId = id;
	var params = {
		'deviceId': deviceId
	}
	$.ajax({
		url: '/gmvcsomm/resource/mgr/dsjmgr/deviceUnLock.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		data: params,
		cache: false,
		error: function() {
			alert("命令执行失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('命令执行失败');
				return;
			}
			if(ret.headers.ret == 0) {
				alert("执法仪远程解锁成功");
			}
		}
	});
}
//操作 开机 关机 锁定 解锁
function operator(value, row, index) {
	var deviceId = row.deviceId;
	var tdHTML = '<button class="btn btn-warning startUp" onclick="startUp(\'' + deviceId + '\')">开机</button>';
	tdHTML += '<button class="btn btn-warning powerOff" onclick="powerOff(\'' + deviceId + '\')">关机</button>';
	tdHTML += '<button class="btn btn-warning lock" onclick="lock(\'' + deviceId + '\')">锁定</button>';
	tdHTML += '<button class="btn btn-warning unlock" onclick="deblock(\'' + deviceId + '\')">解锁</button>';
	return tdHTML;
}