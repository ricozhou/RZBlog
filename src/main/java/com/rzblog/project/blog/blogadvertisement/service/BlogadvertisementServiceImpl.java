package com.rzblog.project.blog.blogadvertisement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.project.blog.blogadvertisement.mapper.BlogadvertisementMapper;
import com.rzblog.project.blog.blogadvertisement.domain.Blogadvertisement;
import com.rzblog.project.blog.blogadvertisement.service.IBlogadvertisementService;

/**
 * 广告管理 服务层实现
 * 
 * @author ricozhou
 * @date 2019-02-12
 */
@Service
public class BlogadvertisementServiceImpl implements IBlogadvertisementService {
	@Autowired
	private BlogadvertisementMapper blogadvertisementMapper;

	/**
	 * 查询广告管理信息
	 * 
	 * @param blogAdId
	 *            广告管理ID
	 * @return 广告管理信息
	 */
	@Override
	public Blogadvertisement selectBlogadvertisementById(Integer blogAdId) {
		return blogadvertisementMapper.selectBlogadvertisementById(blogAdId);
	}

	/**
	 * 查询广告管理列表
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 广告管理集合
	 */
	@Override
	public List<Blogadvertisement> selectBlogadvertisementList(Blogadvertisement blogadvertisement) {
		return blogadvertisementMapper.selectBlogadvertisementList(blogadvertisement);
	}
	

	/**
	 * 查询广告管理列表
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 广告管理集合
	 */
	@Override
	public List<Blogadvertisement> selectBlogadvertisementListByLimit(Blogadvertisement blogadvertisement) {
		return blogadvertisementMapper.selectBlogadvertisementListByLimit(blogadvertisement);
	}

	/**
	 * 新增广告管理
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 结果
	 */
	@Override
	public int insertBlogadvertisement(Blogadvertisement blogadvertisement) {
		return blogadvertisementMapper.insertBlogadvertisement(blogadvertisement);
	}

	/**
	 * 修改广告管理
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 结果
	 */
	@Override
	public int updateBlogadvertisement(Blogadvertisement blogadvertisement) {
		return blogadvertisementMapper.updateBlogadvertisement(blogadvertisement);
	}

	/**
	 * 保存广告管理
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 结果
	 */
	@Override
	public int saveBlogadvertisement(Blogadvertisement blogadvertisement) {
		System.out.println(blogadvertisement);
		Integer blogAdId = blogadvertisement.getBlogAdId();
		int rows = 0;
		blogadvertisement.setUpdateBy(ShiroUtils.getLoginName());
		if (StringUtils.isNotNull(blogAdId)) {
			rows = blogadvertisementMapper.updateBlogadvertisement(blogadvertisement);
		} else {
			blogadvertisement.setCreateBy(ShiroUtils.getLoginName());
			rows = blogadvertisementMapper.insertBlogadvertisement(blogadvertisement);
		}
		return rows;
	}

	/**
	 * 删除广告管理信息
	 * 
	 * @param blogAdId
	 *            广告管理ID
	 * @return 结果
	 */
	@Override
	public int deleteBlogadvertisementById(Integer blogAdId) {
		return blogadvertisementMapper.deleteBlogadvertisementById(blogAdId);
	}

	/**
	 * 批量删除广告管理对象
	 * 
	 * @param blogAdIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int batchDeleteBlogadvertisement(Integer[] blogAdIds) {
		return blogadvertisementMapper.batchDeleteBlogadvertisement(blogAdIds);
	}

}
