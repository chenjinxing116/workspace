var centerCity = '广州';
var markerArr = [];
var map;
window.onload = function(){
	map = new BMap.Map("allmap");// 创建Map实例    
	map.centerAndZoom(centerCity, 12);
	map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
	map.enableScrollWheelZoom(true);
	
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	
	map.addControl(top_left_control);        
	map.addControl(top_left_navigation);
};

// 清空覆盖物
function remove_overlay(){
	map.clearOverlays();         
}

function setMapCenter(data){
	var point = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
	point = GPS.bd_encrypt(point.lat, point.lon);
	map.centerAndZoom(new BMap.Point(point.lon, point.lat), 12);
}
function isShowMap(deviceId)
{
    for(var j =0,len=markerArr.length;j< len;j++){
        if(markerArr[j].dev == deviceId){
        	return true;
        }
    }
    
    return false;
}
//创建标签
function createMarker(data, center)
{
	var point = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
	point = GPS.bd_encrypt(point.lat, point.lon);
	data.detail.gps.latitude = point.lat;
	data.detail.gps.longitude= point.lon;
	
	if(isShowMap(data.detail.gps.deviceId)){
		center && map.centerAndZoom(new BMap.Point(point.lon, point.lat), 12);
		return;
	}

	var pt      = new BMap.Point(point.lon, point.lat);
	var myIcon  = new BMap.Icon("ic_police1_0.png", new BMap.Size(47, 35));
	var marker = new BMap.Marker(pt, {icon:myIcon});
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
		
	var infoWindow = new BMap.InfoWindow(content);
	map.addOverlay(marker);  
	
	var label = new BMap.Label(data.user_name, {offset:new BMap.Size(0, -18)});
	label.setStyle({color:"#2f94fc",border:"none",'max-width':'none'});
	marker.setLabel(label);
	
	center && map.centerAndZoom(new BMap.Point(point.lon, point.lat), 12);
	
	marker.addEventListener("click", function(){
		this.openInfoWindow(infoWindow);
	});
	
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
        	map.removeOverlay(markerArr[i]);
            markerArr.splice(i--, 1);
            break;
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


//var lushu = null;
//删除所有轨迹
function removerAll()
{
	map.clearOverlays();     
}
//绘制轨迹
function historyTrack(points){
    var a = [];
    var point;
    for (var i = 0; i < points.length; i++){
		point = GPS.gcj_encrypt(parseFloat(points[i].latitude), parseFloat(points[i].longitude));
		point = GPS.bd_encrypt(point.lat, point.lon);
        a[i] = new BMap.Point(point.lon, point.lat);
    }
    
    createHisMarker(null, points[0].longitude, points[0].latitude, points[0].timeDisplay);
    createHisMarker(null, points[points.length-1].longitude, points[points.length-1].latitude, points[points.length-1].timeDisplay);

    var polyline = new BMap.Polyline(a, {strokeColor:"red", strokeWeight:2, strokeOpacity:0.8}); 
	map.addOverlay(polyline);
	
	var viewport=map.getViewport(a);
    map.setViewport(viewport);//设置缩放级别
	
//	var myIcon = new BMap.Icon("ic_police1_0.png", new BMap.Size(47, 35));
//  lushu = new BMapLib.LuShu(map, a, {
//              defaultContent:"",
//              autoView:true,
//              icon  : myIcon,
//              speed: 450,
//              landmarkPois:[]
//  });  
}
/**
 * 创建单个历史轨迹点
 * @param {数组} data 包含：标注唯一识别标识id 经度 lon 纬度 la uid name pno sn battery speed storage sign address
 */
//var infoWindow;
function createHisMarker(id,lon,la,submit_time){
    var opts = {width:200,height:150,enabelMessage:true};
    
    var SSpoint = GPS.gcj_encrypt(parseFloat(la), parseFloat(lon));
	SSpoint = GPS.bd_encrypt(SSpoint.lat, SSpoint.lon);
    
    var point  = new BMap.Point(SSpoint.lon, SSpoint.lat);//图片
    var myIcon = new BMap.Icon("ic_police1_0.png", new BMap.Size(47, 35));
    var marker = new BMap.Marker(point, {icon:myIcon});
    map.addOverlay(marker);
    
    var _label = new BMap.Label(submit_time,{offset:new BMap.Size(0,-18)});
    _label.setStyle({color : "#2f94fc",border:"none"});
    marker.setLabel(_label);
    return marker;
       
//  var content = "";
//  content += "<div id='markHis'><span class='title'>历史轨迹演示</span>";
//  content += "<div id='bGroup'><button class='btn btn-primary' onclick='lsStart()'>开始</button><button class='btn btn-primary' onclick='lsPause()'>暂停</button><button class='btn btn-primary' onclick='lsStop()'>取消</button></div></div>";
//  content += "<p>采集时间："+submit_time+"</p><p>经&nbsp;&nbsp;&nbsp;&nbsp;度："+lon+"</p><p>纬&nbsp;&nbsp;&nbsp;&nbsp;度："+la+"</p>";
//
//  marker.addEventListener("click", function(e){
//      var p = e.target;
//      var point = new BMap.Point(p.getPosition().lng,p.getPosition().lat);
//      infoWindow = new BMap.InfoWindow(content,opts);//创建窗口对象
//      infoWindow.disableCloseOnClick();
//      this.openInfoWindow(infoWindow,point);//开启窗口
//  });
}

////开始 
//function lsStart(){
//  lushu.start();
//};
////取消
//function lsStop(){
//  lushu.stop();
//};
////暂停
//function lsPause(){
//  lushu.pause();
//};

var mediaMarker;
function createMediaMarker(data, center){
	mediaMarker = createHisMarker(null, data.longitude, data.latitude, data.time);
}
function updateMediaMarker(data){
	clearMediaMarker();
	createMediaMarker(data, false);
}
function clearMediaMarker(){
	mediaMarker && map.removeOverlay(mediaMarker);
	mediaMarker = null;
}

function drawFence(handler){
    var styleOptions = {
        strokeColor:"red",    //边线颜色。
        fillColor:"black",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 2,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.25,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    }
    //实例化鼠标绘制工具
    var drawingManager = new BMapLib.DrawingManager(map, {
        isOpen: false, //是否开启绘制模式
        enableDrawingTool: false, //是否显示工具栏
        drawingToolOptions: {
            anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
            offset: new BMap.Size(5, 5), //偏离值
        },
        rectangleOptions: styleOptions //矩形的样式
    });  
	 //添加鼠标绘制工具监听事件，用于获取绘制结果
    drawingManager.addEventListener('overlaycomplete', function(e){
    	
    	var xmin = Math.min(e.overlay.ro[0].lng, e.overlay.ro[1].lng, e.overlay.ro[2].lng, e.overlay.ro[3].lng);
    	var xmax = Math.max(e.overlay.ro[0].lng, e.overlay.ro[1].lng, e.overlay.ro[2].lng, e.overlay.ro[3].lng);
    	var ymin = Math.min(e.overlay.ro[0].lat, e.overlay.ro[1].lat, e.overlay.ro[2].lat, e.overlay.ro[3].lat);
    	var ymax = Math.max(e.overlay.ro[0].lat, e.overlay.ro[1].lat, e.overlay.ro[2].lat, e.overlay.ro[3].lat);
    	
    	var pStart = GPS.bd_decrypt(ymin, xmin);
    	var pEnd   = GPS.bd_decrypt(ymax, xmax);
    	
    	pStart = GPS.gcj_decrypt(pStart.lat, pStart.lon);
		pEnd   = GPS.gcj_decrypt(pEnd.lat, pEnd.lon);
		
		var point = {
    		xmin: pStart.lon,
    		ymin: pStart.lat,
    		xmax: pEnd.lon,
    		ymax: pEnd.lat
    	};
    	
    	handler(point);
    });
    
    drawingManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);
	drawingManager.open();
}

function clearFence(){
	map.clearOverlays();
}

function showFence(points){
	var xmin = Math.min(points[0].longitude, points[1].longitude, points[2].longitude, points[3].longitude);
	var xmax = Math.max(points[0].longitude, points[1].longitude, points[2].longitude, points[3].longitude);
	var ymin = Math.min(points[0].latitude, points[1].latitude, points[2].latitude, points[3].latitude);
	var ymax = Math.max(points[0].latitude, points[1].latitude, points[2].latitude, points[3].latitude);
	
	var pStart = GPS.gcj_encrypt(ymin, xmin);
	var pEnd   = GPS.gcj_encrypt(ymax, xmax);
	
	pStart = GPS.bd_encrypt(pStart.lat, pStart.lon);
	pEnd   = GPS.bd_encrypt(pEnd.lat, pEnd.lon);
	
	pStart = new BMap.Point(pStart.lon, pStart.lat);
	pEnd = new BMap.Point(pEnd.lon, pEnd.lat);
	
	var rectangle = new BMap.Polygon([
		new BMap.Point(pStart.lng,pStart.lat),
		new BMap.Point(pEnd.lng,pStart.lat),
		new BMap.Point(pEnd.lng,pEnd.lat),
		new BMap.Point(pStart.lng,pEnd.lat)
	], {
        strokeColor:"red",    //边线颜色。
        fillColor:"black",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 2,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.25,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
  	});
	
	map.addOverlay(rectangle);         //增加矩形
}

//创建标签
function createJQMarker(accountId, name, lon, lat, center)
{
	var point = GPS.gcj_encrypt(parseFloat(lat), parseFloat(lon));
	point = GPS.bd_encrypt(point.lat, point.lon);

	var pt      = new BMap.Point(point.lon, point.lat);
	var myIcon  = new BMap.Icon("ic_police1_0.png", new BMap.Size(47, 35));
	var marker = new BMap.Marker(pt, {icon:myIcon});
	
	map.addOverlay(marker);  
	
	var label = new BMap.Label(name, {offset:new BMap.Size(0, -18)});
	label.setStyle({color:"#2f94fc",border:"none",'max-width':'none'});
	marker.setLabel(label);
	
	center && map.centerAndZoom(new BMap.Point(point.lon, point.lat), 12);

	marker.id  = accountId;
	markerArr.push(marker);
}