package com.rzblog.framework.web.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回体
 *
 */
public class ReturnBody {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 状态描述
	 */
	private String desc;

	/**
	 * 业务对象
	 */
	private Object data;

	/**
	 * 
	 */
	public ReturnBody() {
		asStatus();
	}

	/**
	 * 
	 * @param status
	 */
	public ReturnBody(ReturnCode returnCode) {
		asStatus(returnCode);
	}

	/**
	 * 
	 * @param data
	 */
	public ReturnBody(Object data) {
		asStatus();
		asObject(data);
	}

	/**
	 * 
	 * @param data
	 */
	public ReturnBody(Object data, ReturnCode code) {
		asObject(data, code);
	}

	/**
	 * 装载状态码
	 * 
	 * @return
	 */
	public ReturnBody asStatus() {
		return asStatus(ReturnCode.SUCCESS);
	}

	/**
	 * 装载状态码
	 * 
	 * @param status
	 * @return
	 */
	public ReturnBody asStatus(ReturnCode returnCode) {
		this.setCode(returnCode.getCode());
		this.setDesc(returnCode.getDesc());
		return this;
	}

	/**
	 * 装载Object对象
	 * 
	 * @param data
	 * @return
	 */
	public ReturnBody asObject(Object data) {
		this.data = data;
		return this;
	}

	/**
	 * 装载Map对象
	 * 
	 * @param data
	 * @return
	 */
	public ReturnBody asObject(String key, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		this.data = map;
		return this;
	}

	/**
	 * 装载Object对象和状态码
	 * 
	 * @param data
	 * @return
	 */
	public ReturnBody asObject(Object data, ReturnCode returnCode) {
		asStatus(returnCode);
		this.data = data;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReturnBody [code=" + code + ", desc=" + desc + ", data=" + data + "]";
	}

}
