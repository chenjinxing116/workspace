var centerCity = '广州';
var markerArr = [];
var uEzMap = null;
window.onload = function(){
	 //1） ********构造地图控件对象，用于装载地图
	  uEzMap = new EzMap(document.getElementById("allmap"));
	  
	  //2）********初始化地图，并显示地图
	  uEzMap.initialize();
	  
	  //3）********以下为其它一些附属功能
	  // 显示左侧导航工具条
	  uEzMap.showMapControl();
	  
	  $('.noprint').not(':empty').css('height', 19);
};

// 清空覆盖物
function remove_overlay(){
	uEzMap.clear();
	markerArr = [];
}

function setMapCenter(data){
	EzMap.centerAtLatLng(new Point('' + data.detail.gps.longitude + ',' + data.detail.gps.latitude));
}

function isShowMap(deviceId){
    for(var j =0,len=markerArr.length;j< len;j++){
        if(markerArr[j].dev == deviceId){
        	return true;
        }
    }
    
    return false;
}

//创建标签
function createMarker(data, center){
	if(isShowMap(data.detail.gps.deviceId)){
		center && setMapCenter(data);
		return;
	}
	
	var uIcon = new Icon();
	uIcon.image="ic_police1_0.png";
	uIcon.height=35;
	uIcon.width=47;
	uIcon.topOffset=-17;
	uIcon.leftOffset=23;
	var point = new Point('' + data.detail.gps.longitude + ',' + data.detail.gps.latitude);
	var marker = new Marker(point, uIcon, new Title(data.user_name, 12, 7, "微软雅黑", "#7870D7", "", null, 0));
	marker.addListener("click", function(){
			var content = "";
			content += '<div class="container">';
			    content += '<p>' + getI18NStr('msg.nameAndID') + ': ' + data.user_name + '(' + data.user_code + ')</p>';
			    content += '<p>' + getI18NStr('msg.deviceID') + ': ' + data.detail.gps.deviceId + '</p>';
				content += '<p>' + getI18NStr('msg.batteryPercent') + ': ' + data.detail.dsjStatus.battery + '%</p>';
				content += '<p>' + getI18NStr('msg.speed') + ': ' + data.detail.gps.speed + 'km/h</p>';
				content += '<p>' + getI18NStr('msg.signal') + ': ' + '4G</p>';
				content += '<div style="margin-bottom: 5px;">';
					content += '<button class="btn btn-success btn-sm" style="margin-right: 5px;" onclick="playSS(\''+data.detail.gateway.extend.ssIp+'\',\''+data.detail.gateway.extend.ssPort+'\',\''+data.detail.gps.deviceId+'\');">' + getI18NStr('msg.videoCall') + '</button>';
					content += '<button class="btn btn-success btn-sm" onclick="startVideo(\''+data.detail.gps.deviceId+'\',\''+ data.accountId +'\');">' + getI18NStr('msg.record') + '</button>';
				content += '</div>';
				content += '<div">';
					content += '<button class="btn btn-success btn-sm" style="margin-right: 5px;" onclick="startTalk(\''+data.detail.gateway.extend.ssIp+'\',\''+data.detail.gateway.extend.ssPort+'\',\''+data.detail.gps.deviceId+'\');">' + getI18NStr('msg.startTalk') + '</button>';
					content += '<button class="btn btn-success btn-sm" onclick="stopTalk();">' + getI18NStr('msg.stopTalk') + '</button>';
				content += '</div>';
			content += '</div>';
		
		marker.openInfoWindowHtml(content);
	});
	
	uEzMap.addOverlay(marker);
	uEzMap.centerAtLatLng(point);
	
	marker.id  = data.user_code;
	marker.dev = data.detail.gps.deviceId;
	
	markerArr.push(marker);
}

// 删除标签
function removerMarker(data){
	for(var i =0;i<markerArr.length;i++)
    {
        markerTmp = markerArr[i];
        if(markerArr[i].id == data)
        {
        	uEzMap.removeOverlay(markerArr[i]);
            markerArr.splice(i--, 1);
        }
    }
}

function playSS(ssIp, ssPort, deviceId){
	parent.playSS(ssIp, ssPort, deviceId);
}
function startVideo(deviceId, acountId){
	parent.startVideo(deviceId, acountId);
}
function startTalk(ssIp, ssPort, deviceId){
	parent.startTalk(ssIp, ssPort, deviceId);
}
function stopTalk(){
	parent.stopTalk();
}

//删除所有轨迹
function removerAll()
{
	uEzMap.clear();     
}
//绘制轨迹
function historyTrack(points){
    var pointstr = "";
    for (var i = 0; i < points.length; i++){
		if(i != 0){
			pointstr += ','; 
		}
		
		pointstr += points[i].longitude + ',' + points[i].latitude
    }
    
    createHisMarker(null, points[0].longitude, points[0].latitude, points[0].timeDisplay);
    createHisMarker(null, points[points.length-1].longitude, points[points.length-1].latitude, points[points.length-1].timeDisplay);
	
	var uLine = new Polyline(pointstr, "#ff0000", 2, 0.8);// 构造一个多义线对象
	uEzMap.addOverlay(uLine);
	uEzMap.centerAtLatLng(uLine.getPoints()[0]);
}

/**
 * 创建单个历史轨迹点
 * @param {数组} data 包含：标注唯一识别标识id 经度 lon 纬度 la uid name pno sn battery speed storage sign address
 */
//var infoWindow;
function createHisMarker(id,lon,la,submit_time){
	var uIcon = new Icon();
	uIcon.image="ic_police1_0.png";
	uIcon.height=35;
	uIcon.width=47;
	uIcon.topOffset=-17;
	uIcon.leftOffset=23;
	var point = new Point('' + lon + ',' + la);
	var marker = new Marker(point, uIcon, new Title(submit_time, 12, 7, "微软雅黑", "#7870D7", "", 'red', 0));
	
	uEzMap.addOverlay(marker);
	uEzMap.centerAtLatLng(point);
	
    return marker;
}

var mediaMarker;
function createMediaMarker(data, center){
	mediaMarker = createHisMarker(null, data.longitude, data.latitude, data.time);
}
function updateMediaMarker(data){
	clearMediaMarker();
	createMediaMarker(data, false);
}
function clearMediaMarker(){
	mediaMarker && uEzMap.removeOverlay(mediaMarker);
	mediaMarker = null;
}

function drawFence(handler){
	var input1 = document.createElement('input');
	var input2 = document.createElement('input');
	uEzMap.changeDragMode("drawRect", null, null, function(evt){
		var points = evt.split(',');
		var point = {
    		xmin: points[0],
    		ymin: points[1],
    		xmax: points[2],
    		ymax: points[3]
    	};
    	
    	handler(point);
	});
}

function clearFence(){
	uEzMap.clear();     
}

function showFence(points){
	var xmin = Math.min(points[0].longitude, points[1].longitude, points[2].longitude, points[3].longitude);
	var xmax = Math.max(points[0].longitude, points[1].longitude, points[2].longitude, points[3].longitude);
	var ymin = Math.min(points[0].latitude, points[1].latitude, points[2].latitude, points[3].latitude);
	var ymax = Math.max(points[0].latitude, points[1].latitude, points[2].latitude, points[3].latitude);
	
	var pointstr = [xmin, ymin, xmax, ymax].join();

	var uRect = new Rectangle(pointstr, "red", 2, 0.25, "black");// 构造一个矩形对象
	uEzMap.addOverlay(uRect);
	uEzMap.centerAtLatLng(uRect.getPoints()[0]);
}

//创建标签
function createJQMarker(accountId, name, lon, lat, center)
{
	var uIcon = new Icon();
	uIcon.image="ic_police1_0.png";
	uIcon.height=35;
	uIcon.width=47;
	uIcon.topOffset=-17;
	uIcon.leftOffset=23;
	var point = new Point('' + lon + ',' + lat);
	var marker = new Marker(point, uIcon, new Title(name, 12, 7, "微软雅黑", "#7870D7", "", null, 0));
	
	uEzMap.addOverlay(marker);
	uEzMap.centerAtLatLng(point);
	
	marker.id  = accountId;	
	markerArr.push(marker);
}