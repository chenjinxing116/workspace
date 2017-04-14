package com.goldmsg.gmomm.controller.response.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 用户信息列表response
 * 
 * @author QH Email: qhs_dream@163.com 2016年9月27日 : 下午4:10:36
 */
public class UserInfoListResponse {

	private List<UserInfo> users = new ArrayList<UserInfo>(); // 用户信息列表
	private Integer total; // 用户总数

	public List<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public static class UserInfo {
		private String orgName; // 部门名称
		private String orgCode; // 部门编号
		private String userName; // 警员名称
		private String userCode; // 警号
		private String postStr; // 岗位
		private Integer postId; // 岗位id
		private List<Integer> roleIds; // 角色id列表
		private List<String> roleNames; // 角色名称列表
		private String typeStr; // 警员类别
		private Integer typeId; // 类别id
		private String businessStr; // 业务名称
		private Integer businessId; // 业务id
		private Date lastLogintime; // 上次登录时间

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getOrgCode() {
			return orgCode;
		}

		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
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

		public String getPostStr() {
			return postStr;
		}

		public void setPostStr(String postStr) {
			this.postStr = postStr;
		}

		public Integer getPostId() {
			return postId;
		}

		public void setPostId(Integer postId) {
			this.postId = postId;
		}

		public List<Integer> getRoleIds() {
			return roleIds;
		}

		public void setRoleIds(List<Integer> roleIds) {
			this.roleIds = roleIds;
		}

		public List<String> getRoleNames() {
			return roleNames;
		}

		public void setRoleNames(List<String> roleNames) {
			this.roleNames = roleNames;
		}

		public String getTypeStr() {
			return typeStr;
		}

		public void setTypeStr(String typeStr) {
			this.typeStr = typeStr;
		}

		public Integer getTypeId() {
			return typeId;
		}

		public void setTypeId(Integer typeId) {
			this.typeId = typeId;
		}

		public String getBusinessStr() {
			return businessStr;
		}

		public void setBusinessStr(String businessStr) {
			this.businessStr = businessStr;
		}

		public Integer getBusinessId() {
			return businessId;
		}

		public void setBusinessId(Integer businessId) {
			this.businessId = businessId;
		}

		public Date getLastLogintime() {
			return lastLogintime;
		}

		public void setLastLogintime(Date lastLogintime) {
			this.lastLogintime = lastLogintime;
		}
	}
}
