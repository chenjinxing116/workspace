package com.goldmsg.gmdoc.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDistrictDict;

public interface TDistDao extends PagingAndSortingRepository<TDistrictDict, Integer>, 
JpaSpecificationExecutor<TDistrictDict> {

	TDistrictDict findByDistId(int dist_id);

	@Query("select t.catoInfos from TDistrictDict t where t.distId=?1")
	Set<TCatogory> findCatoInfosByDistId(int dist_id);
	
}
