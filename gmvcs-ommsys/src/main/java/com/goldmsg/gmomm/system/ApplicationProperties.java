package com.goldmsg.gmomm.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * application.properties中的配置参数映射类
 * 
 * @author QH
 *
 */
@Component
public class ApplicationProperties {

	/*
	 * 项目代号
	 */
	@Value("#{localPropertiesReader['project.code']}")
	private String projectCode;

	/*
	 * 存储过期时间（秒）
	 */
	@Value("#{localPropertiesReader['storage.expired.time']}")
	private int storageExpiredTime;

	/*
	 * 临时文件存放目录
	 */
	@Value("#{localPropertiesReader['upload.temp.dir']}")
	private String uploadTempdir;
	
	@Value("#{localPropertiesReader['subject.ptt.add.user']}")
	private String subjectPttAddUser;

	
	public String getSubjectPttAddUser() {
		return subjectPttAddUser;
	}

	public void setSubjectPttAddUser(String subjectPttAddUser) {
		this.subjectPttAddUser = subjectPttAddUser;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getUploadTempdir() {
		return uploadTempdir;
	}

	public void setUploadTempdir(String uploadTempdir) {
		this.uploadTempdir = uploadTempdir;
	}

	public int getStorageExpiredTime() {
		return storageExpiredTime;
	}

	public void setStorageExpiredTime(int storageExpiredTime) {
		this.storageExpiredTime = storageExpiredTime;
	}
}
