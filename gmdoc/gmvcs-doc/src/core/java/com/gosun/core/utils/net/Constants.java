package com.gosun.core.utils.net;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.gosun.core.utils.SystemConfig;

/***
 * @ClassName: Constants
 * @Description: 网络库静态变量存储
 * @author houyx
 * @date 2012-05-16
 * 
 */
public class Constants {
    
    /*
     * 存储全局变量
     */
    //全局变量存储数据版本hashmap
	public static String CharacterSet = SystemConfig.getSystemConfig().getProperty("gsnet.charset");//"GBK";
    public static CharsetDecoder decoder = Charset.forName(CharacterSet).newDecoder();
    
    public static final int DEFAULT_BUFFER_SIZE = 1500;//每个通信包大小，按这个基准值来分包
    
    public static final String gsCommonRequestHead = "GSSmp: REQUEST";//14位
    
    public static final String gsCommonReponseHead = "GSSmp: RESPONSE";//15位
    
    public static final String gsCommonHead1 = "Content-Type: application/";//26位
    public static final String gsCommonHead2 = "Content-Length: ";//16位
    public static final String gsCommonSeparator1 = "\r\n";//分隔符
    public static final String gsCommonSeparator2 = "\r\n\r\n";//分隔符
    
    public static final String MESSAGE_TYPE_JSON ="json";
    public static final String MESSAGE_TYPE_XML ="xml";
    public static final String MESSAGE_TYPE_BINARY ="binary";
    
}
