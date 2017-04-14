package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goldmsg.gmomm.controller.request.storage.AssignWsRequest;
import com.goldmsg.gmomm.controller.request.storage.ModifyStoragePolicyRequest;
import com.goldmsg.gmomm.controller.request.storage.ModifyStorageRequest;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.storage.StorageInfoListResponse;
import com.goldmsg.gmomm.controller.response.storage.StorageInfoListResponse.StorageResponse;
import com.goldmsg.gmomm.controller.response.storage.StoragePolicyInfoResponse;
import com.goldmsg.gmomm.controller.response.storage.WSAssignedInfoResponse;
import com.goldmsg.gmomm.system.ApplicationProperties;
import com.goldmsg.gmomm.utils.OrgUtils;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;
import com.goldmsg.res.entity.vo.Workstation;
import com.goldmsg.res.service.WorkstationService;
import com.goldmsg.storage.protocol.StorageService;
import com.goldmsg.storage.protocol.bean.StorageException;
import com.goldmsg.storage.protocol.bean.StorageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gosun.core.utils.date.DateTimeUtils;

/***
 * 存储管理controller
 * 
 * @author QH Email: qhs_dream@163.com 2016年9月27日 : 下午3:11:41
 */
@Controller
@RequestMapping("/resource/mgr")
public class StorageMgrController {

	private final Logger logger = LoggerFactory.getLogger(StorageMgrController.class);

	@Autowired
	StorageService storageService;

	@Autowired
	ApplicationProperties props;

	@Autowired
	WorkstationService wsService;

	/***
	 * 查询存储信息列表
	 * 
	 * @return 存储信息列表
	 * @throws StorageException
	 */
	@RequestMapping(value = "/org/storage/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<StorageInfoListResponse> listStorage(@RequestParam("orgId") Integer orgId,
			@Valid @NotNull @RequestParam("assign") Integer assign) throws StorageException {

		// unassign未分配工作站参数
		String unassign = "";
		List<String> orgIdsStr = new ArrayList<String>();
		if (assign == 2) { // 未分配

			orgIdsStr.add(unassign);

		} else { // 全部和已分配

			if (assign == 0) { // 全部
				orgIdsStr.add(unassign);
			}

			if (orgId != null) {
				List<Integer> orgIds = OrgUtils.getOrgIds(orgId);
				for (Integer id : orgIds) {
					orgIdsStr.add(String.valueOf(id));
				}
			}

		}

		List<StorageInfo> storages = storageService.getStorageList(orgIdsStr, StorageService.STORAGE_TYPE_CENTER);

		StorageInfoListResponse response = new StorageInfoListResponse();
		response.setTotal(storages.size());

		List<StorageResponse> list = new ArrayList<StorageResponse>();
		response.setStorageList(list);

		for (StorageInfo s : storages) {
			StorageResponse storageInfo = new StorageResponse();

			storageInfo.setId(s.getStorageId());
			storageInfo.setName(s.getName());
			if (s.getOrgId() != null && !s.getOrgId().isEmpty()) {
				storageInfo.setOrgId(Integer.parseInt(s.getOrgId()));
			}
			storageInfo.setOrgName(s.getOrgName());
			storageInfo.setIp(s.getIp());

			/*
			 * 判断存储的心跳是否过期
			 */
			if (isStorageExpired(DateTimeUtils.convertDateStringToDate(s.getHeartBeat().getLastAliveTime()))) {
				// 过期
				storageInfo.setStatusStr("离线");
			} else {
				// 正常
				storageInfo.setStatusStr("在线");
			}

			storageInfo.setTypeDisplay(s.getType());
			storageInfo.setTotalDisk(s.getHeartBeat().getTotalCapacity());
			storageInfo.setUsedDisk(storageInfo.getTotalDisk() - s.getHeartBeat().getFreeCapacity());
			storageInfo.setLastAliveTimeDisplay(s.getHeartBeat().getLastAliveTime());
			storageInfo.setAdmin(s.getAdmin());
			storageInfo.setPhone(s.getPhone());
			storageInfo.setAddress(s.getAddress());

			list.add(storageInfo);
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 修改存储信息
	 * 
	 * @param request
	 *            存储信息
	 * @return 成功或者失败
	 * @throws StorageException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/storage/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyStorage(@Valid @RequestBody ModifyStorageRequest request) throws StorageException {
		StorageInfo s = storageService.getStorage(request.getId());
		s.setName(request.getName());
		s.setAddress(request.getAddress());
		s.setAdmin(request.getAdmin());
		s.setPhone(request.getPhone());
		s.setOrgId(request.getOrgId() + "");
		s.setOrgName(request.getOrgName());
		storageService.updateStorageInfo(s);

		// 修改存储系统日志
		SysLogUtils.addMsysLog(1, 3302, "", "");

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 获取存储策略配置
	 * 
	 * @param storageId
	 *            存储id
	 * @return 策略配置内容
	 * @throws StorageException
	 */
	@RequestMapping(value = "/storage/policy/configuration/info.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<StoragePolicyInfoResponse> storagePolicyInfo(
			@Valid @NotBlank @RequestParam("storageId") String storageId) throws StorageException {
		StorageInfo storageInfo = storageService.getStorage(storageId);
		String json = storageInfo.getConf();

		StoragePolicyEntity entity = null;

		if (json != null && !json.trim().equals("")) {
			try {
				entity = new Gson().fromJson(json, StoragePolicyEntity.class);
			} catch (JsonSyntaxException e) {
				// 配置文件json格式错误
				this.logger.error(e.getMessage());
				return ReturnInfo.genResponseEntity(ReturnInfo.CONFIG_FORMAT_ERROR);
			}
		}

		StoragePolicyInfoResponse response = new StoragePolicyInfoResponse();
		response.setStorageId(storageId);
		if (entity != null) {
			response.setMaxUploadInst(entity.getMaxUploadInst());
			response.setiNetSpeed(entity.getiNetSpeed());
			response.setStartTimeDisplay(entity.getStartTime());
			response.setEndTimeDisplay(entity.getEndTime());
		}
		response.setDays(storageInfo.getDefaultExpireDays());

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 修改存储策略配置
	 * 
	 * @param request
	 *            存储策略配置
	 * @return 成功或者失败
	 * @throws StorageException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/storage/policy/configuration/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyStoragePolicy(@Valid @RequestBody ModifyStoragePolicyRequest request)
			throws StorageException {
		StoragePolicyEntity entity = new StoragePolicyEntity();

		entity.setEndTime(DateTimeUtils.convertDateToStringByFormat(request.getEndTime()));
		entity.setiNetSpeed(request.getiNetSpeed());
		entity.setMaxUploadInst(request.getMaxUploadInst());
		entity.setStartTime(DateTimeUtils.convertDateToStringByFormat(request.getStartTime()));

		String json = new Gson().toJson(entity);

		storageService.updateStorageConf(request.getStorageId(), json, request.getDays());

		// 修改存储策略配置系统日志
		SysLogUtils.addMsysLog(1, 3305, "", "");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 根据存储id查询存储被分配的工作站信息
	 * 
	 * @param id
	 *            存储id
	 * @return
	 */
	@RequestMapping(value = "/storage/listWs.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<WSAssignedInfoResponse> listWS(@Valid @NotBlank @RequestParam("id") String id) {
		Map<String, List<Workstation>> m = wsService.getWSStorageRelation(id);
		WSAssignedInfoResponse response = new WSAssignedInfoResponse();

		/*
		 * 设置已分配存储的工作站信息列表
		 */
		List<NameValuePair> assigned = new ArrayList<NameValuePair>();
		for (Workstation s : m.get("assigned")) {
			NameValuePair nv = new BasicNameValuePair(s.getName(), s.getWsId());
			assigned.add(nv);
		}
		response.setAsigned(assigned);

		/*
		 * 设置未分配存储的工作站信息列表
		 */
		List<NameValuePair> unsigned = new ArrayList<NameValuePair>();
		for (Workstation s : m.get("unassigned")) {
			NameValuePair nv = new BasicNameValuePair(s.getName(), s.getWsId());
			unsigned.add(nv);
		}
		response.setUnsigned(unsigned);

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 给存储分配工作站
	 * 
	 * @param request
	 *            工作站与存储的对应关系
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/storage/assignWs.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse allocateWs(@Valid @RequestBody AssignWsRequest request) {
		wsService.assignWSToStorage(request.getWsid(), request.getStorageId());
		// 存储分配工作站系统日志
		SysLogUtils.addMsysLog(1, 3304, "", "");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 判断存储心跳时间是否过期
	 * 
	 * @param aliveTime
	 *            最后接收心跳的时间
	 * @return ture代表存错，false代表离线
	 */
	private boolean isStorageExpired(Date aliveTime) {
		long[] part = DateTimeUtils.diff(DateTimeUtils.convertDateToStringByFormat(aliveTime),
				DateTimeUtils.convertDateToStringByFormat(new Date()));

		if (part[3] <= props.getStorageExpiredTime()) {
			/*
			 * 存储心跳时间过期
			 */
			return false;
		}

		/*
		 * 没有过期
		 */
		return true;
	}

	/***
	 * 存储策略配置实体类
	 * 
	 * @author QH
	 *
	 */
	public static class StoragePolicyEntity {
		private Integer maxUploadInst; // 最大上传实例
		private Integer iNetSpeed; // 限速(MB)
		private String startTime; // 工作时间段开始时间
		private String endTime; // 工作时间段结束时间

		public Integer getMaxUploadInst() {
			return maxUploadInst;
		}

		public void setMaxUploadInst(Integer maxUploadInst) {
			this.maxUploadInst = maxUploadInst;
		}

		public Integer getiNetSpeed() {
			return iNetSpeed;
		}

		public void setiNetSpeed(Integer iNetSpeed) {
			this.iNetSpeed = iNetSpeed;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
	}
}
