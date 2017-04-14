package com.goldmsg.gmomm.controller.response.dsj;

import java.util.ArrayList;
import java.util.List;

/***
 * 执法仪信息列表查询response
 * 
 * @author QH
 *
 */
public class DSJListInfoResponse {

	List<DJSInfo> dsjs = new ArrayList<DJSInfo>(); // 执法仪信息列表
	public Integer total; // 本次查询总数

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<DJSInfo> getDsjs() {
		return dsjs;
	}

	public void setDsjs(List<DJSInfo> dsjs) {
		this.dsjs = dsjs;
	}

	public static class DJSInfo {
		private String deviceId; // 设备编号
		private String orgName; // 所属部门名称
		private String userCode; // 配发警员编号
		private String userName; // 配发警员名称
		private String dsjType; // 执法仪类型
		private String manufactures; // 厂商
		private String model; // 型号
		private long capacity; // 容量(MB)
		private String status; // 设备状态
		private String registrationTimeDisplay; // 注册时间
		private String simCode; // sim卡号
		private String domain; // 区域

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getSimCode() {
			return simCode;
		}

		public void setSimCode(String simCode) {
			this.simCode = simCode;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getUserCode() {
			return userCode;
		}

		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getDsjType() {
			return dsjType;
		}

		public void setDsjType(String dsjType) {
			this.dsjType = dsjType;
		}

		public String getManufactures() {
			return manufactures;
		}

		public void setManufactures(String manufactures) {
			this.manufactures = manufactures;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public long getCapacity() {
			return capacity;
		}

		public void setCapacity(long capacity) {
			this.capacity = capacity;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getRegistrationTimeDisplay() {
			return registrationTimeDisplay;
		}

		public void setRegistrationTimeDisplay(String registrationTimeDisplay) {
			this.registrationTimeDisplay = registrationTimeDisplay;
		}
	}
}
