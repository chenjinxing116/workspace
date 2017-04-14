var markerArr = new Array;//标签数组
var labelArr = new Array;//文本数组
var GisObject;

window.onload = function(){
	dojo.require("extras.MapInitObject");
	dojo.require("esri.geometry.Extent");
	dojo.require("esri.toolbars.draw");
	dojo.require("esri.symbols.SimpleFillSymbol");
	dojo.require("esri.symbols.SimpleLineSymbol");
	dojo.require("esri.symbols.SimpleMarkerSymbol");
	dojo.require("esri.symbols.PictureMarkerSymbol");
	dojo.require("esri.symbols.TextSymbol");
	dojo.require("esri.dijit.Scalebar");
	
	dojo.ready(function(){
		GisObject = new extras.MapInitObject("allmap");
		var tmpPoint = new esri.geometry.Point(113.2759952545166, 23.117055306224895);
		GisObject.setMapOptions({
			logo:false,
			extent : "12557877.595482401,2596928.9267310356,12723134.450635016,2688653.360673282",
			level:10,
			center:tmpPoint
		});
	
		GisObject.addDefaultLayers();
	});
};
//清除覆盖物
function remove_overlay(){
    GisObject.toolbar.drawLayer.clear();
    markerArr.splice(0,markerArr.length);
    labelArr.splice(0,labelArr.length);
}
/**
* 设置地图中心点
*/
function setMapCenter(lon, lat)
{
	//var point = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
    point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(parseFloat(lon), parseFloat(lat)));
    GisObject.map.centerAndZoom(point,13);
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
function createMarker(data, center)
{
	var point = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
	data.detail.gps.latitude = point.lat;
	data.detail.gps.longitude= point.lon;
	
	if(isShowMap(data.detail.gps.deviceId)){
		center && setMapCenter(point.lon, point.lat);
		return;
	}

	var iconUrl = "";
	var icon_h  = 35 / 2;
	var icon_w  = 35 / 2;
	switch(data.detail.gps.deviceType){
		case 100 :
			iconUrl = "./ic_police1_0.png";
			icon_h = 35 / 2; 
			icon_w = 47 / 2;
			break;
		case 2 :
			iconUrl = "./ic_camera.png";
			break;
		case 3 :
			iconUrl = "./ic_GH.png";
			break;
		case 4 :
			iconUrl = "./ic_police2_pLcar.png";
			break;
		
		default:
			iconUrl = "";
	}
	
	//var ggPoint = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
	//data.detail.gps.latitude = ggPoint.lat;
	//data.detail.gps.longitude= ggPoint.lon;
    var myIcon = new esri.symbols.PictureMarkerSymbol({"url":iconUrl,"height":icon_h,"width":icon_w,"type":"esriPMS",xoffset:0,yoffset:0});
    var cruPoint = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(point.lon, point.lat));
    var pictureSymbol = new esri.symbols.PictureMarkerSymbol(myIcon);//图片
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
	
    if(data.user_name){
        var ls = new esri.symbols.TextSymbol(data.user_name).setColor(new esri.Color([4, 128, 209, 0.9])).setFont(new esri.symbol.Font("12px").setWeight(esri.symbol.Font.WEIGHT_BOLD)).setOffset(-20, -28).setAlign(esri.symbol.TextSymbol.ALIGN_START);
        var pointLabel = new esri.graphic(cruPoint,ls);
        pointLabel.id  = data.user_code;
        pointLabel.name= data.user_name;
        pointLabel.dev = data.detail.gps.deviceId;
        labelArr.push(pointLabel);
        GisObject.toolbar.drawLayer.add(pointLabel);
    }
   
	var infoTemp = new esri.InfoTemplate(getI18NStr('map.policeInfo'), content);
	var marker = new esri.Graphic(cruPoint,pictureSymbol,{},infoTemp);
	marker.id  = data.user_code;
	marker.name= data.user_name;
	marker.dev = data.detail.gps.deviceId;
	marker.type = data.detail.gps.deviceType;
	markerArr.push(marker);
	GisObject.toolbar.drawLayer.add(marker);
	// GisObject.loadDefaultCluster(goPonit);//画点
	GisObject.map.infoWindow.hide();
	
	center && setMapCenter(point.lon, point.lat);
}

function createFenceMarker(data, center)
{
	var point = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
	data.detail.gps.latitude = point.lat;
	data.detail.gps.longitude= point.lon;
	
	if(isShowMap(data.detail.gps.deviceId)){
		center && setMapCenter(point.lon, point.lat);
		return;
	}

	var iconUrl = "";
	var icon_h  = 35 / 2;
	var icon_w  = 35 / 2;
	switch(data.detail.gps.deviceType){
		case 100 :
			iconUrl = "./ic_police1_0.png";
			icon_h = 35 / 2; 
			icon_w = 47 / 2;
			break;
		case 2 :
			iconUrl = "./ic_camera.png";
			break;
		case 3 :
			iconUrl = "./ic_GH.png";
			break;
		case 4 :
			iconUrl = "./ic_police2_pLcar.png";
			break;
		
		default:
			iconUrl = "";
	}
	
	//var ggPoint = GPS.gcj_encrypt(parseFloat(data.detail.gps.latitude), parseFloat(data.detail.gps.longitude));
	//data.detail.gps.latitude = ggPoint.lat;
	//data.detail.gps.longitude= ggPoint.lon;
    var myIcon = new esri.symbols.PictureMarkerSymbol({"url":iconUrl,"height":icon_h,"width":icon_w,"type":"esriPMS",xoffset:0,yoffset:0});
    var cruPoint = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(point.lon, point.lat));
    var pictureSymbol = new esri.symbols.PictureMarkerSymbol(myIcon);//图片
    var content = "";
  	
  	content += '<div class="container">';
	    content += '<p>' + getI18NStr('msg.nameAndID') + ': ' + data.user_name + '(' + data.user_code + ')</p>';
	    content += '<p>' + getI18NStr('msg.deviceID') + ': ' + data.detail.gps.deviceId + '</p>';
		content += '<p>' + getI18NStr('msg.batteryPercent') + ': ' + data.detail.dsjStatus.battery + '%</p>';
		content += '<p>' + getI18NStr('msg.speed') + ': ' + data.detail.gps.speed + 'km/h</p>';
		content += '<p>' + getI18NStr('msg.signal') + ': ' + '4G</p>';
//		content += '<div style="margin-bottom: 5px;">';
//			content += '<button class="btn btn-success btn-sm" style="margin-right: 5px;" onclick="playSS(\''+data.detail.gateway.extend.ssIp+'\',\''+data.detail.gateway.extend.ssPort+'\',\''+data.detail.gps.deviceId+'\');">' + getI18NStr('msg.videoCall') + '</button>';
//			content += '<button class="btn btn-success btn-sm" onclick="startVideo(\''+data.detail.gps.deviceId+'\',\''+ data.accountId +'\');">' + getI18NStr('msg.record') + '</button>';
//		content += '</div>';
//		content += '<div">';
//			content += '<button class="btn btn-success btn-sm" style="margin-right: 5px;" onclick="startTalk(\''+data.detail.gateway.extend.ssIp+'\',\''+data.detail.gateway.extend.ssPort+'\',\''+data.detail.gps.deviceId+'\');">' + getI18NStr('msg.startTalk') + '</button>';
//			content += '<button class="btn btn-success btn-sm" onclick="stopTalk();">' + getI18NStr('msg.stopTalk') + '</button>';
//		content += '</div>';
	content += '</div>';
	
    if(data.user_name){
        var ls = new esri.symbols.TextSymbol(data.user_name).setColor(new esri.Color([4, 128, 209, 0.9])).setFont(new esri.symbol.Font("12px").setWeight(esri.symbol.Font.WEIGHT_BOLD)).setOffset(-20, -28).setAlign(esri.symbol.TextSymbol.ALIGN_START);
        var pointLabel = new esri.graphic(cruPoint,ls);
        pointLabel.id  = data.user_code;
        pointLabel.name= data.user_name;
        pointLabel.dev = data.detail.gps.deviceId;
        labelArr.push(pointLabel);
        GisObject.toolbar.drawLayer.add(pointLabel);
    }
   
	var infoTemp = new esri.InfoTemplate(getI18NStr('map.policeInfo'), content);
	var marker = new esri.Graphic(cruPoint,pictureSymbol,{},infoTemp);
	marker.id  = data.user_code;
	marker.name= data.user_name;
	marker.dev = data.detail.gps.deviceId;
	marker.type = data.detail.gps.deviceType;
	markerArr.push(marker);
	GisObject.toolbar.drawLayer.add(marker);
	// GisObject.loadDefaultCluster(points);//画点
	GisObject.map.infoWindow.hide();
	
	center && setMapCenter(point.lon, point.lat);
}

/**
* 删除的单个标注
*/
function removerMarker(data)
{
    var markerTmp;
    for(var i =0;i< markerArr.length;i++)
    {
        markerTmp = markerArr[i];
        if(markerTmp.id == data)
        {
            GisObject.map.infoWindow.hide();
            GisObject.toolbar.drawLayer.remove(markerTmp);
            markerArr.splice(i--, 1);
        }
    }
    for(var j =0;j< labelArr.length;j++)
    {
        markerTmp = labelArr[j];
        if(markerTmp.id == data)
        {
            GisObject.toolbar.drawLayer.remove(markerTmp);
            labelArr.splice(j--,1);
        }
    }
}
// 历史轨迹
function historyTrack(points)
{
	var arr = [];
	var obj = [];
	for(var i=0;i<points.length;i++)
	{
		var ggPoint = GPS.gcj_encrypt(parseFloat(points[i].latitude), parseFloat(points[i].longitude));
		obj = [ggPoint.lon, ggPoint.lat];
		arr.push(obj);
	}
	
	var point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(parseFloat(arr[0][0]), parseFloat(arr[0][1])));
	GisObject.map.centerAndZoom(point,12);

	GisObject.addPolyline(arr);//画线
	GisObject.drawDefaultTrack(arr);//画箭头

	var symbol = new esri.symbols.PictureMarkerSymbol({"url":"./ic_police1_0.png", 'height':35/2,"width":47/2,
		"type":"esriPMS",xoffset:0,yoffset:8});

	var k = points.length - 1;
	//创建模版
	//var content1 = "<div><p>设备编号: "+points[0].deviceId+"</p><p>采集时间："+points[0].time+"</p><p>经&nbsp;&nbsp;纬&nbsp;度："+points[0].longitude+"-"+points[0].latitude+"</p></div>";
	//var infoTemp1 = new esri.InfoTemplate("警员信息", content1);
	GisObject.addPictureMarker(arr[0][0],arr[0][1],symbol,{},"");
	
	var ls = new esri.symbols.TextSymbol(points[0].timeDisplay).setColor(new esri.Color([4, 128, 209, 0.9])).setFont(new esri.symbol.Font("12px").setWeight(esri.symbol.Font.WEIGHT_BOLD)).setOffset(-20, -12).setAlign(esri.symbol.TextSymbol.ALIGN_START);  
    point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(arr[0][0], arr[0][1]));
    var pointLabel = new esri.graphic(point, ls);
    GisObject.toolbar.drawLayer.add(pointLabel);

	//var content2 = "<div><p>设备编号: "+points[k].deviceId+"</p><p>采集时间："+points[k].time+"</p><p>经&nbsp;&nbsp;纬&nbsp;度："+points[k].longitude+"-"+points[k].latitude+"</p></div>";
	//var infoTemp2 = new esri.InfoTemplate("警员信息", content2);
	GisObject.addPictureMarker(arr[k][0],arr[k][1],symbol,{},"");
	
	ls = new esri.symbols.TextSymbol(points[k].timeDisplay).setColor(new esri.Color([4, 128, 209, 0.9])).setFont(new esri.symbol.Font("12px").setWeight(esri.symbol.Font.WEIGHT_BOLD)).setOffset(-20, -12).setAlign(esri.symbol.TextSymbol.ALIGN_START);  
    point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(arr[k][0], arr[k][1]));
    pointLabel = new esri.graphic(point, ls);
    GisObject.toolbar.drawLayer.add(pointLabel);
}

//删除所有轨迹
function removerAll()
{
	GisObject.toolbar.drawLayer.clear();
	//GisObject.toolbar.markerLayer.clear();
}

function playSS(ssIp, ssPort, deviceId){
	parent.playSS(ssIp, ssPort, deviceId);
}
function startVideo(deviceId, accountId){
	parent.startVideo(deviceId, accountId);
}
function startTalk(ssIp, ssPort, deviceId){
	parent.startTalk(ssIp, ssPort, deviceId);
}
function stopTalk(){
	parent.stopTalk();
}

var mediaMarker;
var labelMarker;
function createMediaMarker(data, center){

	var ggPoint = GPS.gcj_encrypt(parseFloat(data.latitude), parseFloat(data.longitude));

	var myIcon = new esri.symbols.PictureMarkerSymbol({"url":"./ic_police1_0.png", 'height':35/2,"width":47/2,
		"type":"esriPMS",xoffset:0,yoffset:8});
		
    var point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(ggPoint.lon, ggPoint.lat));
    var pictureSymbol = new esri.symbols.PictureMarkerSymbol(myIcon);//图片
		
	mediaMarker = new esri.Graphic(point,pictureSymbol,{},"");
	GisObject.toolbar.drawLayer.add(mediaMarker);
	
	var ls = new esri.symbols.TextSymbol(data.time).setColor(new esri.Color([4, 128, 209, 0.9])).setFont(new esri.symbol.Font("12px").setWeight(esri.symbol.Font.WEIGHT_BOLD)).setOffset(-20, -12).setAlign(esri.symbol.TextSymbol.ALIGN_START);  
    point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(ggPoint.lon, ggPoint.lat));
    labelMarker = new esri.graphic(point, ls);
    GisObject.toolbar.drawLayer.add(labelMarker);
}
function updateMediaMarker(data){
	clearMediaMarker();
	createMediaMarker(data, false);
}
function clearMediaMarker(){
	mediaMarker && GisObject.toolbar.drawLayer.remove(mediaMarker);
	labelMarker && GisObject.toolbar.drawLayer.remove(labelMarker);
	delete mediaMarker;
	delete labelMarker;
	mediaMarker = null;
	labelMarker = null;
}

function formatDate(now){     
	var year = now.getFullYear();       //年  
    var month = now.getMonth() + 1;     //月  
    var day = now.getDate();            //日  
         
    var hh = now.getHours();            //时  
    var mm = now.getMinutes();          //分  
    var ss=now.getSeconds();            //秒  
         
    var clock = year + "-";  
         
    if(month < 10) clock += "0";         
    clock += month + "-";  
         
    if(day < 10) clock += "0";   
    clock += day + " ";  
         
    if(hh < 10) clock += "0";  
    clock += hh + ":";  
  
    if (mm < 10) clock += '0';   
    clock += mm+ ":";  
          
    if (ss < 10) clock += '0';   
    clock += ss;  
  
    return clock;   
}

function drawFence(handler){
	GisObject.toolbar.draw(esri.toolbars.draw.EXTENT,new esri.symbols.SimpleFillSymbol({
    	type:"esriSFS",
        style:"esriSFSSolid",
        color:[0,0,0,64],
        outline:{
        	type:"esriSLS",
        	style:"esriSLSSolid",
        	width:1.5,
        	color:[255,0,0,255]
        }
    }), function(geometry){
    	
    	var point_lt = esri.geometry.webMercatorUtils.xyToLngLat(geometry.xmin, geometry.ymin);
    	var point_rb = esri.geometry.webMercatorUtils.xyToLngLat(geometry.xmax, geometry.ymax);
    
    	var pStart = GPS.gcj_decrypt(point_lt[1], point_lt[0]);
		var pEnd   = GPS.gcj_decrypt(point_rb[1], point_rb[0]);
    	
    	var point = {
    		xmin: pStart.lon,
    		ymin: pStart.lat,
    		xmax: pEnd.lon,
    		ymax: pEnd.lat
    	};
    	
    	handler(point);
    });
}

function clearFence(){
	GisObject.toolbar.drawLayer.clear();
}

function showFence(points){
	var goPonit = [];
	var point   = null;
	for(var i=0; i<points.length; i++){
		point = GPS.gcj_encrypt(points[i].latitude, points[i].longitude);
		
		goPonit.push({
			latitude: point.lat,
			longitude: point.lon
		});
	}
	
	GisObject.layerDraw.addPolygon([[goPonit[0].longitude,goPonit[0].latitude],[goPonit[1].longitude,goPonit[1].latitude],[goPonit[2].longitude,goPonit[2].latitude],[goPonit[3].longitude,goPonit[3].latitude],[goPonit[0].longitude,goPonit[0].latitude]]);
}

//创建标签
function createJQMarker(accountId, name, lon, lat, center)
{
	var iconUrl = "./ic_police1_0.png";
	var icon_h  = 35 / 2;
	var icon_w  = 47 / 2;
			
	var ggPoint = GPS.gcj_encrypt(parseFloat(lat), parseFloat(lon));
    var myIcon = new esri.symbols.PictureMarkerSymbol({"url":iconUrl,"height":icon_h,"width":icon_w,"type":"esriPMS",xoffset:0,yoffset:0});
    var point = esri.geometry.webMercatorUtils.geographicToWebMercator(new esri.geometry.Point(ggPoint.lon, ggPoint.lat));
    var pictureSymbol = new esri.symbols.PictureMarkerSymbol(myIcon);//图片
    
    if(name){
        var ls = new esri.symbols.TextSymbol(name).setColor(new esri.Color([4, 128, 209, 0.9])).setFont(new esri.symbol.Font("12px").setWeight(esri.symbol.Font.WEIGHT_BOLD)).setOffset(-20, -28).setAlign(esri.symbol.TextSymbol.ALIGN_START);
        var pointLabel = new esri.graphic(point,ls);
        pointLabel.id  = accountId;
        labelArr.push(pointLabel);
        GisObject.toolbar.drawLayer.add(pointLabel);
    }
	
	var marker = new esri.Graphic(point,pictureSymbol,{});
	marker.id  = accountId;
	markerArr.push(marker);
	GisObject.toolbar.drawLayer.add(marker);
	
	center && setMapCenter(ggPoint.lon, ggPoint.lat);
}