/**
 * Created by kinkoo on 2016-04-07.
 */
var pData = {};
var curpage  = 0;
var pageSize = 10;
$("document").ready(function(){
    //$("#form_search").onsubmit = function(){return false;};
    var form = $("#form_search");
    $("#form_search").submit(onBtnSearch);
    
    $("#btn_search").on("click", onBtnSearch);
    $("#pre-page").on("click", onprepage);
    $("#next-page").on("click", onnextpage);
    
    onSearch();
    
    navSelect(100);
});

function navSelect(index)
{
    $("#nar_mian").children().removeClass("nav-current");
    $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
}

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
	if(keyword!=null && keyword!="")
		pData["keyword"] = keyword;
		
	if(doc_type!=null && doc_type!="" && doc_type!="all")
		pData["doc_type"] = doc_type;
		
	if(cato_id!=null && cato_id!="")
		pData["cato_id"] = parseInt(cato_id);

    Search(0);
}

function onBtnSearch(e)
{
	e.preventDefault();
	
	var doc_type  = $("input[name='doc_type']:checked").val();
	var keyword   = $("#input_search").val();
	var catoid   = cato_id ? cato_id : "";
	
	var url       = "../search/s.action";
	
	if(doc_type.length!=0 || keyword.length!=0 || catoid.length!=0){
		url += '?';
	}
	
	var param = "";
	if(doc_type.length !=0)
		param += "&doc_type=" + doc_type;
		
	if(keyword.length !=0)
		param += "&keyword=" + keyword;
	
	if(catoid.length !=0)
		param += "&cato_id=" + catoid;
		
	url += param.substr(1);
	
	window.location.href = url;
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

function CollectedIcon(collect)
{
	if(collect)
		return '<i class="fa fa-star"></i>';
	else
		return '<i class="fa fa-star-o"></i>';
}

function Search(page)
{
    $.ajax({
        url: basePath + "search/result.action?page="+page+"&pageSize="+pageSize,
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(pData),
        async: false,
        cache: false,
        error: function () {
            showMessage('提交请求失败, 请检测网络连接是否通畅并重试!');
        },
        success: function (result) {
            $("#result_search").empty();

            if(result.headers.ret != 0)
                return;

            var html = "";
            for(var i=0; i<result.body.length;++i){
                var item = result.body[i];
                
                var pinfo = {};
                pinfo.doc_id = item.doc_id;
                pinfo.is_collected = item.is_collected;
                
                var jsonitem = JSON.stringify(pinfo);
                
                var title   = item.doc_title_hl!=null?item.doc_title_hl:item.doc_title;
                var content = item.doc_content_hl!=null?item.doc_content_hl:item.doc_content;
                
                html += '<div class="list-group-item">';
                	html += '<h4><a class="a_doc" docId='+item.doc_id+' readabled='+item.readable+' href="javascript:void(0);">'+DoctypeIcon(item.doc_type)+'&nbsp;'+ title + '</a></h4>';
                	html += '<p>' + content + '</p>';
                	html += '<div style="margin: 5px auto 10px;">';
                		html += '<p style="display: inline;">所属分类:&nbsp;' + item.doc_cato_name + '&nbsp;&nbsp;&nbsp;&nbsp;发布时间:&nbsp;' + item.pub_dateline + '&nbsp;&nbsp;&nbsp;&nbsp;格式:&nbsp;' + item.doc_type + '&nbsp;&nbsp;&nbsp;&nbsp;</p>';
                		html += '<a class="a-collect" style="margin-right: 2px; margin-left: 5px;cursor: pointer;"><p class="hidden">'+jsonitem+'</p>'+CollectedIcon(item.is_collected)+'收藏</a>';
                		html += '<a href="../doc/download.action?doc_id='+ item.doc_id +'" class="btn btn-success btn-sm pull-right" style="margin-top: -5px;"><i class="fa fa-download"></i>下载</a>';
                	html += '</div>';
                html += '</div>';
            }

            if(html == "")
                return;

            if(result.body.length < 10)
                $(".li-next").addClass("disabled");
            else
                $(".li-next").removeClass("disabled");

            $('#result_search').append(html);
            
            $('.a_doc').off("click", onReadFile);
            $('.a_doc').on("click", onReadFile);
            
            $(".a-collect").off("click", Doccollect);
            $(".a-collect").on("click", Doccollect);
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

function Doccollect()
{	
	var self = this;
	var str  = $(this).find('p')[0].innerHTML;
	var item = JSON.parse(str);
	
	var url  = item.is_collected?"user/collected/cancel.action":"doc/collect.action";
	
	item.is_collected = !item.is_collected;
	
	
	var pData = {
		"doc_id" : item.doc_id
	};
	
	$.ajax({		
        url: basePath + url,
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({"doc_id" : item.doc_id}),
        async: false,
        cache: false,
        success: function (result) {
        	var html = '<p class="hidden">' + JSON.stringify(item) + '</p>' + CollectedIcon(item.is_collected)+'收藏';
        	 $(self).html(html);
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