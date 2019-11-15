package com.rzblog.project.blog.blogadvertisement.service;

import com.rzblog.project.blog.blogadvertisement.domain.Blogadvertisement;
import java.util.List;

/**
 * 广告管理 服务层
 * 
 * @author ricozhou
 * @date 2019-02-12
 */
public interface IBlogadvertisementService {

	/**
	 * 查询广告管理信息
	 * 
	 * @param blogAdId
	 *            广告管理ID
	 * @return 广告管理信息
	 */
	public Blogadvertisement selectBlogadvertisementById(Integer blogAdId);

	/**
	 * 查询广告管理列表
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 广告管理集合
	 */
	public List<Blogadvertisement> selectBlogadvertisementList(Blogadvertisement blogadvertisement);

	/**
	 * 新增广告管理
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 结果
	 */
	public int insertBlogadvertisement(Blogadvertisement blogadvertisement);

	/**
	 * 修改广告管理
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 结果
	 */
	public int updateBlogadvertisement(Blogadvertisement blogadvertisement);

	/**
	 * 保存广告管理
	 * 
	 * @param blogadvertisement
	 *            广告管理信息
	 * @return 结果
	 */
	public int saveBlogadvertisement(Blogadvertisement blogadvertisement);

	/**
	 * 删除广告管理信息
	 * 
	 * @param blogAdId
	 *            广告管理ID
	 * @return 结果
	 */
	public int deleteBlogadvertisementById(Integer blogAdId);

	/**
	 * 批量删除广告管理信息
	 * 
	 * @param blogAdIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int batchDeleteBlogadvertisement(Integer[] blogAdIds);

	public List<Blogadvertisement> selectBlogadvertisementListByLimit(Blogadvertisement blogadvertisement);

}
