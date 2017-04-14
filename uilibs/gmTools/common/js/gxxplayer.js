/**
 * 高新兴播放器
 */
(function(window,document){
	
	var defaults = {
		'element': '',
		'proxy'  : function(){return;},
		'captureSavePath' : 'E:\\capture',
		'videoSavePath'   : 'E:\\capture',
		'nMaxSplit'		  : 16,
		'nDefaultSplit'	  : 1
	};
	
	function Gxxplayer()
	{
		return this;
	}
	
	Gxxplayer.prototype = {
		init : function(opt){
			// 配置信息
			this.settings = extend({}, defaults, opt);
			// 实时视频登陆信息
			this.loginInfo= [];
			// 语音对讲句柄
			this.lTalkHandle = -1;
			
			if(this.settings.element.lenght == 0){
				return "未指定插件元素";
			}
			
			// 初始化设备SDK
			this.player = document.getElementById(this.settings.element);
			this.player.style.display = "block";
			this.player.RegJsFunctionCallback(this.settings.proxy);
			this.player.GS_ReplayFunc(JSON.stringify({'action' : 'InitDeviceSdk'}));
			
			// 创建实时试图
			this.createRealTimeView();
			
			return 0;
		},
		uninit : function(){
			if(!this.player){
				return 0;
			}
			
			var data = {};
			data.action = 'Delete';
			this.player.GS_ReplayFunc(JSON.stringify(data));
			
			data = {};
			data.action = 'LogOut';                                   
			this.player.GS_ReplayFunc(JSON.stringify(data));
			
			this.settings    = {};
			this.loginInfo   = [];
			this.lTalkHandle = -1;
			return 0;
		},
		createRealTimeView : function(){
			if(!this.player){
				return 0;
			}
			
			var data = {};
		    data.action = 'InitPara';                    							//设置视图标识，作为每个视图回调事件的标识
		    data['arguments'] = {};
		    data['arguments']['ocxID'] = "RealTimeView";							//用户自定义
		    this.player.GS_RealTimeFunc(JSON.stringify(data));
		
		    data = {};
		    data.action = 'SetConfigParam';                         				//设置视图标识，作为每个视图回调事件的标识
		    data['arguments'] = {};
		    data['arguments']['captureSavePath'] = this.settings.captureSavePath; 	//抓图保存路径
		    data['arguments']['savePath'] = this.settings.videoSavePath; 			//录像下载跟本地录像保存路径
		    this.player.GS_RealTimeFunc(JSON.stringify(data));
		    
		    data = {};
		    data.action = 'InitMonitorWnd';                                         //创建实时视图
		    data['arguments'] = {};
		    data['arguments']['nDefaultSplit'] = this.settings.nDefaultSplit;
		    data['arguments']['nMaxSplit'] = this.settings.nMaxSplit;
		    this.player.GS_RealTimeFunc(JSON.stringify(data));
		    
		    return 0;
		},
		login : function(opt){
			if(!this.player){
				return '控件尚未加载';
			}
			
			var data = {};
				
			//流媒体序号
			var index = this.loginInfo.length;
			for(var i=0; i<index; ++i){
				var item = this.loginInfo[i];
				if(item.ssIp==opt.ssIp && item.ssPort==opt.ssPort){
					if(item.status == 1){
						return 0;
					}
					else{
						index = i;
						break;
					}
				}
			}
		
		    data.action = 'Login_SSServer';
		    data['arguments'] = {};
		    data['arguments']['nIndex'] = index + 1;
		    data['arguments']['strIp'] = opt.ssIp;
		    data['arguments']['nPort'] = opt.ssPort;
		    data['arguments']['userName'] = opt.ssUsername;
		    data['arguments']['passWord'] = opt.ssPasswd;
		    var ret = this.player.GS_RealTimeFunc(JSON.stringify(data));
		    ret = eval('(' + ret + ')');
		    if (ret.code != 0) {
				return "登陆失败";
		    }
		    
		    if(index == this.loginInfo.length){
				opt.status = 1;
				this.loginInfo.push(opt);
			}
		    else{
				this.loginInfo[index].status = 1;
			}

			return 0;
		},
		logoff : function(opt){
			if(!this.player){
				return '控件尚未加载';
			}

			var data = {};
				
			//流媒体序号
			var index = this.loginInfo.length;
			for(var i=0; i<index; ++i){
				var item = this.loginInfo[i];
				if(item.ssIp==opt.ssIp && item.ssPort==opt.ssPort){
					if(item.status == 1){
						index = i;
						break;
					}else{
						return "尚未登陆流媒体服务器";
					}
				}
			}
		
		    data.action = 'Logout_SSServer';
		    data['arguments'] = {};
		    data['arguments']['nIndex'] = index + 1;		
		    var ret = this.player.GS_RealTimeFunc(JSON.stringify(data));
		    ret = eval('(' + ret + ')');
		    if (ret.code != 0) {
				return "登出失败";
		    }
		    
		    this.loginInfo[index].status = 0;
			return 0;
		},
		splitWnd : function(total, row, column){

			if(!this.player){
				return '控件尚未加载';
			}
			
			var data = {};
			data.action = 'ChangeLiveDispSplit';
			data['arguments'] = {};
			data['arguments']['nDispSplit'] = total;
			row && (data['arguments']['nRow'] = row);
			column && (data['arguments']['nColumn'] = column);
			this.player.GS_RealTimeFunc(JSON.stringify(data));

			return 0;
		},
		getStatusByIndex : function(n){
			if(!this.player){
				return '控件未加载';
			}
			
			var data = {};
			data.action = 'GetLiveDispWndInfo';                                         
			data['arguments'] = {};
			data['arguments']['nIndex'] = n;
		
			var ret = this.player.GS_ReplayFunc(JSON.stringify(data));
			ret = eval('(' + ret + ')');
			if(ret.code != 0){
				return -1;
			}	
			
			return ret.data.dispStatus;
		},
		playRec : function(opt){

			if(!this.player){
				return '控件尚未加载';
			}
			
			var data = {};
			
			//流媒体序号
			var index = this.loginInfo.length;
			for(var i=0; i<index; ++i){
				var item = this.loginInfo[i];
				if(item.ip==opt.ip && item.port==opt.port){
					if(item.status == 1){
						index = i;
						break;
					}else{
						return "尚未登陆流媒体服务器";
					}
				}
			}
			
			if(index == this.loginInfo.length){
				return "尚未登陆流媒体服务器";
			}
		
		    data.action = 'PlayRealVideoBySS';
		    data['arguments'] = {};
		    data['arguments']['nSSIndex'] = index + 1;			//流媒体序号
		    data['arguments']['szDevID'] = opt.devId;
		    data['arguments']['nStreamType'] = 1;
		    data['arguments']['nDevType'] = 150001;         	//高新兴设备-150001；海康-30001
		    data['arguments']['nIndex'] = -1;   				//填写-1默认选择空闲窗口
		    this.player.GS_RealTimeFunc(JSON.stringify(data));

			return 0;
		},
		stopRec : function(index){
			if(!this.player){
				return '控件尚未加载';
			}

			var data = {};
			data.action = 'CloseRealVideo';
			data['arguments'] = {};
			data['arguments']['nIndex'] = index;
			this.player.GS_RealTimeFunc(JSON.stringify(data));
		},
		startTalk : function (opt){
			
			if(!this.player){
				return '控件尚未加载';
			}
			
			//流媒体序号
			var index = this.loginInfo.length;
			for(var i=0; i<index; ++i){
				var item = this.loginInfo[i];
				if(item.ip==opt.ip && item.port==opt.port){
					if(item.status == 1){
						index = i;
						break;
					}else{
						return "尚未登陆流媒体服务器";
					}
				}
			}
			
			if(index == this.loginInfo.length){
				return "尚未登陆流媒体服务器";
			}
			
			if (this.lTalkHandle != -1) {
			    StopTalk();
			}
			
			var data = {};
			data.action = 'StartTalk';                                         
			data['arguments'] = {};
			data['arguments']['nIndex'] = index + 1;				//流媒体序号
			data['arguments']['szDevID'] = opt.devId;
			data['arguments']['nDevType'] = 150001;
			var ret = this.player.GS_RealTimeFunc(JSON.stringify(data));
			ret = eval('(' + ret + ')');
			this.lTalkHandle = ret.code;
			if (this.lTalkHandle == -1) {
			    return "启用语音对讲失败";
			}
			
			return 0;
		},
		stopTalk : function (){

			if(!this.player){
				return '控件尚未加载';
			}
			
			var data = {};
			data.action = 'StopTalk';                                         
			data['arguments'] = {};
			data['arguments']['lHandle'] = this.lTalkHandle;
			this.player.GS_RealTimeFunc(JSON.stringify(data));
			
			this.lTalkHandle = -1;
			return 0;
		},
		// 获取插件版本
		getVersion : function(){
			if(!this.player){
				return -1;
			}
			
			var data = {};
			data.action = 'GetVersion';
			var str = JSON.stringify(data);
			var ret = this.player.GS_ReplayFunc(str);
			ret = eval('(' + ret + ')');
			if (ret.code != 0) {
				return -1;
			}
			
			return ret.data.version;
		},
		// 隐藏/显示 视频工具条  1-显示 0-隐藏
		toggleTools : function(visible){
			if(!this.player){
				return "控件未加载";
			}
			
			var data = {};
			data.action = 'SetLiveWndStyle';
			data['arguments'] = {};
			data['arguments'].nWindowStyle = visible;
			var str = JSON.stringify(data);
			var ret = this.player.GS_ReplayFunc(str);
			ret = eval('(' + ret + ')');
			if (ret.code != 0) {
				return visible==1 ? "显示工具条失败":"隐藏工具条失败";
			}
			
			return 0;
		},
		// 获取窗口状态
		getWndStateByIndex : function(index){
			if(!this.player){
				return -1;
			}

			var index = document.getElementById('splitNum').value;
			var data = {};
			data.action = 'GetLiveDispWndInfo';
			data['arguments'] = {};
			data['arguments'].nIndex =  parseInt(index);
			var str = JSON.stringify(data);
			var ret = this.player.GS_ReplayFunc(str);
			ret = eval('(' + ret + ')');
			if (ret.code != 0) {
				return -1;
			}

			// typedef enum EnumDispWndState
			// {
			// 	DISP_FREE = 0,
			// 	DISP_CONNECTING,
			// 	DISP_PLAYING,
			// 	DISP_CLOSING,
			// 	DISP_DEVOFF,
			// 	DISP_ERROR,
			// }ENUM_DISP_WND_STATE;
			return ret.data.dispStatus;
		}
	};
	
	var extend = function() {
		var src, copyIsArray, copy, name, options, clone,
			target = arguments[ 0 ] || {},
			i = 1,
			length = arguments.length,
			deep = false;
	
		// Handle a deep copy situation
		if ( typeof target === "boolean" ) {
			deep = target;
	
			// skip the boolean and the target
			target = arguments[ i ] || {};
			i++;
		}
	
		// Handle case when target is a string or something (possible in deep copy)
		if ( typeof target !== "object" && !jQuery.isFunction( target ) ) {
			target = {};
		}
	
		// extend jQuery itself if only one argument is passed
		if ( i === length ) {
			target = this;
			i--;
		}
	
		for ( ; i < length; i++ ) {
	
			// Only deal with non-null/undefined values
			if ( ( options = arguments[ i ] ) != null ) {
	
				// Extend the base object
				for ( name in options ) {
					src = target[ name ];
					copy = options[ name ];
	
					// Prevent never-ending loop
					if ( target === copy ) {
						continue;
					}
	
					// Recurse if we're merging plain objects or arrays
					if ( deep && copy && ( jQuery.isPlainObject( copy ) ||
						( copyIsArray = jQuery.isArray( copy ) ) ) ) {
	
						if ( copyIsArray ) {
							copyIsArray = false;
							clone = src && jQuery.isArray( src ) ? src : [];
	
						} else {
							clone = src && jQuery.isPlainObject( src ) ? src : {};
						}
	
						// Never move original objects, clone them
						target[ name ] = jQuery.extend( deep, clone, copy );
	
					// Don't bring in undefined values
					} else if ( copy !== undefined ) {
						target[ name ] = copy;
					}
				}
			}
		}
	
		// Return the modified object
		return target;
	};
	
	window.Gxxplayer = Gxxplayer;
}(window, document, undefined));
