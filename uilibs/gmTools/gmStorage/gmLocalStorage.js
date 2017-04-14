/**
 * @name 封装localStorage, 使用命名空间存储不同数据
 * @author hcxowe
 * @last-write-time 2016-12-02 
 */

/**
 * @name 构造函数
 * @spaceName 命名空间名称
 * @type 用于保存数据的本地存储 localStorage or sessionStorage 默认localStorage
 */
var GMLocalStorage = function(spaceName, type){
    this.spaceName = spaceName;
    if(type=='sessionStorage'){
        this.storage = sessionStorage || window.sessionStorage;
    }
    else{
        this.storage = localStorage || window.localStorage;
    }
};

/**
 * @name 静态方法 判断localStorage中是否已经存在了spaceName
 */
GMLocalStorage.isExistSpace = function(storage, spaceName){
    for(key in storage){
        if(key === spaceName){
            return true;
        }
    }

    return false;
};

/**
 * @name 原型
 */
GMLocalStorage.prototype = {
    status: {
        SUCCESS: 0,  // 成功
        FAILURE: 1,  // 失败
        OVERFLOW: 2 // 溢出
    },
    init: function(){
    	if(GMLocalStorage.isExistSpace(this.storage, this.spaceName)){
	        var temp = this.getAllData();
	        this.data = (temp.status == this.status.SUCCESS) && temp.value;
	    }
	    else{
	        this.storage.setItem(this.spaceName, "{}");
	        this.data = {};
	    }
    },
    /**
     * @name 获取空间名称
     */
    getSpaceName: function(){
        return this.spaceName;
    },
    /**
     * @name 获取空间内所以键值组成的对象
     */
    getAllData: function(){
        var value = null;
        try{
            value = this.storage.getItem(this.spaceName);
            if(value){
                value = JSON.parse(value);
            }else{
                value = {};
            }
        }
        catch(e){
            return {
                status: this.status.FAILURE,
                value: null
            };
        }

        return {
            status: this.status.SUCCESS,
            value: value
        };
    },
    /**
     * @name 判断空间中是否存在该键
     */
    isExistKey: function(key){
        for(var item in this.data){
            if(key === item){
                return true;
            }
        }

        return false;
    },
    /**
     * @name 获取值， 直接冲缓存中获取值
     */
    getItem: function(key){
        if(!this.isExistKey(key)){
            return {
                status: this.status.FAILURE,
                value: null
            };
        }

        return this.data[key];
    },
    /**
     * @name 设置值
     */
    setItem: function(key, value){
        this.data[key] = value;
        try{
            this.storage.setItem(this.spaceName, JSON.stringify(this.data))
        }
        catch(e){
            delete this.data[key];
            return this.status.OVERFLOW;
        }

        return this.status.SUCCESS;
    },
    /**
     * @name 移除某项
     */
    removeItem: function(key){
        if(!this.isExistKey(key)){
            return this.status.FAILURE;
        }

        var value = this.data[key];
        delete this.data[key];

        try{
            this.storage.setItem(this.spaceName, JSON.stringify(this.data))
        }
        catch(e){
            this.data[key] = value;
            return this.status.OVERFLOW;
        }

        return this.status.SUCCESS;
    },
    /**
     * @name 清楚空间所有键值
     */
    clear: function(){
        try{
            this.storage.setItem(this.spaceName, "{}");
        }
        catch(e){
            return this.status.OVERFLOW;
        }

        this.data = {};
        return this.status.SUCCESS;
    }
};