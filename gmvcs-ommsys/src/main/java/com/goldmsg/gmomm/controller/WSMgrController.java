package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

import com.goldmsg.gmomm.controller.WSMgrController.WSSecurityConfigEntity.DeviceRule;
import com.goldmsg.gmomm.controller.WSMgrController.WSSecurityConfigEntity.ExportRule;
import com.goldmsg.gmomm.controller.WSMgrController.WSSecurityConfigEntity.NetRule;
import com.goldmsg.gmomm.controller.WSMgrController.WSSecurityConfigEntity.SysRule;
import com.goldmsg.gmomm.controller.WSMgrController.WSSecurityConfigEntity.SysRule.Process;
import com.goldmsg.gmomm.controller.request.ws.PolicyConfigModifyRequest;
import com.goldmsg.gmomm.controller.request.ws.SecurityConfigModifyRequest;
import com.goldmsg.gmomm.controller.request.ws.WSAddRequest;
import com.goldmsg.gmomm.controller.request.ws.WSListInfoRequest;
import com.goldmsg.gmomm.controller.request.ws.WSModifyRequest;
import com.goldmsg.gmomm.controller.response.BaseIdCodeNameResponse;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.ws.PolicyConfigInfoResponse;
import com.goldmsg.gmomm.controller.response.ws.SecurityConfigInfoResponse;
import com.goldmsg.gmomm.controller.response.ws.SecurityConfigInfoResponse.DeviceControlPolicy;
import com.goldmsg.gmomm.controller.response.ws.SecurityConfigInfoResponse.ExportPolicy;
import com.goldmsg.gmomm.controller.response.ws.SecurityConfigInfoResponse.NetControlPolicy;
import com.goldmsg.gmomm.controller.response.ws.WSListInfoResponse;
import com.goldmsg.gmomm.utils.OrgUtils;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;
import com.goldmsg.res.entity.vo.DeviceDictionary;
import com.goldmsg.res.entity.vo.Workstation;
import com.goldmsg.res.entity.vo.WorkstationStatus;
import com.goldmsg.res.exception.ResException;
import com.goldmsg.res.params.WorkstationServiceParams.RegistWSParams;
import com.goldmsg.res.params.WorkstationServiceParams.UpdateWorkstationParams;
import com.goldmsg.res.service.WorkstationService;
import com.goldmsg.storage.protocol.StorageService;
import com.goldmsg.storage.protocol.bean.StorageException;
import com.goldmsg.storage.protocol.bean.StorageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gosun.service.entity.OrgRsp;
import com.gosun.service.org.IOrgService;

/***
 * 工作站相关接口controller
 * 
 * @author QH
 *
 */
@Controller
@RequestMapping("/resource/mgr")
public class WSMgrController {

	private final Logger logger = LoggerFactory.getLogger(WSMgrController.class);

	@Autowired
	WorkstationService wsService;

	@Autowired
	IOrgService iOrgService;

	
	@Autowired
	StorageService storageService;
	
	/***
	 * 添加工作站
	 * 
	 * @param request
	 *            工作站信息
	 * @return
	 * @throws ResException
	 */
	@RequestMapping(value = "/workstation/add.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<?> addWS(@Valid @RequestBody WSAddRequest request) throws ResException {
		RegistWSParams params = new RegistWSParams();
		OrgRsp org = iOrgService.getOrgInfoByOrgId(request.getOrgId());
		params.setOrgId(org.getId());
		params.setOrgCode(org.getOrgCode());
		params.setOrgName(org.getName());
		params.setManufacturer(request.getManufacturer());
		params.setName(request.getWsName());
		params.setIp(request.getIp());
		if (request.getStorageId() != null) {
			params.setParentStorageId(request.getStorageId());
		} else {
			params.setParentStorageId("");
		}
		params.setAdmin(request.getAdmin());
		params.setPhone(request.getPhone());
		params.setAddress(request.getAddr());
		String wsId = wsService.registWS(params);
		if (wsId == null || wsId.length() == 0) {
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 列出工作站厂商信息列表
	 * 
	 * @return 厂商code和name列表
	 */
	@RequestMapping(value = "/workstation/listManufacturer.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BaseIdCodeNameResponse>> listManufacturer() {
		List<DeviceDictionary<String, String>> dic = wsService.getManufacturer();

		List<BaseIdCodeNameResponse> results = new ArrayList<BaseIdCodeNameResponse>();

		for (DeviceDictionary<String, String> d : dic) {
			BaseIdCodeNameResponse r = new BaseIdCodeNameResponse();
			r.setCode(d.getKey());
			r.setName(d.getValue());

			results.add(r);
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, results);
	}

	/***
	 * 根据查询条件查询采集工作站信息列表
	 * 
	 * @param request
	 *            查询条件
	 * @return 返回工作站信息列表，找不到返回空的列表
	 * @throws ResException
	 */
	@RequestMapping(value = "/workstation/list.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<WSListInfoResponse> listWS(@Valid @RequestBody WSListInfoRequest request) throws ResException {
		List<Integer> ids = OrgUtils.getOrgIds(request.getOrgId());
		List<Workstation> list = wsService.getWSList(null, ids, request.getPage(), request.getPageSize());
		if (list == null || list.isEmpty()) {
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}
		Integer count = wsService.getWSListCount(null, ids);
		WSListInfoResponse response = new WSListInfoResponse();
		response.setTotal(count);
		List<WSListInfoResponse.WSInfo> results = response.getWss();
		Map<String, WSListInfoResponse.WSInfo> map = new HashMap<String, WSListInfoResponse.WSInfo>();
		for (Workstation w : list) {
			WSListInfoResponse.WSInfo r = new WSListInfoResponse.WSInfo();
			// 将对应response保存起来，方便后面做列表查询，对应wsId一一做修改
			map.put(w.getWsId(), r);
			r.setManufacturer(w.getManufacturer());
			r.setWsId(w.getWsId());
			r.setWsName(w.getName());
			r.setOrgName(w.getOrgname());
			r.setIpAddr(w.getIp());
			r.setPhoneNumber(w.getPhone());
			r.setAdmin(w.getAdmin());
			r.setPhoneNumber(w.getPhone());
			r.setAddr(w.getAddress());
			r.setIpAddr(w.getIp());

			if (w.getParentStorageId() != null && !w.getParentStorageId().isEmpty()) {
				try {
					StorageInfo storageInfo = storageService.getStorage(w.getParentStorageId());

					if (storageInfo != null && storageInfo.getName() != null) {
						r.setSuperStorageStr(storageInfo.getName());
					}

				} catch (StorageException e) {
					e.printStackTrace();
				}
			}
			r.setSuperStorage(w.getParentStorageId());
			results.add(r);
		}
		/*
		 * 构造工作状态、总容量等信息
		 */
		List<String> wsIds = new ArrayList<String>();
		wsIds.addAll(map.keySet());
		List<WorkstationStatus> status = wsService.getWSStatusList(wsIds);
		for (WorkstationStatus s : status) {
			WSListInfoResponse.WSInfo r = (WSListInfoResponse.WSInfo) map.get(s.getWsId());
			r.setWorkStatus(s.getStatus() + "");
			r.setTotalCapacity(s.getTotalCapacity() / 1024 / 1024);
			r.setUsedCapacity(s.getUsedCapacity() / 1024 / 1024);
			r.setCollectionTimeDisplay(s.getArriveGatewayTime());
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 修改采集工作站基本信息
	 * 
	 * @param request
	 *            采集工作站基本信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/workstation/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modify(@Valid @RequestBody WSModifyRequest request) {
		UpdateWorkstationParams params = new UpdateWorkstationParams();

		OrgRsp org = iOrgService.getOrgInfoByOrgId(request.getOrgId());

		if (org == null) {
			return ReturnInfo.genResponseEntity(ReturnInfo.ERROR_ORG_INFO);
		}

		params.setWsId(request.getWsId());
		params.setAddress(request.getWsAddr());
		params.setPhone(request.getPhoneNumber());
		params.setAdmin(request.getAdmin());
		params.setName(request.getWsName());
		params.setOrgId(request.getOrgId());
		params.setOrgCode(org.getOrgCode());
		params.setOrgName(org.getName());
		params.setManufacturer(request.getManufacturer());
		params.setIp(request.getIp());
		if (request.getStorageId() != null) {
			params.setParentStorageId(request.getStorageId());
		}

		wsService.updateWorkstation(params);

		// 添加修改采集工作站系统日志
		SysLogUtils.addMsysLog(1, 3201, "", "");

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 获取策略配置详细信息
	 * 
	 * @return 策略配置详细
	 * @throws ResException
	 */
	@RequestMapping(value = "/ws/policy/configuration/info.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<PolicyConfigInfoResponse> policyConfigInfo(@Valid @NotBlank @RequestParam("wsId") String wsId)
			throws ResException {
		Workstation ws = wsService.getWSInfo(wsId);

		if (ws == null) {
			return ReturnInfo.genResponseEntity(ReturnInfo.WS_NOT_EXISTS);
		}

		PolicyConfigInfoResponse response = new PolicyConfigInfoResponse();
		response.setDays(ws.getDefaultExpireDays());

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 修改工作站策略配置信息
	 * 
	 * @param request
	 *            配置信息
	 * @return 成功或者失败
	 * @throws ResException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ws/policy/configuration/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse policyConfigModify(@Valid @RequestBody PolicyConfigModifyRequest request) throws ResException {
		wsService.updateConfOrDefaultExpiredays("", request.getDays(), request.getWsId());

		// 添加修改采集工作站策略配置系统日志
		SysLogUtils.addMsysLog(1, 3203, "", "");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 获取安全配置信息，安全配置信息使用json字符串表示，其格式如下 { "device_rule": { "BLUETOOTH": "0",
	 * //蓝牙 "CDROM": "0", //光驱设备 "Devicemon": "0", //未知 "MODEM": "0", //调制解调器
	 * "UDISK": "0", //usb存储设备 "USB_KEYBOARD": "0" //usb键盘 }, "net_rule": {
	 * "netsmon": "0" //网络控制策略，是否启动 }, "sys_rule": { //进程管理策略白名单 "process": [ {
	 * "desc": "", "name": "2" } ], "sysmon": "0" //是否启用白名单 }, "export_rule": {
	 * //导出策略 "export_to_common": "0", //允许导出到普通U盘 "export_to_ga": "0",
	 * //允许导出到公安U盘 "exportmon": "0" //是否允许导出 } }
	 * 
	 * @return 安全配置信息
	 * @throws ResException
	 */
	@RequestMapping(value = "/ws/security/configuration/info.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<SecurityConfigInfoResponse> securityConfigInfo(
			@Valid @NotNull @RequestParam("wsId") String wsId) throws ResException {
		Workstation ws = wsService.getWSInfo(wsId);

		// 获取安全配置json字符串
		String json = ws.getSecurityRules();

		WSSecurityConfigEntity entity = null;
		if (json != null && !json.trim().equals("")) {
			try {
				entity = new Gson().fromJson(json, WSSecurityConfigEntity.class);
			} catch (JsonSyntaxException e) {
				// 配置文件json格式错误
				this.logger.error(e.getMessage());
				return ReturnInfo.genResponseEntity(ReturnInfo.CONFIG_FORMAT_ERROR);
			}
		}

		SecurityConfigInfoResponse response = new SecurityConfigInfoResponse();

		/*
		 * 设置设备控制策略
		 */
		DeviceControlPolicy devicePolicy = new DeviceControlPolicy();

		if (entity != null) {
			DeviceRule deviceRule = entity.getDevice_rule();

			// 蓝牙
			if (deviceRule.getBLUETOOTH().equals("1")) {
				devicePolicy.setBluetooth(true);
			}
			// 光驱设备
			if (deviceRule.getCDROM().equals("1")) {
				devicePolicy.setCdDriver(true);
			}
			// 调制解调器
			if (deviceRule.getMODEM().equals("1")) {
				devicePolicy.setModem(true);
			}
			// usb存储设备
			if (deviceRule.getUDISK().equals("1")) {
				devicePolicy.setUsb(true);
			}
			// usb键盘
			if (deviceRule.getUSB_KEYBOARD().equals("1")) {
				devicePolicy.setKeyboard(true);
			}
			response.setDeviceControlPolicy(devicePolicy);

			/*
			 * 设置导出策略
			 */
			ExportPolicy exportPolicy = new ExportPolicy();
			ExportRule exportRule = entity.getExport_rule();

			// 允许普通U盘导出
			if (exportRule.getExport_to_common().equals("1")) {
				exportPolicy.setnUSBExport(true);
			}
			// 允许公安U盘导出
			if (exportRule.getExport_to_ga().equals("1")) {
				exportPolicy.setsUSBExport(true);
			}
			response.setExportPolicy(exportPolicy);

			/*
			 * 设置网络控制策略
			 */
			NetControlPolicy netPolicy = new NetControlPolicy();
			NetRule netRule = entity.getNet_rule();

			// 是否启用网络控制策略
			if (netRule.getNetsmon().equals("1")) {
				netPolicy.setEnable(true);
			}
			response.setNetControlPolicy(netPolicy);

			/*
			 * 设置进程管理白名单
			 */
			SysRule sysRule = entity.getSys_rule();
			List<Process> process = sysRule.getProcess();
			StringBuffer whiteList = new StringBuffer();

			for (Process p : process) {
				if (whiteList.length() > 0) {
					whiteList.append(",");
				}

				whiteList.append(p.getName());
			}
			response.setWhiteList(whiteList.toString());
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 修改安全策略配置信息
	 * 
	 * @param request
	 *            安全配置信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ws/security/configuration/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse securityConfigModify(@Valid @RequestBody SecurityConfigModifyRequest request) {
		WSSecurityConfigEntity entity = new WSSecurityConfigEntity();
		/*
		 * 修改设备控制策略
		 */
		DeviceRule deviceRule = new WSSecurityConfigEntity.DeviceRule();
		DeviceControlPolicy deviceControlPolicy = request.getDeviceControlPolicy();
		if (deviceControlPolicy.getBluetooth()) {
			deviceRule.setBLUETOOTH("1");
		} else {
			deviceRule.setBLUETOOTH("0");
		}
		if (deviceControlPolicy.getCdDriver()) {
			deviceRule.setCDROM("1");
		} else {
			deviceRule.setCDROM("0");
		}
		if (deviceControlPolicy.getKeyboard()) {
			deviceRule.setUSB_KEYBOARD("1");
		} else {
			deviceRule.setUSB_KEYBOARD("0");
		}
		if (deviceControlPolicy.getModem()) {
			deviceRule.setMODEM("1");
		} else {
			deviceRule.setMODEM("0");
		}
		if (deviceControlPolicy.getUsb()) {
			deviceRule.setUDISK("1");
		} else {
			deviceRule.setUDISK("0");
		}
		entity.setDevice_rule(deviceRule);

		/*
		 * 修改导出策略
		 */
		ExportPolicy exportPolicy = request.getExportPolicy();
		ExportRule exportRule = new WSSecurityConfigEntity.ExportRule();
		if (exportPolicy.getnUSBExport()) {
			exportRule.setExport_to_common("1");
		} else {
			exportRule.setExport_to_common("0");
		}
		if (exportPolicy.getsUSBExport()) {
			exportRule.setExport_to_ga("1");
		} else {
			exportRule.setExport_to_ga("0");
		}
		entity.setExport_rule(exportRule);

		/*
		 * 修改网络控制策略
		 */
		NetControlPolicy netControlPolicy = request.getNetControlPolicy();
		NetRule netRule = new WSSecurityConfigEntity.NetRule();
		if (netControlPolicy.getEnable()) {
			netRule.setNetsmon("1");
		} else {
			netRule.setNetsmon("0");
		}
		entity.setNet_rule(netRule);

		/*
		 * 修改进程管理策略
		 */
		String whiteList = request.getWhiteList();
		SysRule sysRule = new WSSecurityConfigEntity.SysRule();
		List<Process> process = new ArrayList<Process>();
		sysRule.setProcess(process);
		if (whiteList.length() > 0) {
			String[] names = whiteList.split(",");
			for (String s : names) {
				Process p = new SysRule.Process();
				p.setDesc("");
				p.setName(s);
				process.add(p);
			}
		}
		entity.setSys_rule(sysRule);

		// 转化实体为json字符串
		String json = new Gson().toJson(entity);

		// 更新安全配置信息
		wsService.updateWSSecurityRules(json, request.getWsId());

		// 添加修改采集工作站安全配置系统日志
		SysLogUtils.addMsysLog(1, 3204, "", "");

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 工作站策略配置json字符串对应的实体
	 * 
	 * @author QH
	 *
	 */
	public static class WSPolicyConfigEntity {
		private Integer days; // 默认存储时间

		public Integer getDays() {
			return days;
		}

		public void setDays(Integer days) {
			this.days = days;
		}
	}

	/***
	 * 工作站安全配置json字符串对应的实体
	 * 
	 * @author QH
	 *
	 */
	public static class WSSecurityConfigEntity {

		private DeviceRule device_rule;
		private NetRule net_rule;
		private SysRule sys_rule;
		private ExportRule export_rule;

		public DeviceRule getDevice_rule() {
			return device_rule;
		}

		public void setDevice_rule(DeviceRule device_rule) {
			this.device_rule = device_rule;
		}

		public NetRule getNet_rule() {
			return net_rule;
		}

		public void setNet_rule(NetRule net_rule) {
			this.net_rule = net_rule;
		}

		public SysRule getSys_rule() {
			return sys_rule;
		}

		public void setSys_rule(SysRule sys_rule) {
			this.sys_rule = sys_rule;
		}

		public ExportRule getExport_rule() {
			return export_rule;
		}

		public void setExport_rule(ExportRule export_rule) {
			this.export_rule = export_rule;
		}

		/***
		 * 导出策略控制
		 * 
		 * @author QH
		 *
		 */
		public static class ExportRule {
			private String export_to_common; // 允许导出到普通usb存储
			private String export_to_ga; // 允许导出到公安usb存储
			private String exportmon; // 是否开启导出控制策略

			public String getExport_to_common() {
				return export_to_common;
			}

			public void setExport_to_common(String export_to_common) {
				this.export_to_common = export_to_common;
			}

			public String getExport_to_ga() {
				return export_to_ga;
			}

			public void setExport_to_ga(String export_to_ga) {
				this.export_to_ga = export_to_ga;
			}

			public String getExportmon() {
				return exportmon;
			}

			public void setExportmon(String exportmon) {
				this.exportmon = exportmon;
			}
		}

		/***
		 * 进程白名单控制策略
		 * 
		 * @author QH
		 *
		 */
		public static class SysRule {

			private List<Process> process; // 进程列表

			public List<Process> getProcess() {
				return process;
			}

			public void setProcess(List<Process> process) {
				this.process = process;
			}

			/***
			 * 进程描述
			 * 
			 * @author QH
			 *
			 */
			public static class Process {
				private String desc; // 描述
				private String name; // 进程名称

				public String getDesc() {
					return desc;
				}

				public void setDesc(String desc) {
					this.desc = desc;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}
			}
		}

		/***
		 * 设备控制策略
		 * 
		 * @author QH
		 *
		 */
		public static class DeviceRule {
			private String BLUETOOTH; // 蓝牙
			private String CDROM; // 光驱设备
			private String Devicemon; // 未知
			private String MODEM; // 调制解调器
			private String UDISK; // usb存储设备
			private String USB_KEYBOARD; // usb键盘

			public String getBLUETOOTH() {
				return BLUETOOTH;
			}

			public void setBLUETOOTH(String bLUETOOTH) {
				BLUETOOTH = bLUETOOTH;
			}

			public String getCDROM() {
				return CDROM;
			}

			public void setCDROM(String cDROM) {
				CDROM = cDROM;
			}

			public String getDevicemon() {
				return Devicemon;
			}

			public void setDevicemon(String devicemon) {
				Devicemon = devicemon;
			}

			public String getMODEM() {
				return MODEM;
			}

			public void setMODEM(String mODEM) {
				MODEM = mODEM;
			}

			public String getUDISK() {
				return UDISK;
			}

			public void setUDISK(String uDISK) {
				UDISK = uDISK;
			}

			public String getUSB_KEYBOARD() {
				return USB_KEYBOARD;
			}

			public void setUSB_KEYBOARD(String uSB_KEYBOARD) {
				USB_KEYBOARD = uSB_KEYBOARD;
			}
		}

		/***
		 * 网络控制策略
		 * 
		 * @author QH
		 *
		 */
		public static class NetRule {
			private String netsmon; // 是否启用

			public String getNetsmon() {
				return netsmon;
			}

			public void setNetsmon(String netsmon) {
				this.netsmon = netsmon;
			}
		}
	}
}
