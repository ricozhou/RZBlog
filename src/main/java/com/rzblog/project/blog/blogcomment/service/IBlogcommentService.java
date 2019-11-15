package com.rzblog.project.blog.blogcomment.service;

import com.rzblog.project.blog.blogcomment.domain.Blogcomment;
import java.util.List;

/**
 * 博客评论 服务层
 * 
 * @author ricozhou
 * @date 2018-11-07
 */
public interface IBlogcommentService {

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
	 * 保存博客评论
	 * 
	 * @param blogcomment
	 *            博客评论信息
	 * @return 结果
	 */
	public int saveBlogcomment(Blogcomment blogcomment);

	/**
	 * 删除博客评论信息
	 * 
	 * @param blogCommentId
	 *            博客评论ID
	 * @param showId
	 * @return 结果
	 */
	public int deleteBlogcommentById(Integer blogCommentId, String showId);

	/**
	 * 批量删除博客评论信息
	 * 
	 * @param blogCommentIds
	 *            需要删除的数据ID
	 * @param showIds
	 * @return 结果
	 */
	public int batchDeleteBlogcomment(Integer[] blogCommentIds, String[] showIds);

	/**
	 * @param showId
	 * @date Nov 7, 2018 4:42:58 PM
	 * @Desc
	 * @return
	 */
	public List<Blogcomment> selectBlogcommentListToShow(String showId);

	/**
	 * @date Nov 8, 2018 9:19:24 AM
	 * @Desc
	 * @param blogcomment
	 * @return
	 */
	public int[] addBlogcomment(Blogcomment blogcomment);

	/**
	 * @date Nov 8, 2018 4:07:40 PM
	 * @Desc
	 * @param showId
	 */
	public int deleteBlogcommentByShowId(String showId);

	/**
	 * @date Nov 9, 2018 1:19:02 PM
	 * @Desc
	 * @param blogCommentId
	 * @return
	 */
	public int addCommentLike(Integer blogCommentId);

	/**
	 * @date Nov 27, 2018 5:03:43 PM
	 * @Desc
	 * @return
	 */
	public Integer selectAllBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:03:58 PM
	 * @Desc
	 * @return
	 */
	public Integer selectDayBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:04:06 PM
	 * @Desc
	 * @return
	 */
	public Integer selectMonthBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:04:14 PM
	 * @Desc
	 * @return
	 */
	public Integer selectYearBlogCommentNum();

	/**
	 * @date Nov 27, 2018 5:04:24 PM
	 * @Desc
	 * @param i
	 * @return
	 */
	public Integer selectBlogCommentNumPreByDayNum(Integer dayNum);

	/**
	 * @date Nov 27, 2018 5:23:52 PM
	 * @Desc
	 * @return
	 */
	public int selectYearBlogCommentNumPre();

	/**
	 * @date Nov 27, 2018 5:23:59 PM
	 * @Desc
	 * @return
	 */
	public int selectMonthBlogCommentNumPre();

	/**
	 * @date Nov 27, 2018 5:24:06 PM
	 * @Desc
	 * @return
	 */
	public int selectDayBlogCommentNumPre();

}
