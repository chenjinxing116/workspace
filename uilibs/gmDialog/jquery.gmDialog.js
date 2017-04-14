(function($,window,document){
    var pluginName = 'myDialog';
    var defaults = {
        'width': '300',
        'height':'200',
        'msg':'are you sure to do this?',
        'btns': 'MB_OK',
        'onOKclick': null,
        'onCancelclick': null,
        'onYesclick': null,
        'onNoclick' : null,
        'icon': 'info',
        'language': 'zh'
    };
    
    var Mydialog = function(options){
        this.settings = $.extend({}, defaults, options);
        this.defaults = defaults;
    };
    
    Mydialog.prototype = {
        init : function(){
            var that = this;
            
            var $body = $(document.body);
            $body.append("\
<div class='overlay'>\
    <div class='dialog'>\
        <div class='dialog-head'>\
            <h3>" + that.getI18NStr('notice') + "</h3>\
            <hr/>\
        </div>\
        <div class='dialog-content'>\
        </div>\
        <div class='dialog-footer'>\
        </div>\
    </div>\
</div>");
            var $dialog = $('.overlay .dialog');
            $dialog.css({
                'width' : that.settings.width,
                'height': that.settings.height,
                'margin-top':-that.settings.height/2,
                'margin-left':-that.settings.width/2
            });
            
            var $content = $('.overlay .dialog .dialog-content');
            var $p = $('<p>'+that.settings.msg+'</p>');
            var $icon = $(this.getIcon(that.settings.icon));
            
            $content.append($icon);
            $content.append($p);
            $p.css('margin-top', ($content.height()-$p.height())/2);
            $icon.css('margin-top', ($content.height()-$icon.height())/2);
            
            var btnArr = that.settings.btns.split('|');
            var $footer = $('.dialog .dialog-footer');
            for(var i=0,len=btnArr.length;i<len;++i){
                var $btn;
                if(btnArr[i] == 'MB_OK'){
                    $btn = $('<button>' + that.getI18NStr('sure') + '</button>');
                    $footer.append($btn);  
                    $btn.click(function(){
                        if(that.settings.onOKclick)
                            that.settings.onOKclick();
                        
                        $('.overlay').remove();
                    });
                }
                if(btnArr[i] == 'MB_CANCEL'){
                    $btn = $('<button>' + that.getI18NStr('cancel') + '</button>');
                    $footer.append($btn);
                    $btn.click(function(){
                        if(that.settings.onCancelclick)
                            that.settings.onCancelclick();
                        
                        $('.overlay').remove();
                    });
                }
                if(btnArr[i] == 'MB_YES'){
                    $btn = $('<button>' + that.getI18NStr('yes') + '</button>');
                    $footer.append($btn);
                    $btn.click(function(){
                        if(that.settings.onYesclick)
                            that.settings.onYesclick();
                        
                        $('.overlay').remove();
                    });
                }
                if(btnArr[i] == 'MB_NO'){
                    $btn = $('<button>' + that.getI18NStr('no') + '</button>');
                    $footer.append($btn);
                    $btn.click(function(){
                        if(that.settings.onNoclick)
                            that.settings.onNoclick();
                        
                        $('.overlay').remove();
                    });
                }
            }
        },
        getIcon:function(type){
            switch(type){
                case 'info':
                    return '<i class="fa fa-info-circle fa-4x" aria-hidden="true"></i>';
                case 'error':
                    return '<i class="fa fa-times-circle fa-4x" aria-hidden="true"></i>';
                case 'warning':
                    return '<i class="fa fa-exclamation-triangle fa-4x" aria-hidden="true"></i>';
                case 'question':
                    return '<i class="fa fa-question-circle fa-4x" aria-hidden="true"></i>';
            }
        },
        getI18NStr: function(key) {
    		if (this.settings.language == 'zh') {
    			switch (key) {
    				case 'notice': return '提示';
					case 'yes': return '是'; 
					case 'no': return '否'; 
					case 'sure': return '确定';
					case 'cancel': return '取消'; 
    			}
    		}
    		else {
    			switch (key) {
    				case 'notice': return 'Notice';
					case 'yes': return 'Yes'; 
					case 'no': return 'No'; 
					case 'sure': return 'Sure';
					case 'cancel': return 'Cancel'; 
    			}
    		}
    	}
    };
    
    $.fn.Mydialog = function(opt){
        var mydialog = new Mydialog(opt);
        
        mydialog.init();
    };
}(jQuery,window,document));