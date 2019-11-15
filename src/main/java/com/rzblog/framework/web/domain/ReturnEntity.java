package com.rzblog.framework.web.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * 接口返回封装
 *
 */
public class ReturnEntity extends ResponseEntity<ReturnBody> {

	public ReturnEntity(ReturnBody body) {
		this(body, asStatus(body));
	}

	public ReturnEntity(HttpStatus status) {
		super(status);
	}

	public ReturnEntity(ReturnBody body, HttpStatus status) {
		super(body, status);
	}

	public ReturnEntity(MultiValueMap<String, String> headers, HttpStatus status) {
		super(null, headers, status);
	}

	public ReturnEntity(ReturnBody body, MultiValueMap<String, String> headers, HttpStatus status) {
		super(body, headers, status);
	}

	private static HttpStatus asStatus(ReturnBody body) {
		return HttpStatus.OK;
	}

}
