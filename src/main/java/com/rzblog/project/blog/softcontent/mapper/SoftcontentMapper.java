package com.rzblog.project.blog.softcontent.mapper;

import com.rzblog.project.blog.softcontent.domain.Softcontent;
import java.util.List;

/**
 * 博客模块软件管理 数据层
 * 
 * @author ricozhou
 * @date 2019-02-10
 */
public interface SoftcontentMapper {

	/**
	 * 查询博客模块软件管理信息
	 * 
	 * @param cid
	 *            博客模块软件管理ID
	 * @return 博客模块软件管理信息
	 */
	public Softcontent selectSoftcontentById(Long cid);

	/**
	 * 查询博客模块软件管理列表
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 博客模块软件管理集合
	 */
	public List<Softcontent> selectSoftcontentList(Softcontent softcontent);

	/**
	 * 新增博客模块软件管理
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 结果
	 */
	public int insertSoftcontent(Softcontent softcontent);

	/**
	 * 修改博客模块软件管理
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 结果
	 */
	public int updateSoftcontent(Softcontent softcontent);

	/**
	 * 删除博客模块软件管理
	 * 
	 * @param cid
	 *            博客模块软件管理ID
	 * @return 结果
	 */
	public int deleteSoftcontentById(Long cid);

	/**
	 * 批量删除博客模块软件管理
	 * 
	 * @param cids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int batchDeleteSoftcontent(Long[] cids);

}