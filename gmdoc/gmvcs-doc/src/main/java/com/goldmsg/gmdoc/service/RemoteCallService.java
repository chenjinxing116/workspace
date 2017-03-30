package com.goldmsg.gmdoc.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.goldmsg.cooldoc.core.CoolDocClient;
import com.goldmsg.coolsearch.bean.XQueryResponse;
import com.goldmsg.coolsearch.core.XSolrField;
import com.goldmsg.core.helpers.PropertiesHelper;
import com.goldmsg.core.utils.HTTPParam;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDocInfo;
import com.goldmsg.gmdoc.entity.solr.GoldmsgDocument;

@Service
public class RemoteCallService {

	@Autowired
	DocService docService;

	@Autowired
	CatoService catoService;

	@Autowired
	CollectionService coleService;

	public String sendGet(String url, List<HTTPParam> list) throws Exception {
		StringBuffer buffer = new StringBuffer(); // 用来拼接参数
		StringBuffer result = new StringBuffer(); // 用来接受返回值
		URL httpUrl = null; // HTTP URL类 用这个类来创建连接
		URLConnection connection = null; // 创建的http连接
		BufferedReader bufferedReader = null; // 接受连接受的参数
		// 如果存在参数，我们才需要拼接参数 类似于 localhost/index.html?a=a&b=b
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				buffer.append(list.get(i).getKey()).append("=").append(list.get(i).getValue());
				// 如果不是最后一个参数，不需要添加&
				if ((i + 1) < list.size()) {
					buffer.append("&");
				}
			}
			url = (url + "?" + buffer.toString()).trim();
		}
		// 创建URL
		httpUrl = new URL(url);
		// 建立连接
		connection = httpUrl.openConnection();
		connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("connection", "keep-alive");
		connection.setRequestProperty("user-agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
		connection.connect();
		// 接受连接返回参数
		bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}
		bufferedReader.close();
		return result.toString();
	}

	public List<Map<String, Object>> search(int user_id, String[] keywords, Integer[] cato_ids, String[] doc_types,
			int sec_level, int start, int limit) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		CoolDocClient instance = CoolDocClient.getInstance();
		String solr_ip = PropertiesHelper.getProperty("solr.ip");
		int solr_port = Integer.parseInt(PropertiesHelper.getProperty("solr.port"));
		if (!instance.init(solr_ip, solr_port)) {
			return ret;
		} else {
			// TODO:排序方案
			XQueryResponse xQueryResponse = instance.query(keywords, cato_ids, doc_types, sec_level, start, limit,
					GoldmsgDocument.class);
			if (null != xQueryResponse && 0 < xQueryResponse.getNumFound()) {
				List<GoldmsgDocument> solr_docs = parseRet(xQueryResponse);
				if (solr_docs != null && !solr_docs.isEmpty()) {
					for (GoldmsgDocument doc : solr_docs) {
						TDocInfo docInfo = docService.getByDocCode(doc.getDoc_code());
						if (docInfo == null) {
							continue;
						}
						Map<String, Object> docMap = new HashMap<String, Object>();
						docMap.put("doc_id", docInfo.getDocId());
						docMap.put("doc_code", docInfo.getDocCode());
						docMap.put("doc_title", docInfo.getDocTitle());
						docMap.put("doc_title_hl", doc.fetchHighlightField("doc_title_hl"));
						docMap.put("doc_content", doc.getContent());
						docMap.put("doc_content_hl", doc.fetchHighlightField(XSolrField.X_CONTENT_HL_NAME));
						docMap.put("doc_type", docInfo.getDocType());
						RelUserColDocPK pk = new RelUserColDocPK(user_id, docInfo.getDocId());
						int doc_cato_id = docInfo.getDocCatoId();
						TCatogory cato = catoService.findOne(doc_cato_id);
						docMap.put("doc_cato_name", cato.getCatoName());
						docMap.put("pub_dateline",
								sdf.format(docInfo.getPubInfo().getOperateTime()));
						docMap.put("is_collected", coleService.exist(pk));
						ret.add(docMap);
					}
				}
			}
			return ret;
		}
	}

	public List<Map<String, Object>> searchByDocCodes(Page<TDocInfo> docs, int user_id) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> doc_codes = new ArrayList<String>();
		if (docs == null || docs.getNumberOfElements() == 0) {
			return ret;
		} else {
			for (TDocInfo doc : docs) {
				doc_codes.add(doc.getDocCode());
			}
		}
		String[] ids = doc_codes.toArray(new String[doc_codes.size()]);
		CoolDocClient instance = CoolDocClient.getInstance();
		String solr_ip = PropertiesHelper.getProperty("solr.ip");
		int solr_port = Integer.parseInt(PropertiesHelper.getProperty("solr.port"));
		if (!instance.init(solr_ip, solr_port)) {
			return ret;
		} else {
			XQueryResponse xQueryResponse = instance.query(ids, 0, ids.length, GoldmsgDocument.class);
			if (null != xQueryResponse && 0 < xQueryResponse.getNumFound()) {
				List<GoldmsgDocument> solr_docs = parseRet(xQueryResponse);
				if (solr_docs != null && !solr_docs.isEmpty()) {
					for (TDocInfo docInfo : docs) {
						GoldmsgDocument doc = findSolr_docByDocInfo(docInfo, solr_docs);
						if (doc == null) {
							continue;
						}
						Map<String, Object> docMap = new HashMap<String, Object>();
						docMap.put("doc_id", docInfo.getDocId());
						docMap.put("doc_code", docInfo.getDocCode());
						docMap.put("doc_title", docInfo.getDocTitle());
						docMap.put("doc_title_hl", doc.fetchHighlightField("doc_title_hl"));
						docMap.put("doc_content", doc.getContent());
						docMap.put("doc_content_hl", doc.fetchHighlightField(XSolrField.X_CONTENT_HL_NAME));
						docMap.put("doc_type", docInfo.getDocType());
						RelUserColDocPK pk = new RelUserColDocPK(user_id, docInfo.getDocId());
						int doc_cato_id = docInfo.getDocCatoId();
						TCatogory cato = catoService.findOne(doc_cato_id);
						String catoName = cato.getCatoName();
						docMap.put("doc_cato_name", catoName);
						docMap.put("pub_dateline",
								sdf.format(docInfo.getPubInfo().getOperateTime()));
						docMap.put("is_collected", coleService.exist(pk));
						ret.add(docMap);
					}
				}
			}
			return ret;
		}
	}

	private GoldmsgDocument findSolr_docByDocInfo(TDocInfo docInfo, List<GoldmsgDocument> solr_docs) {
		for (GoldmsgDocument solr_doc : solr_docs) {
			if (solr_doc.getDoc_code().equals(docInfo.getDocCode())) {
				return solr_doc;
			}
		}
		return null;
	}

	private List<GoldmsgDocument> parseRet(XQueryResponse xQueryResponse) {
		if (null == xQueryResponse) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<GoldmsgDocument> docs = (List<GoldmsgDocument>) xQueryResponse.getDocuments();
		if (docs == null | docs.isEmpty()) {
			return null;
		} else {
			return docs;
		}
	}

	public Map<String, Object> getSuggest(String preKeyword, String lastKeyword) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		CoolDocClient instance = CoolDocClient.getInstance();
		String solr_ip = PropertiesHelper.getProperty("solr.ip");
		int solr_port = Integer.parseInt(PropertiesHelper.getProperty("solr.port"));
		if (!instance.init(solr_ip, solr_port)) {
			return mapList;
		} else {
			List<String> suggestList = instance.suggest(lastKeyword);
			if (suggestList == null || suggestList.isEmpty()) {
				return mapList;
			} else {
				List<String> retList = new ArrayList<String>();
				for (String str : suggestList) {
					retList.add(preKeyword + str);
				}
				mapList.put("suggests", retList);
				return mapList;
			}
		}
	}
}
