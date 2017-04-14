package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goldmsg.data.dentity.BSDicJobType;
import com.goldmsg.data.dentity.DicBusinessTypeForm;
import com.goldmsg.data.dentity.DicPoliceTypeForm;
import com.goldmsg.data.dentity.UserPriv;
import com.goldmsg.data.service.BaseRoleService;
import com.goldmsg.data.service.DicBusinessTypeService;
import com.goldmsg.data.service.DicJobTypeService;
import com.goldmsg.data.service.DicPoliceTypeService;
import com.goldmsg.data.service.UserJobService;
import com.goldmsg.data.service.UserOrgPrivilegesService;
import com.goldmsg.data.service.UserPoliceTypeService;
import com.goldmsg.gmomm.controller.request.user.ModifyManagerScopeRequest;
import com.goldmsg.gmomm.controller.request.user.ModifyUserRequest;
import com.goldmsg.gmomm.controller.response.BaseIdCodeNameResponse;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.user.UserInfoListResponse;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;
import com.gosun.service.entity.OrgRsp;
import com.gosun.service.entity.RoleRsp;
import com.gosun.service.entity.UserRsp;
import com.gosun.service.org.IOrgService;
import com.gosun.service.privilege.IPrivilegeService;
import com.gosun.service.user.IUserService;

/***
 * 用户管理相关controller
 * 
 * @author QH Email: qhs_dream@163.com 2016年9月27日 : 下午4:02:58
 */
@Controller
@RequestMapping("/user/mgr")
public class UserMgrController {

	@Autowired
	IOrgService orgService;

	@Autowired
	IUserService userService;

	@Autowired
	UserOrgPrivilegesService userOrgPrivilegesService;

	@Autowired
	UserJobService userJobService;

	@Autowired
	DicJobTypeService dicJobTypeService;

	@Autowired
	DicBusinessTypeService dicBusinessService;

	@Autowired
	BaseRoleService baseRoleService;

	@Autowired
	DicPoliceTypeService dicPoliceTypeService;

	@Autowired
	UserPoliceTypeService userPoliceTypeService;

	@Autowired
	IPrivilegeService privilegeService;

	/***
	 * 根据查询条件查询用户信息列表
	 * 
	 * @param orgCode
	 *            部门编号
	 * @param subOrg
	 *            是否包含子部门搜索
	 * @param kw
	 *            警员编号或者警员姓名的关键字
	 * @return 警员信息列表
	 */
	@RequestMapping(value = "/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<UserInfoListResponse> listUser(@Valid @NotBlank @RequestParam("orgId") int orgId, // 部门Id
			@Valid @NotNull @RequestParam("subOrg") Boolean subOrg, // 是否包含子部门
			@RequestParam("kw") String kw/* 警员名称或者编号 */
	) {
		UserInfoListResponse responseList = new UserInfoListResponse();
		List<UserRsp> userList = new ArrayList<UserRsp>();
		if (subOrg) {// 勾选子部门
			String childrenIds = "";
			OrgRsp childrenOrgs = orgService.getOrgAndChildByOrgId(orgId);
			List<OrgRsp> orgList = childrenOrgs.getChildren();
			StringBuffer sb = new StringBuffer();
			if (orgList != null && orgList.size() > 0) {
				childrenIds = getOrgIds(orgList, sb);
			}
			if (!"".equals(childrenIds)) {// 该部门下有子部门
				String[] idArray = childrenIds.substring(0, childrenIds.length() - 1).split(",");
				List<UserRsp> l = userService.getUserInfosByOrgId(orgId);
				if (l != null && l.size() > 0) {
					userList.addAll(l);
				}
				for (String id : idArray) {
					int childId = Integer.parseInt(id);
					List<UserRsp> childUserList = userService.getUserInfosByOrgId(childId);
					if (childUserList != null && childUserList.size() > 0) {
						userList.addAll(childUserList);
					}
				}
			} else {// 无子部门
				userList = userService.getUserInfosByOrgId(orgId);
			}
		} else {// 未勾选子部门
			userList = userService.getUserInfosByOrgId(orgId);
		}

		// 处理警员名称/编号
		if (userList != null && userList.size() > 0) {
			if (!"".equals(kw)) {
				for (UserRsp user : userList) {
					if (user.getUserName().indexOf(kw) != -1 || user.getUserCode().indexOf(kw) != -1) {
						UserInfoListResponse.UserInfo responseUser = new UserInfoListResponse.UserInfo();
						setParameterForUser(responseUser, user, orgId);
						responseList.getUsers().add(responseUser);
					}
				}
			} else {
				for (UserRsp user : userList) {
					UserInfoListResponse.UserInfo responseUser = new UserInfoListResponse.UserInfo();
					setParameterForUser(responseUser, user, orgId);
					responseList.getUsers().add(responseUser);
				}
			}
		}

		responseList.setTotal(responseList.getUsers().size());

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, responseList);
	}

	private String getOrgIds(List<OrgRsp> orgList, StringBuffer sb) {
		if (orgList != null && !orgList.isEmpty()) {
			for (OrgRsp org : orgList) {
				sb.append(org.getId() + ",");
				if (org.getChildren() != null && org.getChildren().size() > 0) {
					getOrgIds(org.getChildren(), sb);
				}
			}
		}
		return sb.toString();
	}

	/***
	 * 修改用户信息
	 * 
	 * @param request
	 *            新的用户信息
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyUser(@Valid @RequestBody ModifyUserRequest request) {

		// 更新岗位表和用户岗位表

		String bsCode = request.getBsCode();
		String userCode = request.getUserCode();
		Integer typeId = request.getType();// 警员类别
		Integer postId = request.getPostId();
		UserRsp user = userService.getUserInfoByUserCode(userCode);
		int accountId = user.getAccountId();
		Integer policeTypeId = userPoliceTypeService.findPoliceTypeIdByAccountId(accountId);
		Integer jobId = userJobService.getJobIdByAccountId(accountId);

		if (policeTypeId == -1) {// 判断是否存在该项，不存在则添加
			userPoliceTypeService.save(accountId, typeId);
		} else {
			userPoliceTypeService.updatePoliceTypeIdByAccountId(typeId, accountId);
		}

		if (jobId == -1) {// 判断是否存在该项，不存在则添加
			userJobService.save(accountId, postId);
		} else {
			userJobService.updateJobIdByAccountId(postId, accountId);
		}
		dicJobTypeService.updateJobTypeStatusAndBSCodeById("1", bsCode, postId);
		// 修改用户系统日志
		SysLogUtils.addMsysLog(1, 3802, "", "");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	/***
	 * 获取警员类别列表
	 * 
	 * @return 警员类别列表
	 */
	@RequestMapping(value = "/type/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BaseIdCodeNameResponse>> listJobType() {
		List<DicPoliceTypeForm> policeTypeFormList = dicPoliceTypeService.getAllPoliceType();
		List<BaseIdCodeNameResponse> responseList = new ArrayList<BaseIdCodeNameResponse>();
		for (DicPoliceTypeForm policeType : policeTypeFormList) {
			BaseIdCodeNameResponse responseObject = new BaseIdCodeNameResponse();
			responseObject.setCode(policeType.getCode());
			responseObject.setId(policeType.getId());
			responseObject.setName(policeType.getName());
			responseList.add(responseObject);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, responseList);
	}

	/***
	 * 获取用户部门管理范围信息列表
	 * 
	 * @param userCode
	 *            警号
	 * @return 管理范围信息列表
	 */
	@RequestMapping(value = "/scope/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<UserPriv>> listScope(@Valid @NotBlank @RequestParam("userCode") String userCode) {
		List<UserPriv> response = userOrgPrivilegesService.listAll(userCode);

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, response);
	}

	/***
	 * 设置用户部门管理范围
	 * 
	 * @param request
	 *            警员与部门编号列表的映射关系
	 * @return 成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scope/modify.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse modifyScope(@Valid @RequestBody ModifyManagerScopeRequest request) {
		String userCode = request.getUserCode();

		/*
		 * 新增的用户权限信息
		 */
		List<UserPriv> privs = new ArrayList<UserPriv>();
		for (Integer id : request.getAddOrgIds()) {
			UserPriv p = new UserPriv();
			p.setUserCode(userCode);
			p.setOrgId(id);

			OrgRsp rsp = orgService.getOrgInfoByOrgId(id);
			p.setOrgCode(rsp.getOrgCode());

			privs.add(p);
		}
		userOrgPrivilegesService.addPrivileges(userCode, privs);

		/*
		 * 移除的用户权限信息
		 */
		List<Integer> orgIds = new ArrayList<Integer>();
		for (Integer id : request.getDeleteOrgIds()) {
			orgIds.add(id);
		}
		userOrgPrivilegesService.removePrivileges(userCode, orgIds);
		// 设置用户管理权限系统日志
		SysLogUtils.addMsysLog(1, 3808, "", "");

		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}

	private void setParameterForUser(UserInfoListResponse.UserInfo userInfo, UserRsp user, int orgId) {

		OrgRsp org = orgService.getOrgInfoByOrgId(orgId);
		Integer postId = userJobService.getJobIdByAccountId(user.getAccountId());
		Integer policeTypeId = userPoliceTypeService.findPoliceTypeIdByAccountId(user.getAccountId());

		if (postId != -1) {
			BSDicJobType bsjobType = dicJobTypeService.findJobTypeByJobId(postId);
			if (bsjobType != null && !"".equals(bsjobType.getBsCode())) {
				DicBusinessTypeForm dicBusinessTypeForm = dicBusinessService.findByBsCode(bsjobType.getBsCode());
				userInfo.setBusinessId(dicBusinessTypeForm.getId());
				userInfo.setBusinessStr(dicBusinessTypeForm.getName());
				userInfo.setPostStr(bsjobType.getName());
			}
		}
		if (policeTypeId != -1) {
			DicPoliceTypeForm dicPoliceTypeForm = dicPoliceTypeService.findPoliceTypeById(policeTypeId);
			if (dicPoliceTypeForm != null) {
				userInfo.setTypeId(dicPoliceTypeForm.getId());
				userInfo.setTypeStr(dicPoliceTypeForm.getName());
			}
		}
		List<RoleRsp> roles = privilegeService.getRoleListByAccountId(user.getAccountId());
		if (roles != null && !roles.isEmpty()) {
			List<Integer> roleIds = new ArrayList<>(roles.size());
			List<String> roleNames = new ArrayList<>(roles.size());
			for (RoleRsp role : roles) {
				roleIds.add(role.getId());
				roleNames.add(role.getRoleName());
			}
			userInfo.setRoleIds(roleIds);
			userInfo.setRoleNames(roleNames);
		}
		userInfo.setOrgCode(org.getOrgCode());// 部门编号
		userInfo.setOrgName(user.getOrgName());// 部门名称
		userInfo.setUserCode(user.getUserCode());// 警号
		userInfo.setUserName(user.getUserName());// 警员名称
		userInfo.setPostId(postId);
	}
}
