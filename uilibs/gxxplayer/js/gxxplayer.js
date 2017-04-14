var globalOcxPlayer;
var speed = 0;

function getI18NStr(key) {
	return parent.getI18NStr(key);
}

$('document').ready(function(){
	
	$('#playSpeed').append(getI18NStr('html.currate') + ': 1x');
	$('[data-i18ntitle]').each(function() {
		var $this = $(this);
		$this.attr('title', getI18NStr($this.attr('data-i18ntitle')));
	});
	
	InitPlay();
	CreateRePlay();
	
	$('#btn_play').show();
	$('#btn_suspend').hide();
	$('#btn_nsound').hide();
	$('#btn_sound').show();
	
	//document.getElementById("bar_left").style.width = '24px';
	//new scale('btn','bar');
	
	$('#playControll').on('click', 'i', playControll);
});

window.onunload = function(){
  	if(!globalOcxPlayer){
		return;
	}
  	
  	var data = {};
//	data.action = 'ReplayCtrl';


//	data['arguments'] = {};
//	data['arguments']['nCtrlType'] = 9;
//	data['arguments']['lParam'] = 0;
//	data['arguments']['wParam'] = 0;
//	data['arguments']['nIndex'] = 1;
//	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
//		
	data = {}
	data.action = 'Delete';
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
	
	data = {};
	data.action = 'LogOut';                                   
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
};

function playControll(event)
{
	switch(event.target.parentElement.id){
		case 'btn_play'		: 
			controlRec(0, 0, 0, 1);
			$('#btn_play').hide();
			$('#btn_suspend').show();
			break;
		case 'btn_suspend' 	: 
			controlRec(1, 0, 0, 1);
			$('#btn_suspend').hide();
			$('#btn_play').show();
			break;
		case 'btn_stop' 	:
		{
			controlRec(9, 0, 0, 1);
			ctrlPlaySpeed(-1);
			break;
		}
		case 'btn_forward' 	:
		{
			ctrlPlaySpeed(1);
			controlRec(2, 0, 0, 1);break;
		}
		case 'btn_backward' : 
		{
			
			ctrlPlaySpeed(0);
			controlRec(3, 0, 0, 1);break;
		}
		case 'btn_forframe' :
			controlRec(10, 0, 0, 1);break;
		case 'btn_backframe':
			controlRec(4, 0, 0, 1);break;
		case 'btn_nsound':
			SoundCtrl(1);
			$('#btn_nsound').hide();
			$('#btn_sound').show();
			break;
		case 'btn_sound':
			SoundCtrl(0);
			$('#btn_sound').hide();
			$('#btn_nsound').show();
			break;
	}
}

function ctrlPlaySpeed(type)
{
	if(type == 1){
		if(speed >= 3){
			return;
		}
			
		speed++;
	}
	else if(type == 0){
		if(speed <= -3){
			return;
		}
		
		speed--;
	}
	else{
		speed = 0;
	}
	
	$('#playSpeed').html(getI18NStr('html.currate') + ': '+ (speed>=0?1:-1)*Math.pow(2, Math.abs(speed)) +'x');
}

// 获取窗口状态
function getWndStateByIndex()
{
	if (globalOcxPlayer == null) {
        return;
    }

	var data = {};
	data.action = 'GetLiveDispWndInfo';
	data['arguments'] = {};
	data['arguments'].nIndex = 0;
	var str = JSON.stringify(data);
	var ret = globalOcxPlayer.GS_ReplayFunc(str);
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

//回调入口函数
function _onOcxEventProxy(data){		

}

function InitPlay(){
	globalOcxPlayer = document.getElementById('gxxPlayOcx');
	globalOcxPlayer.style.display = "block";
	globalOcxPlayer.RegJsFunctionCallback(_onOcxEventProxy); 
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify({'action':'InitDeviceSdk'}));	
}

//创建录像视图
function CreateRePlay() {
    
    if (globalOcxPlayer == null) {
        alert(getI18NStr('alert.videoNotLoad'));
        return;
    }

    var data = {};
    data.action = 'InitPara';						//设置视图标识，作为每个视图回调事件的标识
    data['arguments'] = {};
    data['arguments']['ocxID'] = "ReplayView"; 		//用户自定义
    globalOcxPlayer.GS_RealTimeFunc(JSON.stringify(data));
    
    data = {};
    data.action = 'InitReplayWnd'; 					//创建录像视图
    data['arguments'] = {};
    data['arguments']['nDefaultSplit'] = 1;
    data['arguments']['nMaxSplit'] = 4;
    globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
}

//登录SS服务
function LoadSS(ip, port, userName, passWord){
	
	//logOut();
	
	if(globalOcxPlayer == null){
		alert(getI18NStr('alert.videoNotLoad'));
		return ;
	}

	var data 						= {};
    data.action 					= 'Login_VodServer';
    data['arguments'] 				= {};
    data['arguments']['strIp']		= ip;
    data['arguments']['userName'] 	= userName;
    data['arguments']['passWord'] 	= passWord;
    data['arguments']['nPort'] 		= parseInt(port);

    var ret = globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
    ret = eval('(' + ret + ')');
    if (ret.code != 0) {
        //alert("登录失败,错误码:" + ret.code);
        alert(getI18NStr('alert.loginFail'));
        return;
    }
}

//播放视频
function PlayRec(path, startTime, endTime){
	
	if(globalOcxPlayer == null){
		alert(getI18NStr('alert.videoNotLoad'));
		return ;
	}

	var data 							= {};
    data.action 						= 'Replay_Ex';
    data['arguments'] = {};
    data['arguments']['recordUrl'] 		= /*'local://'+*/path;
    data['arguments']['szStartTime'] 	= startTime;
    data['arguments']['szEndTime'] 		= endTime;
    data['arguments']['nIndex'] 		= -1;   //填写-1默认选择空闲窗口

    globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
    
    $('#btn_play').hide();
	$('#btn_suspend').show();
	
	ctrlPlaySpeed(-1);
	
	SoundCtrl(1);

	setTimeout(updateMarker, 1000);
}

function updateMarker()
{
	if(0 == getTime()){
		ctrlPlaySpeed(-1);
		parent.clearMarker();
	}
	
	parent.updateMarker(getTime());
	
	setTimeout(updateMarker, 1000);
}

function getTime()
{
	if(globalOcxPlayer == null){
		return ;
	}
	
	var data = {};	
	data.action = 'GetPlayTimeStamp';                                         
	data['arguments'] = {};
	data['arguments']['nIndex'] = 1;

	var str = JSON.stringify(data);
	var ret = globalOcxPlayer.GS_ReplayFunc(str);
	ret = eval('(' + ret + ')');
	if(ret.code != 0){
		return 0;
	}
	
	return ret.data.nTimeStamp;
}


//关闭视频
function StopRec() {
    
    if (globalOcxPlayer == null) {
        alert(getI18NStr('alert.videoNotLoad'));
        return;
    }

    var data 						= {};
    data.action 					= 'ReplayCtrl';
    data['arguments'] 				= {};
    data['arguments']['nCtrlType'] 	= 9;
    data['arguments']['lParam'] 	= 0;
    data['arguments']['wParam'] 	= 0;
    data['arguments']['nIndex'] 	= 1;

 	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
 	
 	parent.clearMarker();
}

//退出 
function logOut(){
	if(globalOcxPlayer == null){
		return;
	}
	
	var data = {};
	data.action = 'LogOut';                                       
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
}

//释放控件
function Delete(){
	
	if(globalOcxPlayer == null){
		return;
	}
	
	var data = {};
	data.action = 'Delete';                                     
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
}

function controlRec(type, lParam, wParam, index)
{
	if(globalOcxPlayer == null){
		alert(getI18NStr('alert.videoNotLoad'));
		return ;
	}
	
	var data = {};					
	data.action = 'ReplayCtrl';                                         
	data['arguments'] = {};
	data['arguments']['nCtrlType'] 	= type;
	data['arguments']['lParam'] 	= lParam;
	data['arguments']['wParam'] 	= wParam;
	data['arguments']['nIndex']	 	= index;
	
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
}

// 禁音开关
function SoundCtrl(value)
{
	if(globalOcxPlayer == null){
		return ;
	}
	
	var data = {};					
	data.action = 'SoundCtrl';                                         
	data['arguments'] = {};
	data['arguments']['nIndex'] = 1;  
	data['arguments']['nCtrlType'] = value;
	data['arguments']['nVideoType'] = 2;
	
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
}

// 音量调整
function SetVolume(value)
{
	if(globalOcxPlayer == null){
		return ;
	}
	
	var data = {};					
	data.action = 'SetVolume';                                         
	data['arguments'] = {};
	data['arguments']['nIndex'] = 1;   
	data['arguments']['nVolume'] = parseInt(value);
	
	globalOcxPlayer.GS_ReplayFunc(JSON.stringify(data));
}

scale = function (btn,bar){
	this.btn=document.getElementById(btn);
	this.bar=document.getElementById(bar);
	this.step=this.bar.getElementsByTagName("DIV")[0];
	this.init();
};
scale.prototype={
	init:function (){
		var f=this,g=document,b=window,m=Math;
		f.btn.onmousedown=function (e){
			var x=(e||b.event).clientX;
			var l=this.offsetLeft;
			var max=f.bar.offsetWidth-this.offsetWidth;
			g.onmousemove=function (e){
				var thisX=(e||b.event).clientX;
				var to=m.min(max,m.max(-2,l+(thisX-x)));
				f.btn.style.left=to+'px';
				f.ondrag(m.round(m.max(0,to/max)*100),to);
				b.getSelection ? b.getSelection().removeAllRanges() : g.selection.empty();
			};
			g.onmouseup=new Function('this.onmousemove=null');
		};
	},
	ondrag:function (pos,x){
		this.step.style.width=Math.max(0,x)+'px';
		
		SetVolume(pos);
	}
};
