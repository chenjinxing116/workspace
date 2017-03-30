package com.goldmsg.gmdoc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.RelUserColDoc;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.entity.TUserInfo;

public interface CollectionDao extends PagingAndSortingRepository<RelUserColDoc, RelUserColDocPK>,
JpaSpecificationExecutor<RelUserColDoc>{

	Page<RelUserColDoc> findByUserInfoOrderByColeTimeDesc(TUserInfo userInfo, Pageable pageable);
}
