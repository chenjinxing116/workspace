package com.goldmsg.gmomm.controller.request.storage;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/***
 * 给存储分配工作站request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午3:53:39
 */
public class AssignWsRequest {

	@NotBlank
	private String storageId;	//存储id
	
	private List<String> wsid;	//工作站id列表
	
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	public List<String> getWsid() {
		return wsid;
	}
	public void setWsid(List<String> wsid) {
		this.wsid = wsid;
	}
}
