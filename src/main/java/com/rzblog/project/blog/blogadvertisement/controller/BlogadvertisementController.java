package com.rzblog.project.blog.blogadvertisement.controller;

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
import com.rzblog.project.blog.blogadvertisement.domain.Blogadvertisement;
import com.rzblog.project.blog.blogadvertisement.service.IBlogadvertisementService;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 广告管理 信息操作处理
 * 
 * @author ricozhou
 * @date 2019-02-12
 */
@Controller
@RequestMapping("/admin/blog/blogadvertisement")
public class BlogadvertisementController extends BaseController {
	private String prefix = "blog/blogadvertisement";

	@Autowired
	private IBlogadvertisementService blogadvertisementService;

	@GetMapping()
	@RequiresPermissions("blog:blogadvertisement:view")
	public String blogadvertisement() {
		return prefix + "/blogadvertisement";
	}

	/**
	 * 查询广告管理列表
	 */
	@RequiresPermissions("blog:blogadvertisement:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogadvertisement blogadvertisement) {
		startPage();
		List<Blogadvertisement> list = blogadvertisementService.selectBlogadvertisementList(blogadvertisement);
		return getDataTable(list);
	}

	/**
	 * 新增广告管理
	 */
	@RequiresPermissions("blog:blogadvertisement:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改广告管理
	 */
	@RequiresPermissions("blog:blogadvertisement:edit")
	@GetMapping("/edit/{blogAdId}")
	public String edit(@PathVariable("blogAdId") Integer blogAdId, Model model) {
		Blogadvertisement blogadvertisement = blogadvertisementService.selectBlogadvertisementById(blogAdId);
		model.addAttribute("blogadvertisement", blogadvertisement);
		return prefix + "/edit";
	}

	/**
	 * 保存广告管理
	 */
	@RequiresPermissions("blog:blogadvertisement:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogadvertisement blogadvertisement) {
		if (blogadvertisementService.saveBlogadvertisement(blogadvertisement) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除广告管理
	 */
	@RequiresPermissions("blog:blogadvertisement:remove")
	@PostMapping("/remove/{blogAdId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogAdId") Integer blogAdId) {
		if (blogadvertisementService.deleteBlogadvertisementById(blogAdId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除广告管理
	 */
	@RequiresPermissions("blog:blogadvertisement:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogAdIds) {
		int rows = blogadvertisementService.batchDeleteBlogadvertisement(blogAdIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

}
