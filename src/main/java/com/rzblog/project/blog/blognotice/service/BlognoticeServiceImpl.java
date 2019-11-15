package com.rzblog.project.blog.blognotice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.project.blog.blognotice.mapper.BlognoticeMapper;
import com.rzblog.project.blog.blognotice.domain.Blognotice;
import com.rzblog.project.blog.blognotice.service.IBlognoticeService;

/**
 * 博客公告 服务层实现
 * 
 * @author ricozhou
 * @date 2018-10-23
 */
@Service
public class BlognoticeServiceImpl implements IBlognoticeService {
	@Autowired
	private BlognoticeMapper blognoticeMapper;

	/**
	 * 查询博客公告信息
	 * 
	 * @param blogNoticeId
	 *            博客公告ID
	 * @return 博客公告信息
	 */
	@Override
	public Blognotice selectBlognoticeById(Integer blogNoticeId) {
		return blognoticeMapper.selectBlognoticeById(blogNoticeId);
	}

	/**
	 * 查询博客公告列表
	 * 
	 * @param blognotice
	 *            博客公告信息
	 * @return 博客公告集合
	 */
	@Override
	public List<Blognotice> selectBlognoticeList(Blognotice blognotice) {
		return blognoticeMapper.selectBlognoticeList(blognotice);
	}

	/**
	 * 查询博客公告列表
	 * 
	 * @param blognotice
	 *            博客公告信息
	 * @return 博客公告集合
	 */
	@Override
	public List<Blognotice> selectOpenBlognoticeList() {
		return blognoticeMapper.selectOpenBlognoticeList();
	}

	/**
	 * 新增博客公告
	 * 
	 * @param blognotice
	 *            博客公告信息
	 * @return 结果
	 */
	@Override
	public int insertBlognotice(Blognotice blognotice) {
		return blognoticeMapper.insertBlognotice(blognotice);
	}

	/**
	 * 修改博客公告
	 * 
	 * @param blognotice
	 *            博客公告信息
	 * @return 结果
	 */
	@Override
	public int updateBlognotice(Blognotice blognotice) {
		return blognoticeMapper.updateBlognotice(blognotice);
	}

	/**
	 * 保存博客公告
	 * 
	 * @param blognotice
	 *            博客公告信息
	 * @return 结果
	 */
	@Override
	public int saveBlognotice(Blognotice blognotice) {
		Integer blogNoticeId = blognotice.getBlogNoticeId();
		int rows = 0;
		blognotice.setUpdateBy(ShiroUtils.getLoginName());
		if (StringUtils.isNotNull(blogNoticeId)) {
			rows = blognoticeMapper.updateBlognotice(blognotice);
		} else {
			blognotice.setCreateBy(ShiroUtils.getLoginName());
			rows = blognoticeMapper.insertBlognotice(blognotice);
		}
		return rows;
	}

	/**
	 * 删除博客公告信息
	 * 
	 * @param blogNoticeId
	 *            博客公告ID
	 * @return 结果
	 */
	@Override
	public int deleteBlognoticeById(Integer blogNoticeId) {
		return blognoticeMapper.deleteBlognoticeById(blogNoticeId);
	}

	/**
	 * 批量删除博客公告对象
	 * 
	 * @param blogNoticeIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int batchDeleteBlognotice(Integer[] blogNoticeIds) {
		return blognoticeMapper.batchDeleteBlognotice(blogNoticeIds);
	}

	/**
	 * 总公告数
	 */
	@Override
	public Integer selectAllBlogNoticeNum() {
		return blognoticeMapper.selectAllBlogNoticeNum();
	}

}
