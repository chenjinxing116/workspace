/**
 * 部门下拉选择框
 * 依赖： jquery， bootstrap, bootstrap-typeahead
 * 
 */
;(function($, window){
	var pluginName = 'gmSelectorTypeahead';
	var defaults = {
		'source':   [],
		'val':   	'value',
		'display':	'name'
    };
    
    var gmSelectorTypeahead = function($ele, settings){
    	this.$ele     = $ele;
    	this.settings = $.extend({}, defaults, settings);
    };
    
    gmSelectorTypeahead.prototype = {
    	init: function(){
    		var selectstr = '<div class="input-group">';
    				selectstr+= '<input type="text" class="form-control" readonly style="background-color: #FFF;">';
					selectstr+= '<div class="input-group-btn">';
						selectstr+= '<button type="button" class="btn btn-default">';
							selectstr+= '<span class="caret"></span>';
						selectstr+= '</button>';
					selectstr+= '</div>';
				selectstr+= '</div>'
			
				selectstr+= '<div class="menuContainer" style="display:none; position: absolute; top: 34px;left: 0;right:0;">';
					selectstr+= '<input type="text" autocomplete="off" class="form-control " placeholder="请输入关键字">';
					selectstr+= '<input type="text" autocomplete="off" class="form-control placeholder" style="display:none;" value="请输入关键字">';
				selectstr+= '</div>';
				
    		this.$ele.append(selectstr);
    		
    		this.settings.itemSelected = this.itemSelected.bind(this);
    		
    		$('.menuContainer input:first', this.$ele).typeahead(this.settings);
    		
    		$('.input-group', this.$ele).on('click', this.showMenu.bind(this));
    		
    		var isOperaMini = Object.prototype.toString.call(window.operamini) === '[object OperaMini]';
		    var isInputSupported = 'placeholder' in document.createElement('input') && !isOperaMini;
		    var isTextareaSupported = 'placeholder' in document.createElement('textarea') && !isOperaMini;
			
			if(!isInputSupported || !isTextareaSupported){
				$('input.placeholder', this.$ele).show().prev().hide();
				
				$('input.placeholder', this.$ele).focus(function(){
					$(this).hide().prev().show().focus();
				});
				
				$('.menuContainer > input:first', this.$ele).blur(function(){
					if(this.value.length === 0){
						$(this).hide().next().show();
					}
				});
			}
    	},
    	showMenu: function(){
    		$(".menuContainer", this.$ele).slideDown('fast');
			$("body").bind("mousedown", this.onBodyDown.bind(this));
    	},
    	hideMenu: function(){
    		$(".menuContainer", this.$ele).slideUp("fast");
			$("body").unbind("mousedown", this.onBodyDown.bind(this));
    	},
    	onBodyDown: function(event) {
    		if($(event.target).closest(this.$ele).length > 0){
    			return;
    		}
    			
			this.hideMenu();
		},
    	itemSelected: function(item, val, text){
    		$('.input-group>input', this.$ele).val(text);
    		this.$ele.data('selValue', val);
    		this.hideMenu();
    	}
    };
	
	$.fn.gmTypeSelector = function(settings){
		var Selector = new gmSelectorTypeahead(this.length>1?$(this[0]):this, settings);		
		
		Selector.init();
	};
	
}(jQuery, window))
