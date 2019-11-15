package com.rzblog.project.blog.bloganniversary.controller;

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
import com.rzblog.project.blog.bloganniversary.domain.Bloganniversary;
import com.rzblog.project.blog.bloganniversary.service.IBloganniversaryService;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 纪念日管理 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-12-04
 */
@Controller
@RequestMapping("/admin/blog/bloganniversary")
public class BloganniversaryController extends BaseController {
	private String prefix = "blog/bloganniversary";

	@Autowired
	private IBloganniversaryService bloganniversaryService;

	@GetMapping()
	@RequiresPermissions("blog:bloganniversary:view")
	public String bloganniversary() {
		return prefix + "/bloganniversary";
	}

	/**
	 * 查询纪念日管理列表
	 */
	@RequiresPermissions("blog:bloganniversary:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Bloganniversary bloganniversary) {
		startPage();
		List<Bloganniversary> list = bloganniversaryService.selectBloganniversaryList(bloganniversary);
		return getDataTable(list);
	}

	/**
	 * 新增纪念日管理
	 */
	@RequiresPermissions("blog:bloganniversary:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改纪念日管理
	 */
	@RequiresPermissions("blog:bloganniversary:edit")
	@GetMapping("/edit/{blogAnniversaryId}")
	public String edit(@PathVariable("blogAnniversaryId") Integer blogAnniversaryId, Model model) {
		Bloganniversary bloganniversary = bloganniversaryService.selectBloganniversaryById(blogAnniversaryId);
		model.addAttribute("bloganniversary", bloganniversary);
		return prefix + "/edit";
	}

	/**
	 * 保存纪念日管理
	 */
	@RequiresPermissions("blog:bloganniversary:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Bloganniversary bloganniversary) {
		if (bloganniversaryService.saveBloganniversary(bloganniversary) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除纪念日管理
	 */
	@RequiresPermissions("blog:bloganniversary:remove")
	@PostMapping("/remove/{blogAnniversaryId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogAnniversaryId") Integer blogAnniversaryId) {
		if (bloganniversaryService.deleteBloganniversaryById(blogAnniversaryId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除纪念日管理
	 */
	@RequiresPermissions("blog:bloganniversary:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogAnniversaryIds) {
		int rows = bloganniversaryService.batchDeleteBloganniversary(blogAnniversaryIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

}
