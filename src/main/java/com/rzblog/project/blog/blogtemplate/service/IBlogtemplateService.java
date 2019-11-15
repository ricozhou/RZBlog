package com.rzblog.project.blog.blogtemplate.service;

import com.rzblog.project.blog.blogtemplate.domain.Blogtemplate;
import java.util.List;

/**
 * 模板管理 服务层
 * 
 * @author ricozhou
 * @date 2019-02-18
 */
public interface IBlogtemplateService {

	/**
	 * 查询模板管理信息
	 * 
	 * @param blogTemplateId
	 *            模板管理ID
	 * @return 模板管理信息
	 */
	public Blogtemplate selectBlogtemplateById(Integer blogTemplateId);

	/**
	 * 查询模板管理列表
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 模板管理集合
	 */
	public List<Blogtemplate> selectBlogtemplateList(Blogtemplate blogtemplate);

	/**
	 * 新增模板管理
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 结果
	 */
	public int insertBlogtemplate(Blogtemplate blogtemplate);

	/**
	 * 修改模板管理
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 结果
	 */
	public int updateBlogtemplate(Blogtemplate blogtemplate);

	/**
	 * 保存模板管理
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 结果
	 */
	public int saveBlogtemplate(Blogtemplate blogtemplate);

	/**
	 * 删除模板管理信息
	 * 
	 * @param blogTemplateId
	 *            模板管理ID
	 * @return 结果
	 */
	public int deleteBlogtemplateById(Integer blogTemplateId);

	/**
	 * 批量删除模板管理信息
	 * 
	 * @param blogTemplateIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int batchDeleteBlogtemplate(Integer[] blogTemplateIds);

}
