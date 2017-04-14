$(function(){
	
	$('li').click(function(){

		var $this = $(this);
		var dataFor = $this.attr('data-for');
		if($(dataFor).length < 1){
			switch(dataFor){
				case "#iframeYHPZ": 
					$('.wrap').append('<iframe id="iframeYHPZ" src="/gmvcsomm/content/resManage/userManage/userConfig/userConfig.html" width="100%" height="100%" frameborder="0"></iframe>');break;
				case "#iframeYWGWZD":
					$('.wrap').append('<iframe id="iframeYWGWZD" src="/gmvcsomm/content/resManage/userManage/busJobDic/busJobDic.html" width="100%" height="100%" frameborder="0"></iframe>');break;
			}
		}
			
		$(dataFor).removeClass('hidden').siblings().addClass('hidden');
		$(this).addClass('active').siblings().removeClass('active');
	});
	
	$('ul li:first').trigger("click");
});
