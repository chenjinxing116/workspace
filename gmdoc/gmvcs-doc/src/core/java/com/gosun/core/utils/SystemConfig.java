package com.gosun.core.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * net全局配置类
 * @author Abe
 *
 */
public final class SystemConfig {

	/** 多语言列表 */
   @Autowired
   FormattingConversionServiceFactoryBean conversionService;
   
   public static Properties SYSTEM_CONFIG;
   
   /**
    * 通过key得到String键值
    * @param key key值
    * @return Object 返回String键值
    */
   public static String getProperty(String key){
       return (String) getSystemConfig().get(key);
   }
   /**
    * 通过key修改对应的键,如果key没有就新增对应的key
    * @param key
    * @param value
    */
   public static void setProperty(Object key,Object value){
	   getSystemConfig().put(key, value);
   }
   
   /**
    * 得到相关网络通信的配置资源
    * @return
    */
   public static synchronized Properties getSystemConfig(){
       if(SYSTEM_CONFIG == null){
           SYSTEM_CONFIG = new Properties();
           PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
           PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
           try {
               propertiesPersister.load(SYSTEM_CONFIG, (resolver.getResources("classpath*:application.properties")[0]).getInputStream());
           } catch (IOException e) {
               e.printStackTrace();
           }
           return SYSTEM_CONFIG;
       }else
           return SYSTEM_CONFIG;
           
   }
}