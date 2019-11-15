package com.rzblog.project.tool.straightlinkmanage.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.FileExtensionConstant;
import com.rzblog.common.constant.OtherConstant;
import com.rzblog.common.constant.project.ToolConstant;
import com.rzblog.common.utils.ServiceInfoUtil;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.project.common.file.utilt.FileUtils;
import com.rzblog.project.system.website.domain.Website;
import com.rzblog.project.system.website.service.IWebsiteService;
import com.rzblog.project.tool.straightlinkmanage.domain.Straightlinkmanage;
import com.rzblog.project.tool.straightlinkmanage.mapper.StraightlinkmanageMapper;

/**
 * 直链管理详情 服务层实现
 * 
 * @author ricozhou
 * @date 2018-10-16
 */
@Service
public class StraightlinkmanageServiceImpl implements IStraightlinkmanageService {
	@Autowired
	private StraightlinkmanageMapper straightlinkmanageMapper;
	@Autowired
	private IWebsiteService websiteService;

	/**
	 * 查询直链管理详情信息
	 * 
	 * @param straightlinkmanageId
	 *            直链管理详情ID
	 * @return 直链管理详情信息
	 */
	@Override
	public Straightlinkmanage selectStraightlinkmanageById(Integer straightlinkmanageId) {
		return straightlinkmanageMapper.selectStraightlinkmanageById(straightlinkmanageId);
	}

	/**
	 * 查询直链管理详情列表
	 * 
	 * @param straightlinkmanage
	 *            直链管理详情信息
	 * @return 直链管理详情集合
	 */
	@Override
	public List<Straightlinkmanage> selectStraightlinkmanageList(Straightlinkmanage straightlinkmanage) {
		return straightlinkmanageMapper.selectStraightlinkmanageList(straightlinkmanage);
	}

	/**
	 * 新增直链管理详情
	 * 
	 * @param straightlinkmanage
	 *            直链管理详情信息
	 * @return 结果
	 */
	@Override
	public int insertStraightlinkmanage(Straightlinkmanage straightlinkmanage) {
		return straightlinkmanageMapper.insertStraightlinkmanage(straightlinkmanage);
	}

	/**
	 * 修改直链管理详情
	 * 
	 * @param straightlinkmanage
	 *            直链管理详情信息
	 * @return 结果
	 */
	@Override
	public int updateStraightlinkmanage(Straightlinkmanage straightlinkmanage) {
		return straightlinkmanageMapper.updateStraightlinkmanage(straightlinkmanage);
	}

	/**
	 * 保存直链管理详情
	 * 
	 * @param straightlinkmanage
	 *            直链管理详情信息
	 * @return 结果
	 */
	@Override
	public int saveStraightlinkmanage(Straightlinkmanage straightlinkmanage) {
		Integer straightlinkmanageId = straightlinkmanage.getStraightlinkmanageId();
		int rows = 0;
		straightlinkmanage.setUpdateBy(ShiroUtils.getLoginName());
		String straightlinkFileUrl = getStraightlinkFileUrl(straightlinkmanage.getStraightlinkFileUrlKey());
		straightlinkFileUrl = getCompleteStraightlinkFileUrl(straightlinkmanage, straightlinkFileUrl);
		straightlinkmanage.setStraightlinkFileUrl(straightlinkFileUrl);
		if (StringUtils.isNotNull(straightlinkmanageId)) {
			rows = straightlinkmanageMapper.updateStraightlinkmanage(straightlinkmanage);
		} else {
			straightlinkmanage.setCreateBy(ShiroUtils.getLoginName());
			rows = straightlinkmanageMapper.insertStraightlinkmanage(straightlinkmanage);
		}

		// 最后解压原来的文件
		// 如果是压缩文件还要解压
		File file2 = new File(FilePathConfig.getStraightlinkManagePath() + File.separator
				+ straightlinkmanage.getStraightlinkFileUrlKey() + File.separator
				+ straightlinkmanage.getStraightlinkFileName() + straightlinkmanage.getStraightlinkFileExtensionName());
		if (straightlinkmanage.getStraightlinkFileExtensionName()
				.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_COMPRESSEDFILE_ZIP)) {
			// 解压前清除这个文件夹下除了file2的其他文件
			File farFile = file2.getParentFile();
			File[] fArr = farFile.listFiles();
			if (fArr != null && fArr.length > 1) {
				for (File ff : fArr) {
					if (!(ff.getName().equals(file2.getName()) && ff.isFile())) {
						// 删除
						FileUtils.deleteFile(ff);
					}
				}
			}

			// 清除完重新解压
			FileUtils.unZipFiles(file2, FilePathConfig.getStraightlinkManagePath() + File.separator
					+ straightlinkmanage.getStraightlinkFileUrlKey(), false);
			// 删除原文件
			file2.delete();
		}
		return rows;
	}

	/**
	 * @date Sep 10, 2018 1:24:20 PM
	 * @Desc
	 * @param straightlinkFileUrlKey
	 * @return
	 */
	private String getCompleteStraightlinkFileUrl(Straightlinkmanage straightlinkmanage, String straightlinkFileUrl) {
		// 压缩文件返回到文件夹，其他文件直接返回至文件
		if (FileExtensionConstant.FILE_EXTENSION_POINT_COMPRESSEDFILE_ZIP
				.equals(straightlinkmanage.getStraightlinkFileExtensionName())
				|| FileExtensionConstant.FILE_EXTENSION_POINT_COMPRESSEDFILE_7Z
						.equals(straightlinkmanage.getStraightlinkFileExtensionName())
				|| FileExtensionConstant.FILE_EXTENSION_POINT_COMPRESSEDFILE_RAR
						.equals(straightlinkmanage.getStraightlinkFileExtensionName())) {
			return straightlinkFileUrl + CommonSymbolicConstant.FORWARD_SLASH + CommonSymbolicConstant.OTHERCS_2;
		} else {
			return straightlinkFileUrl + CommonSymbolicConstant.FORWARD_SLASH
					+ straightlinkmanage.getStraightlinkFileName()
					+ straightlinkmanage.getStraightlinkFileExtensionName();
		}
	}

	/**
	 * @date Sep 10, 2018 1:24:20 PM
	 * @Desc
	 * @param straightlinkFileUrlKey
	 * @return
	 */
	private String getStraightlinkFileUrl(String straightlinkFileUrlKey) {
		String straightlinkFileUrl = null;
		List<Website> list = websiteService.selectWebsiteList(null);
		Website website;
		String domain;
		if (list != null && list.size() > 0) {
			website = list.get(0);
			domain = website.getWebsiteDomainName();
			if (domain != null || CommonSymbolicConstant.EMPTY_STRING.equals(domain)) {
				if (domain.endsWith(CommonSymbolicConstant.FORWARD_SLASH)) {
					domain = domain.substring(0, domain.length() - 1);
				}
				straightlinkFileUrl = domain + CommonSymbolicConstant.FORWARD_SLASH
						+ ToolConstant.TOOL_BASESET_STRAIGHTLINK_URL_DEFAULT_NAME + CommonSymbolicConstant.FORWARD_SLASH
						+ straightlinkFileUrlKey;
			} else {
				straightlinkFileUrl = OtherConstant.OTHER_URL_START + website.getWebsitePublicIp()
						+ CommonSymbolicConstant.COLON + website.getWebsitePublicPort()
						+ CommonSymbolicConstant.FORWARD_SLASH + ToolConstant.TOOL_BASESET_STRAIGHTLINK_URL_DEFAULT_NAME
						+ CommonSymbolicConstant.FORWARD_SLASH + straightlinkFileUrlKey;
			}
		} else {
			straightlinkFileUrl = OtherConstant.OTHER_URL_START + OtherConstant.OTHER_DEFAULT_IP
					+ CommonSymbolicConstant.COLON + ServiceInfoUtil.getPort() + CommonSymbolicConstant.FORWARD_SLASH
					+ ToolConstant.TOOL_BASESET_STRAIGHTLINK_URL_DEFAULT_NAME + CommonSymbolicConstant.FORWARD_SLASH
					+ straightlinkFileUrlKey;
		}

		return straightlinkFileUrl;
	}

	/**
	 * 删除直链管理详情信息
	 * 
	 * @param straightlinkmanageId
	 *            直链管理详情ID
	 * @return 结果
	 */
	@Override
	public int deleteStraightlinkmanageById(Integer straightlinkmanageId) {
		// 删除文件
		Straightlinkmanage straightlinkmanage = selectStraightlinkmanageById(straightlinkmanageId);
		if (!deleteStraightlinkmanageFileByFolderName(straightlinkmanage.getStraightlinkFileUrlKey())) {
			return 0;
		}
		return straightlinkmanageMapper.deleteStraightlinkmanageById(straightlinkmanageId);
	}

	/**
	 * 删除直链管理详情信息
	 * 
	 * @param straightlinkmanageId
	 *            直链管理详情ID
	 * @return 结果
	 */
	@Override
	public boolean deleteStraightlinkmanageFileByFolderName(String straightlinkFileUrlKey) {
		// 删除文件
		File file = new File(FilePathConfig.getStraightlinkManagePath() + File.separator + straightlinkFileUrlKey);
		if (!file.exists()) {
			return true;
		}
		return FileUtils.deleteFile(file.getAbsolutePath());
	}

	/**
	 * 批量删除直链管理详情对象
	 * 
	 * @param straightlinkmanageIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int batchDeleteStraightlinkmanage(Integer[] straightlinkmanageIds) {
		// return
		// straightlinkmanageMapper.batchDeleteStraightlinkmanage(straightlinkmanageIds);
		int row = 0;
		for (Integer id : straightlinkmanageIds) {
			row = deleteStraightlinkmanageById(id);
		}
		return row;
	}

}
