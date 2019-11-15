package com.rzblog.project.blog.softcontent.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.project.blog.softcontent.mapper.SoftcontentMapper;
import com.rzblog.project.blog.softcontent.domain.Softcontent;
import com.rzblog.project.blog.softcontent.service.ISoftcontentService;

/**
 * 博客模块软件管理 服务层实现
 * 
 * @author ricozhou
 * @date 2019-02-10
 */
@Service
public class SoftcontentServiceImpl implements ISoftcontentService {
	@Autowired
	private SoftcontentMapper softcontentMapper;

	/**
	 * 查询博客模块软件管理信息
	 * 
	 * @param cid
	 *            博客模块软件管理ID
	 * @return 博客模块软件管理信息
	 */
	@Override
	public Softcontent selectSoftcontentById(Long cid) {
		return softcontentMapper.selectSoftcontentById(cid);
	}

	/**
	 * 查询博客模块软件管理列表
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 博客模块软件管理集合
	 */
	@Override
	public List<Softcontent> selectSoftcontentList(Softcontent softcontent) {
		return softcontentMapper.selectSoftcontentList(softcontent);
	}

	/**
	 * 新增博客模块软件管理
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 结果
	 */
	@Override
	public int insertSoftcontent(Softcontent softcontent) {
		return softcontentMapper.insertSoftcontent(softcontent);
	}

	/**
	 * 修改博客模块软件管理
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 结果
	 */
	@Override
	public int updateSoftcontent(Softcontent softcontent) {
		return softcontentMapper.updateSoftcontent(softcontent);
	}

	/**
	 * 保存博客模块软件管理
	 * 
	 * @param softcontent
	 *            博客模块软件管理信息
	 * @return 结果
	 */
	@Override
	public int saveSoftcontent(Softcontent softcontent) {
		Long cid = softcontent.getCid();
		int rows = 0;
		if (StringUtils.isNotNull(cid)) {
			rows = softcontentMapper.updateSoftcontent(softcontent);
		} else {
			rows = softcontentMapper.insertSoftcontent(softcontent);
		}
		return rows;
	}

	/**
	 * 删除博客模块软件管理信息
	 * 
	 * @param cid
	 *            博客模块软件管理ID
	 * @return 结果
	 */
	@Override
	public int deleteSoftcontentById(Long cid) {
		return softcontentMapper.deleteSoftcontentById(cid);
	}

	/**
	 * 批量删除博客模块软件管理对象
	 * 
	 * @param cids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int batchDeleteSoftcontent(Long[] cids) {
		return softcontentMapper.batchDeleteSoftcontent(cids);
	}

}
