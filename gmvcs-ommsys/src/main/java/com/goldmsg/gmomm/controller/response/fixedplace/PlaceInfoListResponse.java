package com.goldmsg.gmomm.controller.response.fixedplace;

import java.util.Date;
import java.util.List;

/***
 * 固定场所信息列表response
 * 
 * @author QH Email: qhs_dream@163.com 2016年9月27日 : 上午11:28:44
 */
public class PlaceInfoListResponse {

	private String orgCode; // 部门编号
	private String desc; // 描述
	private String name; // 中心名称
	private String sid; // 中心id
	private Date insertTime; // 插入时间
	private Integer isDelete; // 删除标识
	private List<RoomInfo> rooms; // 中心的房间列表

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public List<RoomInfo> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomInfo> rooms) {
		this.rooms = rooms;
	}

	public static class RoomInfo {
		private String desc; // 房间描述
		private String hcSid; // id
		private String name; // 房间名
		private String typeStr; // 房间类型名称
		private List<String> type; // 房间类型序号
		private String sid; // 房间编号
		private Date insertTime; // 插入时间
		private Integer isDelete; // 删除标识

		public RoomInfo() {
			super();
		}

		public RoomInfo(String desc, String hcSid, String name, String typeStr, List<String> type, String sid,
				Date insertTime, Integer isDelete) {
			super();
			this.desc = desc;
			this.hcSid = hcSid;
			this.name = name;
			this.typeStr = typeStr;
			this.type = type;
			this.sid = sid;
			this.insertTime = insertTime;
			this.isDelete = isDelete;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getHcSid() {
			return hcSid;
		}

		public void setHcSid(String hcSid) {
			this.hcSid = hcSid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTypeStr() {
			return typeStr;
		}

		public void setTypeStr(String typeStr) {
			this.typeStr = typeStr;
		}

		public List<String> getType() {
			return type;
		}

		public void setType(List<String> type) {
			this.type = type;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public Date getInsertTime() {
			return insertTime;
		}

		public void setInsertTime(Date insertTime) {
			this.insertTime = insertTime;
		}

		public Integer getIsDelete() {
			return isDelete;
		}

		public void setIsDelete(Integer isDelete) {
			this.isDelete = isDelete;
		}

	}
}
