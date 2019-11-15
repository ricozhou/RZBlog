package com.rzblog.project.blog.blogcomment.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.rzblog.project.blog.blogcomment.domain.Blogcomment;
import com.rzblog.project.blog.blogcomment.service.IBlogcommentService;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客评论 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-11-07
 */
@Controller
@RequestMapping("/admin/blog/blogcomment")
public class BlogcommentController extends BaseController {
	private String prefix = "blog/blogcomment";

	@Autowired
	private IBlogcommentService blogcommentService;

	@GetMapping()
	@RequiresPermissions("blog:blogcomment:view")
	public String blogcomment() {
		return prefix + "/blogcomment";
	}

	/**
	 * 查询博客评论列表
	 */
	@RequiresPermissions("blog:blogcomment:list")
	@GetMapping("/list")
	@ResponseBody
	public List<Blogcomment> list(Blogcomment blogcomment) {
		List<Blogcomment> list = blogcommentService.selectBlogcommentList(blogcomment);
		return list;
	}

	/**
	 * 修改博客评论
	 */
	@RequiresPermissions("blog:blogcomment:edit")
	@GetMapping("/edit/{blogCommentId}")
	public String edit(@PathVariable("blogCommentId") Integer blogCommentId, Model model) {
		Blogcomment blogcomment = blogcommentService.selectBlogcommentById(blogCommentId);
		model.addAttribute("blogcomment", blogcomment);
		return prefix + "/edit";
	}

	/**
	 * 博客评论详情查看
	 */
	@GetMapping("/detail/{blogCommentId}")
	public String detail(@PathVariable("blogCommentId") Integer blogCommentId, Model model) {
		Blogcomment blogcomment = blogcommentService.selectBlogcommentById(blogCommentId);
		model.addAttribute("blogcomment", blogcomment);
		return prefix + "/detail";
	}

	/**
	 * 保存博客评论
	 */
	@RequiresPermissions("blog:blogcomment:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogcomment blogcomment) {
		if (blogcommentService.saveBlogcomment(blogcomment) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除博客评论
	 */
	@RequiresPermissions("blog:blogcomment:remove")
	@PostMapping("/remove/{blogCommentId}/{showId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogCommentId") Integer blogCommentId, @PathVariable("showId") String showId) {
		if (blogcommentService.deleteBlogcommentById(blogCommentId, showId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除博客评论
	 */
	@RequiresPermissions("blog:blogcomment:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogCommentIds,
			@RequestParam("showIds[]") String[] showIds) {
		int rows = blogcommentService.batchDeleteBlogcomment(blogCommentIds, showIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

}
