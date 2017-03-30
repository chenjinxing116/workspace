package com.goldmsg.gmdoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.goldmsg.gmdoc.entity.TUserInfo;

public interface TUserInfoDao
		extends PagingAndSortingRepository<TUserInfo, Integer>, JpaSpecificationExecutor<TUserInfo> {

	public List<TUserInfo> findByUserCode(String user_code);

	@Modifying
	@Query("update TUserInfo u set u.secLevel=?1 where u.userCode=?2")
	public int updateSecLevelByUserCode(int sec_level, String user_code);

	@Query("select u.secLevel from TUserInfo u where u.userId=?1")
	public int findSecLevelByUserId(int user_id);

}
