package com.goldmsg.gmomm.controller.response.storage;

import com.goldmsg.gmomm.controller.request.storage.ModifyStoragePolicyRequest;

/***
 * 
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年10月20日 : 下午5:03:15
 */
public class StoragePolicyInfoResponse extends ModifyStoragePolicyRequest {
	
	private String startTimeDisplay;	//工作时间段开始时间格式字符串
	private String endTimeDisplay;	//工作时间段结束时间格式字符串
	
	public String getStartTimeDisplay() {
		return startTimeDisplay;
	}
	public void setStartTimeDisplay(String startTimeDisplay) {
		this.startTimeDisplay = startTimeDisplay;
	}
	public String getEndTimeDisplay() {
		return endTimeDisplay;
	}
	public void setEndTimeDisplay(String endTimeDisplay) {
		this.endTimeDisplay = endTimeDisplay;
	}
}
