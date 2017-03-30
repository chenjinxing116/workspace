/**
 * Created by kinkoo on 2016-04-08.
 */
var cato_id = -1;
var curpage  = 0;
var pageSize = 20;
$("document").ready(function(){
    $(function () {
        $('input, textarea').placeholder();

        navSelect(1);
    });
    
    $("#pre-page").on("click", onprepage);
    $("#next-page").on("click", onnextpage);
    $("#cato_list li a").on("click", onSearch);
    
    var lefth = $('#div_left').height();
    var righh = 820;
    
    if(lefth < righh){
    	$('#div_left').height(righh);
    }else{	
    	$('#div_right').height(lefth-72);
    	
    	pageSize = parseInt((lefth - 160) / 32);
    }
    
    Search(0);
});

function onprepage()
{
	if($(".li-pre").hasClass("disabled"))
		return;
		
    if(curpage <= 0)
        return;

    curpage--;

    if(curpage == 0)
        $(".li-pre").addClass("disabled");

    Search(curpage);
}

function onnextpage()
{
	if($(".li-next").hasClass("disabled"))
		return;
		
    curpage++;
    Search(curpage);

    $(".li-pre").removeClass("disabled");
}

function onSearch()
{
	cato_id = $(this).find(':input:eq(0)').val(); 
    if(cato_id==null || cato_id == "")
        return;
    
	$("#cato_list").find("a").removeClass("classifyactive");
	$(this).addClass("classifyactive");

	var curpage  = 0;
    $(".li-pre").addClass("disabled");
    $(".li-next").addClass("disabled");
	
    Search(0);
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

function Search(page)
{
	var name= "";
	if(cato_id!="-1")
		name = "&cato_id="+ cato_id;
	
    $.ajax({
        url: "../doc/cato.action?page="+page+"&pageSize="+pageSize+name,
        type: "get",
        contentType: "application/json",
        dataType: 'json',
        data: null,
        async: false,
        cache: false,
        error: function () {
            showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success: function (result) {
            $("#tbody_list").empty();

            if(result.headers.ret != 0)
            	result.body = [];

            var html = "";
            for(var i=0; i<pageSize;++i){
                var item = result.body[i];
                if(item){
	                html += "<tr>";
		                html += '<td>' + DoctypeIcon(item.doc_type) + "&nbsp;" + item.doc_title + '</td>';
		                html += '<td class="text-center">' + item.upload_time + '</td>';
		                html += '<td class="text-center" style="color: #7fd75a;">' + '已发布' + '</td>';
		                html += '<td class="text-center"><a class="a_doc" docId='+item.doc_id+' readabled='+item.readable+' href="javascript:void(0);">查看</a></td>';
	                html += '</tr>';
	            }else{
	            	html += "<tr>";
		                html += '<td>' + "&nbsp;" + '</td>';
		                html += '<td>' + "&nbsp;" + '</td>';;
		                html += '<td>' + "&nbsp;" + '</td>';;
		                html += '<td>' + "&nbsp;" + '</td>';;
	                html += '</tr>';
	            }
            }

            if(result.body.length < pageSize)
                $(".li-next").addClass("disabled");
            else
                $(".li-next").removeClass("disabled");

            $('#tbody_list').append(html);
            
            $('.a_doc').off("click", onReadFile);
            $('.a_doc').on("click", onReadFile);
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

function navSelect(index)
{
    $("#nar_mian").children().removeClass("nav-current");
    $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

function L(msg)
{
    //console.log(msg);
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