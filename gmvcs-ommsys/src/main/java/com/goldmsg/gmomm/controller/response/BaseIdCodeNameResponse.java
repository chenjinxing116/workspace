package com.goldmsg.gmomm.controller.response;

/***
 * id+code+name 类型response
 * @author QH
 * Email: qhs_dream@163.com
 * 2016年9月27日 : 下午4:35:47
 */
public class BaseIdCodeNameResponse extends BaseIdNameResponse {

	public BaseIdCodeNameResponse() {}
	
	public BaseIdCodeNameResponse(Integer id, String code, String name) {
		this.setId(id);
		this.setCode(code);
		this.setName(name);
	}
	
	private String code;	//代号编码

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
