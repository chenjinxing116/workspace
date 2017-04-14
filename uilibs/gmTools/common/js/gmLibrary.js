/**
 * @author hcxowe
 * @desc 公共方法类库 
 * @last-write-time 2016-12-12
 */
var gmLibrary = {
	/**
	 * 为日期对象添加格式化输出
	 * 调用： new Date().Format("yyyy-MM-dd") or new Date().Format("yyyy-MM-dd hh:mm:ss")  
	 */
	dateFormat: function(date, fmt) {
		var o = {
			"M+": date.getMonth() + 1, //月份 
			"d+": date.getDate(), //日 
			"h+": date.getHours(), //小时 
			"m+": date.getMinutes(), //分 
			"s+": date.getSeconds(), //秒 
			"q+": Math.floor((date.getMonth() + 3) / 3), //季度 
			"S": date.getMilliseconds() //毫秒 
		};
		if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		for(var k in o)
			if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	},
	/**
	 * @desc  根据日期获取时间戳
	 * @param {Object} stringTime
	 */
	getTimeByDateStr: function(stringTime) {
		var dt = new Date(stringTime.replace(/-/, "/"));
		return dt.getTime();
	},

	/**
	 * 根据秒数时间字符串      72s =》 01:12
	 * @param {Object} num
	 */
	getDurationStr: function(num) {
		var seconds = Math.floor(num % 60);
		var minites = Math.floor(num / 60 % 60);
		var hours = Math.floor(num / 60 / 60);
		var ret = "";
		ret += " " + (hours < 10 ? ("0" + hours) : hours) + ":";
		ret += (minites < 10 ? ("0" + minites) : minites) + ":";
		ret += seconds < 10 ? ("0" + seconds) : seconds;
		return ret;
	},
	/**
	 * post请求，依赖jquery
	 * @param {Object} url
	 * @param {Object} pData
	 * @param {Object} success
	 * @param {Object} error
	 */
	ajaxPost: function(url, pData, success, error) {
		if(!$ && !jQuery) {
			alert('not find jquery');
			return;
		}

		$.ajax({
			type: "post",
			url: url,
			data: JSON.stringify(pData),
			contentType: "application/json",
			dataType: 'json',
			async: true,
			cache: false,
			error: function(ret) {
				error && error(ret);
				return;
			},
			success: function(ret) {
				success && success(ret);
				return;
			}
		});
	},
	/**
	 * get请求，依赖jquery
	 * @param {Object} url
	 * @param {Object} pData
	 * @param {Object} success
	 * @param {Object} error
	 */
	ajaxGet: function(url, pData, success, error) {
		if(!$ && !jQuery) {
			alert('not find jquery');
			return;
		}

		$.ajax({
			type: "get",
			url: url,
			data: pData,
			async: true,
			cache: false,
			error: function(msg) {
				error && error(msg);
			},
			success: function(ret) {
				success && success(ret);
			}
		});
	}
};