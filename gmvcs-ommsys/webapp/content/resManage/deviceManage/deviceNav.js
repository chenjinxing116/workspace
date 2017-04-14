$(function(){
	
	$('[data-i18n]').each(function() {
		var $this = $(this);
		$this.append(getI18NStr($this.attr('data-i18n')));
	});
	
	$('li').click(function(){
		var $this = $(this);
		var dataFor = $this.attr('data-for');
		if($(dataFor).length < 1){
			switch(dataFor){
				case "#iframeJLY": 
					$('.wrap').append('<iframe id="iframeJLY" src="/gmvcsomm/content/resManage/deviceManage/zfyManage/zfyManage.html" width="100%" height="100%" frameborder="0"></iframe>');break;
				case "#iframeWS":
					$('.wrap').append('<iframe id="iframeWS" src="/gmvcsomm/content/resManage/deviceManage/wsManage/wsManage.html" width="100%" height="100%" frameborder="0"></iframe>');break;
			}
		}
			
		$(dataFor).removeClass('hidden').siblings().addClass('hidden');
		$(this).addClass('active').siblings().removeClass('active');
	});
	
	$('ul li:first').trigger("click");
});

function getI18NStr(key) {
	return parent.Tab.geti18nStr(key);
}
