package com.goldmsg.gmomm.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * errorcode中的配置参数映射类
 * @author QH
 *
 */
@Component
public class SystemCode {
	
	@Value("#{errorCodeReader['SUCCESS']}")
	private String SUCCESS;
	
	@Value("#{errorCodeReader['FAILED']}")
	private String FAILED;
	
	@Value("#{errorCodeReader['PARAMERROR']}")
	private String PARAMERROR;
	
	@Value("#{errorCodeReader['UNKNOWERROR']}")
	private String UNKNOWERROR;
	
	@Value("#{errorCodeReader['NOSESSION']}")
	private String NOSESSION;

	@Value("#{errorCodeReader['NOTOKEN']}")
	private String NOTOKEN;
	
	@Value("#{errorCodeReader['NOPRIVILEGE']}")
	private String NOPRIVILEGE;
	
	@Value("#{errorCodeReader['INTERNALERROR']}")
	private String INTERNALERROR;
	
	@Value("#{errorCodeReader['ERROR_ACOUNT']}")
	private String ERROR_ACOUNT;
	
	@Value("#{errorCodeReader['TASK_NOT_EXISTS']}")
	private String TASK_NOT_EXISTS;
	
	@Value("#{errorCodeReader['TASK_RESULTS_NOT_EXISTS']}")
	private String TASK_RESULTS_NOT_EXISTS;
	
	@Value("#{errorCodeReader['DSJ_NOT_EXISTS']}")
	private String DSJ_NOT_EXISTS;

	@Value("#{errorCodeReader['CONFIG_FORMAT_ERROR']}")
	private String CONFIG_FORMAT_ERROR;
	
	@Value("#{errorCodeReader['OBJECT_EXISTS_ERROR']}")
	private String OBJECT_EXISTS_ERROR;
	
	@Value("#{errorCodeReader['DSJ_ALREADY_EXISTS']}")
	private String DSJ_ALREADY_EXISTS;
	
	@Value("#{errorCodeReader['ERROR_ORG_INFO']}")
	private String ERROR_ORG_INFO;
	
	@Value("#{errorCodeReader['WS_NOT_EXISTS']}")
	private String WS_NOT_EXISTS;
	
	
	public String getDSJ_NOT_EXISTS() {
		return DSJ_NOT_EXISTS;
	}

	public void setDSJ_NOT_EXISTS(String dSJ_NOT_EXISTS) {
		DSJ_NOT_EXISTS = dSJ_NOT_EXISTS;
	}

	public String getCONFIG_FORMAT_ERROR() {
		return CONFIG_FORMAT_ERROR;
	}

	public void setCONFIG_FORMAT_ERROR(String cONFIG_FORMAT_ERROR) {
		CONFIG_FORMAT_ERROR = cONFIG_FORMAT_ERROR;
	}

	public String getOBJECT_EXISTS_ERROR() {
		return OBJECT_EXISTS_ERROR;
	}

	public void setOBJECT_EXISTS_ERROR(String oBJECT_EXISTS_ERROR) {
		OBJECT_EXISTS_ERROR = oBJECT_EXISTS_ERROR;
	}

	public String getDSJ_ALREADY_EXISTS() {
		return DSJ_ALREADY_EXISTS;
	}

	public void setDSJ_ALREADY_EXISTS(String dSJ_ALREADY_EXISTS) {
		DSJ_ALREADY_EXISTS = dSJ_ALREADY_EXISTS;
	}

	public String getERROR_ORG_INFO() {
		return ERROR_ORG_INFO;
	}

	public void setERROR_ORG_INFO(String eRROR_ORG_INFO) {
		ERROR_ORG_INFO = eRROR_ORG_INFO;
	}

	public String getWS_NOT_EXISTS() {
		return WS_NOT_EXISTS;
	}

	public void setWS_NOT_EXISTS(String wS_NOT_EXISTS) {
		WS_NOT_EXISTS = wS_NOT_EXISTS;
	}

	public String getNOSESSION() {
		return NOSESSION;
	}

	public void setNOSESSION(String nOSESSION) {
		NOSESSION = nOSESSION;
	}

	public String getNOTOKEN() {
		return NOTOKEN;
	}

	public void setNOTOKEN(String nOTOKEN) {
		NOTOKEN = nOTOKEN;
	}

	public String getNOPRIVILEGE() {
		return NOPRIVILEGE;
	}

	public void setNOPRIVILEGE(String nOPRIVILEGE) {
		NOPRIVILEGE = nOPRIVILEGE;
	}

	public String getINTERNALERROR() {
		return INTERNALERROR;
	}

	public void setINTERNALERROR(String iNTERNALERROR) {
		INTERNALERROR = iNTERNALERROR;
	}

	public String getERROR_ACOUNT() {
		return ERROR_ACOUNT;
	}

	public void setERROR_ACOUNT(String eRROR_ACOUNT) {
		ERROR_ACOUNT = eRROR_ACOUNT;
	}

	public String getTASK_NOT_EXISTS() {
		return TASK_NOT_EXISTS;
	}

	public void setTASK_NOT_EXISTS(String tASK_NOT_EXISTS) {
		TASK_NOT_EXISTS = tASK_NOT_EXISTS;
	}

	public String getTASK_RESULTS_NOT_EXISTS() {
		return TASK_RESULTS_NOT_EXISTS;
	}

	public void setTASK_RESULTS_NOT_EXISTS(String tASK_RESULTS_NOT_EXISTS) {
		TASK_RESULTS_NOT_EXISTS = tASK_RESULTS_NOT_EXISTS;
	}

	public String getUNKNOWERROR() {
		return UNKNOWERROR;
	}

	public void setUNKNOWERROR(String uNKNOWERROR) {
		UNKNOWERROR = uNKNOWERROR;
	}

	public String getSUCCESS() {
		return SUCCESS;
	}

	public void setSUCCESS(String sUCCESS) {
		SUCCESS = sUCCESS;
	}

	public String getFAILED() {
		return FAILED;
	}

	public void setFAILED(String fAILED) {
		FAILED = fAILED;
	}

	public String getPARAMERROR() {
		return PARAMERROR;
	}

	public void setPARAMERROR(String pARAMERROR) {
		PARAMERROR = pARAMERROR;
	}
}
