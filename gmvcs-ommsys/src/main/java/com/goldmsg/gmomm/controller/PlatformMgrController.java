package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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

import com.goldmsg.data.dentity.CameraInfo;
import com.goldmsg.data.dentity.PlatformInfo;
import com.goldmsg.data.service.PlatformInfoService;
import com.goldmsg.gmomm.constants.CameraEnum;
import com.goldmsg.gmomm.controller.request.platform.CameraInfoModifyRequest;
import com.goldmsg.gmomm.controller.request.platform.ModifyPlatformRequest;
import com.goldmsg.gmomm.controller.request.platform.RegisterPlatformRequest;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.platform.CameraInfoListResponse;
import com.goldmsg.gmomm.controller.response.platform.PlatformInfoListResponse;
import com.goldmsg.gmomm.utils.OrgUtils;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;
import com.goldmsg.storage.protocol.StorageService;
import com.goldmsg.storage.protocol.bean.StorageException;
import com.goldmsg.storage.protocol.bean.StorageInfo;
import com.gosun.service.entity.OrgRsp;
import com.gosun.service.org.IOrgService;

/***
 * 办案区平台controller
 * 
 * @author QH
 *
 */
@Controller
@RequestMapping("/resource/mgr")
public class PlatformMgrController {

	@Autowired
	PlatformInfoService platformInfoService;

	@Autowired
	IOrgService iOrgService;

	@Autowired
	StorageService storageService;

	private final Logger logger = LoggerFactory.getLogger(PlatformMgrController.class);

	/***
	 * 获取平台信息列表
	 * 
	 * @param orgId
	 *            部门id
	 * @return 平台信息列表
	 */
	@RequestMapping(value = "/org/baqpt/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<PlatformInfoListResponse>> listPlatform(
			@Valid @NotBlank @RequestParam("orgId") int orgId) {
		List<String> orgCode = OrgUtils.getOrgCodes(orgId); // 根据orgId获取以下部门编号
		List<PlatformInfo> pfInfo = platformInfoService.getListPlatform(orgCode);
		if (pfInfo == null || pfInfo.isEmpty()) {
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}
		List<PlatformInfoListResponse> platformInfoListResponses = new ArrayList<PlatformInfoListResponse>();
		for (PlatformInfo pf : pfInfo) {

			PlatformInfoListResponse pfr = new PlatformInfoListResponse();
			pfr.setCsid(pf.getCsid());
			pfr.setFacturer(pf.getFacturer());
			// 厂商中文名待定
			String facturerChs = "dh".equals(pf.getFacturer()) ? "大华"
					: ("hk".equals(pf.getFacturer()) ? "海康" : ("kd".equals(pf.getFacturer()) ? "科达" : ""));
			pfr.setFacturerChs(facturerChs);
			pfr.setIp(pf.getIp());
			pfr.setModel(pf.getModel());
			pfr.setName(pf.getName());
			pfr.setPort(pf.getPort());
			pfr.setPwd(pf.getPwd());
			pfr.setSid(pf.getSid());
			pfr.setStatus(pf.getStatus());
			pfr.setType(pf.getType());

			// 平台类型名
			if (pf.getType() == 1) {
				pfr.setTypeStr("nvr");
			} else if (pf.getType() == 2) {
				pfr.setTypeStr("dvr");
			} else if (pf.getType() == 3) {
				pfr.setTypeStr("nvr/dvr 混合");
			}
			pfr.setUsername(pf.getUsername());
			platformInfoListResponses.add(pfr);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, platformInfoListResponses);
	}

	/***
	 * 根据平台id获取该平台下所有的摄像头信息
	 * 
	 * @param sid
	 *            平台id
	 * @return 摄像头信息列表
	 */
	@RequestMapping(value = "/baqpt/camera/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<CameraInfoListResponse>> listCamera(@Valid @NotBlank @RequestParam("sid") String sid) {
		List<CameraInfo> camera = platformInfoService.getListCamera(sid);
		if (camera == null || camera.isEmpty()) {
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}
		List<CameraInfoListResponse> CameraInfoList = new ArrayList<CameraInfoListResponse>();

		for (CameraInfo cf : camera) {
			CameraInfoListResponse clr = new CameraInfoListResponse();
			clr.setChannel(cf.getChannel());

			if (cf.getDesc() != null) {
				clr.setDesc(cf.getDesc());
			} else {
				clr.setDesc("-");
			}
			clr.setName(cf.getName());
			clr.setRoomId(cf.getRoomId());
			clr.setSid(cf.getSid());
			clr.setSynTime(cf.getSynTime());
			clr.setType(cf.getType());
			// 摄像头类型名称
			if (cf.getType() == 1) {
				clr.setTypeStr(CameraEnum.PC_CAMERA.getTypeName());
			} else if (cf.getType() == 2) {
				clr.setTypeStr(CameraEnum.SIMULATION_CAMERA.getTypeName());
			}
			clr.setVrSid(cf.getVrSid());
			CameraInfoList.add(clr);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, CameraInfoList);
	}

	/***
	 * 修改摄像头信息
	 * 
	 * @param request
	 *            修改内容
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/baqpt/camera/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modify(@Valid @RequestBody CameraInfoModifyRequest request) {

		int cameraIndetiy = platformInfoService.modifyCameraInfo(request.getChannel(), request.getDesc(),
				request.getName(), request.getType(), request.getSid());

		if (cameraIndetiy == 0) {

			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		} else {
			// 修改摄像头系统日志
			SysLogUtils.addMsysLog(1, 4905, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}

	}

	/**
	 * 删除摄像头信息
	 * 
	 * @param sid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/baqpt/camera/del.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delCameraInfo(@Valid @NotBlank @RequestParam("sid") String sid) {
		int cameraIndet = platformInfoService.delCmeraInfo(sid);
		if (cameraIndet != 0) {
			// 删除摄像头系统日志
			SysLogUtils.addMsysLog(1, 4906, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/**
	 * 存储服务器列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/baqpt/storage.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<StorageInfo>> getStorageServer() {
		List<StorageInfo> storage;
		try {
			storage = storageService.getStorageList(null, "center");
		} catch (StorageException e) {
			logger.error(e.getMessage());
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, storage);

	}

	/***
	 * 注册一个新的平台信息
	 * 
	 * @param request
	 *            平台信息内容
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pt/register.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse registerPlatform(@Valid @RequestBody RegisterPlatformRequest request) {

		OrgRsp org = iOrgService.getOrgInfoByOrgId(request.getOrgId());

		PlatformInfo platformInfo = new PlatformInfo();
		platformInfo.setCsid(request.getCsid());
		platformInfo.setFacturer(request.getFacturer());
		platformInfo.setIp(request.getIp());
		platformInfo.setModel(request.getModel());
		platformInfo.setName(request.getName());
		platformInfo.setPort(request.getPort());
		platformInfo.setPwd(request.getPwd());
		platformInfo.setType(request.getType());
		platformInfo.setUsername(request.getUsername());
		platformInfo.setOrgCode(org.getOrgCode());
		platformInfo.setStatus("00");
		platformInfo.setIsDelete(0);

		boolean pfs = platformInfoService.savePlatformInfo(platformInfo);
		if (pfs != true) {

			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		} else {
			// 注册平台系统日志
			SysLogUtils.addMsysLog(1, 4901, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}
	}

	/***
	 * 修改平台信息
	 * 
	 * @param request
	 *            平台信息内容
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pt/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyPlatform(@Valid @RequestBody ModifyPlatformRequest request) {

		int pfs = platformInfoService.modifyPlatformInfo(request.getCsid(), request.getFacturer(), request.getIp(),
				request.getModel(), request.getName(), request.getPort(), request.getPwd(), request.getType(),
				request.getUsername(), request.getSid());

		if (pfs == 0) {

			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		} else {
			// 修改平台系统日志
			SysLogUtils.addMsysLog(1, 4902, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}

	}

	/**
	 * 删除平台信息
	 * 
	 * @param sid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pt/del.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delPlatform(@Valid @NotBlank @RequestParam("sid") String sid) {
		int platIndet = platformInfoService.delPlatform(sid);
		if (platIndet != 0) {
			// 删除平台系统日志
			SysLogUtils.addMsysLog(1, 4903, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/**
	 * 同步摄像头
	 * 
	 * @param sid
	 *            平台编号
	 * @return
	 */
	@RequestMapping(value = "/camera/sync.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<?> syncCamera(@Valid @NotBlank @RequestParam("sid") String sid) {
		// TODO
		// 摄像头同步功能通过调用摄像头同步服务远程获取摄像头信息
		// 远程获取的摄像头信息写入数据库
		// 将同步到的摄像头信息返回给前端
		// 如果同步失败，给出同步出错提示信息
		return null;
	}
}
