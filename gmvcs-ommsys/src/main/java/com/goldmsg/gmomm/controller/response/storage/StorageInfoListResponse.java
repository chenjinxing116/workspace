package com.goldmsg.gmomm.controller.response.storage;

import java.util.List;

/***
 * 存储信息列表response
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午3:17:28
 */
public class StorageInfoListResponse {

	private Integer total;	//存储数量
	private List<StorageResponse> storageList;	//存储信息列表
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<StorageResponse> getStorageList() {
		return storageList;
	}

	public void setStorageList(List<StorageResponse> storageList) {
		this.storageList = storageList;
	}

	/***
	 * 存储信息
	 * @author QH
	 *
	 */
	public static class StorageResponse {
		private String address;	//存储地址
		private String admin;	//管理员
		private Integer orgId;	//部门id
		private String orgName;	//部门名称
		private String ip;	//IP地址
		private Integer port;	//端口
		private String lastAliveTimeDisplay;	//最新存活时间
		private String name;	//名称
		private String phone;	//电话
		private String id;	//存储id
		private String statusStr;	//状态
		private String typeDisplay;	//存储类型
		private Long totalDisk;	//磁盘总容量，MB
		private Long usedDisk;	//已用容量，MB
		
		
		public String getOrgName() {
			return orgName;
		}
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getAdmin() {
			return admin;
		}
		public void setAdmin(String admin) {
			this.admin = admin;
		}
		public Integer getOrgId() {
			return orgId;
		}
		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public Integer getPort() {
			return port;
		}
		public void setPort(Integer port) {
			this.port = port;
		}
		public String getLastAliveTimeDisplay() {
			return lastAliveTimeDisplay;
		}
		public void setLastAliveTimeDisplay(String lastAliveTimeDisplay) {
			this.lastAliveTimeDisplay = lastAliveTimeDisplay;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getStatusStr() {
			return statusStr;
		}
		public void setStatusStr(String statusStr) {
			this.statusStr = statusStr;
		}
		public String getTypeDisplay() {
			return typeDisplay;
		}
		public void setTypeDisplay(String typeDisplay) {
			this.typeDisplay = typeDisplay;
		}
		public Long getTotalDisk() {
			return totalDisk;
		}
		public void setTotalDisk(Long totalDisk) {
			this.totalDisk = totalDisk;
		}
		public Long getUsedDisk() {
			return usedDisk;
		}
		public void setUsedDisk(Long usedDisk) {
			this.usedDisk = usedDisk;
		}
	}
}
