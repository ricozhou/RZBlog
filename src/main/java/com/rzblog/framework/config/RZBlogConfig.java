package com.rzblog.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author ricozhou
 */
@Component
@ConfigurationProperties(prefix = "rzblog")
public class RZBlogConfig {
	/** 项目名称 */
	private String name;
	/** 版本 */
	private String version;
	/** 版权年份 */
	private String copyrightYear;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCopyrightYear() {
		return copyrightYear;
	}

	public void setCopyrightYear(String copyrightYear) {
		this.copyrightYear = copyrightYear;
	}

}
