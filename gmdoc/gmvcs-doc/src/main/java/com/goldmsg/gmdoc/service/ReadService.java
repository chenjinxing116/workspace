package com.goldmsg.gmdoc.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goldmsg.gmdoc.entity.RelUserReadDoc;
import com.goldmsg.gmdoc.repository.ReadDao;
import com.goldmsg.gmdoc.repository.TDocInfoDao;
import com.goldmsg.gmdoc.repository.TUserInfoDao;

@Service
public class ReadService {

	@Autowired
	TUserInfoDao userDao;

	@Autowired
	ReadDao readDao;

	@Autowired
	TDocInfoDao docDao;

	@Autowired
	GMUserService userService;

	@Autowired
	CollectionService docCollectionService;

	@Autowired
	EntityManager em;

	@Modifying
	@Transactional
	public boolean disReadDoc(RelUserReadDoc read) {
		RelUserReadDoc exist = readDao.findOne(read.getId());
		if (exist != null) {
			readDao.delete(read);
			int doc_id = read.getId().getDocId();
			int ret = docDao.updateReadTimesByDocId(-1, doc_id);
			if (ret >= 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Modifying
	@Transactional
	public int readDoc(RelUserReadDoc read) {
		RelUserReadDoc exist = readDao.findOne(read.getId());
		RelUserReadDoc result = readDao.save(read);
		if (result == null) {
			return 0;
		} else {
			if (exist == null) {
				readDao.updateReadTimesByDocId(1, result.getId().getDocId());
				return 1;
			} else {
				return 1;
			}
		}
	}
}
