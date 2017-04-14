package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goldmsg.data.dentity.CameraInfo;
import com.goldmsg.data.dentity.FixedPlaceInfo;
import com.goldmsg.data.dentity.RoomInfo;
import com.goldmsg.data.dentity.RoomType;
import com.goldmsg.data.service.FixedPlaceInfoService;
import com.goldmsg.gmomm.constants.CameraEnum;
import com.goldmsg.gmomm.controller.request.fixedplace.CameraAllocateRequest;
import com.goldmsg.gmomm.controller.request.fixedplace.ModifyCenterRequest;
import com.goldmsg.gmomm.controller.request.fixedplace.ModifyRoomRequest;
import com.goldmsg.gmomm.controller.request.fixedplace.RegisterCenterRequest;
import com.goldmsg.gmomm.controller.request.fixedplace.RegisterRoomRequest;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.fixedplace.CameraListInfoResponse;
import com.goldmsg.gmomm.controller.response.fixedplace.PlaceInfoListResponse;
import com.goldmsg.gmomm.controller.response.fixedplace.RoomTypeListResponse;
import com.goldmsg.gmomm.utils.OrgUtils;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;
import com.gosun.service.entity.OrgRsp;
import com.gosun.service.org.IOrgService;

/***
 * 固定场所关联controller
 * 
 * @author QH Email: qhs_dream@163.com 2016年9月27日 : 上午11:24:24
 */
@Controller
@RequestMapping("/resource/mgr")
public class FixedPlaceMgrController {

	@Autowired
	IOrgService iOrgService;

	@Autowired
	FixedPlaceInfoService fixedPlaceInfoService;

	/***
	 * 根据部门编号查询固定场所列表
	 * 
	 * @param orgCode
	 *            部门编号
	 * @return 固定场所信息列表
	 */
	@RequestMapping(value = "/gdcs/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<PlaceInfoListResponse>> listPlatformByOrg(@Valid @RequestParam("orgId") int orgId) {
		List<String> orgCode = OrgUtils.getOrgCodes(orgId); // 根据orgId获取以下部门编号
		List<FixedPlaceInfo> fpiList = fixedPlaceInfoService.getFixedPlaceInfoList(orgCode);
		if (fpiList == null || fpiList.isEmpty()) {
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}
		List<PlaceInfoListResponse> pilrList = new ArrayList<PlaceInfoListResponse>();
		for (FixedPlaceInfo fpi : fpiList) {
			PlaceInfoListResponse pilr = new PlaceInfoListResponse();
			pilr.setDesc(fpi.getDesc());
			pilr.setInsertTime(fpi.getInsertTime());
			pilr.setIsDelete(fpi.getIsDelete());
			pilr.setName(fpi.getName());
			pilr.setOrgCode(fpi.getOrgCode());
			pilr.setSid(fpi.getSid());
			List<RoomInfo> rooms = fixedPlaceInfoService.getRoomInfoList(fpi.getSid());
			List<PlaceInfoListResponse.RoomInfo> room = new ArrayList<>(rooms.size());
			for (RoomInfo rif : rooms) {
				List<String> roomList = new ArrayList<String>();
				String[] roomstr = rif.getType().split(","); // 将字符串转换为list输出
				for (String roomsa : roomstr) {
					roomList.add(roomsa);
				}
				room.add(new PlaceInfoListResponse.RoomInfo(rif.getDesc(), rif.getHcSid(), rif.getName(), "", roomList,
						rif.getSid(), rif.getInsertTime(), rif.getIsDelete()));
			}
			pilr.setRooms(room);
			pilrList.add(pilr);
		}
		SysLogUtils.addMsysLog(1, 4802, "", "");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, pilrList);
	}

	/***
	 * 根据房间id查询房间里的所有摄像头信息
	 * 
	 * @param roomId
	 *            房间id
	 * @return 摄像头信息列表
	 */
	@RequestMapping(value = "/room/camera/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<CameraListInfoResponse>> listCameraByRoom(
			@Valid @NotBlank @RequestParam("roomId") String roomId) {

		List<CameraInfo> cameraList = fixedPlaceInfoService.getCameraInfoList();
		List<CameraListInfoResponse> cameraLists = new ArrayList<CameraListInfoResponse>();
		CameraListInfoResponse cameraResponse = new CameraListInfoResponse();
		if (cameraList != null) {

			for (CameraInfo caf : cameraList) {
				RoomInfo roomInfo = null;
				if (roomId.equals(caf.getRoomId())) {
					roomInfo = fixedPlaceInfoService.getRoomInfoByRoomId(caf.getRoomId());
					if (roomInfo != null) {
						cameraResponse.setRoomName(roomInfo.getName());
					}
					cameraResponse.setChecked(true);
					cameraResponse.setDeviceId(caf.getSid());
					cameraResponse.setName(caf.getName());
					cameraResponse.setRoomId(caf.getRoomId());
					cameraResponse.setType(caf.getType());
					if (caf.getType() == 1) {
						cameraResponse.setTypeStr(CameraEnum.PC_CAMERA.getTypeName());
					} else {
						cameraResponse.setTypeStr(CameraEnum.SIMULATION_CAMERA.getTypeName());
					}
					cameraLists.add(cameraResponse);
				} else {

					roomInfo = fixedPlaceInfoService.getRoomInfoByRoomId(caf.getRoomId());
					if (roomInfo != null) {
						cameraResponse.setRoomName(roomInfo.getName());
					}
					cameraResponse.setChecked(false);
					cameraResponse.setDeviceId(caf.getSid());
					cameraResponse.setName(caf.getName());
					cameraResponse.setRoomId(caf.getRoomId());
					cameraResponse.setType(caf.getType());
					if (caf.getType() == 1) {
						cameraResponse.setTypeStr(CameraEnum.PC_CAMERA.getTypeName());
					} else {
						cameraResponse.setTypeStr(CameraEnum.SIMULATION_CAMERA.getTypeName());
					}
					cameraLists.add(cameraResponse);
				}

			}

		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, cameraLists);
	}

	/**
	 * 查询房间类型列表
	 * 
	 * @return 返回房间类型列表
	 */
	@RequestMapping(value = "/room/type/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<RoomTypeListResponse>> listRoomType() {

		List<RoomTypeListResponse> listRoomType = new ArrayList<RoomTypeListResponse>();
		List<RoomType> roomTypeList = fixedPlaceInfoService.getRoomTypeList();

		for (RoomType rt : roomTypeList) {
			RoomTypeListResponse rtlr = new RoomTypeListResponse();
			rtlr.setCode(rt.getCode());
			rtlr.setName(rt.getName());
			rtlr.setNtType(rt.getNtType());

			listRoomType.add(rtlr);
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, listRoomType);
	}

	/***
	 * 注册房间信息
	 * 
	 * @param request
	 *            房间信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/room/register.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse registerRoom(@Valid @RequestBody RegisterRoomRequest request) {
		RoomInfo roomInfo = new RoomInfo();
		roomInfo.setDesc(request.getDesc());
		roomInfo.setHcSid(request.getBazxId());
		roomInfo.setName(request.getName());
		List<String> roomType = request.getRoomType();
		// 将房间类型转换字符串存储
		if (roomType != null) {
			String type = "";
			for (int i = 0; i < roomType.size(); i++) {
				if (i < roomType.size() - 1) {
					type += roomType.get(i) + ",";
				} else {
					type += roomType.get(i);
				}
			}
			roomInfo.setType(type);
			roomInfo.setInsertTime(new Date());
			roomInfo.setIsDelete(0);
		}
		boolean fpis = fixedPlaceInfoService.addRoomInfo(roomInfo);
		if (fpis) {
			// 注册审讯室系统日志
			SysLogUtils.addMsysLog(1, 4802, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/***
	 * 修改房间信息
	 * 
	 * @param request
	 *            新的房间信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/room/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyRoom(@Valid @RequestBody ModifyRoomRequest request) {

		List<String> roomType = request.getRoomType();
		String type = "";
		// 将房间类型转换字符串存储
		if (roomType != null) {

			for (int i = 0; i < roomType.size(); i++) {
				if (i < roomType.size() - 1) {
					type += roomType.get(i) + ",";
				} else {
					type += roomType.get(i);
				}
			}
		}

		boolean fips = fixedPlaceInfoService.updateRoomInfo(request.getDesc(), type, request.getBazxId(),
				request.getName(), request.getRoomId());
		if (fips) {
			// 修改审讯室系统日志
			SysLogUtils.addMsysLog(1, 4806, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/***
	 * 删除房间信息
	 * 
	 * @param
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/room/del.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delRoom(@Valid @NotBlank @RequestParam("sid") String sid) {

		int fixIndeti = fixedPlaceInfoService.delRoomInfo(sid);
		if (fixIndeti != 0) {
			// 修改审讯室系统日志
			SysLogUtils.addMsysLog(1, 4803, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/***
	 * 新增办案中心
	 * 
	 * @param request
	 *            办案中心信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/bazx/register.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse registerCenter(@Valid @RequestBody RegisterCenterRequest request) {
		OrgRsp org = iOrgService.getOrgInfoByOrgId(request.getOrgId());
		FixedPlaceInfo fixedPlaceInfo = new FixedPlaceInfo();
		fixedPlaceInfo.setDesc(request.getDesc());
		fixedPlaceInfo.setName(request.getName());
		fixedPlaceInfo.setOrgCode(org.getOrgCode());
		fixedPlaceInfo.setInsertTime(new Date());
		fixedPlaceInfo.setIsDelete(0);
		boolean fixps = fixedPlaceInfoService.addFixedPlaceInfo(fixedPlaceInfo);
		if (fixps) {
			// 新增办案中心系统日志
			SysLogUtils.addMsysLog(1, 4801, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/***
	 * 修改办案中心信息
	 * 
	 * @param request
	 *            新的办案中心信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/bazx/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyCenter(@Valid @RequestBody ModifyCenterRequest request) {

		OrgRsp org = iOrgService.getOrgInfoByOrgId(request.getOrgId());

		boolean fips = fixedPlaceInfoService.updateFixedPlaceInfo(org.getOrgCode(), request.getDesc(),
				request.getName(), request.getBaxzId());
		if (fips) {
			// 修改办案中心系统日志
			SysLogUtils.addMsysLog(1, 4805, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/***
	 * 删除办案中心平台信息
	 * 
	 * @param
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/bazx/del.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delCenter(@Valid @NotBlank @RequestParam("sid") String sid) {

		int fixIndeti = fixedPlaceInfoService.delFixedPlaceInfo(sid);
		if (fixIndeti != 0) {
			// 修改删除办案中心平台系统日志
			SysLogUtils.addMsysLog(1, 4803, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

	/***
	 * @param request
	 *            摄像头和房间的映射关系
	 * @return 成功或者失败 保存摄像头分配信息
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/room/camera/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse cameraAllocate(@Valid @RequestBody CameraAllocateRequest request) {

		boolean result = fixedPlaceInfoService.allotCamera(request.getRoomId(), request.getDeviceIds());

		if (result) {
			// 保存摄像头分配日志
			SysLogUtils.addMsysLog(1, 4804, "", "");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		} else {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
	}

}
