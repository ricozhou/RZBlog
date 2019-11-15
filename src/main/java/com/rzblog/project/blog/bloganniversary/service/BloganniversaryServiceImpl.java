package com.rzblog.project.blog.bloganniversary.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.utils.DateUtils;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.project.blog.bloganniversary.mapper.BloganniversaryMapper;
import com.rzblog.project.blog.bloganniversary.domain.Bloganniversary;
import com.rzblog.project.blog.bloganniversary.service.IBloganniversaryService;

/**
 * 纪念日管理 服务层实现
 * 
 * @author ricozhou
 * @date 2018-12-04
 */
@Service
public class BloganniversaryServiceImpl implements IBloganniversaryService {
	@Autowired
	private BloganniversaryMapper bloganniversaryMapper;

	/**
	 * 查询纪念日管理信息
	 * 
	 * @param blogAnniversaryId
	 *            纪念日管理ID
	 * @return 纪念日管理信息
	 */
	@Override
	public Bloganniversary selectBloganniversaryById(Integer blogAnniversaryId) {
		return bloganniversaryMapper.selectBloganniversaryById(blogAnniversaryId);
	}

	/**
	 * 查询纪念日管理列表
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 纪念日管理集合
	 */
	@Override
	public List<Bloganniversary> selectBloganniversaryList(Bloganniversary bloganniversary) {
		return bloganniversaryMapper.selectBloganniversaryList(bloganniversary);
	}

	/**
	 * 查询纪念日管理列表
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 纪念日管理集合
	 */
	@Override
	public List<Bloganniversary> selectBloganniversaryListByDate(Date date) {
		int[] dateTime = DateUtils.getTimeComponent(date);
		String anniversaryDate = dateTime[1] + CommonSymbolicConstant.MIDDLELINE + dateTime[2];
		return bloganniversaryMapper.selectBloganniversaryListByDate(anniversaryDate);
	}

	/**
	 * 新增纪念日管理
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 结果
	 */
	@Override
	public int insertBloganniversary(Bloganniversary bloganniversary) {
		return bloganniversaryMapper.insertBloganniversary(bloganniversary);
	}

	/**
	 * 修改纪念日管理
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 结果
	 */
	@Override
	public int updateBloganniversary(Bloganniversary bloganniversary) {
		return bloganniversaryMapper.updateBloganniversary(bloganniversary);
	}

	/**
	 * 保存纪念日管理
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 结果
	 */
	@Override
	public int saveBloganniversary(Bloganniversary bloganniversary) {
		Integer blogAnniversaryId = bloganniversary.getBlogAnniversaryId();
		int rows = 0;
		bloganniversary.setUpdateBy(ShiroUtils.getLoginName());
		if (StringUtils.isNotNull(blogAnniversaryId)) {
			rows = bloganniversaryMapper.updateBloganniversary(bloganniversary);
		} else {
			bloganniversary.setCreateBy(ShiroUtils.getLoginName());
			rows = bloganniversaryMapper.insertBloganniversary(bloganniversary);
		}
		return rows;
	}

	/**
	 * 删除纪念日管理信息
	 * 
	 * @param blogAnniversaryId
	 *            纪念日管理ID
	 * @return 结果
	 */
	@Override
	public int deleteBloganniversaryById(Integer blogAnniversaryId) {
		return bloganniversaryMapper.deleteBloganniversaryById(blogAnniversaryId);
	}

	/**
	 * 批量删除纪念日管理对象
	 * 
	 * @param blogAnniversaryIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int batchDeleteBloganniversary(Integer[] blogAnniversaryIds) {
		return bloganniversaryMapper.batchDeleteBloganniversary(blogAnniversaryIds);
	}

}
