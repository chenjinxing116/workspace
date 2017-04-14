package com.goldmsg.gmomm.controller.response.ws;

import java.util.ArrayList;
import java.util.List;

/***
 * 工作站信息列表response
 * 
 * @author QH
 *
 */
public class WSListInfoResponse {

	private List<WSInfo> wss = new ArrayList<WSInfo>();
	public Integer total; // 本次查询总数

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<WSInfo> getWss() {
		return wss;
	}

	public void setWss(List<WSInfo> wss) {
		this.wss = wss;
	}

	public static class WSInfo {
		private String wsId; // 工作站id
		private String wsTag; // 工作站标识
		private String storageId; // 存储id
		private String storageName; // 存储名称
		private String manufacturer; // 厂商code
		private String manufacturerName; // 厂商名称
		private String wsName; // 工作站名称
		private String orgName; // 部门名称
		private String ipAddr; // ip地址
		private String upgradeState; // 升级状态
		private String workStatus; // 工作状态
		private Long totalCapacity; // 总容量(MB)
		private Long usedCapacity; // 已用容量(MB)
		private String collectionTimeDisplay; // 采集时间
		private String superStorage; // 上级存储id
		private String superStorageStr; //上级存储名称
		private String admin; // 负责人
		private String phoneNumber; // 负责人电话
		private String addr; // 工作站地址

		public String getStorageId() {
			return storageId;
		}

		public void setStorageId(String storageId) {
			this.storageId = storageId;
		}

		public String getStorageName() {
			return storageName;
		}

		public void setStorageName(String storageName) {
			this.storageName = storageName;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getManufacturerName() {
			return manufacturerName;
		}

		public void setManufacturerName(String manufacturerName) {
			this.manufacturerName = manufacturerName;
		}

		public String getWsTag() {
			return wsTag;
		}

		public void setWsTag(String wsTag) {
			this.wsTag = wsTag;
		}

		public String getAddr() {
			return addr;
		}

		public void setAddr(String addr) {
			this.addr = addr;
		}

		public String getWsId() {
			return wsId;
		}

		public void setWsId(String wsId) {
			this.wsId = wsId;
		}

		public String getWsName() {
			return wsName;
		}

		public void setWsName(String wsName) {
			this.wsName = wsName;
		}

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getIpAddr() {
			return ipAddr;
		}

		public void setIpAddr(String ipAddr) {
			this.ipAddr = ipAddr;
		}

		public String getUpgradeState() {
			return upgradeState;
		}

		public void setUpgradeState(String upgradeState) {
			this.upgradeState = upgradeState;
		}

		public String getWorkStatus() {
			return workStatus;
		}

		public void setWorkStatus(String workStatus) {
			this.workStatus = workStatus;
		}

		public Long getTotalCapacity() {
			return totalCapacity;
		}

		public void setTotalCapacity(Long totalCapacity) {
			this.totalCapacity = totalCapacity;
		}

		public Long getUsedCapacity() {
			return usedCapacity;
		}

		public void setUsedCapacity(Long usedCapacity) {
			this.usedCapacity = usedCapacity;
		}

		public String getCollectionTimeDisplay() {
			return collectionTimeDisplay;
		}

		public void setCollectionTimeDisplay(String collectionTimeDisplay) {
			this.collectionTimeDisplay = collectionTimeDisplay;
		}

		public String getSuperStorage() {
			return superStorage;
		}

		public void setSuperStorage(String superStorage) {
			this.superStorage = superStorage;
		}

	
		public String getSuperStorageStr() {
			return superStorageStr;
		}

		public void setSuperStorageStr(String superStorageStr) {
			this.superStorageStr = superStorageStr;
		}

		public String getAdmin() {
			return admin;
		}

		public void setAdmin(String admin) {
			this.admin = admin;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	}

}
