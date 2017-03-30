package com.gosun.core.utils.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * @ClassName: Common 
 * @Description: TODO 
 * @author ZhaoliSha
 * @date 2012-6-6 下午7:04:00
 */
public class Common {
	//把时间格式转换成协议的结构体格式
	public static void changeTimeFormatToStruct(String timeStr,IoBuffer ioBuffer) throws ParseException{
		java.text.SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = longSdf.parse(timeStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int milliSecond = cal.get(Calendar.MILLISECOND);
		ioBuffer.putShort((short)year);
		ioBuffer.putShort((short)month);
		ioBuffer.putShort((short)date);
		ioBuffer.putShort((short)hour);
		ioBuffer.putShort((short)minute);
		ioBuffer.putShort((short)second);
		ioBuffer.putInt(milliSecond);
	}
	//把结构体协议转换成字符串
	public static String changeStructToTimeFormat(IoBuffer ioBuffer) throws ParseException{
		int year = ioBuffer.getShort();
		int month = ioBuffer.getShort()-1;
		int date = ioBuffer.getShort();
		int hour = ioBuffer.getShort();
		int minute = ioBuffer.getShort();
		int second = ioBuffer.getShort();
		ioBuffer.getInt();//milliSecond
		java.text.SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, hour, minute, second);
		return longSdf.format(cal.getTime());
	}
	//判断操作系统是否是linux
    public static boolean isLinuxOS(){
        boolean isLinuxOS = false;
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("linux")>-1){
            isLinuxOS = true;
        }
        return isLinuxOS;
     }
}
