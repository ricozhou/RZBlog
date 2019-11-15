package com.rzblog.project.blog.blogcomment.mapper;

import com.rzblog.project.blog.blogcomment.domain.Blogcomment;
import java.util.List;

/**
 * 博客评论 数据层
 * 
 * @author ricozhou
 * @date 2018-11-07
 */
public interface BlogcommentMapper {

	/**
	 * 查询博客评论信息
	 * 
	 * @param blogCommentId
	 *            博客评论ID
	 * @return 博客评论信息
	 */
	public Blogcomment selectBlogcommentById(Integer blogCommentId);

	/**
	 * 查询博客评论列表
	 * 
	 * @param blogcomment
	 *            博客评论信息
	 * @return 博客评论集合
	 */
	public List<Blogcomment> selectBlogcommentList(Blogcomment blogcomment);

	/**
	 * 新增博客评论
	 * 
	 * @param blogcomment
	 *            博客评论信息
	 * @return 结果
	 */
	public int insertBlogcomment(Blogcomment blogcomment);

	/**
	 * 修改博客评论
	 * 
	 * @param blogcomment
	 *            博客评论信息
	 * @return 结果
	 */
	public int updateBlogcomment(Blogcomment blogcomment);

	/**
	 * 删除博客评论
	 * 
	 * @param blogCommentId
	 *            博客评论ID
	 * @return 结果
	 */
	public int deleteBlogcommentById(Integer blogCommentId);

	/**
	 * 批量删除博客评论
	 * 
	 * @param blogCommentIds
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int batchDeleteBlogcomment(Integer[] blogCommentIds);

	/**
	 * @param showId
	 * @date Nov 7, 2018 4:48:26 PM
	 * @Desc
	 * @param i
	 * @return
	 */
	public List<Blogcomment> selectBlogcommentListByParentId(String showId, Integer parentId);

	/**
	 * @date Nov 8, 2018 4:08:51 PM
	 * @Desc
	 * @param showId
	 * @return
	 */
	public int deleteBlogcommentByShowId(String showId);

	/**
	 * @date Nov 9, 2018 1:20:11 PM
	 * @Desc
	 * @param blogCommentId
	 * @return
	 */
	public int addCommentLike(Integer blogCommentId);

	/**
	 * @date Nov 27, 2018 5:14:17 PM
	 * @Desc
	 * @return
	 */
	public Integer selectAllBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:15:37 PM
	 * @Desc
	 * @param dayNum
	 * @return
	 */
	public Integer selectBlogCommentNumPreByDayNum(Integer dayNum);

	/**
	 * @date Nov 27, 2018 5:17:02 PM
	 * @Desc
	 * @return
	 */
	public Integer selectDayBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:17:30 PM
	 * @Desc
	 * @return
	 */
	public Integer selectMonthBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:17:37 PM
	 * @Desc
	 * @return
	 */
	public Integer selectYearBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:24:40 PM
	 * @Desc
	 * @return
	 */
	public int selectDayBlogCommentNumPre();

	/**
	 * @date Nov 27, 2018 5:24:46 PM
	 * @Desc
	 * @return
	 */
	public int selectMonthBlogCommentNumPre();

	/**
	 * @date Nov 27, 2018 5:24:52 PM
	 * @Desc
	 * @return
	 */
	public int selectYearBlogCommentNumPre();

}