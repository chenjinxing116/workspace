package com.goldmsg.gmdoc.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goldmsg.gmdoc.entity.RelUserColDoc;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.repository.CollectionDao;
import com.goldmsg.gmdoc.repository.TDocInfoDao;
import com.goldmsg.gmdoc.repository.TUserInfoDao;

@Service
public class CollectionService {

	@Autowired
	TUserInfoDao userDao;

	@Autowired
	CollectionDao colDao;

	@Autowired
	TDocInfoDao docDao;

	@Autowired
	EntityManager em;

	@Autowired
	GMUserService userService;

	@Modifying
	@Transactional
	public int collectDoc(RelUserColDoc col) {
		RelUserColDoc exist = colDao.findOne(col.getId());
		RelUserColDoc result = colDao.save(col);
		if (result == null) {
			return 0;
		} else {
			if (exist == null) {
				docDao.updateColeTimesByDocId(1, result.getId().getDocId());
				return 1;
			} else {
				return 1;
			}
		}
	}

	@Modifying
	@Transactional
	public boolean disCollectDoc(RelUserColDoc col) {
		RelUserColDoc exist = colDao.findOne(col.getId());
		if (exist != null) {
			colDao.delete(col);
			int doc_id = col.getId().getDocId();
			int ret = docDao.updateColeTimesByDocId(-1, doc_id);
			if (ret >= 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean exist(RelUserColDocPK pk) {
		RelUserColDoc exist = colDao.findOne(pk);
		if (exist != null) {
			return true;
		} else {
			return false;
		}

	}
}
