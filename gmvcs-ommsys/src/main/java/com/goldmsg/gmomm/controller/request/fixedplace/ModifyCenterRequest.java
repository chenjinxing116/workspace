package com.goldmsg.gmomm.controller.request.fixedplace;

import org.hibernate.validator.constraints.NotBlank;

/***
 * 修改办案中心信息request
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午2:38:08
 */
public class ModifyCenterRequest extends RegisterCenterRequest {

	@NotBlank
	private String baxzId;	//办案中心id

	public String getBaxzId() {
		return baxzId;
	}

	public void setBaxzId(String baxzId) {
		this.baxzId = baxzId;
	}
}
