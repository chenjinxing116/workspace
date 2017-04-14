package com.goldmsg.gmomm.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.goldmsg.data.dentity.BSDSJLabelType;
import com.goldmsg.data.dentity.BSDicJobType;
import com.goldmsg.data.dentity.DicBusinessSystemForm;
import com.goldmsg.data.dentity.DicBusinessTypeForm;
import com.goldmsg.data.service.DSJLabelTypeService;
import com.goldmsg.data.service.DicBusinessSystemService;
import com.goldmsg.data.service.DicBusinessTypeService;
import com.goldmsg.data.service.DicJobTypeService;
import com.goldmsg.gmomm.controller.response.BaseIdCodeNameResponse;
import com.goldmsg.gmomm.controller.response.BaseResponse;
import com.goldmsg.gmomm.controller.response.bs.AddPostTypeRequest;
import com.goldmsg.gmomm.controller.response.bs.BSDictResponse;
import com.goldmsg.gmomm.controller.response.bs.BSDictResponse.DataType;
import com.goldmsg.gmomm.controller.response.bs.BSDictResponse.PostType;
import com.goldmsg.gmomm.controller.response.bs.BSEnableRequest;
import com.goldmsg.gmomm.controller.response.bs.RemovePostTypeRequest;
import com.goldmsg.gmomm.utils.ReturnInfo;
import com.goldmsg.gmomm.utils.SysLogUtils;

import edu.emory.mathcs.backport.java.util.Arrays;


/***
 * 业务相关controller
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午4:34:16
 */
@Controller
@RequestMapping("/bs/mgr")
public class BSMgrController {
	
	@Autowired
	DicJobTypeService dicJobTypeService;
	
	@Autowired
	DicBusinessSystemService dicBusinessSystemService;
	
	@Autowired
	DicBusinessTypeService dicBusinessTypeService;
	
	@Autowired
	DSJLabelTypeService dsjLabelTypeService;
	
	/***
	 * 获取业务类别类型列表
	 * @return 业务类别列表
	 */
	@RequestMapping(value = "/type/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BaseIdCodeNameResponse>> listBSType() {
		List<DicBusinessTypeForm> bsFormlist = dicBusinessTypeService.findAllBusinessType();
		List<BaseIdCodeNameResponse> list = new ArrayList<BaseIdCodeNameResponse>();
		for(DicBusinessTypeForm bsForm : bsFormlist ){
			BaseIdCodeNameResponse baseIdCodeNameResponse = new BaseIdCodeNameResponse();
			baseIdCodeNameResponse.setId(bsForm.getId());
			baseIdCodeNameResponse.setName(bsForm.getName());
			baseIdCodeNameResponse.setCode(bsForm.getCode());
			list.add(baseIdCodeNameResponse);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, list);
	}
	
	/***
	 * 获取某个业务下的岗位列表
	 * @param id	业务id
	 * @return	岗位列表
	 */
	@RequestMapping(value = "/job/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BSDicJobType>> listJob(@Valid @NotNull @RequestParam("id") Integer id) {
		DicBusinessTypeForm dicBusinessTypeForm = dicBusinessTypeService.findById(id);
		List<BSDicJobType> JobList = dicJobTypeService.findByBsCode(dicBusinessTypeForm.getCode());
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, JobList);
	}
	
	/***
	 * 获取某个业务下的数据类型列表
	 * @param id	业务id
	 * @return	数据类型列表
	 */
	@RequestMapping(value = "/data/type/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BaseIdCodeNameResponse>> listDataType(@Valid @NotNull @RequestParam("id") Integer id) {
		DicBusinessTypeForm dicBusinessTypeForm = dicBusinessTypeService.findById(id);
		String [] dataTypes = null;
		List<BaseIdCodeNameResponse> dataList = new ArrayList<BaseIdCodeNameResponse>();
		if(!"".equals(dicBusinessTypeForm.getRelBusiness())){
			 dataTypes = dicBusinessTypeForm.getRelBusiness().split(";");
			 for(String data : dataTypes){
				 BaseIdCodeNameResponse baseIdCodeNameResponse = new BaseIdCodeNameResponse();
				 DicBusinessSystemForm dicBusinessSystemForm = dicBusinessSystemService.findBSSystemFormByCode(data);
				 baseIdCodeNameResponse.setCode(dicBusinessSystemForm.getCode());
				 baseIdCodeNameResponse.setId(dicBusinessSystemForm.getId());
				 baseIdCodeNameResponse.setName(dicBusinessSystemForm.getName());
				 dataList.add(baseIdCodeNameResponse);
			}
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, dataList);
	}
	
	/***
	 * 获取所有的数据类型列表
	 * @return	数据类型列表
	 */
	@RequestMapping(value = "/data/type/datalist.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<DicBusinessSystemForm>> listAllDataType() {
		 List<DicBusinessSystemForm> dataList = dicBusinessSystemService.findAllDataType();
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, dataList);
	}
	
	/***
	 * 获取业务岗位字典信息
	 * @return	业务岗位字典信息
	 */
	@RequestMapping(value = "/dict/list.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse<List<BSDictResponse>> listBSDict() {
		List<BSDictResponse> responseList = new ArrayList<BSDictResponse>();
		List<DicBusinessTypeForm> bsFormList = dicBusinessTypeService.findAllBusinessType();//业务类型
		for(DicBusinessTypeForm bsForm : bsFormList){
			BSDictResponse bSDictResponse = new BSDictResponse();
			List<DataType> dataList = new ArrayList<DataType>();
			List<PostType> jobList = new ArrayList<PostType>();
			List<BaseIdCodeNameResponse> labelList = new ArrayList<BaseIdCodeNameResponse>();
			bSDictResponse.setCode(bsForm.getCode());
			bSDictResponse.setName(bsForm.getName());
			bSDictResponse.setId(bsForm.getId());
			bSDictResponse.setStatus(Integer.parseInt(bsForm.getStatus()));
			
			//数据类型信息
			DicBusinessTypeForm dicBusinessTypeForm = dicBusinessTypeService.findById(bsForm.getId());
			String [] types = null;
			if(!"".equals(dicBusinessTypeForm.getRelBusiness())){
				 types = dicBusinessTypeForm.getRelBusiness().split(";");
				 for(String data : types){
					 DataType dataType = new DataType();
					 DicBusinessSystemForm dicBusinessSystemForm = dicBusinessSystemService.findBSSystemFormByCode(data);
					 if(dicBusinessSystemForm != null){
						 dataType.setDataCode(dicBusinessSystemForm.getCode());
						 dataType.setDataId(dicBusinessSystemForm.getId());
						 dataType.setDataName(dicBusinessSystemForm.getName());
						 dataList.add(dataType);
						 bSDictResponse.setDataList(dataList);
					 }
				}
			}
			
			//标注类型信息
			if(!"".equals(dicBusinessTypeForm.getRelLabel())){
				 types = dicBusinessTypeForm.getRelLabel().split(";");
				 for(String data : types){
					 BaseIdCodeNameResponse baseIdCodeNameResponse = new BaseIdCodeNameResponse();
					 BSDSJLabelType	bsDSJLabelType = dsjLabelTypeService.findByCode(data);
					 if(bsDSJLabelType != null){
					 baseIdCodeNameResponse.setCode(bsDSJLabelType.getCode());
					 baseIdCodeNameResponse.setId(bsDSJLabelType.getId());
					 baseIdCodeNameResponse.setName(bsDSJLabelType.getName());
					 labelList.add(baseIdCodeNameResponse);
					 bSDictResponse.setLabelList(labelList);
					 }
				}
			}
			
			//岗位信息
			List<BSDicJobType> list = dicJobTypeService.findByBsCode(bsForm.getCode());
			if(list != null && list.size() > 0){
				for(BSDicJobType jobType :list){
					PostType postType = new PostType();
					postType.setId(jobType.getId());
					postType.setCode(jobType.getCode());
					postType.setName(jobType.getName());
					postType.setStatus(Integer.parseInt(jobType.getStatus()));
					jobList.add(postType);
					bSDictResponse.setPostList(jobList);
				}
			}
			responseList.add(bSDictResponse);
		}
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS, responseList);
	}
	
	
	/***
	 * 添加业务类别
	 * @param typeName 业务类别名称
	 * @return	成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dict/add.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse addBSType(@Valid @NotBlank @RequestParam("typeName") String typeName) {
		if(!"".equals(typeName)){//业务名输入不为空
			DicBusinessTypeForm dicBusinessTypeForm = dicBusinessTypeService.findByName(typeName);
			if(dicBusinessTypeForm != null){//数据库中已经有该业务名
				String status = dicBusinessTypeForm.getStatus();
				if(status.equals("2") || status.equals("0")){//已删除或者停用
					if(status.equals("2")){//已删除，则重置后启用
						dicBusinessTypeForm.setRelBusiness("");
						dicBusinessTypeForm.setRelLabel("");
						dicBusinessTypeForm.setStatus("1");
						dicBusinessTypeService.addBusinessType(dicBusinessTypeForm);
					}else{
						dicBusinessTypeService.updateStatusByName("1", typeName);//启用
					}
					
				}else{
					 return ReturnInfo.genResponseEntity(ReturnInfo.OBJECT_EXISTS_ERROR);
				}	
			}else{//数据库没有该业务名-新建
				DicBusinessTypeForm dicBusinessType = new DicBusinessTypeForm();
				dicBusinessType.setName(typeName);
				String initcode= "BS0001";
				String maxCode = dicBusinessTypeService.getMaxCode();
				if(maxCode == null){
					dicBusinessType.setCode(initcode);
				}else{
					int code =Integer.parseInt(maxCode.substring(2));
					++code;
					dicBusinessType.setCode("BS" + String.format("%04d", code));
				}
				dicBusinessType.setStatus("1");
				dicBusinessType.setRelBusiness("");
				dicBusinessType.setRelLabel("");
				dicBusinessTypeService.addBusinessType(dicBusinessType);
			}
		}else{			
			return ReturnInfo.genResponseEntity(ReturnInfo.PARAMERROR);
		}
		//添加业务类型系统日志
		SysLogUtils.addMsysLog(1, 4402,"","");
	 
		 
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}
	
	
	/***
	 * 删除业务类别
	 * @param typeName 业务类别id
	 * @return	成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dict/remove.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse removeBSType(@Valid @NotBlank @RequestParam("id") Integer id) {
		dicBusinessTypeService.updateStatusByTypeid("2", id);
		//删除业务类型系统日志
		SysLogUtils.addMsysLog(1, 4403,"","");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}
	
	
	/***
	 * 启用或者停用 业务
	 * @param request 启用或者停用标识
	 * @return	成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dict/changeStatus.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse enableBSType(@Valid @RequestBody BSEnableRequest request) {
		int id= request.getId();
		boolean enable = request.getEnable();
		if(enable){			
				dicBusinessTypeService.updateStatusByTypeid("0", id);
			}else{
				dicBusinessTypeService.updateStatusByTypeid("1", id);
			}
		//启用或者停用 业务系统日志
		SysLogUtils.addMsysLog(1, 4404,"","");
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}
	
	/***
	 * 修改业务
	 * @param request 启用或者停用标识
	 * @return	成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dict/changeBsType.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse changeBSType(@Valid @RequestParam("labelCode") String labelCode,@Valid @RequestParam("dataCode")String dataCode,@Valid @RequestParam("bsCode")String bsCode) {
		DicBusinessTypeForm dicBusinessTypeForm = dicBusinessTypeService.findByBsCode(bsCode);
		if(!"".equals(dataCode)){
			Boolean isExist = false;
			String dataType = dicBusinessTypeForm.getRelBusiness();
			String data = removeDouble(dataType,dataCode,isExist,dicBusinessTypeForm);
			dicBusinessTypeForm.setRelBusiness(data);
		}else if(!"".equals(labelCode)){
			Boolean isExist = false;
			String labelType = dicBusinessTypeForm.getRelLabel();
			String label = removeDouble(labelType,labelCode,isExist,dicBusinessTypeForm);
			dicBusinessTypeForm.setRelLabel(label);
		}
		dicBusinessTypeService.addBusinessType(dicBusinessTypeForm);
		return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
	}
	
	
	private String removeDouble(String type,String code,Boolean isExist, DicBusinessTypeForm dicBusinessTypeForm){
		StringBuffer sb = new StringBuffer();
		String [] dataTypeList = type.split(";");
		List<String> list2 = Arrays.asList(dataTypeList);
		Set<String> setTemp = new HashSet<String>();
		Set<String> set = new HashSet<String>();
		set.addAll(list2);
		setTemp.addAll(list2);
		for(String str : setTemp){
			if(str.equals(code)){
				set.remove(str);
				isExist = true;
			}
		}
		if(isExist){
			for(String data: set){
				if(set.size() >0){
					data += ";";
					sb.append(data);
				}
			}
			return sb.toString();
		}else{
			StringBuffer strBuffer = new StringBuffer(type);
			strBuffer.append(code + ";");
			return strBuffer.toString();
		}
	}
	
	/***
	 * 添加岗位类别
	 * @param request 岗位类别信息
	 * @return	成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dict/post/add.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse addPostType(@Valid @RequestBody AddPostTypeRequest request) {
		String jobName = request.getPostName();
		Integer bsId = request.getBsId();
		if(!"".equals(jobName) && bsId != null){
			BSDicJobType bsDicJobType = dicJobTypeService.findJobTypeByJobName(jobName);
			if(bsDicJobType != null && "2".equals(bsDicJobType.getStatus())){//岗位名称已存在且状态为删除时，才修改
				String bsCode = dicBusinessTypeService.findById(bsId).getCode();
				dicJobTypeService.updateJobTypeStatusAndBSCodeByName("1", bsCode, jobName);
			}else{//岗位不存在，新建
				BSDicJobType bsDicJobTypeNew = new BSDicJobType();
				bsDicJobTypeNew.setName(jobName);
				String bsCode = dicBusinessTypeService.findById(bsId).getCode();
				bsDicJobTypeNew.setBsCode(bsCode);
				String initJobCode = "JOB0001";
				String maxJobCode = dicJobTypeService.getMaxJobCode();
				if(maxJobCode == null){
					bsDicJobTypeNew.setCode(initJobCode);
				}else{
					int code = Integer.parseInt(maxJobCode.substring(3));
					++code;
					bsDicJobTypeNew.setCode("JOB" + String.format("%04d", code));
				}
				bsDicJobTypeNew.setStatus("1");	
				dicJobTypeService.addJobType(bsDicJobTypeNew);
			}	
			// 添加岗位类别系统日志
			SysLogUtils.addMsysLog(1, 4408,"","");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}else{
			return ReturnInfo.genResponseEntity(ReturnInfo.PARAMERROR);
		}
	}
	
	
	/***
	 * 移除岗位类别
	 * @param request 岗位类别信息
	 * @return	成功或者失败
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dict/post/remove.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse removePostType(@Valid @RequestBody RemovePostTypeRequest request) {
		Integer jobId = request.getPostId();
		Integer bsId = request.getBsId();
		if(jobId != null && bsId!=null){//更新状态为2，bscode为空
			dicJobTypeService.updateJobTypeStatusAndBSCodeById("2", "", jobId);
			// 添加岗位类别系统日志
			SysLogUtils.addMsysLog(1, 4409,"","");
			return ReturnInfo.genResponseEntity(ReturnInfo.SUCCESS);
		}else{
			return ReturnInfo.genResponseEntity(ReturnInfo.PARAMERROR);
		}
	}
}
