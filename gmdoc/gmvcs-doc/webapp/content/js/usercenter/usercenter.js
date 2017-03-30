/**
 * Created by ThinkPad on 2016-04-04.
 */
var contentpath = "";

var onePageCount = 10;
var curPage      = 0;

var read_curPage = 0;

$("document").ready(function(){
	navSelect(3);
    getCollect(0, onePageCount);
    getRead(0, onePageCount);
    
	$('input, textarea').placeholder();
    $('#pre-page').on("click", prePage);
    $('#next-page').on("click", nextPage);
    $('#read-pre-page').on("click", readprePage);
    $('#read-next-page').on("click", readnextPage);
    $('#empty-read').on("click", emptyRead); 
    $('#btn-modifyPwd').on("click", onChangePwd);
});

function delCollect()
{
    var docID = $(this).children('input').val();
	if(docID==null || docID == "")
        return;

	var pData = {
	        "doc_id": parseInt(docID)
	    };

    $.ajax({
        url : "../user/collected/cancel.action",
        type : "post",
        contentType: "application/json",
        dataType : 'json',
        data : JSON.stringify(pData),
        async : false,
        cache : false,
        error : function(msg) {
        	showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success : function(msg)
        {
            getCollect(curPage, onePageCount);
        }
    });
}

function prePage()
{
	if($('#pre-page').hasClass("disabled"))
		return;
		
    if(curPage-1<0)
        return;

    if(curPage-1 == 0)
        $("#pre-page").addClass("disabled");

    getCollect(curPage-1, onePageCount);

    curPage--;
}

function nextPage()
{
	if($('#next-page').hasClass("disabled"))
		return;
		
    getCollect(curPage+1, onePageCount);
    $("#pre-page").removeClass("disabled");
    curPage++;
}

function setContentPath(path)
{
	contentpath = path;
}

function navSelect(index)
{
    $("#nar_mian").children().removeClass("nav-current");
    $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

function DoctypeIcon(doc_type)
{
	switch(doc_type)
	{
		case "doc":
		case "docx":
			return '<i style="color:#0068b7;" class="fa fa-file-word-o"></i>';
			
		case "xls":
		case "xlsx":
			return '<i style="color:#22ac38;" class="fa fa-file-excel-o"></i>';
			
		case "pdf":
			return '<i style="color:#cd0a0a;" class="fa fa-file-pdf-o"></i>';
			
		case "ppt":
		case "pptx":
			return '<i style="color:#eb5c06;" class="fa fa-file-powerpoint-o"></i>';
		
		case "txt":
			return '<i class="fa fa-file-text-o"></i>';
			
		default:
			return '<i class="fa fa-file-o"></i>';
		
	}
}

function getCollect(page, pageSize)
{
    var pData = {
        "page": page,
        "pageSize": pageSize
    };

    $.ajax({
        url : "../user/collected.action",
        type : "get",
        contentType: "application/json",
        data : pData,
        async : false,
        cache : false,
        error : function(msg) {
        	showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success : function(msg)
        {
        	if(!msg.body){
        		return;
        	}
        	
            updateCollect(msg.body);
            
            if(msg.body.length < onePageCount)
            	$("#next-page").addClass('disabled');
            else
            	$("#next-page").removeClass('disabled');
        }
    });
}

function updateCollect(data)
{
    $("#tbody-collect").empty();
    for(var i=0; i<onePageCount; ++i){
        var html = "";
        if(data[i]){
            html += '<tr>';
            html += '<td><a class="a_doc" docId='+data[i].doc_id+' readabled='+data[i].readable+' href="javascript:void(0);">' + DoctypeIcon(data[i].doc_type) + "&nbsp;" + data[i].doc_title + '</a></td>';
            html += '<td class="text-center" style="width: 150px;">' + data[i].cole_time + '</td>';
            html += '<td class="text-center"><a class="btn btn-link del-col"><input type="hidden" value="'+data[i].doc_id+'"></input>删除</a></td>';
            html += '</tr>';
        }
        else{
            html += '<tr>';
            html += '<td>&nbsp;</td>';
            html += '<td>&nbsp;</td>';
            html += '<td>&nbsp;</td>';
            html += '</tr>';
        }

        $("#tbody-collect").append(html);
    }

    $('.a_doc').off("click", onReadFile);
    $('.a_doc').on("click", onReadFile);
    $('.del-col').off("click", delCollect);
    $('.del-col').on("click", delCollect);
}

//- start-----阅读记录----------------- 
function readprePage()
{
	if($('#read-pre-page').hasClass("disabled"))
		return;
		
    if(read_curPage-1<0)
        return;

    if(read_curPage-1 == 0)
        $("#read-pre-page").addClass("disabled");

    getRead(read_curPage-1, onePageCount);

    read_curPage--;
}

function readnextPage()
{
	if($('#read-next-page').hasClass("disabled"))
		return;
		
    getRead(read_curPage+1, onePageCount);
    $("#read-pre-page").removeClass("disabled");
    read_curPage++;
}

// 获取阅读记录 
function getRead(page, pageSize)
{
    var pData = {
        "page": page,
        "pageSize": pageSize
    };

    $.ajax({
        url : "../user/read.action",
        type : "get",
        contentType: "application/json",
        data : pData,
        async : false,
        cache : false,
        error : function(msg) {
        	showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success : function(msg)
        {
        	if(!msg.body){
        		return;
        	}
        	
        	updateRead(msg.body);
        	
        	if(msg.body.length < onePageCount)
            	$("#read-next-page").addClass('disabled');
            else
            	$("#read-next-page").removeClass('disabled');
        }
    });
}

// 更新界面
function updateRead(data)
{
    $("#tbody-read").empty();
    for(var i=0; i<onePageCount; ++i){
        var html = "";
        if(data[i]){
            html += '<tr>';
            html += '<td><a class="a_doc" docId='+data[i].doc_id+' readabled='+data[i].readable+' href="javascript:void(0);">' + DoctypeIcon(data[i].doc_type) + "&nbsp;" + data[i].doc_title + '</a></td>';
            html += '<td class="text-center" style="width: 150px;">' + data[i].read_time + '</td>';
            html += '<td class="text-center"><a class="btn btn-link"><input type="hidden" value="'+data[i].doc_id+'"></input>删除</a></td>';
            html += '</tr>';
        }
        else{
            html += '<tr>';
            html += '<td>&nbsp;</td>';
            html += '<td>&nbsp;</td>';
            html += '<td>&nbsp;</td>';
            html += '</tr>';
        }
        
        $("#tbody-read").append(html);
    }

    $('.a_doc').off("click", onReadFile);
    $('.a_doc').on("click", onReadFile);
    $("#tbody-read tr td a").off("click", delRead);
    $("#tbody-read tr td a").on("click", delRead);
}
// 删除阅读记录
function delRead()
{
    var docID = $(this).children('input').val();
	if(docID==null || docID == "")
        return;

	var pData = {
	        "doc_id": parseInt(docID)
	    };

    $.ajax({
        url : "../user/read/cancel.action",
        type : "post",
        contentType: "application/json",
        dataType : 'json',
        data : JSON.stringify(pData),
        async : false,
        cache : false,
        error : function(msg) {
        	showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success : function(msg)
        {
            getRead(0, onePageCount);
        }
    });
}
// 清空阅读记录
function emptyRead()
{
	$.ajax({
        url : "../user/read/empty.action",
        type : "get",
        contentType: "application/json",
        data: null,
        dataType : 'json',
        async : false,
        cache : false,
        error : function(msg) {
            alert(msg);
        },
        success : function(msg)
        {
        	$("#tbody-read").empty();
            for(var i=0; i<onePageCount; ++i){
                var html = "";
                html += '<tr>';
                html += '<td>&nbsp;</td>';
                html += '<td>&nbsp;</td>';
                html += '<td>&nbsp;</td>';
                html += '</tr>';
                
                $("#tbody-read").append(html);
            }
        }
    });
}
//- end -----------------------
function onChangePwd()
{
	$("#old_pwd").val("");
    $("#new_pwd1").val("");
    $("#new_pwd2").val("");
    //$("#old_pwd").focus();

    $("#changePWDModal").modal("show");
}

//修改密码
function onChangePwdClick()
{
    var oldpwd  = $("#old_pwd").val();
    var newpwd1 = $("#new_pwd1").val();
    var newpwd2 = $("#new_pwd2").val();

	if(oldpwd.length == 0)
	{
		alert('请输入旧密码');
		$("#old_pwd").focus();
		return;
	}
	
	if(newpwd1.length == 0)
	{
		alert('请输入新密码');
		$("#old_pwd").focus();
		return;
	}
	
	if(newpwd2.length == 0)
	{
		alert('请再次输入新密码');
		$("#old_pwd").focus();
		return;
	}

    if(newpwd1 != newpwd2)
    {
        //showMessage('两次密码不一致');
        alert('两次密码不一致');
        return;
    }

    changePWD(oldpwd, newpwd1);
}

function changePWD(oldPwd, newPwd)
{
    var oPwdMd5 = hex_md5(oldPwd);
    var nPwdMd5 = hex_md5(newPwd);
    
    var pData = {
    		oldPwd : oPwdMd5,
            newPwd : nPwdMd5
        }; 
    
    $.ajax({
        url : "../user/password.action",
        type : "post",
        contentType: "application/json",
        data : JSON.stringify(pData),
        async : false,
        cache : false,
        error : function() {
        	showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success : function(msg)
        {
            if (msg.headers.ret == 0)
            {
                //showMessage("修改成功");
                alert("修改成功");
                $('#changePWDModal').modal('hide');
                return;
            }

            //showMessage("修改失败");
            alert("修改失败");
        }
    });
}

function onReadFile()
{
	var doc_id    = $(this).attr("docId");
	var readabled = $(this).attr("readabled");
	
	if(readabled == "false"){
		showMessage("<p style='margin:10px 20px 10px 20px;'>权限不足, 不能阅读该文档!</p>")
		return false;
	}
		
	window.open('../doc/read.action?doc_id='+doc_id);
}

function L(str)
{
    //console.log(str);
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