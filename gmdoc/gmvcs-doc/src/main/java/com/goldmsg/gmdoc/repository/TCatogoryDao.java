package com.goldmsg.gmdoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldmsg.gmdoc.entity.TCatogory;

public interface TCatogoryDao
		extends PagingAndSortingRepository<TCatogory, Integer>, JpaSpecificationExecutor<TCatogory> {

	List<TCatogory> findByParentId(int parentId);

	List<TCatogory> findByCatoCodeAndCatoStatus(String cato_code, String string);

	@Query("select max(t.catoCode) from TCatogory t where parentId=?1")
	String findMaxCatoCodeByParentId(int parent_id);

	List<TCatogory> findByCatoName(String catoName);
}
