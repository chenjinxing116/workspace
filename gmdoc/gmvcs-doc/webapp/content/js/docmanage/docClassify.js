/**
 * Created by kinkoo on 2016-04-05.
 */
$("document").ready(function(){
    $('input, textarea').placeholder();
	$("#btn_add_one").on("click", onBtn_addOne);
	$('.btn-two').on("click", onBtn_addTwo);
	
	$('.btn-edit').on("click", onBtn_edit);
	$('.btn-close').on("click", onBtn_close);
	$('.btn-check').on("click", onBtn_check);

    navSelect(2);
    
    $('#input_one_name').keypress(function(e){	
		var specialKey = "\\\/:*?\"< >|";
		var key;
		if(window.event){
		  	key = e.keyCode; 
		} 
		else if(e.which){ 
			key = e.which; 
		} 
  
		var realkey = String.fromCharCode(key);
		
		if(specialKey.indexOf(realkey) >= 0)
			return false;
	});
	
	$('.input-two').keypress(function(e){	
		var specialKey = "\\\/:*?\"< >|";
		var key;
		if(window.event){
		  	key = e.keyCode; 
		} 
		else if(e.which){ 
			key = e.which; 
		} 
  
		var realkey = String.fromCharCode(key);
		
		if(specialKey.indexOf(realkey) >= 0)
			return false;
	});
});

function onBtn_check()
{
	var self         = this;
	var catoname_new = $(this).siblings('.input-name').val();
	var catoname_old = $(this).siblings('.span-name')[0].value;
	var catoid       = $(this).prevAll("p:last")[0].innerText;
	
	if(catoname_new.length === 0)
		catoname_new = catoname_old;
	
	if(catoname_old == catoname_new){
		$(this).siblings('.input-name').addClass("hide");
		$(this).addClass("hide");
		$(this).siblings('.span-name').removeClass("hide");
		$(this).siblings('.btn-edit').removeClass("hide");
		return;
	}
	
	var pData = {
		"cato_id" : parseInt(catoid),
		"cato_name" : catoname_new
	};
	
	$.ajax({
        url: "edit.action",
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(pData),
        async: false,
        cache: false,
        error: function () {
            showMessage("提交请求失败, 请检测网络连接是否通畅并重试!");
        },
        success: function (result) {
        	if(result.headers.ret==0){
        		$(self).siblings('.span-name')[0].value = catoname_new;
        	}else{
        		showMessage("编辑分类失败, 请重试!");
        	}
        	
        	$(self).siblings('.input-name').addClass("hide");
			$(self).addClass("hide");
			$(self).siblings('.span-name').removeClass("hide");
			$(self).siblings('.btn-edit').removeClass("hide");
			return;
        }
    });
}

function onBtn_close()
{
	var self = this;
	
	// 当前模式
	var mode = $(this).siblings('.input-name').hasClass('hide');
	
	if(!mode){
		// 编辑模式 
		$(this).siblings('.btn-check').addClass("hide");
		$(this).siblings('.input-name').addClass("hide");
		$(this).siblings('.span-name').removeClass("hide");
		$(this).siblings('.btn-edit').removeClass("hide");
	}else{
		// 删除模式
        var catoname = $(this).prevAll("input:last")[0].value;
        var catoid   = $(this).prevAll("p:last")[0].innerText;
        confirmBox("确认删除分类："+catoname, function(){
        	var pData = {
				"cato_id" : parseInt(catoid)
			};
			
			$.ajax({
		        url: "delete.action",
		        type: "post",
		        contentType: "application/json",
		        dataType: 'json',
		        data: JSON.stringify(pData),
		        async: false,
		        cache: false,
		        error: function () {
		            showMessage("提交请求失败, 请检测网络连接是否通畅并重试!");
		        },
		        success: function (result) {
		        	if(result.headers.ret==0){
		        		// 这里删除该分类
		        		if($(self).hasClass('btn-one-close')){
							// 一级分类
							$(self).parents('.li-one').remove();
						}else{
							// 二级分类
							$(self).parent().parent().remove();
						}
					
						return;
		        	}
		        	
		        	showMessage("删除分类失败, 请重试!");
		        	return;
		        }
		    });	
        });
	}
}

function onBtn_edit()
{
	var catoname = $(this).siblings('.span-name')[0].value;
	
	$(this).addClass("hide");
	$(this).siblings('.span-name').addClass("hide");
	$(this).siblings('.btn-check').removeClass("hide");
	$(this).siblings('.input-name').removeClass("hide");
	$(this).siblings('.input-name').val(catoname);
	$(this).siblings('.input-name').focus();
	
	$(this).siblings('.input-name')[0].select();

}

function onBtn_addOne()
{
	if($("#input_one_name").val().length == 0)
	{
		$("#input_one_name").focus();
		return;
	}
	
	var pData = {
		"cato_name" : $("#input_one_name").val(),
		"parent_id" : 1
	};
	
	$.ajax({
        url: "add.action",
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(pData),
        async: false,
        cache: false,
        error: function () {
            showMessage("提交请求失败, 请检测网络连接是否通畅并重试!");
        },
        success: function (result) {
        	if(result.headers.ret==0)
        		addOneCato(result.body);
        }
    });
}

function addOneCato(map)
{
	var html = createOneLi({"cato_id" : map.catoId, "cato_name": map.catoName});
	$("#ul_one").append(html);
	
	$("#input_one_name").val("");
	
	$('.btn-two').off("click", onBtn_addTwo);
	$('.btn-two').on("click", onBtn_addTwo);
	$('.btn-edit').off("click", onBtn_edit);
	$('.btn-close').off("click", onBtn_close);
	$('.btn-check').off("click", onBtn_check);
	
	$('.btn-edit').on("click", onBtn_edit);
	$('.btn-close').on("click", onBtn_close);
	$('.btn-check').on("click", onBtn_check);

	$('.input-two').keypress(function(e){	
		var specialKey = "\\\/:*?\"< >|";
		var key;
		if(window.event){
		  	key = e.keyCode; 
		} 
		else if(e.which){ 
			key = e.which; 
		} 
  
		var realkey = String.fromCharCode(key);
		
		if(specialKey.indexOf(realkey) >= 0)
			return false;
	});
}

function onBtn_addTwo()
{
	var self  = this;
	var input = $(this).parent().prev().find("input");
	var catoName = input.val();
	if(catoName.length == 0)
	{
		$(input).focus();
		return;
	}
	
	var p = $(this).parents('.li-one').find('.p-oneInfo');
	var parentId = $(this).parents('.li-one').find('.p-oneInfo')[0].innerHTML;
	
	var pData = {
		"cato_name" : catoName,
		"parent_id" : parseInt(parentId)
	};
	
	$.ajax({
        url: "add.action",
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(pData),
        async: false,
        cache: false,
        error: function () {
            showMessage("提交请求失败, 请检测网络连接是否通畅并重试!");
        },
        success: function (result) {
        	if(result.headers.ret==0){
        		var html = createTwoLi({"cato_id": result.body.catoId, "cato_name": result.body.catoName});
				$(self).parents('.panel-body').find('.ul-two-classify').append(html);
	
				$(input).val("");
				
				$('.btn-edit').off("click", onBtn_edit);
				$('.btn-close').off("click", onBtn_close);
				$('.btn-check').off("click", onBtn_check);
				
				$('.btn-edit').on("click", onBtn_edit);
				$('.btn-close').on("click", onBtn_close);
				$('.btn-check').on("click", onBtn_check);
				return;
        	}
        	
        	showMessage("添加分类失败, 请重试!");
        }
    });		
}

function addTwoCato(map)
{
	var html = createTwoLi({"cato_id": map.cato_id, "cato_name": map.cato_name});
	$(this).parents('.panel-body').find('.ul-two-classify').append(html);
	
	$(input).val("");	
	$('.btn-edit').off("click", onBtn_edit);
	$('.btn-close').off("click", onBtn_close);
	$('.btn-check').off("click", onBtn_check);
	
	$('.btn-edit').on("click", onBtn_edit);
	$('.btn-close').on("click", onBtn_close);
	$('.btn-check').on("click", onBtn_check);
}
	
function createOneLi(map)
{
	var html = "";
	var count = $('#ul_one').children('li').length;
	if(count%2==0 && count!=0)
		html += '<div class="clearfix"></div>';
		
	html += '<li class="col-lg-6 col-xs-12 li-one" style="min-height: 200px;">';
		html += '<div class="panel panel-default">';
			html += '<div class="panel-heading">';
				html += '<h4 class="panel-title">';
				    html += '<div class="list-group-item" style="background-color: #F5F5F5;border-color: #F5F5F5;">';
				        html += '<p class="hidden p-oneInfo">'+ map.cato_id + '</p>';
				        html += '<input readOnly="true" style="font-size: 24px;background-color:#f5f5f5;display:inline; width: 420px;color: #000;margin:-10px auto -10px -4px;border:none;" type="text" class="list-group-item-heading span-name" value='+map.cato_name+'>';
				        /*html += '<span style="font-size: 24px;width:300px;" class="span-name"><strong>' + map.cato_name + '</strong></span>';*/
				        html += '<button class="btn btn-danger badge btn-close btn-one-close"><i class="fa fa-times"></i></button>';
				        html += '<button class="btn btn-danger badge btn-check hide"><i class="fa fa-check"></i></button>';
				        html += '<button class="btn btn-danger badge btn-edit"><i class="fa fa-pencil"></i></button>';
				        html += '<input maxlength="16" style="width: 300px;color: #000;margin-top:-11px;margin-bottom:-9px;" type="text" class="list-group-item-heading form-control input-name hide input-lg" placeholder="请输入名称">';
				    html += '</div>';
				html += '</h4>';
			html += '</div>';
			html += '<div id="collapseOne" class="panel-collapse collapse in">';
				html += '<div class="panel-body">';
					html += '<ul class="list-inline ul-two-classify"></ul>';        	
				    html += '<div class="clearfix"></div>';
				    html += '<div style="margin-top:5px;">';
				    	html += '<div class="row">';
				    		html += '<div class="col-xs-10">';
				    			html += '<input maxlength="16" type="text" class="form-control input-two" placeholder="请输入分类名称"/>';
				    		html += '</div>';
				    		html += '<div class="col-xs-2">';
				    			html += '<button class="btn btn-default btn-two"><i class="fa fa-plus"></i></button>';
				    		html += '</div>';
				    	html += '</div>';
				    html += '</div>';
			    html += '</div>';
			html += '</div>';
		html += '</div>';
	html += '</li>';
	
	return html;
}

function createTwoLi(map)
{
	var html = "";
	html += '<li class="col-lg-6">';
		html += '<div class="list-group-item">';
			html += '<p class="hidden p-twoInfo">' + map.cato_id + '</p>';
	        //html += '<span class="span-name" style="width:150px;">' + map.cato_name + '</span>';
	        html += '<input readOnly="true" style="display:inline; width: 170px;color: #000;margin:-5px;border:none;" type="text" class="list-group-item-heading span-name" value='+ map.cato_name +'>';
	        html += '<input style="display:inline; width: 170px;color: #000;margin:-10px auto -10px -12px;" type="text" class="list-group-item-heading form-control input-name hide" placeholder="请输入名称">';
	        html += '<button class="btn btn-danger badge btn-close"><i class="fa fa-times"></i></button>';
	        html += '<button class="btn btn-danger badge btn-check hide"><i class="fa fa-check"></i></button>';
	        html += '<button class="btn btn-danger badge btn-edit"><i class="fa fa-pencil"></i></button>';
		html += '</div>';
	html += '</li>';
	
	return html;
}

function onAddOneCla()
{
	console.log($("#input_add_cla").val());
}

// 添加分类
function addClassifyWnd(confirmFun) 
{
	dialog({
			title : "添加一级分类",
			content : "<input id='input_add_cla' type='text' class='form-control'/>",
			button : [ {
				value : '添加',
				autofocus : true,
				callback : confirmFun
			}, {
				value : '取消',
				callback : function(){
					return true;
				}
			} ]
	}).showModal();
}

function navSelect(index)
{
    $("#nar_mian").children().removeClass("nav-current");
    $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

function L(msg)
{
    console.log(msg);
}

function showMessage(str)
{
	 var d = dialog({
                title: '提示',
                content: str,
                cancelValue: '确定',
                cancel: function () {
                }
            });

    d.showModal();
}
