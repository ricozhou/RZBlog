package com.rzblog.framework.web.domain;

public class RequestHeaderEntity {
	/** cookie */
	private String cookie;

	/** host */
	private String host;

	/** referer */
	private String referer;

	/** User-Agent */
	private String userAgent;

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public String toString() {
		return "RequestHeaderEntity [cookie=" + cookie + ", host=" + host + ", referer=" + referer + ", userAgent="
				+ userAgent + "]";
	}

}
