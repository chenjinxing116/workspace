;(function($, window, document, undefined){
	var pluginName = "gmLoading";
	var defaults   = {
		imgUrl : '/front-resource/gmLoading/loading.gif'
	};
	
	var GmLoading = function($ele, opt){
		this.$ele = $ele;
		this.settings = $.extend({}, defaults, opt);
	};
	
	GmLoading.prototype = {
		show : function(){
			that = this;
			
			// 设置父容器的position，让子容器可使用absolute
            var position = this.$ele.css('position');
            if(position!='absolute' && position!='relative'){
                this.$ele.css('position', 'relative');
            }
            
            this.$ele.append("<div class='overlay' style='position:absolute;top:0;left:0;right:0;bottom:0;overflow:hidden;z-index:9999;'>" + 
            					"<div id='imgContainer' style='display:none;margin:auto;width:180px;padding:20px;background-color: #FFF;border-radius:8px;text-align:center;'><img src='" + this.settings.imgUrl + "'/>" +
            					'<iframe border="0" src="" frameborder="0" allowtransparency="true" style="cursor:pointer;width:100%;height:100%;background-color:#000000;opacity:0.4;filter:alpha(opacity=50);position:absolute;top:0;left:0;right:0;bottom:0;z-index:-1;"></iframe>' +
            				"</div>");
            
            var $overlay = $('.overlay', this.$ele);
            var $loading = $('#imgContainer', this.$ele);

            window.onresize = function(){
                $loading.css({
                'margin-top' : ($overlay.height()-$loading.outerHeight())/2,
                });
            }; 
            
            $loading.css({
                'margin-top' : ($overlay.height()-$loading.outerHeight())/2,
            }).show();
		},
		hide : function(){
			$('.overlay', this.$ele).remove();
		}
	};
	
	$.fn.gmLoading = function(opt){
		var gmloading = new GmLoading(this.length>1?$(this[0]):this, opt);		
		return gmloading;
	}
}(jQuery, window, document));