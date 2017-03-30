package com.goldmsg.gmdoc.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.RelUserReadDoc;
import com.goldmsg.gmdoc.entity.RelUserReadDocPK;
import com.goldmsg.gmdoc.entity.TUserInfo;

public interface ReadDao
		extends PagingAndSortingRepository<RelUserReadDoc, RelUserReadDocPK>, JpaSpecificationExecutor<RelUserReadDoc> {

	int deleteByUserInfo(TUserInfo userInfo);

	@Modifying
	@Query("update TDocInfo set readTimes=readTimes+?1 where docId=?2")
	int updateReadTimesByDocId(int number, int doc_id);

}
