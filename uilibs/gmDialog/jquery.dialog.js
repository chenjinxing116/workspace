;(function($, window, document){
	
	var defaults = {
		title: "模态对话框",
		dragEnabled: true,
		iframeUrl: "",
		htmlContent: "",
		width: 700,
		height: 500,
		closeWindow: null,
	};

	$.gmModelDialog = function(opts){
		
		if(opts == 'destory'){
			$('.dialog-title').trigger('dialog-uninit');
			return;
		}
		
		var settings = $.extend({}, defaults, opts);
		
		var dialog = {
			init: function(){
				var $body = $(document.body);
				var dialoghtml = "";
				dialoghtml += "<div class='gm-overlay' style='position:absolute;top:0;left:0;right:0;bottom:0;overflow:hidden;z-index:9999;'>";
					dialoghtml += "<div class='gm-dialog' style='width: 800px;height: 600px;background-color: white;position: relative;margin: 0 auto;'>";
						dialoghtml += 	"<div class='dialog-title' style='padding: 7px;background-color: #0078D7;color: #fff;height: 36px;'>";
							dialoghtml += "<p style='margin:0;font-size:18px;float:left;'>" + settings.title +"</p>";
							dialoghtml += "<a href='javascript:void(0);' class='dialog-close' style='float: right;'><i class='fa fa-times' style='color: #fff;'></i></a>";
						dialoghtml += "</div>";
						dialoghtml += "<div class='dialog-content' style='position: absolute;top: 36px;left: 0;right: 0;bottom: 0;'>";
						dialoghtml += "</div>";
					dialoghtml += "</div>";
					dialoghtml += "<div style='width:100%;height:100%;background-color:#000000;opacity:0.4;filter:alpha(opacity=50);position:absolute;top:0;left:0;right:0;bottom:0;z-index:-2;'></div>"
					dialoghtml += '<iframe border="0" frameborder="0" style="cursor:pointer;width:100%;height:100%;position:absolute;top:0;left:0;right:0;bottom:0;z-index:-1;opacity:0;filter:alpha(opacity=0);" allowtransparency="true"></iframe>';
				dialoghtml += "</div>";
				
				$body.append(dialoghtml);
				
				if(settings.iframeUrl != ""){
					var $iframe = $('<iframe width="100%" height="100%" frameborder="0"></iframe>');
					$iframe.attr("src", settings.iframeUrl);
					
					$('.dialog-content').append($iframe);
				}
				else if(settings.htmlContent != ""){
					$('.dialog-content').append(settings.htmlContent);
				}
				
				var $gmOverlay = $('.gm-overlay');
				var $gmDialog  = $('.gm-dialog');
				dialog.$gmDialog = $gmDialog;
				var top = Math.max(($body.height() - settings.height)/2, 0);
				var left= Math.max(($body.width() - settings.width)/2, 0);
				
				$gmDialog.css({'margin-top':top, 'margin-left':left, 'width':settings.width+'px', 'height':settings.height+'px'});
				
				$('.dialog-close').click(function(){
					settings.closeWindow && settings.closeWindow();
					$gmOverlay.empty();
					$gmOverlay.remove();
				});
				
				if(settings.dragEnabled){
					$('.dialog-title').mousedown(dialog.grabTitle).bind("dialog-uninit", function(){
							settings.closeWindow && settings.closeWindow();
							$gmOverlay.empty();
							$gmOverlay.remove();
						});
				}
			},
			grabTitle: function(e){	
				e.preventDefault();
				
				var $title = $('.dialog-title');
				
				dialog.mouseoffset = {};
				var offset = $title.offset();
	        	dialog.mouseoffset.top  =  e.pageY - offset.top;
	        	dialog.mouseoffset.left =  e.pageX - offset.left;
	        	
				var $body = $(document.body);
				dialog.limitArea = {
					top: 0,
					left: 0,
					right: $body.width(),
					bottom: $body.height()
				}
				
				var listElem = this;
				var trigger = function() {
					dialog.dragStart.call(listElem, e);
					$title.unbind("mousemove", trigger);
				};
				
				$title.mousemove(trigger).mouseup(function() { $title.unbind("mousemove", trigger);});
			},
			dragStart: function(){
				$(document).on('mousemove', dialog.dragElement);
				$(document).on('mouseup', dialog.dragEnd);
			},
			dragElement: function(event)
		    {
		    	event.preventDefault();
		    	
		    	var height = dialog.$gmDialog.height();
		    	var width  = dialog.$gmDialog.width();
		    	
		    	var top  = event.pageY-dialog.mouseoffset.top;
		    	var left = event.pageX-dialog.mouseoffset.left;
		    	
		    	if(top < 0){
		    		top = 0;
		    	}else{
		    		top = top+height > dialog.limitArea.bottom ? dialog.limitArea.bottom-height : top;
		    	}
		    	
		    	if(left < 0){
		    		left = 0;
		    	}else{
		    		left = left+width > dialog.limitArea.right ? dialog.limitArea.right-width : left;
		    	}

		        dialog.$gmDialog.css({
		            'top' :  top + 'px',
		            'left' : left + 'px',
		            'position' : 'absolute',
		            'margin-top': 0,
		            'margin-left': 0
		        });
		        
		        return false;
		    },
		    dragEnd: function(e)
		    {
		        $(document).off('mousemove', dialog.dragElement);
		        $(document).off('mouseup', dialog.dragEnd);
		    }
		};
		
		dialog.init();
	};
}(jQuery, window, document));
