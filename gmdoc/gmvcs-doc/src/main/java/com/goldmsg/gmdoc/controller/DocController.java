package com.goldmsg.gmdoc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.goldmsg.cooldoc.core.CoolDocClient;
import com.goldmsg.coolformat.client.CoolFormatClient;
import com.goldmsg.core.helpers.PropertiesHelper;
import com.goldmsg.core.utils.JsonUtil;
import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.entity.RelUserColDoc;
import com.goldmsg.gmdoc.entity.RelUserColDocPK;
import com.goldmsg.gmdoc.entity.RelUserReadDoc;
import com.goldmsg.gmdoc.entity.RelUserReadDocPK;
import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.entity.TDocInfo;
import com.goldmsg.gmdoc.entity.TPublish;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.entity.solr.GoldmsgDocument;
import com.goldmsg.gmdoc.interceptor.auth.AuthorityType;
import com.goldmsg.gmdoc.interceptor.auth.FireAuthority;
import com.goldmsg.gmdoc.interceptor.auth.ResultTypeEnum;
import com.goldmsg.gmdoc.service.CatoService;
import com.goldmsg.gmdoc.service.CollectionService;
import com.goldmsg.gmdoc.service.DocService;
import com.goldmsg.gmdoc.service.GMUserService;
import com.goldmsg.gmdoc.service.ReadService;
import com.gosun.core.exception.BusinessException;
import com.gosun.core.exception.SystemException;
import com.gosun.core.utils.file.FileUtil;

import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * 文档管理controller，提供文档上传等功能
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年4月28日 下午5:27:53
 */
@Controller
@RequestMapping(value = "/doc")
public class DocController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	GMUserService userService;

	@Autowired
	DocService docService;

	@Autowired
	ReadService readService;

	@Autowired
	CatoService catoService;

	@Autowired
	CollectionService coleService;

	@Autowired
	HttpSession session;

	/**
	 * 获取文档管理界面方法
	 * 
	 * @return 如果用户存在且有权限，返回文档管理界面；如果用户不存在，返回404页面
	 */
	@FireAuthority(authorityTypes = { AuthorityType.DOCMANAGE }, resultType = ResultTypeEnum.PAGE)
	@RequestMapping("/manage.action")
	public String docManage() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			int dist_id = distInfo.getDistId();
			List<Map<String, Object>> catoInfoList = catoService.getCatoInfoList(userInfo, dist_id);
			request.setAttribute("distInfo", distInfo);
			request.setAttribute("catoList", catoInfoList);
			return "docManage";
		}
	}

	/**
	 * 获取文档上传界面方法
	 * 
	 * @return 如果用户存在且有权限，返回文档上传界面；如果用户不存在，返回404页面
	 */
	@RequestMapping("/upload.action")
	public String docUpload() {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			request.setAttribute("distInfo", distInfo);
			return "docUpload";
		}
	}

	/**
	 * 上传文档接口，将文档上传到临时目录下
	 * 
	 * @param file
	 *            上传的文档
	 * @return 以JSON格式返回上传成功或失败信息
	 * @throws SystemException
	 *             系统异常
	 */
	@RequestMapping(value = "/upfile.action", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(MultipartFile file) throws SystemException {
		// 保存文档的操作
		if (!file.isEmpty()) {
			// 存储文件
			String path = docService.storeTmpDoc(file);
			if (path != null && !path.equals("")) {
				session.setAttribute("tmpFilePath", path);
				return ReturnInfo.genResultJson(ReturnInfo.SUCCESS);
			} else {
				return ReturnInfo.genResultJson(ReturnInfo.FAILED);
			}
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		}
	}

	@RequestMapping(value = "/cancel.action", method = RequestMethod.GET)
	@ResponseBody
	public String uploadCancel() {
		if (session.getAttribute("tmpFilePath") == null) {
			return ReturnInfo.genResultJson(1);
		}
		String path = null != session.getAttribute("tmpFilePath") ? session.getAttribute("tmpFilePath").toString() : "";
		// 删除临时文件
		if (!path.equals("")) {
			boolean result = docService.deleteTmpDoc(path);
			return ReturnInfo.genResultJson(result ? 1 : 0);
		} else {
			return ReturnInfo.genResultJson(0);
		}
	}

	@RequestMapping(value = "/commit.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String uploadCommit(@RequestBody String requestBody) throws SystemException {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("doc_title") || !map.containsKey("doc_label") || !map.containsKey("cato_id")
				|| !map.containsKey("security_level")) {
			return ReturnInfo.genResultJson(4);
		}
		map.put("doc_code", UUID.randomUUID().toString().replace("-", ""));
		String path = null != session.getAttribute("tmpFilePath") ? session.getAttribute("tmpFilePath").toString() : "";
		if (path.equals("")) {
			return ReturnInfo.genResultJson(2);
		}
		File tempFile = new File(path);
		if (tempFile.exists()) {
			String doc_size = FileUtil.getFileSize(tempFile);
			String doc_type = tempFile.getName().substring(tempFile.getName().lastIndexOf(".") + 1,
					tempFile.getName().length());
			map.put("doc_size", doc_size);
			map.put("doc_type", doc_type);
		} else {
			return ReturnInfo.genResultJson(5);
		}
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		map.put("upload_user_id", userInfo.getUserId());
		// 首先，将临时文件夹下的文件复制到文件存储目录下
		String file_temp_path = PropertiesHelper.getProperty("file.temp.path").trim();
		String file_store_path = PropertiesHelper.getProperty("file.store.path").trim();
		String realPath = file_store_path + path.replace(file_temp_path, "");
		File distFile = new File(realPath);
		boolean copyResult = docService.copyDoc(tempFile, distFile);
		if (!copyResult) {
			return ReturnInfo.genResultJson(0);
		}
		map.put("doc_path", realPath);
		// 调用文档格式转换服务
		CoolFormatClient.format(distFile);
		// 创建全文索引
		CoolDocClient instance = CoolDocClient.getInstance();
		String solr_ip = PropertiesHelper.getProperty("solr.ip");
		int solr_port = Integer.parseInt(PropertiesHelper.getProperty("solr.port"));
		if (!instance.init(solr_ip, solr_port)) {
			return ReturnInfo.genResultJson(0);
		}
		// 数据库写入文档信息
		GoldmsgDocument solrDoc = getGoldmsgDocument(map);
		instance.extract(path, solrDoc);
		long retSolr = instance.extract(path, solrDoc);
		if (retSolr == -1L) {
			docService.deleteTmpDoc(realPath);
			docService.deleteTmpDoc(path);
			String id = solrDoc.getId();
			instance.delete(new String[] { id });
			return ReturnInfo.genResultJson(0);
		}
		TDocInfo docInfo = getTDocInfo(map);
		boolean saveResult = docService.commitDoc(docInfo);
		// 最后，删除临时文件
		docService.deleteTmpDoc(path);
		return ReturnInfo.genResultJson(saveResult ? 1 : 0);
	}

	private GoldmsgDocument getGoldmsgDocument(Map<String, Object> map) {
		GoldmsgDocument docInfo = new GoldmsgDocument();
		docInfo.setDoc_code(map.get("doc_code").toString());
		docInfo.setDoc_title(map.get("doc_title").toString());
		String doc_label = map.get("doc_label").toString();
		doc_label = doc_label.replace('，', ',');
		String[] doc_label_array = doc_label.split(",");
		docInfo.setDoc_label(doc_label_array);
		docInfo.setDoc_type(map.get("doc_type").toString());
		docInfo.setDoc_cato_id((int) map.get("cato_id"));
		docInfo.setUpload_user_id((int) map.get("upload_user_id"));
		docInfo.setSecurity_level((int) map.get("security_level"));
		return docInfo;
	}

	private TDocInfo getTDocInfo(Map<String, Object> map) {
		TDocInfo docInfo = new TDocInfo();
		docInfo.setDocCode(map.get("doc_code").toString());
		docInfo.setDocTitle(map.get("doc_title").toString());
		String doc_label = map.get("doc_label").toString();
		docInfo.setDocLabel(doc_label);
		docInfo.setDocSize(map.get("doc_size").toString());
		docInfo.setDocType(map.get("doc_type").toString());
		docInfo.setDocPath(map.get("doc_path").toString());
		TCatogory catoInfo = new TCatogory();
		catoInfo.setCatoId((int) map.get("cato_id"));
		docInfo.setCatoInfo(catoInfo);
		docInfo.setSecLevel((int) map.get("security_level"));
		docInfo.setUploadUserId((int) map.get("upload_user_id"));
		docInfo.setUploadTime(new Date());
		TPublish pubInfo = new TPublish();
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		pubInfo.setAuditUserInfo(userInfo);
		pubInfo.setOperateTime(new Date());
		pubInfo.setPubUserInfo(userInfo);
		pubInfo.setPubStatus(3);
		pubInfo.setDocInfo(docInfo);
		docInfo.setPubInfo(pubInfo);
		return docInfo;
	}

	/**
	 * 获取指定分类的文档
	 * 
	 * 
	 */
	@RequestMapping(value = "/cato.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getDocsByCatoId(@RequestParam(value = "cato_id", defaultValue = "9999") int cato_id,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		List<Map<String, Object>> docs = null;
		if (cato_id == 9999) {
			docs = docService.getDocsOrderByUploadTime(userInfo, page, pageSize);
		} else {
			docs = docService.getDocsByCatoId(userInfo, cato_id, page, pageSize);
		}
		if (docs == null || docs.size() == 0) {
			return ReturnInfo.genResultJson(0);
		} else {
			return ReturnInfo.genResultJson(1, docs);
		}
	}

	/***
	 * 编辑文档
	 * 
	 * 
	 */
	@RequestMapping(value = "/edit.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editDoc(@RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("doc_id")) {
			return ReturnInfo.genResultJson(0);
		} else {
			int result = docService.editDoc(map);
			return ReturnInfo.genResultJson(result);
		}
	}

	/***
	 * 删除文档
	 * 
	 * 
	 */
	@RequestMapping(value = "/delete.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteDoc(@RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("doc_id")) {
			return ReturnInfo.genResultJson(0);
		} else {
			int doc_id = (int) map.get("doc_id");
			int result = docService.deleteDoc(doc_id);
			return ReturnInfo.genResultJson(result);
		}
	}

	/***
	 * 搜索结果-收藏-app 收藏指定的文档
	 * 
	 * 
	 */
	@RequestMapping(value = "/collect.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appCollection(@RequestParam(value = "token") String token, @RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("doc_id")) {
			return ReturnInfo.genResultJson(4);
		}
		int doc_id = (int) map.get("doc_id");
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(4);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			return ReturnInfo.genResultJson(3);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(3);
		}
		int user_id = userInfo.getUserId();
		RelUserColDocPK pk = new RelUserColDocPK(user_id, doc_id);
		RelUserColDoc col = new RelUserColDoc();
		col.setId(pk);
		col.setColeTime(new Date());
		int result = coleService.collectDoc(col);
		return ReturnInfo.genResultJson(result);
	}

	/***
	 * 搜索结果-收藏 收藏指定的文档
	 * 
	 * 
	 */
	@RequestMapping(value = "/collect.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String collection(@RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		int doc_id = (int) map.get("doc_id");
		int user_id = ((TUserInfo) session.getAttribute("userInfo")).getUserId();
		RelUserColDocPK pk = new RelUserColDocPK(user_id, doc_id);
		RelUserColDoc col = new RelUserColDoc();
		col.setId(pk);
		col.setColeTime(new Date());
		int result = coleService.collectDoc(col);
		return ReturnInfo.genResultJson(result);
	}

	/***
	 * 阅读 阅读指定的文档
	 * 
	 * 
	 * 
	 */
	@RequestMapping(value = "/read.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appReadDoc(@RequestParam("doc_id") int doc_id, @RequestParam("token") String token) {
		TDocInfo docInfo = docService.getById(doc_id);
		if (docInfo == null) {
			return ReturnInfo.genResultJson(0);
		}
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(4);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			return ReturnInfo.genResultJson(3);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(3);
		}
		int user_id = userInfo.getUserId();
		boolean isReadable = userService.readable(userInfo, docInfo);
		if (isReadable) {
			RelUserReadDocPK pk = new RelUserReadDocPK(user_id, doc_id);
			RelUserReadDoc read = new RelUserReadDoc();
			read.setId(pk);
			read.setReadTime(new Date());
			readService.readDoc(read);
			String url = docInfo.getDocPath();
			int index = url.lastIndexOf('\\');
			int lastIndex = url.lastIndexOf(".");
			url = url.substring(index + 1, lastIndex);
			String nginx_url = PropertiesHelper.getProperty("nginx.url");
			url = nginx_url + url + ".pdf";
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("url", url);
			return ReturnInfo.genResultJson(1, body);
		} else {
			return ReturnInfo.genResultJson(5);
		}
	}

	/***
	 * 阅读 阅读指定的文档
	 * 
	 * @throws IOException
	 * 
	 * 
	 */
	@RequestMapping(value = "/read.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String readDoc(@RequestParam("doc_id") int doc_id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TDocInfo docInfo = docService.getById(doc_id);
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		int user_id = userInfo.getUserId();
		boolean isReadable = userService.readable(userInfo, docInfo);
		if (isReadable) {
			request.setAttribute("distInfo", userInfo.getDistInfo());
			RelUserReadDocPK pk = new RelUserReadDocPK(user_id, doc_id);
			RelUserReadDoc read = new RelUserReadDoc();
			read.setId(pk);
			read.setReadTime(new Date());
			readService.readDoc(read);
			String url = docInfo.getDocPath();
			int index = url.lastIndexOf('\\');
			int lastIndex = url.lastIndexOf(".");
			url = url.substring(index + 1, lastIndex);
			String nginx_url = PropertiesHelper.getProperty("nginx.url");
			url = nginx_url + url + ".swf";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_id", docInfo.getDocId());
			map.put("doc_code", docInfo.getDocCode());
			map.put("doc_title", docInfo.getDocTitle());
			map.put("doc_type", docInfo.getDocType());
			map.put("doc_cato_name", docInfo.getCatoInfo().getCatoName());
			map.put("pub_dateline", sdf.format(docInfo.getPubInfo().getOperateTime()));
			map.put("cole_times", docInfo.getColeTimes());
			map.put("read_times", docInfo.getReadTimes());
			RelUserColDocPK cole_pk = new RelUserColDocPK(user_id, docInfo.getDocId());
			map.put("is_collected", coleService.exist(cole_pk));
			request.setAttribute("docInfo", map);
			request.setAttribute("url", url);
			return "docReader";
		} else {
			return "error/404";
		}
	}

	/***
	 * 下载 下载指定的文档
	 * 
	 * @throws SystemException
	 * 
	 * 
	 * 
	 */
	@RequestMapping(value = "/download.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String downloadDoc(@RequestParam("doc_id") int doc_id, HttpServletRequest request,
			HttpServletResponse response) throws SystemException, UnsupportedEncodingException {
		TDocInfo docInfo = docService.getById(doc_id);
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		boolean isReadable = userService.readable(userInfo, docInfo);
		if (isReadable) {
			String docName = docInfo.getDocTitle() + "." + docInfo.getDocType();
			response.setHeader("Content-disposition",
					"attachment;filename=" + new String(docName.getBytes("gb2312"), "iso8859-1"));
			String docPath = docInfo.getDocPath();
			String filePath = PropertiesHelper.getProperty("file.store.path")
					+ docPath.substring(docPath.lastIndexOf('\\') + 1);
			File file = new File(filePath);
			response.addHeader("Content-Length", "" + file.length());
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(response.getOutputStream());
				// 写文件
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
				bos.flush();
			} catch (IOException e) {
				throw new SystemException(e);
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (bos != null) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return ReturnInfo.genResultJson(1);
		} else {
			return ReturnInfo.genResultJson(0);
		}
	}

	@ExceptionHandler
	public String exp(HttpServletRequest request, Exception ex) {
		String resultViewName = "errors/error"; // 记录日志 // 根据不同错误转向不同页面
		if (ex instanceof BusinessException) {
			resultViewName = "error/error";
		} else if (ex instanceof SystemException) {
			resultViewName = "error/500";
		} else if (ex instanceof java.lang.Exception) {
			resultViewName = "error/500";
		} else { // 异常转换 ex = new Exception("系统太累了，需要休息!"); }
			request.setAttribute("ex", ex);
			String xRequestedWith = request.getHeader("X-Requested-With");
			if (xRequestedWith != null) {
				// ajax请求
				resultViewName = "error/ajax-error";
			}
		}
		return resultViewName;
	}
}
