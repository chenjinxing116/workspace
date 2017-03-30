package com.goldmsg.gmdoc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goldmsg.core.utils.JsonUtil;
import com.goldmsg.core.utils.ReturnInfo;
import com.goldmsg.gmdoc.entity.TCatogory;
import com.goldmsg.gmdoc.entity.TDistrictDict;
import com.goldmsg.gmdoc.entity.TDocInfo;
import com.goldmsg.gmdoc.entity.TUserInfo;
import com.goldmsg.gmdoc.service.CatoService;
import com.goldmsg.gmdoc.service.DocService;
import com.goldmsg.gmdoc.service.GMUserService;
import com.goldmsg.gmdoc.service.RemoteCallService;

/**
 * 文档搜索controller，提供搜索建议，文档搜索等功能
 * 
 * @author xiangrandy E-mail:351615708@qq.com
 * @version 创建时间：2016年4月28日 下午5:49:45
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

	@Autowired
	GMUserService userService;

	@Autowired
	CatoService catoService;

	@Autowired
	RemoteCallService remoteCallService;

	@Autowired
	DocService docService;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	/**
	 * 获取文档搜索界面
	 * 
	 * @param dist_id
	 *            搜索的地区id
	 * @param doc_type
	 *            搜索的文档类型
	 * @param cato_id
	 *            搜索的分类id
	 * @param keyword
	 *            搜索的关键字
	 * @return 如果用户session存在，返回搜索结果页面；否则，返回404界面
	 */
	@RequestMapping(value = "/s.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSearchPage(@RequestParam(value = "dist_id", defaultValue = "9999") int dist_id,
			@RequestParam(value = "doc_type", defaultValue = "all") String doc_type,
			@RequestParam(value = "cato_id", defaultValue = "9999") int cato_id,
			@RequestParam(value = "keyword", defaultValue = "") String keyword) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return "error/404";
		} else {
			TDistrictDict distInfo = userInfo.getDistInfo();
			request.setAttribute("distInfo", distInfo);
			List<Map<String, Object>> catoInfoList = catoService.getCatoInfoList(distInfo.getDistId());
			request.setAttribute("catoList", catoInfoList);
			if (dist_id != 9999) {
				request.setAttribute("dist_id", dist_id);
			} else {
				request.setAttribute("dist_id", distInfo.getDistId());
			}
			if (doc_type != null) {
				request.setAttribute("doc_type", doc_type);
			}
			if (cato_id != 9999) {
				TCatogory catoInfo = catoService.findOne(cato_id);
				if (catoInfo != null) {
					request.setAttribute("catoInfo", catoInfo);
				}
				for (Map<String, Object> catoMap : catoInfoList) {
					if (catoInfo.getParentId() == (int) catoMap.get("cato_id")) {
						request.setAttribute("pCatoName", catoMap.get("cato_name"));
						break;
					} else {
						continue;
					}
				}
			}
			if (keyword != null) {
				request.setAttribute("keyword", keyword);
			}
			return "Search";
		}
	}

	/**
	 * app搜索 根据区域主键dist_id、文档所属分类doc_cato_id、关键词或文件名keyword搜索文档
	 * 
	 * @param token
	 *            app端需要传入的token验证信息
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @param requestBody
	 *            请求体中包括地区主键dist_id、文档所属分类doc_cato_id、关键词或文件名keyword这三个参数
	 * @return 以JSON格式返回查询到的文档信息
	 */
	@RequestMapping(value = "result.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appSearch(@RequestParam(value = "token") String token,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize, @RequestBody String requestBody) {
		if (token == null || token.equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		TUserInfo userInfo;
		try {
			userInfo = userService.getUserInfoByToken(token);
		} catch (Exception e) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		if (userInfo == null) {
			return ReturnInfo.genResultJson(ReturnInfo.NOTOKEN);
		}
		int user_id = userInfo.getUserId();
		int sec_level = userService.getSecLevelByUserId(user_id);
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		int cato_id = 0;
		Integer[] cato_ids = null;
		if (map.containsKey("cato_id")) {
			cato_id = (int) map.get("cato_id");
			if (cato_id != 0) {
				cato_ids = new Integer[] { cato_id };
			}
		}
		int dist_id = 0;
		Integer[] dist_ids = null;
		if (map.containsKey("dist_id")) {
			dist_id = (int) map.get("dist_id");
			if (dist_id != 0) {
				dist_ids = new Integer[] { dist_id };
			}
		}
		String keyword;
		String[] keywords = null;
		List<Map<String, Object>> ret = null;
		if (map.containsKey("keyword") && !map.get("keyword").toString().equals("")) {
			keyword = map.get("keyword").toString();
			keywords = keyword.split(" ");
			ret = remoteCallService.search(user_id, keywords, cato_ids, null, sec_level, page * pageSize, pageSize);
		} else {
			Page<TDocInfo> docs = docService.searchDoc(dist_ids, cato_ids, null, sec_level, page, pageSize);
			ret = remoteCallService.searchByDocCodes(docs, user_id);
		}
		if (ret.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, ret);
		}
	}

	/**
	 * web搜索 根据区域主键dist_id、文档所属分类doc_cato_id、文档类型doc_type、关键词或文件名keyword搜索文档
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页显示的条数
	 * @param requestBody
	 *            请求体中包括地区主键dist_id、文档所属分类doc_cato_id、关键词或文件名keyword这三个参数
	 * @return 以JSON格式返回查询到的文档信息
	 */
	@RequestMapping(value = "result.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String search(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize, @RequestBody String requestBody) {
		TUserInfo userInfo = (TUserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			return ReturnInfo.genResultJson(2);
		}
		int user_id = userInfo.getUserId();
		int sec_level = userInfo.getSecLevel();
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		int cato_id = 0;
		Integer[] cato_ids = null;
		if (map.containsKey("cato_id")) {
			cato_id = (int) map.get("cato_id");
			if (cato_id != 0) {
				cato_ids = new Integer[] { cato_id };
			}
		}
		int dist_id = 0;
		Integer[] dist_ids = null;
		if (map.containsKey("dist_id")) {
			dist_id = (int) map.get("dist_id");
			if (dist_id != 0) {
				dist_ids = new Integer[] { dist_id };
			}
		}
		String doc_type = null;
		String[] doc_types = null;
		if (map.containsKey("doc_type")) {
			if (map.get("doc_type").toString().equals("doc")) {
				doc_type = "doc";
				doc_types = new String[] { "doc", "docx" };
			} else if (map.get("doc_type").toString().equals("xls")) {
				doc_type = "xls";
				doc_types = new String[] { "xls", "xlsx" };
			} else if (map.get("doc_type").toString().equals("ppt")) {
				doc_type = "ppt";
				doc_types = new String[] { "ppt", "pptx" };
			} else {
				doc_type = map.get("doc_type").toString();
				doc_types = new String[] { doc_type };
			}
		}
		String keyword;
		String tem;
		String[] keywords = null;
		List<Map<String, Object>> ret = null;
		if (map.containsKey("keyword") && !map.get("keyword").toString().equals("")) {
			keyword = map.get("keyword").toString();
			tem = keyword.replaceAll("\\s{1,}", " ");
			System.out.println(tem);
			keywords = tem.split(" ");
			ret = remoteCallService.search(user_id, keywords, cato_ids, doc_types, sec_level, page * pageSize,
					pageSize);
		} else {
			Page<TDocInfo> docs = docService.searchDoc(dist_ids, cato_ids, doc_types, sec_level, page, pageSize);
			ret = remoteCallService.searchByDocCodes(docs, user_id);
		}
		if (ret.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, ret);
		}
	}

	/**
	 * app端-搜索-搜索建议 根据输入的关键词内容给出搜索建议
	 * 
	 * @param token
	 *            app端用户验证标识
	 * @param requestBody
	 *            请求体中包含keyword关键字字段
	 * @return 以JSON格式返回搜索建议数据
	 */
	@RequestMapping(value = "/suggest.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String appSuggest(@RequestParam(value = "token") String token, @RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("keyword") || map.get("keyword").toString().trim().equals("")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		String keyword = map.get("keyword").toString();
		keyword = keyword.trim();
		String preKeyword = "";
		String lastKeyword = "";
		if (keyword.contains(" ")) {
			int index = keyword.lastIndexOf(" ");
			lastKeyword = keyword.substring(index + 1);
			preKeyword = keyword.substring(0, index + 1);
		} else {
			lastKeyword = keyword;
			preKeyword = "";
		}
		Map<String, Object> retMap = remoteCallService.getSuggest(preKeyword, lastKeyword);
		if (retMap.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, retMap);
		}
	}

	/**
	 * web端-搜索-搜索建议 根据输入的关键词内容给出搜索建议
	 * 
	 * @param requestBody
	 *            请求体中包含keyword关键字字段
	 * @return 以JSON格式返回搜索建议数据
	 */
	@RequestMapping(value = "/suggest.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String suggest(@RequestBody String requestBody) {
		// 解析POST请求体json数据
		Map<String, Object> map = JsonUtil.parseMap(requestBody);
		if (!map.containsKey("keyword")) {
			return ReturnInfo.genResultJson(ReturnInfo.PARAMERROR);
		}
		String keyword = map.get("keyword").toString();
		keyword = keyword.trim();
		String preKeyword = "";
		String lastKeyword = "";
		if (keyword.contains(" ")) {
			int index = keyword.lastIndexOf(" ");
			lastKeyword = keyword.substring(index + 1);
			preKeyword = keyword.substring(0, index + 1);
		} else {
			lastKeyword = keyword;
			preKeyword = "";
		}
		Map<String, Object> retMap = remoteCallService.getSuggest(preKeyword, lastKeyword);
		if (retMap.isEmpty()) {
			return ReturnInfo.genResultJson(ReturnInfo.FAILED);
		} else {
			return ReturnInfo.genResultJson(ReturnInfo.SUCCESS, retMap);
		}
	}
}
