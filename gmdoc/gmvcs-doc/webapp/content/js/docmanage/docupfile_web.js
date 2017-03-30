$(document).ready(function(){
	//initSWF(basePath, seesionID);
	
	navSelect(1);
	
	$('input, textarea').placeholder();
	
	$('#doc_title').keypress(function(e){	
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
	
	uploader = WebUploader.create({
        resize: false, // 不压缩image
        swf: basePath+'content/uilib/webupload/Uploader.swf',// swf文件路径
        server: '../doc/upfile.action',// 文件接收服务端。 
        pick: '#picker',// 选择文件的按钮。可选。内部根据当前运行是创建，可能是input元素，也可能是flash.
		fileSizeLimit: 52428800,
        accept: {
            title: '文档',
            extensions: 'doc,xls,docx,xlsx,pdf,ppt,pptx,txt',
            mimeTypes: '.doc,.xls,.docx,.xlsx,.pdf,.ppt,.pptx,.txt'
        }
    });
    
	// 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var point = file.name.lastIndexOf("."); 
	    var filename = file.name.substr(0, point); 
		$("#doc_title").val(filename);    
		$("#onePage").addClass("pagehide");
		$("#twoPage").removeClass("pagehide");
		
		uploader.upload();
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
    	$('#span_upding').val("正在上传文档，请稍候..."+percentage*100+" %");
    });

    uploader.on( 'uploadSuccess', function( file ) {
        $("#tip-updating").addClass("pagehide");
		$("#tip-upsucess").removeClass("pagehide");
		
		$(this).prop('disabled', false);
    });

    uploader.on( 'uploadError', function( file ) {
        $('#span_upding').val("文档上传失败，请检查网络是否通畅并重试！");
    });

    uploader.on( 'uploadComplete', function( file ) {
    });

    uploader.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
            $(this).prop('disabled', true);
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }
    });
    
    $("#btn_UpfileInfo").click(function () {
		if($("#doc_title").val().length == 0){
			alert("请先填写标题");
			$("#doc_title").focus();
			return;
		}
		
		if(!$("#midselect").val()){
			alert("请选择一个子分类");
			$("#midselect").focus();
			return;
		}
		
		$("#btn_UpfileInfo").button('处理中');
		
		var pData = {
				"doc_title": $("#doc_title").val(),
				"doc_label": $("#doc_mark").val(),
				"cato_id"  : parseInt($("#midselect").val()), 
				"security_level" : parseInt($("#doc_secrict").val())
	        }; 
	    
	    $.ajax({
	        url : contextPath + "/doc/commit.action",
	        type : "post",
	        contentType: "application/json",
	        data : JSON.stringify(pData),
	        async : false,
	        cache : false,
	        error : function(msg) {
	        	alert("error");
	        },
	        success : function(msg)
	        {
	        	$("#doc_title").val("");   
	        	$("#doc_mark").val("");
	        	$("#onePage").addClass("pagehide");
	        	$("#twoPage").addClass("pagehide");
	        	$("#threePage").removeClass("pagehide");
	            $("#tip-updating").removeClass("pagehide");
	        	$("#tip-upsucess").addClass("pagehide");
	        },
	        complete : function() {
	        	$("#btn_UpfileInfo").button('reset');
	        }
	    });   	
	}); 
	
	$("#btn_goUpfile").click(function () {
		$("#onePage").removeClass("pagehide");
		$("#twoPage").addClass("pagehide");
		$("#threePage").addClass("pagehide");
	});
});

function navSelect(index)
{
    $("#nar_mian").children().removeClass("nav-current");
    $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

window.onbeforeunload = function(){
	   if($("#twoPage").hasClass("pagehide"))
   		   return;
	   else
		   return "必您确定要退出页面吗？";
   }

window.onunload= function(){
    if($("#twoPage").hasClass("pagehide"))
        return;
    else
        $.get(contextPath+"/doc/cancel.action");
}

var threeSelectData = {cato_name:"请选择", cato_id:-1, children:{cato_name:"请选择", cato_id:-1}};
	$.ajax({
        url : basePath + "/cato/myCatoList.action",
        type : "get",
        data : null,
        async : false,
        error : function() {
            alert("服务器出错~");
        },
        cache : false,
        success : function(msg) 
        {
            if (msg.headers.ret == 0)
            	threeSelectData = msg.body;
        }
    });
 
var defaults = {
	s1:'mainselect',
	s2:'midselect',
};

$(function(){
	threeSelect(defaults);
});

function threeSelect(config)
{
	var $s1=$("#"+config.s1);
	var $s2=$("#"+config.s2);
	var v1=config.v1?config.v1:null;
	var v2=config.v2?config.v2:null;
	$.each(threeSelectData,function(k,v){
		appendOptionTo($s1,k,v,v1);
	});
	
	$s1.change(function(){
		$s2.html("");
		if(this.selectedIndex==-1)
			return;
		
		var s1_curr_val = this.options[this.selectedIndex].value;
		
		$.each(threeSelectData, function(k,v)
		{
			if(s1_curr_val==v.cato_id)
			{
				if(v.children)
				{
					$.each(v.children,function(k,v)
					{
						appendOptionTo($s2,k,v,v2);
					});
				}
			}
		});
	
		if($s2[0].options.length==0){
			appendOptionTo($s2,"...","",v2);
		}
	}).change();
	
	function appendOptionTo($o,k,v,d)
	{
		var $opt=$("<option>").text(v.cato_name).val(v.cato_id);
		
		if(v==d)
		{
			$opt.attr("selected", "selected")
		}
		
		$opt.appendTo($o);
	}
}