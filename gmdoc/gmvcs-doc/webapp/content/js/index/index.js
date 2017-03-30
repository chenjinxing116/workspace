/**
 * Created by kinkoo on 2016-03-24.
 */
$("document").ready(function(){
    $(function () {
        $('input, textarea').placeholder();
    });
    
    $('.more_cato').on("click", onMoreCato);
    $('.a_doc').on("click", onReadFile);
    
    /* 全部参数 */
    $(function () {
    	$.scrollUp({
        scrollName: 'scrollUp', // Element ID
        topDistance: '300', // Distance from top before showing element (px)
        topSpeed: 300, // Speed back to top (ms)
        animation: 'fade', // Fade, slide, none
        animationInSpeed: 200, // Animation in speed (ms)
        animationOutSpeed: 200, // Animation out speed (ms)
        scrollText: '<i class="fa fa-angle-up"></i>', // Text for element
        activeOverlay: false // Set CSS color to display scrollUp active point, e.g '#00FFFF'
        });
    });
    
    navSelect(0);
});

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

function onMoreCato()
{
	var cato_id = $(this).parent().prevAll('.active').children('input')[0].value;
	window.location = "../search/s.action?cato_id=" + cato_id;
}

function navSelect(index)
{
	$("#nar_mian").children().removeClass("nav-current");
	$("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

function L(msg)
{
	//console.log(msg);
}

function getClassifieds(dist_id)
{
    $.ajax({
        url : contextPath + "/cato/catoList.action",
        type : "get",
        data : {
            dist_id : dist_id
        },
        async : false,
        error : function() {
            alert("服务器出错~");
        },
        cache : false,
        success : function(msg) 
        {
            //var msg = eval('(' + msg + ')');
            if (msg.headers.ret == 0)
            {
            	$("#classfield").empty();
            	
            	var x = msg.body.length;
            	for(var i=0; i<x; i++)
            	{
            		if(0 == i%4)
            			$('<div class="clearfix"></div>').appendTo($("#classfield"));
            		
            		var field = $('<div class="col-lg-3 col-md-4 col-sm-6"></div>').appendTo($("#classfield"));
            		$('<a href="#"><i class="fa fa-square"></i><strong> '+ msg.body[i].cato_name + '</strong></a>').appendTo(field);
            		
            		var table = $('<table class="table"></table>').appendTo(field);
            		
            		var len = msg.body[i].children.length;
            		var count = 0;
            		for(var j=0;j<len;j++)
            		{
            			if(count == 0)
            				var tr = $('<tr></tr>').appendTo(table);
            			
            			$('<td><a href="#">'+msg.body[i].children[j].cato_name+'</a></td>').appendTo(tr);
            			count = ++count % 3;
            		}
            	}
            }
        }
    });
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