package com.rzblog.project.blog.bloganniversary.mapper;

import com.rzblog.project.blog.bloganniversary.domain.Bloganniversary;
import java.util.List;

/**
 * 纪念日管理 数据层
 * 
 * @author ricozhou
 * @date 2018-12-04
 */
public interface BloganniversaryMapper {

	/**
	 * 查询纪念日管理信息
	 * 
	 * @param blogAnniversaryId
	 *            纪念日管理ID
	 * @return 纪念日管理信息
	 */
	public Bloganniversary selectBloganniversaryById(Integer blogAnniversaryId);

	/**
	 * 查询纪念日管理列表
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 纪念日管理集合
	 */
	public List<Bloganniversary> selectBloganniversaryList(Bloganniversary bloganniversary);

	/**
	 * 新增纪念日管理
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 结果
	 */
	public int insertBloganniversary(Bloganniversary bloganniversary);

	/**
	 * 修改纪念日管理
	 * 
	 * @param bloganniversary
	 *            纪念日管理信息
	 * @return 结果
	 */
	public int updateBloganniversary(Bloganniversary bloganniversary);

	/**
	 * 删除纪念日管理
	 * 
	 * @param blogAnniversaryId
	 *            纪念日管理ID
	 * @return 结果
	 */
	public int deleteBloganniversaryById(Integer blogAnniversaryId);

	/**
	 * 批量删除纪念日管理
	 * 
	 * @param blogAnniversaryIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int batchDeleteBloganniversary(Integer[] blogAnniversaryIds);

	/**
	 * @date Dec 4, 2018 2:31:45 PM
	 * @Desc
	 * @param anniversaryDate
	 * @return
	 */
	public List<Bloganniversary> selectBloganniversaryListByDate(String anniversaryDate);

}