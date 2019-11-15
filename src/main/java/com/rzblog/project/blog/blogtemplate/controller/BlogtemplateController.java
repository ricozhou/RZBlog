package com.rzblog.project.blog.blogtemplate.controller;

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

import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.project.blog.blogtemplate.domain.Blogtemplate;
import com.rzblog.project.blog.blogtemplate.service.IBlogtemplateService;

/**
 * 模板管理 信息操作处理
 * 
 * @author ricozhou
 * @date 2019-02-18
 */
@Controller
@RequestMapping("/admin/blog/blogtemplate")
public class BlogtemplateController extends BaseController {
	private String prefix = "blog/blogtemplate";

	@Autowired
	private IBlogtemplateService blogtemplateService;

	@GetMapping()
	@RequiresPermissions("blog:blogtemplate:view")
	public String blogtemplate() {
		return prefix + "/blogtemplate";
	}

	/**
	 * 查询模板管理列表
	 */
	@RequiresPermissions("blog:blogtemplate:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogtemplate blogtemplate) {
		startPage();
		List<Blogtemplate> list = blogtemplateService.selectBlogtemplateList(blogtemplate);
		return getDataTable(list);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("blog:blogtemplate:list")
	@GetMapping("/selectBlogTemplateList")
	@ResponseBody
	public List<Blogtemplate> selectBlogTemplateList(Blogtemplate blogtemplate) {
		List<Blogtemplate> list = blogtemplateService.selectBlogtemplateList(blogtemplate);
		return list;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("blog:blogtemplate:list")
	@GetMapping("/selectBlogTemplateById")
	@ResponseBody
	public Blogtemplate selectBlogTemplateById(Blogtemplate blogtemplate) {
		Blogtemplate blogtemplate2 = blogtemplateService.selectBlogtemplateById(blogtemplate.getBlogTemplateId());
		return blogtemplate2;
	}

	/**
	 * 新增模板管理
	 */
	@RequiresPermissions("blog:blogtemplate:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改模板管理
	 */
	@RequiresPermissions("blog:blogtemplate:edit")
	@GetMapping("/edit/{blogTemplateId}")
	public String edit(@PathVariable("blogTemplateId") Integer blogTemplateId, Model model) {
		Blogtemplate blogtemplate = blogtemplateService.selectBlogtemplateById(blogTemplateId);
		model.addAttribute("blogtemplate", blogtemplate);
		return prefix + "/edit";
	}

	/**
	 * 保存模板管理
	 */
	@RequiresPermissions("blog:blogtemplate:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogtemplate blogtemplate) {
		if (blogtemplateService.saveBlogtemplate(blogtemplate) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除模板管理
	 */
	@RequiresPermissions("blog:blogtemplate:remove")
	@PostMapping("/remove/{blogTemplateId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogTemplateId") Integer blogTemplateId) {
		if (blogtemplateService.deleteBlogtemplateById(blogTemplateId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除模板管理
	 */
	@RequiresPermissions("blog:blogtemplate:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogTemplateIds) {
		int rows = blogtemplateService.batchDeleteBlogtemplate(blogTemplateIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

}
