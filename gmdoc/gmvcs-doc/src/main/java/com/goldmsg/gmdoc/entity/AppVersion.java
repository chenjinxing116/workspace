package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the app_version database table.
 * 
 */
@Entity
@Table(name = "app_version")
@NamedQuery(name = "AppVersion.findAll", query = "SELECT a FROM AppVersion a")
public class AppVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "app_code")
	private String appCode;

	@Column(name = "app_name")
	private String appName;

	@Column(name = "app_op_type")
	private String appOpType;

	@Column(name = "app_op_version")
	private String appOpVersion;

	@Column(name = "app_version")
	private String appVersion;

	@Column(name = "apk_name")
	private String apkName;

	@Column(name = "publish_time")
	private Date publishTime;

	public AppVersion() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppCode() {
		return this.appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppOpType() {
		return this.appOpType;
	}

	public void setAppOpType(String appOpType) {
		this.appOpType = appOpType;
	}

	public String getAppOpVersion() {
		return this.appOpVersion;
	}

	public void setAppOpVersion(String appOpVersion) {
		this.appOpVersion = appOpVersion;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Date getPublishTime() {
		if (this.publishTime == null) {
			return null;
		} else {
			return (Date) this.publishTime.clone();
		}
	}

	public void setPublishTime(Date publishTime) {
		if (publishTime == null) {
			this.publishTime = null;
		} else {
			this.publishTime = (Date) publishTime.clone();
		}
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
}