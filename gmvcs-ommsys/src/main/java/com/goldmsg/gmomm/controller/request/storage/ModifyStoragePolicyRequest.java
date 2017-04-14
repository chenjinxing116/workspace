package com.goldmsg.gmomm.controller.request.storage;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.goldmsg.gmomm.controller.response.ws.PolicyConfigInfoResponse;

/***
 * 修改存储策略配置信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午3:42:47
 */
public class ModifyStoragePolicyRequest {

	@NotBlank
	private String storageId;	//存储id
	
	@NotNull
	private Integer maxUploadInst;	//最大上传实例
	
	@NotNull
	private Integer iNetSpeed;	//限速(MB)
	
	@NotNull
	private Date startTime;	//工作时间段开始时间
	
	@NotNull
	private Date endTime;	//工作时间段结束时间
	
	@NotNull
	private Integer days;	//默认存储天数
	
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getMaxUploadInst() {
		return maxUploadInst;
	}

	public void setMaxUploadInst(Integer maxUploadInst) {
		this.maxUploadInst = maxUploadInst;
	}

	public Integer getiNetSpeed() {
		return iNetSpeed;
	}

	public void setiNetSpeed(Integer iNetSpeed) {
		this.iNetSpeed = iNetSpeed;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
}
