package com.rzblog.framework.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.rzblog.common.constant.FileOtherConstant;

/**
 * 读取文件路径相关配置
 * 
 * @author rico
 */
@Component
@ConfigurationProperties(prefix = "filepath")
public class FilePathConfig {
	/** 基础数据路径 */
	private static String basePath;
	/** 上传头像所在地 */
	private static String uploadProfilePath;
	/** 上传文件所在地 */
	private static String uploadPath;
	/** 上传缓存文件所在地 */
	private static String uploadCachePath;

	/** 上传博客文件所在地 */
	private static String uploadBlogPath;
	// 通用工具所在目录
	private static String uploadToolPath;
	/** 下载管理文件所在路径 */
	private static String downloadManagePath;
	/** 直链管理文件所在路径 */
	private static String straightlinkManagePath;

	public static String getUploadProfilePath() {
		return uploadProfilePath;
	}

	public static void setUploadProfilePath(String uploadProfilePath) {
		FilePathConfig.uploadProfilePath = uploadProfilePath;
	}

	public static String getStraightlinkManagePath() {
		return straightlinkManagePath;
	}

	public static void setStraightlinkManagePath(String straightlinkManagePath) {
		FilePathConfig.straightlinkManagePath = straightlinkManagePath;
	}

	public static String getUploadBlogPath() {
		return uploadBlogPath;
	}

	public static void setUploadBlogPath(String uploadBlogPath) {
		FilePathConfig.uploadBlogPath = uploadBlogPath;
	}

	public static String getDownloadManagePath() {
		return downloadManagePath;
	}

	public static void setDownloadManagePath(String downloadManagePath) {
		FilePathConfig.downloadManagePath = downloadManagePath;
	}

	public static String getBasePath() {
		return basePath;
	}

	// 特别注意，变量是静态的但是赋值方法不能是静态的否则无法取值，springboot1.x不碍事，2.x出问题
	public static void setBasePath(String basePath) {
		FilePathConfig.basePath = basePath;
	}

	public static String getUploadCachePath() {
		return uploadCachePath;
	}

	public static void setUploadCachePath(String uploadCachePath) {
		FilePathConfig.uploadCachePath = uploadCachePath;
	}

	public static String getUploadToolPath() {
		return uploadToolPath;
	}

	public static void setUploadToolPath(String uploadToolPath) {
		FilePathConfig.uploadToolPath = uploadToolPath;
	}

	public static String getUploadPath() {
		return uploadPath;
	}

	public static void setUploadPath(String uploadPath) {
		FilePathConfig.uploadPath = uploadPath;
	}

	// 3.0重新初始化路径,减少配置
	@PostConstruct
	public void init() {
		FilePathConfig.setUploadProfilePath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_UPLOAD_PROFILE_FILE_NAME);
		FilePathConfig.setUploadPath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_UPLOAD_COMMON_FILE_NAME);
		FilePathConfig.setUploadCachePath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_UPLOAD_CACHE_FILE_NAME);
		FilePathConfig.setUploadBlogPath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_UPLOAD_BLOG_FILE_NAME);
		FilePathConfig.setUploadToolPath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_UPLOAD_TOOL_FILE_NAME);
		FilePathConfig.setDownloadManagePath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_DOWNLOAD_MANAGE_FILE_NAME);
		FilePathConfig.setStraightlinkManagePath(
				FilePathConfig.basePath + File.separator + FileOtherConstant.FILE_PATH_STRAIGHTLINK_MANAGE_FILE_NAME);
	}

}
