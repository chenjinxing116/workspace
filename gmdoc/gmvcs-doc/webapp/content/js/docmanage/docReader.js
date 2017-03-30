/**
 * Created by ThinkPad on 2016-04-04.
 */
$("document").ready(function(){
    $(function () {
        $('input, textarea').placeholder();

        navSelect(1);
        
        $(".a-collect").on("click", Doccollect);

        //console.log(fileUrl);
        $('#documentViewer').element = new FlexPaperViewerEmbedding("documentViewer", {
            config: {

                SWFFile: fileUrl,

                Scale: 1,
                ZoomTransition: 'easeOut',
                ZoomTime: 0.5,
                ZoomInterval: 0.2,
                FitPageOnLoad: false,
                FitWidthOnLoad: true,
                FullScreenAsMaxWindow: false,
                ProgressiveLoading: false,
                MinZoomSize: 0.2,
                MaxZoomSize: 5,
                SearchMatchAll: false,
                InitViewMode: 'Portrait',
                RenderingOrder: 'flash',
                StartAtPage: '',

                ViewModeToolsVisible: true,
                ZoomToolsVisible: true,
                NavToolsVisible: false,
                CursorToolsVisible: false,
                SearchToolsVisible: false,
                WMode: 'window',
                localeChain: 'en_US'
            }
        });
    });

    function navSelect(index)
    {
        $("#nar_mian").children().removeClass("nav-current");
        $("#nar_mian").children("li:eq("+index+")").addClass("nav-current");
    }
});

function CollectedIcon(collect)
{
	if(collect)
		return '<i class="fa fa-star"></i>';
	else
		return '<i class="fa fa-star-o"></i>';
}

function Doccollect()
{	
	var self = this;

	var url  = docInfo.is_collected?"../user/collected/cancel.action":"collect.action";
	
	docInfo.is_collected = !docInfo.is_collected;
	
	$.ajax({
        url:  url,
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({"doc_id" : docInfo.doc_id}),
        async: false,
        cache: false,
        success: function (result) {
        	var html = CollectedIcon(docInfo.is_collected)+' 收藏';
        	 $(self).html(html);
        }
	});
}
