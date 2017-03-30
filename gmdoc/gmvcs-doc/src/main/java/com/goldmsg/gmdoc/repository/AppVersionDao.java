package com.goldmsg.gmdoc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.AppVersion;

public interface AppVersionDao
		extends PagingAndSortingRepository<AppVersion, Integer>, JpaSpecificationExecutor<AppVersion> {

	@Query("select app from AppVersion app where app.appOpType=?1 and app.appOpVersion<=?2 order by app.appVersion desc")
	Page<AppVersion> findNewestAvailableVersion(String op_type, String op_version, Pageable pageable);
	
	@Query("select app from AppVersion app order by app.appVersion desc")
	Page<AppVersion> findNewestVersion(Pageable pageable);

}
