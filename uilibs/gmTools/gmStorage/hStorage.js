/**
 * @author hcxowe
 * @last-writetime: 2016-12-01 17:20
 */

/**
 * @name localstorage本地存储操作类
 * @param {pre} 存储前缀
 * @param {deadline} 数据过期时间戳
 */
var HStorage = function(pre){
	this.pre = pre;
}

HStorage.prototype = {
	storage: localStorage || window.localStorage,
	status: {
		SUCCESS:  0,	// 成功
		FAILURE:  1,	// 失败
		OVERFLOW: 2,	// 溢出
		TIMEOUT:  3		// 超时
	},
	getKey: function(key){
		return this.pre + key; 
	},
	/**
	 * 设置/修改 数据
	 * @param {String} key  键
	 * @param {String} value 值
	 * @param {Function} callBack 回调函数
	 * @param {Object} time 过期时间戳, 0代表不限期，默认31天
	 */
	set: function(key, value, time){
		var status = this.status.SUCCESS;
		var key = this.getKey(key);
		
		try{
			time = new Date(time).getTime() || time.getTime();
		}
		catch(e){
			time = new Date().getTime() + 1000 * 60 * 60 * 24 * 31;
		}
		
		try{
			this.storage.setItem(key, time + '-' + value);
		}
		catch(e){
			status = this.status.OVERFLOW;
		}
		
		return status;
	},
	/**
	 * 获取数据
	 * @param {String} key
	 * @param {Function} callBack
	 */
	get: function(key){
		var status = this.status.SUCCESS;
		var key = this.getKey(key);
		var value = null;
		var that = this;
		var index = -1;
		var time = 0;
		var result = null;
		
		try{
			value = that.storage.getItem(key);
		}
		catch(e){
			result = {
				status: that.status.FAILURE,
				value: null
			};
			
			return result;
		}
		
		if(value){
			index = value.indexOf('-');
			time = +value.slice(0, index);
			if(time == 0 || new Date(time).getTime() > new Date().getTime()){
				value = value.slice(index + 1);
			}
			else{
				value = null;
				status = that.status.TIMEOUT;
				that.remove(key);
			}
		}
		else{
			status = that.status.FAILURE;
		}
		
		result = {
			status: status,
			value: value
		};
		
		return result;
	},
	/**
	 * @name  删除字段
	 * @param {String} key
	 * @param {Function} callBack
	 */
	remove: function(key){
		var status = this.status.FAILURE;
		var key = this.getKey(key);
		var value = null;
		try{
			value = this.storage.getItem(key);
		}
		catch(e){
			
		}
		
		if(value){
			try{
				this.storage.removeItem(key);
				status = this.status.SUCCESS;
			}
			catch(e)
			{}
		}
		
		return status;
	}
};
