package com.rzblog.project.blog.blognotice.controller;

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
import com.rzblog.project.blog.blognotice.domain.Blognotice;
import com.rzblog.project.blog.blognotice.service.IBlognoticeService;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客公告 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-10-23
 */
@Controller
@RequestMapping("/admin/blog/blognotice")
public class BlognoticeController extends BaseController {
	private String prefix = "blog/blognotice";

	@Autowired
	private IBlognoticeService blognoticeService;

	@GetMapping()
	@RequiresPermissions("blog:blognotice:view")
	public String blognotice() {
		return prefix + "/blognotice";
	}

	/**
	 * 查询博客公告列表
	 */
	@RequiresPermissions("blog:blognotice:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blognotice blognotice) {
		startPage();
		List<Blognotice> list = blognoticeService.selectBlognoticeList(blognotice);
		return getDataTable(list);
	}

	/**
	 * 新增博客公告
	 */
	@RequiresPermissions("blog:blognotice:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改博客公告
	 */
	@RequiresPermissions("blog:blognotice:edit")
	@GetMapping("/edit/{blogNoticeId}")
	public String edit(@PathVariable("blogNoticeId") Integer blogNoticeId, Model model) {
		Blognotice blognotice = blognoticeService.selectBlognoticeById(blogNoticeId);
		model.addAttribute("blognotice", blognotice);
		return prefix + "/edit";
	}

	/**
	 * 保存博客公告
	 */
	@RequiresPermissions("blog:blognotice:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blognotice blognotice) {
		if (blognoticeService.saveBlognotice(blognotice) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除博客公告
	 */
	@RequiresPermissions("blog:blognotice:remove")
	@PostMapping("/remove/{blogNoticeId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogNoticeId") Integer blogNoticeId) {
		if (blognoticeService.deleteBlognoticeById(blogNoticeId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除博客公告
	 */
	@RequiresPermissions("blog:blognotice:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogNoticeIds) {
		int rows = blognoticeService.batchDeleteBlognotice(blogNoticeIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

}
