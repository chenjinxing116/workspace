package com.goldmsg.gmdoc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.TDocInfo;

public interface TDocInfoDao extends PagingAndSortingRepository<TDocInfo, Integer>, JpaSpecificationExecutor<TDocInfo> {

	List<TDocInfo> findBySecLevelLessThanEqualAndDocCatoIdAndPubInfoPubStatusOrderByPubInfoOperateTimeDesc(
			int sec_level, int cato_id, int pubStatus, Pageable pageable);

	List<TDocInfo> findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByUploadTimeDesc(int sec_level, int pub_status,
			Pageable pageable);

	@Modifying
	@Query("update TDocInfo set coleTimes=coleTimes+?1 where docId=?2")
	int updateColeTimesByDocId(int number, int doc_id);

	@Query("select doc from TDocInfo doc where doc.docCatoId=?1")
	Page<TDocInfo> findOneByDocCatoId(int cato_id, Pageable pageable);

	@Modifying
	@Query("update TDocInfo set readTimes=readTimes+?1 where docId=?2")
	int updateReadTimesByDocId(int number, int doc_id);

	TDocInfo findByDocCode(String doc_code);

	Page<TDocInfo> findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByPubInfoOperateTimeDesc(int sec_level,
			int pub_status, Pageable pageable);

	Page<TDocInfo> findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByReadTimesDescPubInfoOperateTimeDesc(
			int sec_level, int pub_status, Pageable pageable);

	Page<TDocInfo> findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByColeTimesDescPubInfoOperateTimeDesc(
			int sec_level, int pub_status, Pageable pageable);

}
