package com.goldmsg.gmdoc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.TPublish;

public interface TPublishDao extends PagingAndSortingRepository<TPublish, Integer>, JpaSpecificationExecutor<TPublish> {

	Page<TPublish> findByPubStatusOrderByOperateTimeDesc(int status, Pageable pageable);
}
