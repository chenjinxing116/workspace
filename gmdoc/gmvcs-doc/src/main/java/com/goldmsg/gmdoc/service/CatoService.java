package com.goldmsg.gmdoc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goldmsg.gmdoc.entity.RelDistCato;
import com.goldmsg.gmdoc.entity.RelDistCatoPK;
import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDocInfo;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.repository.RelDistCatoDao;
import com.goldmsg.gmdoc.repository.TCatogoryDao;
import com.goldmsg.gmdoc.repository.TDocInfoDao;
import com.goldmsg.gmdoc.repository.TPublishDao;

@Service
public class CatoService {

	@Autowired
	TCatogoryDao tCatogoryDao;

	@Autowired
	TDocInfoDao docDao;

	@Autowired
	DocService docService;

	@Autowired
	TPublishDao pubDao;

	@Autowired
	DistService distService;

	@Autowired
	RelDistCatoDao relDistCatoDao;

	@Modifying
	@Transactional
	public TCatogory saveCatoName(TCatogory cato) {
		List<TCatogory> retList = tCatogoryDao.findByCatoName(cato.getCatoName());
		if (retList.isEmpty()) {
			TCatogory ret = tCatogoryDao.findOne(cato.getCatoId());
			if (ret != null) {
				ret.setCatoName(cato.getCatoName());
				TCatogory saved_cato = tCatogoryDao.save(ret);
				if (saved_cato == null) {
					return null;
				} else {
					return saved_cato;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Modifying
	@Transactional
	public boolean deleteCato(int cato_id, int dist_id) {
		TCatogory exists = tCatogoryDao.findOne(cato_id);
		if (exists != null) {
			if (exists.getParentId() == 1) {
				List<TCatogory> children = tCatogoryDao.findByParentId(exists.getCatoId());
				if (children == null || children.isEmpty()) {
					RelDistCatoPK pk = new RelDistCatoPK(dist_id, cato_id);
					relDistCatoDao.delete(pk);
					tCatogoryDao.delete(cato_id);
					return true;
				} else {
					return false;
				}
			} else {
				Page<TDocInfo> docInfos = docDao.findOneByDocCatoId(cato_id, new PageRequest(0, 1));
				if (docInfos == null || docInfos.getNumberOfElements() == 0) {
					RelDistCatoPK pk = new RelDistCatoPK(dist_id, cato_id);
					relDistCatoDao.delete(pk);
					tCatogoryDao.delete(cato_id);
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}

	public TCatogory findOne(int doc_cato_id) {
		TCatogory cato = tCatogoryDao.findOne(doc_cato_id);
		return cato;
	}

	public String generateCatoCodeByParentId(int parent_id) {
		String catoCode = tCatogoryDao.findMaxCatoCodeByParentId(parent_id);
		if (catoCode == null || catoCode.isEmpty()) {
			TCatogory parent_cato = tCatogoryDao.findOne(parent_id);
			if (parent_cato == null) {
				return null;
			} else {
				String parent_code = parent_cato.getCatoCode();
				catoCode = parent_code.substring(0, 11) + "0";
			}
		}
		if (parent_id == 1) {
			String sub = catoCode.substring(0, 6);
			int index = 0;
			for (int i = 0; i < 6; i++) {
				char c = sub.charAt(i);
				if (c == 0) {
					continue;
				} else {
					index = i;
					break;
				}
			}
			Integer number = Integer.parseInt(sub.substring(index + 1)) + 1;
			String tailer = number.toString();
			int length = tailer.length();
			int count = 6 - length;
			StringBuffer header = new StringBuffer();
			for (int i = 0; i < count; i++) {
				header.append("0");
			}
			catoCode = header + tailer + catoCode.substring(6);
		} else {
			String sub = catoCode.substring(6);
			int index = 0;
			for (int i = 0; i < 6; i++) {
				char c = sub.charAt(i);
				if (c == 0) {
					continue;
				} else {
					index = i;
					break;
				}
			}
			Integer number = Integer.parseInt(sub.substring(index + 1)) + 1;
			String tailer = number.toString();
			int length = tailer.length();
			int count = 6 - length;
			StringBuffer header = new StringBuffer();
			for (int i = 0; i < count; i++) {
				header.append("0");
			}
			catoCode = catoCode.substring(0, 6) + header + tailer;
		}
		return catoCode;
	}

	@Modifying
	@Transactional
	public TCatogory saveCatoAndRelInfo(TCatogory cato, int dist_id) {
		List<TCatogory> retList = tCatogoryDao.findByCatoName(cato.getCatoName());
		boolean exist = tCatogoryDao.exists(cato.getParentId());
		if (retList.isEmpty() && exist) {
			TCatogory saved_cato = tCatogoryDao.save(cato);
			if (saved_cato == null) {
				return null;
			} else {
				RelDistCatoPK pk = new RelDistCatoPK(dist_id, saved_cato.getCatoId());
				RelDistCato rel = new RelDistCato();
				rel.setId(pk);
				RelDistCato ret = relDistCatoDao.save(rel);
				if (ret == null) {
					return null;
				} else {
					return saved_cato;
				}
			}
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> getCatoInfoList(int dist_id) {
		return getCatoInfoList(null, dist_id);
	}

	public List<Map<String, Object>> getCatoInfoList(TUserInfo userInfo, int dist_id) {
		List<Map<String, Object>> catoInfoList = new ArrayList<Map<String, Object>>();
		Set<TCatogory> catoList = distService.getCatoListByDistId(dist_id);
		for (TCatogory cato : catoList) {
			int parent_id = cato.getParentId();
			if (parent_id == 1) {
				Map<String, Object> parent_map = new HashMap<String, Object>();
				int cato_id = cato.getCatoId();
				parent_map.put("cato_id", cato_id);
				parent_map.put("cato_name", cato.getCatoName());
				parent_map.put("parent_id", parent_id);
				List<Map<String, Object>> children = getChildren(userInfo, cato_id, catoList);
				parent_map.put("children", children);
				catoInfoList.add(parent_map);
			} else {
				continue;
			}
		}
		return catoInfoList;
	}

	private List<Map<String, Object>> getChildren(TUserInfo userInfo, int parent_cato_id, Set<TCatogory> catoList) {
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (TCatogory cato : catoList) {
			if (cato.getParentId() == parent_cato_id) {
				Map<String, Object> child = new HashMap<String, Object>();
				int cato_id = cato.getCatoId();
				child.put("cato_id", cato_id);
				child.put("cato_name", cato.getCatoName());
				child.put("parent_id", cato.getParentId());
				if (userInfo != null) {
					List<Map<String, Object>> docs = docService.getDocsByCatoId(userInfo, cato_id, 0, 6);
					child.put("docs", docs);
				}
				children.add(child);
			}
		}
		return children;
	}
}
