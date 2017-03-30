package com.goldmsg.gmdoc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goldmsg.gmdoc.entity.RelUserColDoc;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.entity.RelUserReadDoc;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.entity.TDocInfo;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.repository.ReadDao;
import com.goldmsg.gmdoc.repository.TDocInfoDao;
import com.goldmsg.gmdoc.repository.TUserInfoDao;
import com.gosun.core.utils.date.DateTimeUtils;
import com.gosun.service.entity.UserRsp;
import com.gosun.service.user.IUserService;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

@Service
public class GMUserService {

	@Autowired
	TUserInfoDao userDao;

	@Autowired
	TDocInfoDao tDocInfoDao;

	@Autowired
	EntityManager em;

	@Autowired
	ReadDao readDao;

	@Autowired
	MemcachedClient memcachedClient;

	@Autowired
	IUserService iUserService;

	@Autowired
	CollectionService coleService;

	@Autowired
	HttpSession session;

	public TDistrictDict findDistInfoByUserCode(String user_code) {
		List<TUserInfo> resultList = userDao.findByUserCode(user_code);
		int userId = resultList.get(0).getUserId();
		TUserInfo userInfo = userDao.findOne(userId);
		TDistrictDict ret = userInfo.getDistInfo();
		return ret;
	}

	public TUserInfo getUserInfoByUserCode(String user_code) {
		List<TUserInfo> resultList = userDao.findByUserCode(user_code);
		if (resultList == null || resultList.size() == 0) {
			return null;
		} else {
			TUserInfo userInfo = resultList.get(0);
			return userInfo;
		}
	}

	/*
	 * 获取指定用户的文档收藏记录
	 */
	public List<Map<String, Object>> getColeDocsByUserInfo(TUserInfo userInfo, int page, int pageSize) {
		Query query = em.createQuery("from RelUserColDoc col where col.userInfo=?1 order by col.coleTime desc");
		query.setParameter(1, userInfo);
		@SuppressWarnings("unchecked")
		List<RelUserColDoc> coleDocs = query.setMaxResults((page + 1) * pageSize).setFirstResult(page * pageSize)
				.getResultList();
		List<Map<String, Object>> docList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (RelUserColDoc coleDoc : coleDocs) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_id", coleDoc.getDocInfo().getDocId());
			map.put("doc_code", coleDoc.getDocInfo().getDocCode());
			map.put("doc_title", coleDoc.getDocInfo().getDocTitle());
			map.put("doc_type", coleDoc.getDocInfo().getDocType());
			map.put("doc_cato_name", coleDoc.getDocInfo().getCatoInfo().getCatoName());
			map.put("cole_time", sdf.format(coleDoc.getColeTime()));
			docList.add(map);
		}
		return docList;
	}

	/*
	 * 获取指定用户的文档阅读记录
	 */
	public List<Map<String, Object>> getReadDocByUserInfo(TUserInfo userInfo, int page, int pageSize) {
		Query query = em.createQuery("from RelUserReadDoc read where read.userInfo=?1 order by read.readTime desc");
		query.setParameter(1, userInfo);
		@SuppressWarnings("unchecked")
		List<RelUserReadDoc> readDocs = query.setMaxResults((page + 1) * pageSize).setFirstResult(page * pageSize)
				.getResultList();
		List<Map<String, Object>> docList = new ArrayList<Map<String, Object>>();
		int user_id = userInfo.getUserId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (RelUserReadDoc rdoc : readDocs) {
			Map<String, Object> map = new HashMap<String, Object>();
			TDocInfo docInfo = rdoc.getDocInfo();
			map.put("doc_id", docInfo.getDocId());
			map.put("doc_code", docInfo.getDocCode());
			map.put("doc_title", docInfo.getDocTitle());
			map.put("doc_type", docInfo.getDocType());
			map.put("doc_cato_name", docInfo.getDocCatoId());
			map.put("read_time", sdf.format(rdoc.getReadTime()));
			RelUserColDocPK cole_pk = new RelUserColDocPK(user_id, docInfo.getDocId());
			map.put("is_collected", coleService.exist(cole_pk));
			docList.add(map);
		}
		return docList;
	}

	@Modifying
	@Transactional
	public boolean deleteReadRecordsByUserInfo(TUserInfo userInfo) {
		int result = readDao.deleteByUserInfo(userInfo);
		if (result >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public TUserInfo getById(int user_id) {
		TUserInfo userInfo = userDao.findOne(user_id);
		return userInfo;
	}

	/*
	 * 增加文档阅读次数
	 */
	public boolean addReadTimes(TDocInfo docInfo) {
		if (docInfo != null) {
			docInfo.setReadTimes(docInfo.getReadTimes() + 1);
			docInfo.setUploadTime(DateTimeUtils.getDateByByDaysInt(0));
			tDocInfoDao.save(docInfo);
			return true;
		} else {
			return false;
		}
	}

	public TUserInfo getUserInfoByToken(String token)
			throws TimeoutException, InterruptedException, MemcachedException {
		TUserInfo userInfo = memcachedClient.get(token);
		return userInfo;
	}

	public boolean readable(TUserInfo userInfo, TDocInfo docInfo) {
		int user_sec_level = userInfo.getSecLevel();
		int doc_sec_level = docInfo.getSecLevel();
		if (user_sec_level >= doc_sec_level) {
			return true;
		} else {
			return false;
		}
	}

	public TUserInfo createUserInfo(String user_code, String user_name) {
		TUserInfo newUserInfo = new TUserInfo();
		newUserInfo.setUserCode(user_code);
		newUserInfo.setUserName(user_name);
		newUserInfo.setSecLevel(0);
		TDistrictDict distInfo = new TDistrictDict();
		// TODO:地区切换暂不支持，目前所有警员所在地区全部设定为2483即重庆市
		int dist_id = 2483;
		distInfo.setDistId(dist_id);
		newUserInfo.setDistInfo(distInfo);
		TUserInfo userInfo = userDao.save(newUserInfo);
		if (userInfo == null) {
			return null;
		} else {
			return userInfo;
		}
	}

	@Transactional
	@Modifying
	public boolean updateUserSecLevel(int sec_level, String user_code) {
		List<TUserInfo> exists = userDao.findByUserCode(user_code);
		if (exists != null && !exists.isEmpty()) {
			int ret = userDao.updateSecLevelByUserCode(sec_level, user_code);
			if (ret > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			UserRsp userRsp = iUserService.getUserInfoByAccountName(user_code);
			if (userRsp == null) {
				return false;
			} else {
				TUserInfo newUserInfo = new TUserInfo();
				newUserInfo.setUserCode(user_code);
				newUserInfo.setUserName(userRsp.getUserName());
				newUserInfo.setSecLevel(sec_level);
				TDistrictDict distInfo = new TDistrictDict();
				// TODO:此处默认写成了2483，待后续做处理
				distInfo.setDistId(2483);
				newUserInfo.setDistInfo(distInfo);
				TUserInfo userInfo = userDao.save(newUserInfo);
				if (userInfo == null) {
					return false;
				} else {
					return true;
				}
			}
		}
	}

	public boolean refreshUserInfoInMemory(TUserInfo curUserInfo) {
		// 刷新session中的用户信息
		session.setAttribute("userInfo", curUserInfo);
		// 刷新memcachedClient中的用户信息
		String token;
		try {
			token = memcachedClient.get("gmdoc" + curUserInfo.getUserCode());
			if (token == null) {
				return false;
			} else {
				if (memcachedClient.set(token, 60 * 60 * 24 * 14, curUserInfo)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			return false;
		}
	}

	public int getSecLevelByUserId(int user_id) {
		int sec_level = userDao.findSecLevelByUserId(user_id);
		return sec_level;
	}

	public List<Map<String, Object>> getUserListByOrgId(int org_id) {
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		List<UserRsp> userRspList = iUserService.getUserInfosByOrgId(org_id);
		if (userRspList != null) {
			for (UserRsp userRsp : userRspList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user_code", userRsp.getAccountName());
				map.put("user_name", userRsp.getUserName());
				userList.add(map);
			}
		}
		return userList;
	}

	public Map<String, Object> getUserAuthInfo(String user_code) {
		Map<String, Object> userMap = new HashMap<String, Object>();
		List<TUserInfo> userList = userDao.findByUserCode(user_code);
		TUserInfo userInfo = null;
		if (userList == null || userList.isEmpty()) {
			UserRsp rsp = iUserService.getUserInfoByAccountName(user_code);
			String user_name = rsp.getUserName();
			TUserInfo user = createUserInfo(user_code, user_name);
			if (user == null) {
				return userMap;
			} else {
				userInfo = user;
			}
		} else {
			userInfo = userList.get(0);
		}
		userMap.put("user_code", userInfo.getUserCode());
		userMap.put("user_name", userInfo.getUserName());
		userMap.put("sec_level", userInfo.getSecLevel());
		return userMap;
	}
}
