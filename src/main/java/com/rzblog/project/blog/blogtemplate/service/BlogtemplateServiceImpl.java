package com.rzblog.project.blog.blogtemplate.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.project.blog.blogtemplate.mapper.BlogtemplateMapper;
import com.rzblog.project.blog.blogtemplate.domain.Blogtemplate;
import com.rzblog.project.blog.blogtemplate.service.IBlogtemplateService;

/**
 * 模板管理 服务层实现
 * 
 * @author ricozhou
 * @date 2019-02-18
 */
@Service
public class BlogtemplateServiceImpl implements IBlogtemplateService {
	@Autowired
	private BlogtemplateMapper blogtemplateMapper;

	/**
	 * 查询模板管理信息
	 * 
	 * @param blogTemplateId
	 *            模板管理ID
	 * @return 模板管理信息
	 */
	@Override
	public Blogtemplate selectBlogtemplateById(Integer blogTemplateId) {
		return blogtemplateMapper.selectBlogtemplateById(blogTemplateId);
	}

	/**
	 * 查询模板管理列表
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 模板管理集合
	 */
	@Override
	public List<Blogtemplate> selectBlogtemplateList(Blogtemplate blogtemplate) {
		return blogtemplateMapper.selectBlogtemplateList(blogtemplate);
	}

	/**
	 * 新增模板管理
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 结果
	 */
	@Override
	public int insertBlogtemplate(Blogtemplate blogtemplate) {
		return blogtemplateMapper.insertBlogtemplate(blogtemplate);
	}

	/**
	 * 修改模板管理
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 结果
	 */
	@Override
	public int updateBlogtemplate(Blogtemplate blogtemplate) {
		return blogtemplateMapper.updateBlogtemplate(blogtemplate);
	}

	/**
	 * 保存模板管理
	 * 
	 * @param blogtemplate
	 *            模板管理信息
	 * @return 结果
	 */
	@Override
	public int saveBlogtemplate(Blogtemplate blogtemplate) {
		Integer blogTemplateId = blogtemplate.getBlogTemplateId();
		int rows = 0;
		blogtemplate.setUpdateBy(ShiroUtils.getLoginName());
		if (StringUtils.isNotNull(blogTemplateId)) {
			rows = blogtemplateMapper.updateBlogtemplate(blogtemplate);
		} else {
			blogtemplate.setCreateBy(ShiroUtils.getLoginName());
			rows = blogtemplateMapper.insertBlogtemplate(blogtemplate);
		}
		return rows;
	}

	/**
	 * 删除模板管理信息
	 * 
	 * @param blogTemplateId
	 *            模板管理ID
	 * @return 结果
	 */
	@Override
	public int deleteBlogtemplateById(Integer blogTemplateId) {
		return blogtemplateMapper.deleteBlogtemplateById(blogTemplateId);
	}

	/**
	 * 批量删除模板管理对象
	 * 
	 * @param blogTemplateIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int batchDeleteBlogtemplate(Integer[] blogTemplateIds) {
		return blogtemplateMapper.batchDeleteBlogtemplate(blogTemplateIds);
	}

}
