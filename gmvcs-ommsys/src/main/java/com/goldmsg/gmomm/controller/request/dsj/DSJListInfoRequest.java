package com.goldmsg.gmomm.controller.request.dsj;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.goldmsg.gmomm.controller.request.BasePagingRequest;

/***
 * 执法仪信息列表查询request
 * @author QH
 *
 */
public class DSJListInfoRequest extends BasePagingRequest {

	@NotNull
	private Integer orgId;	//部门id
	@NotNull
	private Date beginTime;	//注册开始时间
	@NotNull
	private Date endTime;	//注册结束时间
	/*
	 * 分配情况
	 * 	0--不限
	 *	1--已分配
	 *	2--未分配
	 */
	private Integer allocations;
	/*
	 * 设备状态
	 * 0--不限
	 * 1--正常
	 * 2--报修
	 * 3--报废
	 */
	@NotNull
	@Min(0)
	@Max(3)
	private Integer status;
	
	private String userName;	//警员姓名
	private String userCode;	//警员编号
	private String deviceId;	//设备编号
	
	/*
	 * 执法仪类型
	 * 0--所有
	 * 1--无网络
	 * 2--2G执法仪
	 * 3--4G执法仪
	 */
	@NotNull
	@Min(0)
	@Max(3)
	private Integer dsjType;	//执法仪类型
	
	private String simCode;	//sim卡号
	private String domain;	//区域id


	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getSimCode() {
		return simCode;
	}

	public void setSimCode(String simCode) {
		this.simCode = simCode;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getAllocations() {
		return allocations;
	}

	public void setAllocations(Integer allocations) {
		this.allocations = allocations;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDsjType() {
		return dsjType;
	}

	public void setDsjType(Integer dsjType) {
		this.dsjType = dsjType;
	}
}
