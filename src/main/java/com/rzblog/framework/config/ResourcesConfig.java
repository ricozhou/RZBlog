package com.rzblog.framework.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.rzblog.common.constant.FileOtherConstant;

/**
 * 通用配置
 * 
 * @author ricozhou
 */
@Configuration
public class ResourcesConfig extends WebMvcConfigurerAdapter {

	/**
	 * 首页地址
	 */
	@Value("${shiro.user.indexUrl}")
	public String indexUrl;

	@Autowired
	private CrossFilter crossFilter;

	/**
	 * 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/admin").setViewName("forward:" + indexUrl);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 头像文件跳转
		registry.addResourceHandler(FileOtherConstant.FILE_JUMP_PATH_PREFIX5 + "**").addResourceLocations(
				"file:///" + new File(FilePathConfig.getUploadProfilePath()).getAbsolutePath() + File.separator);
		// 内部存储文件跳转
		registry.addResourceHandler(FileOtherConstant.FILE_JUMP_PATH_PREFIX + "**").addResourceLocations(
				"file:///" + new File(FilePathConfig.getUploadPath()).getAbsolutePath() + File.separator);
		// 缓存文件跳转
		registry.addResourceHandler(FileOtherConstant.FILE_JUMP_PATH_PREFIX2 + "**").addResourceLocations(
				"file:///" + new File(FilePathConfig.getUploadCachePath()).getAbsolutePath() + File.separator);
		// 博客文件跳转
		registry.addResourceHandler(FileOtherConstant.FILE_JUMP_PATH_PREFIX3 + "**").addResourceLocations(
				"file:///" + new File(FilePathConfig.getUploadBlogPath()).getAbsolutePath() + File.separator);
		// 直链文件跳转
		registry.addResourceHandler(FileOtherConstant.FILE_JUMP_PATH_PREFIX4 + "**").addResourceLocations(
				"file:///" + new File(FilePathConfig.getStraightlinkManagePath()).getAbsolutePath() + File.separator);

		/** swagger配置 */
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public FilterRegistrationBean filterRegist() {
		FilterRegistrationBean frBean = new FilterRegistrationBean();
		frBean.setFilter(crossFilter);
		return frBean;
	}
}