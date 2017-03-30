package com.goldmsg.gmdoc.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.goldmsg.core.helpers.PropertiesHelper;
import com.goldmsg.gmdoc.entity.RelUserColDoc;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.entity.TDocInfo;
import com.goldmsg.gmdoc.entity.TPublish;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.repository.CollectionDao;
import com.goldmsg.gmdoc.repository.TDocInfoDao;
import com.goldmsg.gmdoc.repository.TPublishDao;
import com.goldmsg.gmdoc.repository.TUserInfoDao;
import com.gosun.core.exception.SystemException;
import com.gosun.core.utils.security.MD5;
import com.gosun.service.user.IUserService;

import edu.emory.mathcs.backport.java.util.Collections;

@Service
public class DocService {

	@Autowired
	TUserInfoDao userDao;

	@Autowired
	TDocInfoDao docDao;

	@Autowired
	CollectionDao colDao;

	@Autowired
	TPublishDao pubDao;

	@Autowired
	IUserService iUserService;

	@Autowired
	GMUserService userService;

	@Autowired
	HttpSession session;

	private MD5 md5 = new MD5();

	/***
	 * 保存文档服务
	 * 
	 * @param doc
	 * @return
	 * @throws SystemException
	 * @throws Exception
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@Modifying
	@Transactional
	public String storeTmpDoc(MultipartFile file) throws SystemException {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String fileName = originalFilename.substring(0, index);
			String fileType = originalFilename.substring(index);
			fileName = md5.encrypt(fileName + "xiangrandy");
			// 文件临时保存路径
			String path = PropertiesHelper.getProperty("file.temp.path").trim() + fileName + fileType;
			File tmpFile = new File(path);
			// 转存文件
			try {
				file.transferTo(tmpFile);
			} catch (IllegalStateException | IOException e) {
				return "";
			} catch (Exception e) {
				return "";
			}
			String charset = "";
			try {
				charset = getCharset(new File(path));
			} catch (IOException e) {
				throw new SystemException("unknown charset:" + charset, e);
			}
			if (path.endsWith(".txt")) {
				if (charset.equals("GBK")) {
					try {
						transferFile(path, path);
					} catch (IOException e) {
						throw new SystemException("GBK 转码UTF-8失败", e);
					}
				}
			}
			return path;
		} else {
			return "";
		}
	}

	private static void transferFile(String srcFileName, String destFileName) throws IOException {
		String line_separator = System.getProperty("line.separator");
		FileInputStream fis = new FileInputStream(srcFileName);
		StringBuffer content = new StringBuffer();
		DataInputStream in = new DataInputStream(fis);
		BufferedReader d = new BufferedReader(new InputStreamReader(in, "GBK")); // "UTF-8"
		String line = null;
		while ((line = d.readLine()) != null) {
			content.append(line + line_separator);
		}
		d.close();
		in.close();
		fis.close();
		Writer ow = new OutputStreamWriter(new FileOutputStream(destFileName), "utf-8");
		ow.write(content.toString());
		ow.close();
	}

	private String toHex(byte[] byteArray) {
		int i;
		StringBuffer buf = new StringBuffer("");
		int len = byteArray.length;
		for (int offset = 0; offset < len; offset++) {
			i = byteArray[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString().toUpperCase();
	}

	private String getCharset(File fileName) throws IOException {
		byte[] b = new byte[10];
		try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));) {
			bin.read(b, 0, b.length);
		}
		String first = toHex(b);
		// 这里可以看到各种编码的前几个字符是什么，gbk编码前面没有多余的
		String code = null;
		if (first.startsWith("EFBBBF")) {
			code = "UTF-8";
		} else if (first.startsWith("FEFF00")) {
			code = "UTF-16BE";
		} else if (first.startsWith("FFFE")) {
			code = "Unicode";
		} else {
			code = "GBK";
		}
		return code;
	}

	public TDocInfo getByDocCode(String doc_code) {
		TDocInfo docInfo = docDao.findByDocCode(doc_code);
		return docInfo;
	}

	public boolean deleteTmpDoc(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Modifying
	public boolean commitDoc(TDocInfo docInfo) {
		TDocInfo result = docDao.save(docInfo);
		if (result != null) {
			return true;
		} else {
			return false;
		}

	}

	@Modifying
	@Transactional
	public int editDoc(Map<String, Object> map) {
		int doc_id = (int) map.get("doc_id");
		TDocInfo doc = docDao.findOne(doc_id);
		doc.setDocTitle(map.get("doc_title").toString());
		docDao.save(doc);
		return 1;
	}

	@Modifying
	@Transactional
	public int deleteDoc(int doc_id) {
		TDocInfo doc = docDao.findOne(doc_id);
		docDao.delete(doc);
		return 1;
	}

	public List<Map<String, Object>> getDocsByCatoId(TUserInfo userInfo, int cato_id, int page, int pageSize) {
		int sec_level = userInfo.getSecLevel();
		Pageable pageable = new PageRequest(page, pageSize);
		List<TDocInfo> docs = docDao
				.findBySecLevelLessThanEqualAndDocCatoIdAndPubInfoPubStatusOrderByPubInfoOperateTimeDesc(sec_level,
						cato_id, 3, pageable);
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TDocInfo doc : docs) {
			Map<String, Object> t_map = new HashMap<String, Object>();
			t_map.put("doc_id", doc.getDocId());
			t_map.put("doc_code", doc.getDocCode());
			t_map.put("doc_title", doc.getDocTitle());
			t_map.put("doc_type", doc.getDocType());
			t_map.put("doc_cato_name", doc.getCatoInfo().getCatoName());
			t_map.put("pub_user_name", doc.getPubInfo().getPubUserInfo().getUserName());
			String user_code = doc.getPubInfo().getPubUserInfo().getUserCode();
			t_map.put("pub_org_name", iUserService.getUserInfoByAccountName(user_code).getOrgName());
			t_map.put("upload_time", sdf.format(doc.getUploadTime()));
			t_map.put("pub_dateline", sdf.format(doc.getPubInfo().getOperateTime()));
			body.add(t_map);
		}
		return body;
	}

	public TDocInfo getById(int doc_id) {
		return docDao.findOne(doc_id);
	}

	public boolean copyDoc(File sourceFile, File distFile) throws SystemException {
		int length = 2097152;
		try (FileInputStream in = new FileInputStream(sourceFile);
				FileOutputStream out = new FileOutputStream(distFile);
				FileChannel inC = in.getChannel();
				FileChannel outC = out.getChannel();) {
			while (true) {
				if (inC.position() == inC.size()) {
					out.flush();
					return true;
				} else {
					if ((inC.size() - inC.position()) < 20971520) {
						length = (int) (inC.size() - inC.position());
					} else {
						length = 20971520;
					}
					inC.transferTo(inC.position(), length, outC);
					inC.position(inC.position() + length);
				}
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	public Page<TDocInfo> searchDoc(final Integer[] dist_ids, final Integer[] cato_ids, final String[] doc_types,
			final int sec_level, int page, int pageSize) {
		// Data JPA动态条件查询
		Pageable pageable = new PageRequest(page, pageSize);
		Page<TDocInfo> docs = docDao.findAll(new Specification<TDocInfo>() {
			@Override
			public Predicate toPredicate(Root<TDocInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (cato_ids != null) {
					List<Integer> catoIdList = new ArrayList<Integer>();
					Collections.addAll(catoIdList, cato_ids);
					if (catoIdList != null && !catoIdList.isEmpty()) {
						if (catoIdList.size() == 1) {
							expressions.add(cb.equal(root.<Integer> get("docCatoId"), catoIdList.get(0)));
						} else {
							expressions.add(root.<Integer> get("docCatoId").in(catoIdList));
						}
					}
				}
				if (doc_types != null) {
					List<String> docTypeList = new ArrayList<String>();
					Collections.addAll(docTypeList, doc_types);
					if (docTypeList != null && !docTypeList.isEmpty()) {
						if (docTypeList.size() == 1) {
							expressions.add(cb.equal(root.<String> get("docType"), docTypeList.get(0)));
						} else {
							expressions.add(root.<String> get("docType").in(docTypeList));
						}
					}
				}
				expressions.add(cb.lessThanOrEqualTo(root.<Integer> get("secLevel"), sec_level));
				query.orderBy(cb.desc(root.<TPublish> get("pubInfo").<Date> get("operateTime")));
				return predicate;
			}
		}, pageable);
		return docs;
	}

	public TDocInfo fetchByDocCode(Page<TDocInfo> docs, String doc_code) {
		for (TDocInfo doc : docs) {
			if (doc.getDocCode().equals(doc_code)) {
				return doc;
			} else {
				continue;
			}
		}
		return null;
	}

	public List<Map<String, Object>> getDocsOrderByUploadTime(TUserInfo userInfo, int page, int pageSize) {
		int sec_level = userInfo.getSecLevel();
		int pub_status = 3;
		Pageable pageable = new PageRequest(page, pageSize);
		List<TDocInfo> docs = docDao.findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByUploadTimeDesc(sec_level,
				pub_status, pageable);
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TDocInfo doc : docs) {
			Map<String, Object> t_map = new HashMap<String, Object>();
			t_map.put("doc_id", doc.getDocId());
			t_map.put("doc_code", doc.getDocCode());
			t_map.put("doc_title", doc.getDocTitle());
			t_map.put("doc_type", doc.getDocType());
			t_map.put("doc_cato_name", doc.getCatoInfo().getCatoName());
			t_map.put("pub_user_name", doc.getPubInfo().getPubUserInfo().getUserName());
			String user_code = doc.getPubInfo().getPubUserInfo().getUserCode();
			t_map.put("pub_org_name", iUserService.getUserInfoByAccountName(user_code).getOrgName());
			t_map.put("upload_time", sdf.format(doc.getUploadTime()));
			t_map.put("pub_dateline", sdf.format(doc.getPubInfo().getOperateTime()));
			body.add(t_map);
		}
		return body;
	}

	/**
	 * 获取最新发布的文档列表
	 * 
	 * @param userInfo
	 * @param dist_id
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getLatestPubDocs(TUserInfo userInfo, int dist_id, int page, int pageSize) {
		int user_id = userInfo.getUserId();
		int sec_level = userDao.findSecLevelByUserId(user_id);
		Pageable pageable = new PageRequest(page, pageSize);
		Page<TDocInfo> docs = docDao
				.findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByPubInfoOperateTimeDesc(sec_level, 3, pageable);
		List<Map<String, Object>> docMapList = transdocs2Maps(user_id, docs);
		return docMapList;
	}

	/**
	 * 获取收藏榜单
	 * 
	 * @param userInfo
	 * @param dist_id
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getHotColeDocs(TUserInfo userInfo, int dist_id, int page, int pageSize) {
		// 按照收藏人数降序排序，相同收藏人数则按照发布时间降序排序
		int user_id = userInfo.getUserId();
		int sec_level = userDao.findSecLevelByUserId(user_id);
		Pageable pageable = new PageRequest(page, pageSize);
		Page<TDocInfo> docs = docDao
				.findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByColeTimesDescPubInfoOperateTimeDesc(sec_level, 3,
						pageable);
		List<Map<String, Object>> docMapList = transdocs2Maps(user_id, docs);
		return docMapList;
	}

	/**
	 * 获取阅读榜单
	 * 
	 * @param userInfo
	 * @param dist_id
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getHotReadDocs(TUserInfo userInfo, int dist_id, int page, int pageSize) {
		// 按照阅读人数降序排序，相同阅读人数则按照发布时间降序排序
		int user_id = userInfo.getUserId();
		int sec_level = userDao.findSecLevelByUserId(user_id);
		Pageable pageable = new PageRequest(page, pageSize);
		Page<TDocInfo> docs = docDao
				.findBySecLevelLessThanEqualAndPubInfoPubStatusOrderByReadTimesDescPubInfoOperateTimeDesc(sec_level, 3,
						pageable);
		List<Map<String, Object>> docMapList = transdocs2Maps(user_id, docs);
		return docMapList;
	}

	private List<Map<String, Object>> transdocs2Maps(int user_id, Page<TDocInfo> docs) {
		List<Map<String, Object>> docMapList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TDocInfo doc : docs) {
			Map<String, Object> map = new HashMap<String, Object>();
			int doc_id = doc.getDocId();
			map.put("doc_id", doc.getDocId());
			map.put("doc_code", doc.getDocCode());
			map.put("doc_title", doc.getDocTitle());
			map.put("doc_type", doc.getDocType());
			map.put("doc_cato_name", doc.getCatoInfo().getCatoName());
			map.put("pub_dateline", sdf.format(doc.getPubInfo().getOperateTime()));
			map.put("read_times", doc.getReadTimes());
			map.put("cole_times", doc.getColeTimes());
			RelUserColDocPK cole_pk = new RelUserColDocPK(user_id, doc_id);
			map.put("is_collected", exist(cole_pk));
			docMapList.add(map);
		}
		return docMapList;
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
