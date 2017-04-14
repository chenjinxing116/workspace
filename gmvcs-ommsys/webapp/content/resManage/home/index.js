/**
 * 导航页
 * @author hcxowe
 * @date 2016-7-18 17:00
 */
$(function(){
	
	$.i18n.properties({
		name:'gmvcs-language', 
	    path:'/gmvcsomm/content/common/locales/', 
	    mode:'map',
	    language: 'zh',
	    callback: function() {
	    	document.title = $.i18n.prop('nav.title');

	        $('[data-i18n]').each(function() {
	        	var $this = $(this);
	        	$this.append($.i18n.prop($this.attr('data-i18n')));
	        });
	  
	        $('#aLogout').attr('title', $.i18n.prop('msg.exit'));
	    }
	});
	
	Tab.init();
});

// 菜单点击操作
var Tab = (function(){
	
	function init(){
		initTab();
		observeRefresh();
	}

	/**
	 * 是否支持Storage
	 * @param type storage类型
	 *
	 */
	function storageAvailable(type) {
	    try {
	        var storage = window[type],
	            x = '__storage_test__';
	        storage.setItem(x, x);
	        storage.removeItem(x);
	        return true;
	    }catch(e) {
	        return false;
	    }
	}

	/**
	 * 显示tab和list
	 * @param obj
	 * @param index
	 * @param url
	 */
	function showTabList(obj,index,url){
	    index = index ? index : 0;

	    //tab高亮
	    if(obj){//根据传入的对象设置高亮
	       $(obj).addClass("ma-active").siblings("li").removeClass("ma-active");
	       url =  $(obj).data("url");
	    }else{//根据下标设备高亮
	       $("#nav").find("li").eq(index).addClass("ma-active").siblings("li").removeClass("ma-active");
	    }
	    
	    var idStr = url.substring(url.indexOf("/")+1,url.lastIndexOf("/"));
	    idStr = idStr.replace(/\//g, '_');
		if(!hasIframe(idStr)){//没有相关iframe,则创建
			var iframe = createIframe(idStr);
			iframe.attr("src", url);
			append2Main(iframe);
		}
		
		$("#" + idStr).addClass("show").removeClass("hide").siblings("iframe").addClass("hide").removeClass("show");
	}

	/**
	 * 初始化Tab
	 */
	function initTab (){
	    if (storageAvailable('sessionStorage')) {//浏览器支持storage
	        var preTab = sessionStorage.getItem("tabbed");//上次点击的tab下标 & url
	        var url = sessionStorage.getItem("url");
	        
	        if(null === preTab || url === null){
            	preTab = 0;
            	url    = "/gmvcsomm/system/deviceManage/index.action";
            }
	        
	        $("#nav").find("li").each(function(index){//遍历所有tab
	            var $li = $(this);
	            
	            //非第一次，则初始化显示上次点击的tab
	            if(url == $li.data("url")){
	                showTabList(null, index, url);
	            }
	            
	            //li绑定click事件
	            $li.click(function(){ 
	                showTabList(this, index);
	                sessionStorage.setItem("tabbed", index);//设置tabbed值,url
	                sessionStorage.setItem("url", $(this).data("url"));
	            });
	        });
	    }
	    else {
	        //不支持sessionStorage的不做处理，不支持当前页面刷新---默认显示第一页
	        showTabList(null, 0, "/gmvcsomm/system/deviceManage/index.action");
	    }
	}
	/**
	 * 构建Iframe对象
	 */
	function createIframe(id) {
		var iframe = $('<iframe class="iframe" width="100%" height="100%" frameborder="0">').attr("id",id);
		return iframe;
	}
	/**
	 * 添加到.main DOM中
	 */
	function append2Main (iframe){
		$(".content").append(iframe);
	}
	/**
	 * 判断是否存在指定的Iframe
	 * @param id Iframe id
	 */
	function hasIframe(id){
		var iframes = $(".content").find(".iframe"),flag=false;
		$.each(iframes,function(idx,iframe){
			if($(iframe).attr("id") == id){
				flag = true;
			}
		})
		return flag;
	}
	function removeIframe(id){
		$("iframe[id='"+id+"']").empty();
	}
	/**
	 * 标记刷新操作
	 * 标志状态，当从其他页面切换到监控页面时，进行一次刷新
	 * 否则iframe获取不到高度
	 */
	function observeRefresh() {
		window.onunload = function() { 
			sessionStorage.setItem("refresh", 1);
		}
	}
	
	return {
		init : init,
		geti18nStr: function(key) {
			return $.i18n.prop(key);
		}
	}
})();


