package com.goldmsg.gmdoc.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.repository.TDistDao;
import com.goldmsg.gmdoc.repository.TPublishDao;

@Service
public class DistService {

	@Autowired
	TDistDao distDao;

	@Autowired
	TPublishDao publishDao;

	public Set<TCatogory> getCatoListByDistId(int dist_id) {
		Set<TCatogory> result = distDao.findCatoInfosByDistId(dist_id);
		return result;
	}

	public TDistrictDict findByDistId(int dist_id) {
		return distDao.findOne(dist_id);
	}
}
