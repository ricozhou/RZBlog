package com.rzblog.project.commontool.toolmanage.service;

import com.rzblog.project.commontool.toolmanage.domain.Toolset;

/**
 * 通用工具管理工具设置 服务层
 * 
 * @author ricozhou
 * @date 2018-08-27
 */
public interface IToolsetService {

	/**
	 * 查询通用工具管理工具设置信息
	 * 
	 * @param id
	 *            通用工具管理工具设置ID
	 * @return 通用工具管理工具设置信息
	 */
	public Toolset selectToolsetById(Integer id);

	/**
	 * 保存通用工具管理工具设置
	 * 
	 * @param toolmanageToolset
	 *            通用工具管理工具设置信息
	 * @return 结果
	 */
	public int ocrSaveToolset(Toolset toolset);

}
