package com.gosun.core.utils.date;

import java.util.Date;

import com.ibm.icu.util.Calendar;
/**
 * 日历工具类
 * @author Abe
 *
 */

public class CalendarUtils {
	/**
	 *  合并两个日期的时间，一个是年月日，一个是系统当前时间
	 * @param date 日期对象
	 * @return 返回合并后的日期
	 */
	public static Date addSystemTimeToDate(Date date){
		Calendar now = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR, now.get(Calendar.HOUR));
		c.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, now.get(Calendar.SECOND));
		return c.getTime();
	}
	/**
	 * 合并两个日期的时间，一个是年月日，一个是时间
	 * @param addTodate 日期对象
	 * @param date 日期对象
	 * @return 返回合并后的日期
	 */
	public static Date addTimeToDate(Date addTodate,Date date){
		Calendar addToCalendar = Calendar.getInstance();
		addToCalendar.setTime(addTodate);
		addToCalendar.set(Calendar.HOUR, addToCalendar.get(Calendar.HOUR));
		addToCalendar.set(Calendar.MINUTE, addToCalendar.get(Calendar.MINUTE));
		addToCalendar.set(Calendar.SECOND, addToCalendar.get(Calendar.SECOND));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.YEAR, addToCalendar.get(Calendar.YEAR));
		c.set(Calendar.MONTH, addToCalendar.get(Calendar.MONTH));
		c.set(Calendar.DATE, addToCalendar.get(Calendar.DATE));
		return c.getTime();
	}
}
