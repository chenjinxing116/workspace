$(function() {
	getSJLx(); //获取数据类型
	getBZ(); //获取标注类型
	initInfo();
	$("#type-add").on('click', addYWType);

});
var dataBZ = null;
//4.4.1	获取标注类型列表
function getBZ(html) {
	$.ajax({
		url: '/gmvcsmedia/va/mgr/label/listTypes.action',
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('获取标注类型列表‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				dataBZ = body;
				return;
			}
		}
	});
}
var SJLXList = null;
//4.13.5	获取数据类型列表
function getSJLx() {
	$.ajax({
		url: "/gmvcsomm/bs/mgr/data/type/datalist.action",
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: 'true',
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			SJLXList = ret.body;
			return;
		}
	});
}

//4.12.8	查询业务岗位字典信息
function initInfo() {
	$.ajax({
		url: "/gmvcsomm/bs/mgr/dict/list.action",
		type: "get",
		contentType: "application/json",
		dataType: 'json',
		async: 'true',
		cache: false,
		error: function() {
			return;
		},

		success: function(ret) {
			var body = ret.body;
			for(var i = 0; i < body.length; ++i) {
				initPage(body[i]);
			}
			//			for(var i = body.length - 1; i > -1; i--) {
			//				initPage(body[i]);
			//			}
			return;
		}
	});
}

function initPage(data) {
	dataBZ
	if(!SJLXList){
		return;
	}
	if(!dataBZ){
		return;
	}
	var code = data.code;
	var id = data.id;
	var name = data.name;
	var status = data.status;
	//	业务类型的状态名字 停用or启用
	var statusName = '';
	if(status == '0') {
		statusName = '停用';
	} else if(status == '1') {
		statusName = '启用';
	}

	var dataList = data.dataList; //数据类型 数组

	var labelList = data.labelList; //标注类型 数组

	var postList = data.postList; //岗位类型 数组

	var modelHTML = '<div class="model">';

	modelHTML += '<div class="model-content">';
	//	业务类型 左
	modelHTML += '<div class="ywlb-left">';
	modelHTML += '<div class="jtgl-tool">';
	modelHTML += '<label style="line-height: 31px;" id=' + id + ' code=' + code + '>';
	modelHTML += name;
	modelHTML += '</label>';
	modelHTML += '<div class="buttion-div">';
	modelHTML += '<button class="btn btn-success" onclick="stopOrStartYWType(this)">' + statusName + '</button>';
	modelHTML += '<button class="btn gw-type" id="" onclick="deleteType(this)">删除</button>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	//	相关设置 右初始化
	modelHTML += '<div class="xgsz-right">';
	modelHTML += '<div class="lx">';
	modelHTML += '<div class="type-label"><label>数据类型：</label></div>';
	modelHTML += '<div class="sjlx-checkbox">';
	if(dataList) {
		for(var z = 0; z < SJLXList.length; z++) { //遍历全部的数据类型列表
			var found = false;
			for(var x = 0; x < dataList.length; x++) {//判断返回的数据类型 是否存在于 数据类型列表
				if(dataList[x].dataCode == SJLXList[z].code) {
					found = true;
					break;
				}
			}
			var ckBox = found ? 'checked' : '';
			modelHTML += '<div class="div-checkbox">';
			modelHTML += '<input type="checkbox" ' + 'code=' + SJLXList[z].code + ' onclick=changeBsType(this) ' + ckBox + ' />' + SJLXList[z].name;
			modelHTML += '</div>';
		}
	} else {
		for(var z = 0; z < SJLXList.length; z++) {
			modelHTML += '<div class="div-checkbox">';
			modelHTML += '<input type="checkbox" ' + 'code=' + SJLXList[z].code + ' onclick=changeBsType(this)' + ' />' + SJLXList[z].name;
			modelHTML += '</div>';
		}
	}
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '<div class="lx">';
	modelHTML += '<div class="type-label"><label>标注类型：</label></div>';
	modelHTML += '<div class="bzlx-checkbox">';
	
	if(labelList) {
		for(var z = 0; z < dataBZ.length; z++) {//第一次遍历全部标注类型
			var found = false;
			for(var x = 0; x < labelList.length; x++) {//判断返回的标注类型 是否存在于 全部标注类型
				if(labelList[x].code == dataBZ[z].code) {
					found = true;
					break;
				}
			}
			var bqBox = found ? 'checked' : '';
			modelHTML += '<div class="div-checkbox">';
			modelHTML += '<input type="checkbox" ' + 'code=' + dataBZ[z].code + ' onclick=changeBsType(this) ' + bqBox + ' />' + dataBZ[z].name;
			modelHTML += '</div>';
		}
	} else {
		for(var z = 0; z < dataBZ.length; z++) {
			modelHTML += '<div class="div-checkbox">';
			modelHTML += '<input type="checkbox" ' + 'code=' + dataBZ[z].code + ' onclick=changeBsType(this)' + ' />' + dataBZ[z].name;
			modelHTML += '</div>';
		}
	}
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '<div class="lx">';
	modelHTML += '<div class="gw-label"><label>岗位类型：</label></div>';
	modelHTML += '<div class="gangwei">';
	modelHTML += '<div class="gangwei-content">';
	if(postList) {
		for(var i = 0; i < postList.length; ++i) {
			var code = postList[i].code;
			var id = postList[i].id;
			var name = postList[i].name;
			var code = postList[i].code;
			//			var status = postList[i].status;
			modelHTML += '<div class="gwlx-tool">';
			modelHTML += '<label class="div-gangwei">';
			modelHTML += name;
			modelHTML += '</label>';
			modelHTML += '<div class="buttion-div">';
			modelHTML += '<button class="btn gw-type" code=';
			modelHTML += code;
			modelHTML += ' gwId=';
			modelHTML += id;
			modelHTML += ' onclick="removeJob(this)">删除</button>';
			modelHTML += '</div>';
			modelHTML += '</div>';
		}
	}
	modelHTML += '</div>';
	modelHTML += '<div class="gangwei-buttonAdd">';
	modelHTML += '<input type="text" class="form-control type-input" id="jobAdd" maxlength="16"/>';
	modelHTML += '<button class="btn addJobButtion" onclick="addJobYWType(this)">添加岗位</button>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';

	$(".model-title").after(modelHTML);
}
//4.12.9	添加业务类别
function addYWType() {
	var ywlxName = $("#typeAdd").val();
	var typeName = ywlxName;
	if(!typeName) {
		typeName = '';
		return;
	}
	var params = {
		'typeName': typeName
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/dict/add.action',
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
				alert('‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				addType(ywlxName);
				return;
			}
		}
	});
}
//添加类别HTML
function addType(ywlxName) {
	if(!ywlxName) {
		return;
	}

	var modelHTML = '<div class="model">';

	modelHTML += '<div class="model-content">';
	//	业务类型 左
	modelHTML += '<div class="ywlb-left">';
	modelHTML += '<div class="jtgl-tool">';
	modelHTML += '<label style="line-height: 31px;" >';
	modelHTML += ywlxName;
	modelHTML += '</label>';
	modelHTML += '<div class="buttion-div">';
	modelHTML += '<button class="btn btn-success" onclick="stopOrStartYWType(this)">启用</button>';
	modelHTML += '<button class="btn gw-type" id="" onclick="deleteType(this)">删除</button>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	//	相关设置 右初始化
	modelHTML += '<div class="xgsz-right">';
	modelHTML += '<div class="lx">';
	modelHTML += '<div class="type-label"><label>数据类型：</label></div>';
	modelHTML += '<div class="sjlx-checkbox">';
	if(SJLXList) {
		for(var z = 0; z < SJLXList.length; z++) {
			modelHTML += '<div class="div-checkbox">';
			modelHTML += '<input type="checkbox" ' + 'code=' + SJLXList[z].code + ' onclick=changeBsType(this)' + ' />' + SJLXList[z].name;
			modelHTML += '</div>';
		}
	}
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '<div class="lx">';
	modelHTML += '<div class="type-label"><label>标注类型：</label></div>';
	modelHTML += '<div class="bzlx-checkbox">';
	if(dataBZ) {
		for(var z = 0; z < dataBZ.length; z++) {
			modelHTML += '<div class="div-checkbox">';
			modelHTML += '<input type="checkbox" ' + 'code=' + dataBZ[z].code + ' onclick=changeBsType(this)' + ' />' + dataBZ[z].name;
			modelHTML += '</div>';
		}
	}
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '<div class="lx">';
	modelHTML += '<div class="gw-label"><label>岗位类型：</label></div>';
	modelHTML += '<div class="gangwei">';
	modelHTML += '<div class="gangwei-content">';

	modelHTML += '</div>';
	modelHTML += '<div class="gangwei-buttonAdd">';
	modelHTML += '<input type="text" class="form-control type-input" id="jobAdd" />';
	modelHTML += '<button class="btn addJobButtion" onclick="addJobYWType(this)">添加岗位</button>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';
	modelHTML += '</div>';

	$(".model-title").after(modelHTML);
	$(".model").remove();
	initInfo();
}

//4.12.10	删除业务类别
function deleteType(obj) {
	var id = $(obj).closest('.jtgl-tool').find('label').attr('id');
	if(!id) {
		return;
	}
	var params = {
		'id': id
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/dict/remove.action',
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
				alert('‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				$(obj).closest('.model').remove();
			}
		}
	});
}

//4.12.11	停用/启用 业务类别
function stopOrStartYWType(obj) {
	var id = $(obj).closest('.jtgl-tool').find('label').attr('id');
	var enable;
	if($(obj).html() == '启用') {
		enable = true;
	} else if($(obj).html() == '停用') {
		enable = false;
	}

	if(!id) {
		return;
	}
	var params = {
		'id': id,
		'enable': enable
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/dict/changeStatus.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				if(enable) {
					$(obj).html("停用");
				} else {
					$(obj).html("启用");
				}

			}
		}
	});
}
//4.12.12	添加岗位
function addJobYWType(btnJob) {
	var postName = $(btnJob).prev().val();
	var bsId = $(btnJob).closest('.xgsz-right').prev().find('label').attr('id');
	var params = {
		'postName': postName,
		'bsId': bsId
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/dict/post/add.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				addJob(btnJob);
			}
		}
	});
}
//添加岗位HTML
function addJob(btnJob) {
	var thisObj = btnJob
	var jobAdd = $(thisObj).prev().val();
	if(!jobAdd) {
		return;
	}
	var html = '<div class="gwlx-tool">';
	html += '<label class="div-gangwei">';
	html += jobAdd;
	html += '</label>';
	html += '<div class="buttion-div">';
	html += '<button class="btn gw-type" onclick="removeJob(this)">删除</button>';
	html += '</div>';
	html += '</div>';
	$(thisObj).parent().prev().append(html);
}
//4.12.13	移除岗位
function removeJob(obj) {
	var postName = $(obj).attr('gwId');
	var bsId = $(obj).closest('.xgsz-right').prev().find('label').attr('id');
	var params = {
		'postId': postName,
		'bsId': bsId
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/dict/post/remove.action',
		type: "post",
		contentType: "application/json",
		dataType: 'json',
		data: JSON.stringify(params),
		cache: false,
		error: function() {
			alert("失败");
			return;
		},

		success: function(ret) {
			if(ret.headers.ret != 0) {
				alert('‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				$(obj).closest('.gwlx-tool').remove();
			}
		}
	});
}

//4.12.4	获取业务类别下的数据类型列表
function getBusTypeDataList(id, html) {
	var params = {
		"id": id
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/data/type/list.action',
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
				alert('获取业务类别下的数据类型列表‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				var body = ret.body;
				for(var i = 0; i < body.length; i++) {
					html += '<div class="div-checkbox">';
					html += '<input type="checkbox" id=' + body[i].id + " code=" + body[i].code + '/>' + body[i].name;
					html += '</div>';
				}
			}
		}
	});
}

//4.13.13	修改业务类别
function changeBsType(obj) {
	if(!obj) {
		return;
	}
	var bsCode = $(obj).closest('.xgsz-right').prev().find('label').attr('code');
	var code = $(obj).attr('code');
	var labelCode = null;
	var dataCode = null;
	var type = typeof code.valueOf();
	for(var l = 0; l < SJLXList.length; ++l) {
		if(SJLXList[l].code == code) {
			dataCode = code;
			labelCode = '';
			break;
		}
	}
	for(var bz = 0; bz < dataBZ.length; ++bz) {
		if(dataBZ[bz].code == code) {
			labelCode = code;
			dataCode = '';
			break;
		}
	}
	var params = {
		'bsCode': bsCode,
		'labelCode': labelCode,
		'dataCode': dataCode
	}
	$.ajax({
		url: '/gmvcsomm/bs/mgr/dict/changeBsType.action',
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
				alert('‘异常’！');
				return;
			}
			if(ret.headers.ret == 0) {
				return;
			}
		}
	});
}