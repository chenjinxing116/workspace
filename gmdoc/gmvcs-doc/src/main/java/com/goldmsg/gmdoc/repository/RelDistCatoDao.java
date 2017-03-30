package com.goldmsg.gmdoc.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.RelDistCato;
import com.goldmsg.gmdoc.entity.RelDistCatoPK;

public interface RelDistCatoDao
		extends PagingAndSortingRepository<RelDistCato, RelDistCatoPK>, JpaSpecificationExecutor<RelDistCato> {

}
