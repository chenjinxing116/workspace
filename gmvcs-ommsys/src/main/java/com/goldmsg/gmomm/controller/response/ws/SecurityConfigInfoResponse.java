package com.goldmsg.gmomm.controller.response.ws;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/***
 * 工作站安全策略配置response
 * @author QH
 *
 */
public class SecurityConfigInfoResponse {

	@Valid
	@NotNull
	private DeviceControlPolicy deviceControlPolicy;
	@Valid
	@NotNull
	private ExportPolicy exportPolicy;
	@Valid
	@NotNull
	private NetControlPolicy netControlPolicy;
	@NotNull
	private String whiteList;	//进程管控白名单
	
	public String getWhiteList() {
		return whiteList;
	}


	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}


	public DeviceControlPolicy getDeviceControlPolicy() {
		return deviceControlPolicy;
	}


	public void setDeviceControlPolicy(DeviceControlPolicy deviceControlPolicy) {
		this.deviceControlPolicy = deviceControlPolicy;
	}


	public ExportPolicy getExportPolicy() {
		return exportPolicy;
	}


	public void setExportPolicy(ExportPolicy exportPolicy) {
		this.exportPolicy = exportPolicy;
	}


	public NetControlPolicy getNetControlPolicy() {
		return netControlPolicy;
	}


	public void setNetControlPolicy(NetControlPolicy netControlPolicy) {
		this.netControlPolicy = netControlPolicy;
	}


	/***
	 * 设备控制策略
	 * @author QH
	 *
	 */
	public static class DeviceControlPolicy {
		@NotNull
		private Boolean bluetooth = false;	//是否蓝牙设备
		@NotNull
		private Boolean cdDriver = false;	//是否光驱设备
		@NotNull
		private Boolean modem = false;	//是否modem
		@NotNull
		private Boolean usb = false;	//是否usb设备
		@NotNull
		private Boolean keyboard = false;	//是否键盘设备
		
		public Boolean getBluetooth() {
			return bluetooth;
		}
		public void setBluetooth(Boolean bluetooth) {
			this.bluetooth = bluetooth;
		}
		public Boolean getCdDriver() {
			return cdDriver;
		}
		public void setCdDriver(Boolean cdDriver) {
			this.cdDriver = cdDriver;
		}
		public Boolean getModem() {
			return modem;
		}
		public void setModem(Boolean modem) {
			this.modem = modem;
		}
		public Boolean getUsb() {
			return usb;
		}
		public void setUsb(Boolean usb) {
			this.usb = usb;
		}
		public Boolean getKeyboard() {
			return keyboard;
		}
		public void setKeyboard(Boolean keyboard) {
			this.keyboard = keyboard;
		}
	}
	
	
	/***
	 * 导出策略
	 * @author QH
	 *
	 */
	public static class ExportPolicy {
		@NotNull
		private Boolean nUSBExport = false;	//是否允许usb导出
		@NotNull
		private Boolean sUSBExport = false;	//是否允许公安usb导出
		
		public Boolean getnUSBExport() {
			return nUSBExport;
		}
		public void setnUSBExport(Boolean nUSBExport) {
			this.nUSBExport = nUSBExport;
		}
		public Boolean getsUSBExport() {
			return sUSBExport;
		}
		public void setsUSBExport(Boolean sUSBExport) {
			this.sUSBExport = sUSBExport;
		}
	}
	
	
	/***
	 * 网络控制策略
	 * @author QH
	 *
	 */
	public static class NetControlPolicy {
		@NotNull
		private Boolean enable;	//是否启用
		
		public Boolean getEnable() {
			return enable;
		}
		public void setEnable(Boolean enable) {
			this.enable = enable;
		}
	}
}
