package com.goldmsg.gmomm.controller.request.ws;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.goldmsg.gmomm.controller.response.ws.SecurityConfigInfoResponse;

/***
 * 修改安全配置request
 * @author QH
 *
 */
public class SecurityConfigModifyRequest extends SecurityConfigInfoResponse {

	@NotBlank
	private String wsId;	//工作站id
	
	public String getWsId() {
		return wsId;
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	@NotNull
	@Override
	public DeviceControlPolicy getDeviceControlPolicy() {
		// TODO Auto-generated method stub
		return super.getDeviceControlPolicy();
	}

	@NotNull
	@Override
	public ExportPolicy getExportPolicy() {
		// TODO Auto-generated method stub
		return super.getExportPolicy();
	}

	@NotNull
	@Override
	public NetControlPolicy getNetControlPolicy() {
		// TODO Auto-generated method stub
		return super.getNetControlPolicy();
	}
}
