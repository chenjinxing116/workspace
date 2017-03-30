// UserAuth.js
// 2016-04-29 11:25
$(document).ready(function(){
	navSelect(4);
	
	getOrgtree();
	
	$('#btn_submit').click(function(){
		if($('#label_code').text().length==0){
			alert("无效用户,禁止提交");
			return;
		}
		
		var pData = {
			"user_code" : $('#label_code').text(),
       		"sec_level" : parseInt($('#sel_lv').val())
		};
		
		$.ajax({
	        url		: "./sec_level.action",
	        type: "post",
	        contentType: "application/json",
	        dataType: 'json',
	        data: JSON.stringify(pData),
	        async	: false,
	        cache	: false,
	        error	: function() {
	          	alert("获取数据失败`~`");
	          	return;
			},
	        
	        success : function(msg) 
	        {
	        	alert("修改成功");
	        	return;
	        }
	    });	
	});
	
	if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/7./i)=="7.") 
	{ 
		$('#swrap').css('min-height', '500px');
	} 
	else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8.") 
	{ 
		$('#swrap').css('min-height', '500px');
	}  
	else if(navigator.appName == "Microsoft Internet Explorer") 
	{ 
		$('#swrap').css('min-height', '500px');
	}
	else{
		$('#swrap').css('min-height', '870px');
	}
});

function navSelect(index)
{
    $("#nar_mian").children().removeClass("nav-current");
    $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

function getOrgtree()
{
	var setting = {
		data : {
			key : {
				children : "children",
				name	 : "user_name"
			}
		},
		check: {
			enable: false
		},
		view : {
			
		},
		callback : {
			beforeExpand: zTreeBeforeExpand,
			beforeClick: beforeClick,
			onCheck: null,
			onClick: onNodeClick
		}
	};
	
	$.ajax({
        url		: "./org.action",
        type	: "get",
        data	: null,
        async	: false,
        cache	: false,
        error	: function() {
          	alert("获取数据失败`~`");
          	return;
		},
        
        success : function(msg) 
        {
        	setnoCP(msg);
			$.fn.zTree.init($("#tree_org"), setting, msg);
        }
    });
}

function beforeClick(treeId, treeNode, clickFlag)
{
	return treeNode.click;
}

function onNodeClick(event, treeId, treeNode)
{
	$('#label_name').text("");
    $('#label_code').text("");
    $('#sel_lv').val(0);
       		
	var url = "./user/auth.action?user_code=" + treeNode.user_code;

	$.ajax({
        url		: url,
        type	: "get",
        data	: null,
        async	: false,
        cache	: false,
        error	: function() {
          	alert("获取数据失败`~`");
          	return;
		},
        
        success : function(msg) 
        {
       		$('#label_name').text(msg.body.user_name);
       		$('#label_code').text(msg.body.user_code);
       		$('#sel_lv').val(msg.body.sec_level);
        }
    });			
}

function zTreeBeforeExpand(treeId, treeNode) 
{
	if(treeNode.isLoaded)
		return true;
		
	tree_addPolice(treeId, treeNode);
	return false;
};

function tree_addPolice(treeId, treeNode)
{
	var url = "./org/user.action?org_id=" + treeNode.id;

	$.ajax({
        url		: url,
        type	: "get",
        data	: null,
        async	: false,
        cache	: false,
        error	: function() {
          	alert("获取数据失败`~`");
          	return;
		},
        
        success : function(msg) 
        {
        	var treeObj = $.fn.zTree.getZTreeObj(treeId);
        	treeNode.isLoaded = true;
        	$.each(msg.body, function(i, value) {
        		value.click = true;
        	});
        	
			newNodes = treeObj.addNodes(treeNode, msg.body);
			treeObj.expandNode(treeNode, true);
			return;
        }
    });		
}

function setnoCP(data)
{
	$.each(data, function(i, value) {
		value.user_name = value.text;
		value.isLoaded = false;
		value.click = false;
	    if(value.children){
	    	value.isParent = true;
	    	if(value.children.length != 0){
	    		setnoCP(value.children);
	    	}
	    }
	});
}