package com.goldmsg.gmomm.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.goldmsg.data.dentity.MSystemLog;
import com.goldmsg.data.service.log.MgrSysLogService;

/**
 *
 */
@Repository
public class SysLogUtils {

	@Autowired
	public static MgrSysLogService mgrSysLogService;

	/**
	 * @param type
	 *            日志类型
	 * @param operaType
	 *            操作类型
	 * @return
	 */
	public static boolean addMsysLog(int type, int operaType, String fileInfo, String desc) {
		MSystemLog mSystemLog = new MSystemLog();
		mSystemLog.setOrgCode(UserSessions.getCurrentUser().getOrg().getOrgCode());
		mSystemLog.setOrgName(UserSessions.getCurrentUser().getOrg().getName());
		mSystemLog.setUserCode(UserSessions.getCurrentUser().getUser().getUserCode());
		mSystemLog.setUserName(UserSessions.getCurrentUser().getUser().getUserName());
		mSystemLog.setType(type);
		mSystemLog.setOperaType(operaType);
		mSystemLog.setFileInfo(fileInfo);
		mSystemLog.setOperaTime(new Date());
		mSystemLog.setDesc(desc);

		boolean msls = mgrSysLogService.addLog(mSystemLog);

		return msls;
	}

	public static MgrSysLogService getMgrSysLogService() {
		return mgrSysLogService;
	}

	public static void setMgrSysLogService(MgrSysLogService mgrSysLogService) {
		SysLogUtils.mgrSysLogService = mgrSysLogService;
	}

}
