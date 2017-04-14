package com.goldmsg.gmomm.controller.response.storage;

import java.util.List;

import org.apache.http.NameValuePair;

/***
 * 列举未分配、已分配到某个存储的工作站信息response
 * name是工作站名称，value是工作站id
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年10月24日 : 上午9:23:48
 */
public class WSAssignedInfoResponse {

	private List<NameValuePair> asigned;	//已分配工作站id和名称列表
	private List<NameValuePair> unsigned;	//未分配工作站id和名称列表
	
	
	public List<NameValuePair> getAsigned() {
		return asigned;
	}
	public void setAsigned(List<NameValuePair> asigned) {
		this.asigned = asigned;
	}
	public List<NameValuePair> getUnsigned() {
		return unsigned;
	}
	public void setUnsigned(List<NameValuePair> unsigned) {
		this.unsigned = unsigned;
	}
}
