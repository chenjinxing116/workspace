function initSWF(basePath)
{
	$("#file_upload").uploadify({
	    'swf' : basePath+"content/upfile/uploadify.swf",//控件flash文件位置
	    'uploader' : basePath+"doc/upfile.action",  //后台处理的请求（也就是action请求路径），后面追加了jsessionid，用来标示使用当前session（默认是打开新的session，会导致存在session校验的请求中产生302错误）
	    'queueID' : 'queue',//与下面HTML的div.id对应
	    'width' : '254',//按钮宽度
	    'height' : '65',//按钮高度
	    'fileTypeDesc' : '指定类型文件',
	    'fileTypeExts' : '*.doc;*.docx;*.pdf;*.txt;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
	    'fileObjName' : 'upload_file',//文件对象名称,用于后台获取文件对象时使用,详见下面的java代码
	    'buttonText' : '上传我的文档',//上传按钮显示内容，还有个属性可以设置按钮的背景图片
	    'fileSizeLimit' : '102400KB',
	    'multi' : false,
	    'overrideEvents' : [ 'onDialogClose', 'onUploadSuccess', 'onUploadError', 'onSelectError' ],//重写默认方法
	    'onFallback' : function() {//检测FLASH失败调用
	        alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
	    },
	    //以下方法是对应overrideEvents的重载方法，具体实现是网上找到的一个别的朋友的代码，
	    //我把这些方法抽到了一个自定义js中，我会在最后面贴出来
	    'onSelect' : uploadify_onSelect,
	    'onSelectError' : uploadify_onSelectError,
	    'onUploadError' : uploadify_onUploadError,
	    'onUploadSuccess' : uploadify_onUploadSuccess
	    });
}
		
var uploadify_onSelectError = function(file, errorCode, errorMsg) 
{
    var msgText = "上传失败\n";
    switch (errorCode) {
        case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
            // this.queueData.errorMsg = "每次最多上传 " +
            // this.settings.queueSizeLimit + "个文件";
            msgText += "上传的文件数量已经超出系统限制的" + $('#file_upload').uploadify('settings', 'queueSizeLimit') + "个文件！";
            break;
        case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
            msgText += "文件 [" + file.name + "] 大小超出系统限制的" + $('#file_upload').uploadify('settings', 'fileSizeLimit') + "大小！";
            break;
        case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
            msgText += "文件大小为0";
            break;
        case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
            msgText += "文件格式不正确，仅限 " + this.settings.fileTypeExts;
            break;
        default:
            msgText += "错误代码：" + errorCode + "\n" + errorMsg;
    }
    
    alert(msgText);
};
	
var uploadify_onUploadError = function(file, errorCode, errorMsg, errorString) {
    // 手工取消不弹出提示
    if (errorCode == SWFUpload.UPLOAD_ERROR.FILE_CANCELLED || errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
        return;
    }
    var msgText = "上传失败\n";
    switch (errorCode) {
        case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
            msgText += "HTTP 错误\n" + errorMsg;
            break;
        case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
            msgText += "上传文件丢失，请重新上传";
            break;
        case SWFUpload.UPLOAD_ERROR.IO_ERROR:
            msgText += "IO错误";
            break;
        case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
            msgText += "安全性错误\n" + errorMsg;
            break;
        case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
            msgText += "每次最多上传 " + this.settings.uploadLimit + "个";
            break;
        case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
            msgText += errorMsg;
            break;
        case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
            msgText += "找不到指定文件，请重新操作";
            break;
        case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
            msgText += "参数错误";
            break;
        default:
            msgText += "文件:" + file.name + "\n错误码:" + errorCode + "\n" + errorMsg + "\n" + errorString;
    }
    alert(msgText);
};
	        
var uploadify_onSelect = function(file) {
	var point = file.name.lastIndexOf("."); 
    var filename = file.name.substr(0, point); 
	$("#doc_title").val(filename);    
	$("#onePage").addClass("pagehide");
	$("#twoPage").removeClass("pagehide");
};
	
var uploadify_onUploadSuccess = function(file, data, response) {
    //alert("保存每个文件上传后台返回的相关信息,在onQueueComplete方法中展示");
    $("#tip-updating").addClass("pagehide");
	$("#tip-upsucess").removeClass("pagehide");
};

var uploadify_onQueueComplete = function(){
    //alert("全部完成-->并展示提示信息");
};