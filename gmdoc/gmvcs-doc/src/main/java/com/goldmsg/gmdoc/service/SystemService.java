package com.goldmsg.gmdoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goldmsg.gmdoc.entity.AppVersion;
import com.goldmsg.gmdoc.repository.AppVersionDao;

@Service
public class SystemService {

	@Autowired
	AppVersionDao appVersionDao;

	public AppVersion findNewestAvailableVersion(String op_type, String op_version) {
		Pageable pageable = new PageRequest(0, 1);
		Page<AppVersion> apps = appVersionDao.findNewestAvailableVersion(op_type, op_version, pageable);
		if (apps == null || apps.getNumberOfElements() == 0) {
			return null;
		}
		return apps.getContent().get(0);
	}

	public AppVersion findNewestAppVersion() {
		Pageable pageable = new PageRequest(0, 1);
		Page<AppVersion> apps = appVersionDao.findNewestVersion(pageable);
		if (apps == null || apps.getNumberOfElements() == 0) {
			return null;
		}
		return apps.getContent().get(0);
	}
}
