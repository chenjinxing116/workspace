package com.goldmsg.gmomm.controller.response;

/***
 * id和那么对应的response
 * @author QH
 *
 */
public class BaseIdNameResponse {

	private Integer id;	//id
	private String name;	//名称
	
	public BaseIdNameResponse() {}
	
	public BaseIdNameResponse(Integer id, String name) {
		this.id = id;
		this.name = name;
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
}
