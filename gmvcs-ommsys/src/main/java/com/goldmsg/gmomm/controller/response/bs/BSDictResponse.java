package com.goldmsg.gmomm.controller.response.bs;

import java.util.List;

import com.goldmsg.gmomm.controller.response.BaseIdCodeNameResponse;

/***
 * 业务岗位字典信息response
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午5:03:13
 */
public class BSDictResponse {

	private String code;	//类别编号
	private Integer id;	//类别id
	private String name;	//类别名称
	private Integer status;	//状态
	
	private List<DataType> dataList;	//数据类型列表
	private List<PostType> postList;	//岗位类型信息列表
	private List<BaseIdCodeNameResponse> labelList;	//标注类型信息列表
	
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public List<DataType> getDataList() {
		return dataList;
	}


	public void setDataList(List<DataType> dataList) {
		this.dataList = dataList;
	}


	public List<PostType> getPostList() {
		return postList;
	}


	public void setPostList(List<PostType> postList) {
		this.postList = postList;
	}


	public List<BaseIdCodeNameResponse> getLabelList() {
		return labelList;
	}


	public void setLabelList(List<BaseIdCodeNameResponse> labelList) {
		this.labelList = labelList;
	}


	/***
	 * 岗位类型实体
	 * @author QH
	 *
	 */
	public static class PostType extends BaseIdCodeNameResponse {
		private Integer status;	//岗位状态

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
	}
	
	
	/***
	 * 数据类型类型实体
	 * @author QH
	 *
	 */
	public static class DataType {
		private Integer dataId;	//数据类型id
		private String dataCode;	//数据类型编码
		private String dataName;	//数据类型名
		public Integer getDataId() {
			return dataId;
		}
		public void setDataId(Integer dataId) {
			this.dataId = dataId;
		}
		public String getDataCode() {
			return dataCode;
		}
		public void setDataCode(String dataCode) {
			this.dataCode = dataCode;
		}
		public String getDataName() {
			return dataName;
		}
		public void setDataName(String dataName) {
			this.dataName = dataName;
		}			
	}
}
