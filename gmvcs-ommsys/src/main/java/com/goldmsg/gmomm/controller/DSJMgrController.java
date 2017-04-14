package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.goldmsg.data.service.UserOrgPrivilegesService;
import com.goldmsg.gmomm.controller.request.dsj.DSJListInfoRequest;
import com.goldmsg.gmomm.controller.request.dsj.DSJModifyRequest;
import com.goldmsg.gmomm.controller.request.dsj.DSJRegisterRequest;
import com.goldmsg.gmomm.controller.response.BaseIdCodeNameResponse;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.dsj.DSJListInfoResponse;
import com.goldmsg.gmomm.system.ApplicationProperties;
import com.goldmsg.gmomm.utils.FileUploadUtil;
import com.goldmsg.gmomm.utils.OrgUtils;
import com.goldmsg.gmomm.utils.PoiExcelUtil;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;
import com.goldmsg.gmvcs.common.qpid.ChannelException;
import com.goldmsg.gmvcs.common.qpid.MessagePusher;
import com.goldmsg.pttappserver.dubbo.service.GroupNonexistException;
import com.goldmsg.pttappserver.dubbo.service.ParameterException;
import com.goldmsg.pttappserver.dubbo.service.ServerErrorException;
import com.goldmsg.pttappserver.dubbo.service.User;
import com.goldmsg.pttappserver.dubbo.service.UserExistException;
import com.goldmsg.res.entity.vo.DSJ;
import com.goldmsg.res.entity.vo.DSJCommand;
import com.goldmsg.res.entity.vo.Domain;
import com.goldmsg.res.exception.ResException;
import com.goldmsg.res.params.DSJServiceParams.GetDSJParams;
import com.goldmsg.res.service.DSJService;
import com.goldmsg.res.service.DomainService;
import com.google.gson.Gson;
import com.gosun.core.utils.date.DateTimeUtils;
import com.gosun.service.entity.OrgRsp;
import com.gosun.service.entity.UserRsp;
import com.gosun.service.org.IOrgService;
import com.gosun.service.user.IUserService;

/***
 * 执法记录仪管理controller
 * 
 * @author QH
 *
 */
@Controller
@RequestMapping("/resource/mgr")
public class DSJMgrController {

	private final Logger logger = LoggerFactory.getLogger(DSJMgrController.class);

	@Autowired
	DSJService dsjService;

	@Autowired
	IOrgService orgService;

	@Autowired
	IUserService userService;

	@Autowired
	IUserService iUserService;

	@Autowired
	IOrgService iOrgService;

	@Autowired
	UserOrgPrivilegesService userOrgPrivService;

	@Autowired
	DomainService domainService;

	@Autowired
	MessagePusher messagePusher;

	@Autowired
	ApplicationProperties props;

	/***
	 * 设备开机
	 * 
	 * @param deviceId
	 *            设备id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/powerOn.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse powerOn(@Valid @RequestParam("deviceId") String deviceId) {
		// try {
		// executeCommandInternal(deviceId, null);
		// } catch (ResException e) {
		// this.logger.error(e.getMessage());
		// return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		// }

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 设备关机
	 * 
	 * @param deviceId
	 *            设备id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/powerOff.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse powerOff(@Valid @RequestParam("deviceId") String deviceId) {
		try {
			executeCommandInternal(deviceId, "restart");
		} catch (ResException e) {
			this.logger.error(e.getMessage());
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 设备锁定
	 * 
	 * @param deviceId
	 *            设备id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/deviceLock.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse deviceLock(@Valid @RequestParam("deviceId") String deviceId) {
		try {
			executeCommandInternal(deviceId, "lock");
		} catch (ResException e) {
			this.logger.error(e.getMessage());
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 设备解锁
	 * 
	 * @param deviceId
	 *            设备id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/deviceUnLock.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse deviceUnLock(@Valid @RequestParam("deviceId") String deviceId) {
		try {
			executeCommandInternal(deviceId, "unlock");
		} catch (ResException e) {
			this.logger.error(e.getMessage());
			return ReturnInfo.genResponseEntity(ReturnInfo.FAILED);
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 根据查询条件查询执法仪信息列表
	 * 
	 * @param request
	 *            查询条件
	 * @return 返回执法仪信息列表，找不到返回空的列表
	 */
	@RequestMapping(value = "/dsjmgr/list.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<DSJListInfoResponse> listDsj(HttpServletRequest servletRequest,
			@Valid @RequestBody DSJListInfoRequest request) {
		List<String> orgCodes = null;
		List<String> userCodes = null;
		List<Integer> dsjTypes = null;
		int assignStatus = 0;
		int deviceStatus = 0;
		String beginTime = null;
		String endTime = null;

		DSJListInfoResponse response = new DSJListInfoResponse();

		GetDSJParams params = new GetDSJParams();

		/*
		 * 构造部门编号列表
		 */
		orgCodes = OrgUtils.getOrgCodes(request.getOrgId());

		// 设置警号
		if (request.getUserCode() != null) {
			userCodes = new ArrayList<String>();
			userCodes.add(request.getUserCode());
		}

		// 设置分配情况
		if (request.getAllocations() != null) {
			assignStatus = request.getAllocations().intValue();

			if (assignStatus != 0) {
				params.setAssignSituation(assignStatus);
			}
		}

		// 设置设备状态
		if (request.getStatus() != null) {
			deviceStatus = request.getStatus();

			if (deviceStatus != 0) {
				params.setDeviceStatus(deviceStatus);
			}
		}

		// 设置设备注册开始时间和结束时间
		if (request.getBeginTime() != null) {
			beginTime = DateTimeUtils.convertDateToStringByFormat(request.getBeginTime());
			params.setRegBeginTime(beginTime);
		}
		if (request.getEndTime() != null) {
			endTime = DateTimeUtils.convertDateToStringByFormat(request.getEndTime());
			params.setRegEndTime(endTime);
		}

		if (request.getDsjType() != 0) {
			dsjTypes = new ArrayList<Integer>();
			dsjTypes.add(request.getDsjType());
		}

		if (request.getDomain() != null) {
			List<String> domainIds = new ArrayList<String>();
			domainIds.add(request.getDomain());

			params.setDomainIds(domainIds);
		}

		params.setOrgCodes(orgCodes);
		params.setUserCodes(userCodes);
		params.setDsjTypes(dsjTypes);
		params.setDeviceId(request.getDeviceId());
		params.setSim(request.getSimCode());
		params.setUserName(request.getUserName());

		List<DSJ> dsjs = dsjService.getDSJs(params, request.getPage().intValue(), request.getPageSize().intValue());

		Integer total = dsjService.getDSJsCount(params);
		response.setTotal(total);

		if (dsjs != null) {
			for (DSJ dsj : dsjs) {
				UserRsp u = userService.getUserInfoByUserCode(dsj.getUserCode());
				DSJListInfoResponse.DJSInfo e = new DSJListInfoResponse.DJSInfo();
				e.setDeviceId(dsj.getDeviceId());

				// 从高云平台获取部门名称
				OrgRsp org = iOrgService.getOrgInfoByOrgId(dsj.getOrgId());
				e.setOrgName(org.getName());

				e.setUserCode(dsj.getUserCode());
				e.setUserName(u.getUserName());

				List<Domain> domains = domainService.getDomains(Arrays.asList(dsj.getDomainId()));

				if (domains != null && domains.size() > 0) {
					e.setDomain(domains.get(0).getDomainName());
				} else {
					e.setDomain(dsj.getDomainId());
				}

				// 设置执法仪类型字符串
				switch (dsj.getDsjType()) {
				case 1:
					e.setDsjType("无网络");
					break;
				case 2:
					e.setDsjType("2G网络执法仪");
					break;
				case 3:
					e.setDsjType("4G执法仪");
					break;
				default:
					break;
				}
				e.setManufactures(dsj.getManufacturer());
				e.setModel(dsj.getModel());

				// 转换字节为MB
				long capacity = dsj.getCapacity() / 1024 / 1024;
				e.setCapacity(capacity);
				/*
				 * 设置设备状态信息 设备状态：1 正常 2 保修 3 报废
				 */
				switch (dsj.getDeviceStatus()) {
				case 1:
					e.setStatus("正常");
					break;
				case 2:
					e.setStatus("报修");
					break;
				case 3:
					e.setStatus("报废");
					break;
				default:
					e.setStatus("未知");
				}
				e.setRegistrationTimeDisplay(dsj.getInsertTime());
				e.setSimCode(dsj.getSim());

				response.getDsjs().add(e);
			}
		}

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 注册执法仪信息
	 * 
	 * @param request
	 *            执法仪信息
	 * @return 成功或者失败
	 * @throws ResException
	 * @throws ServerErrorException
	 * @throws GroupNonexistException
	 * @throws ParameterException
	 * @throws UserExistException
	 * @throws ChannelException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/register.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse register(@Valid @RequestBody DSJRegisterRequest request) throws ResException,
			UserExistException, ParameterException, GroupNonexistException, ServerErrorException, ChannelException {
		DSJ d = new DSJ();
		UserRsp user = iUserService.getUserInfoByUserCode(request.getUserCode());
		OrgRsp org = iOrgService.getOrgInfoByOrgId(request.getOrgId());

		DSJ test = dsjService.getDSJByDeviceId(request.getDeviceId());
		if (test != null) {
			return ReturnInfo.genResponseEntity(ReturnInfo.DSJ_ALREADY_EXISTS);
		}

		d.setAccountId(user.getAccountId());
		d.setCapacity(0L);
		d.setDescription("");
		d.setDeviceId(request.getDeviceId());
		d.setDeviceStatus(1);
		d.setDomainId("");
		d.setDsjType(3);
		d.setExtend("");
		d.setGacode("");
		d.setGatewayId("");
		d.setGbcode("");
		d.setInsertTime(DateTimeUtils.convertDateToStringByFormat(new Date()));
		d.setManufacturer(request.getManufacturers());
		d.setModel(request.getModel());
		d.setName("4G执法仪");
		d.setOrgCode(org.getOrgCode());
		d.setOrgId(org.getId());
		d.setPlevel("");
		d.setRelExtend("");
		d.setStandard("");
		d.setUserCode(request.getUserCode());
		d.setUserName(user.getUserName());
		d.setSim(request.getSimCode());

		List<DSJ> l = new ArrayList<DSJ>();
		l.add(d);

		dsjService.registDSJs(l);

		/*
		 * 将该执法仪添加进集群对讲群组中
		 */
		User u = new User();
		u.setAccountId(d.getAccountId());
		u.setGroupId(String.valueOf(d.getOrgId()));
		u.setPriority(1);
		u.setUserCode(user.getUserCode());
		u.setUserName(user.getUserName());

		messagePusher.sendMessage(props.getSubjectPttAddUser(), new Gson().toJson(u));

		// 添加注册执法仪系统日志
		SysLogUtils.addMsysLog(1, 3103, "", "");

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 批量注册执法仪信息
	 * 
	 * @param filePath
	 *            文件 地址
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/batchRegister.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse register(@Valid HttpServletRequest request) {
		boolean isExcel = FileUploadUtil.isExcel(request);
		if (isExcel) {
			String path = FileUploadUtil.upload(request);
			if (!"".equals(path)) {
				List<List<List<String>>> data = PoiExcelUtil.ExcelReader(path);
				System.out.println(data.toString());
				// todo 遍历集合并set值到注册实体
			}
			System.out.println(path);
		}
		return null;
	}

	/***
	 * 修改单个执法仪信息
	 * 
	 * @param request
	 *            修改内容
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dsjmgr/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modify(@Valid @RequestBody DSJModifyRequest request) {
		DSJ dsj = dsjService.getDSJByDeviceId(request.getDeviceId());

		UserRsp user = userService.getUserInfoByUserCode(request.getUserCode());
		OrgRsp orgRsp = orgService.getOrgInfoByOrgId(request.getOrgId());

		dsj.setOrgCode(orgRsp.getOrgCode());
		dsj.setUserCode(request.getUserCode());
		dsj.setOrgId(request.getOrgId());
		dsj.setUserName(user.getUserName());
		dsj.setAccountId(user.getAccountId());

		dsjService.updateDSJ(dsj);

		// 添加修改执法仪系统日志
		SysLogUtils.addMsysLog(1, 3101, "", "");

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 获取执法仪厂商信息列表
	 * 
	 * @return 厂商信息列表
	 */
	@RequestMapping(value = "/manufactures/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BaseIdCodeNameResponse>> listManufactures() {
		List<BaseIdCodeNameResponse> result = new ArrayList<BaseIdCodeNameResponse>();

		result.add(new BaseIdCodeNameResponse(-1, "00001", "测试厂商1"));
		result.add(new BaseIdCodeNameResponse(-1, "00002", "测试厂商2"));

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, result);
	}

	/***
	 * 获取执法仪产品型号列表
	 * 
	 * @return 执法仪产品型号信息列表
	 */
	@RequestMapping(value = "/model/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BaseIdCodeNameResponse>> listModels() {
		List<BaseIdCodeNameResponse> result = new ArrayList<BaseIdCodeNameResponse>();

		result.add(new BaseIdCodeNameResponse(-1, "00001", "测试型号1"));
		result.add(new BaseIdCodeNameResponse(-1, "00002", "测试型号2"));

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, result);
	}

	/***
	 * 执行设备远程命令
	 * 
	 * @param deviceId
	 *            设备id
	 * @param cmdType
	 *            命令类型
	 * @throws ResException
	 */
	private void executeCommandInternal(String deviceId, String cmdType) throws ResException {
		DSJCommand cmd = new DSJCommand();
		cmd.setDeviceId(deviceId);
		cmd.setTime(DateTimeUtils.convertDateToStringByFormat(new Date()));
		cmd.setCmdValue(null);
		cmd.setCmdType(cmdType);
		dsjService.sendDSJCommand(cmd);
	}
}
