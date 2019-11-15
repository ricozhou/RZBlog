package com.rzblog.project.blog.blogbrowe.controller;

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
import com.rzblog.project.blog.blogbrowe.domain.Blogbrowe;
import com.rzblog.project.blog.blogbrowe.service.IBlogbroweService;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客浏览日志 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-10-25
 */
@Controller
@RequestMapping("/admin/blog/blogbrowe")
public class BlogbroweController extends BaseController {
	private String prefix = "blog/blogbrowe";

	@Autowired
	private IBlogbroweService blogbroweService;

	@GetMapping()
	@RequiresPermissions("blog:blogbrowe:view")
	public String blogbrowe() {
		return prefix + "/blogbrowe";
	}

	/**
	 * 查询博客浏览日志列表
	 */
	@RequiresPermissions("blog:blogbrowe:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogbrowe blogbrowe) {
		startPage();
		List<Blogbrowe> list = blogbroweService.selectBlogbroweList(blogbrowe);
		return getDataTable(list);
	}

	// /**
	// * 新增博客浏览日志
	// */
	// @RequiresPermissions("blog:blogbrowe:add")
	// @GetMapping("/add")
	// public String add() {
	// return prefix + "/add";
	// }

	/**
	 * 查看博客浏览日志
	 */
	@GetMapping("/details/{blogBroweId}")
	public String details(@PathVariable("blogBroweId") Integer blogBroweId, Model model) {
		Blogbrowe blogbrowe = blogbroweService.selectBlogbroweById(blogBroweId);
		model.addAttribute("blogbrowe", blogbrowe);
		return prefix + "/details";
	}

	// /**
	// * 保存博客浏览日志
	// */
	// @RequiresPermissions("blog:blogbrowe:save")
	// @PostMapping("/save")
	// @ResponseBody
	// public Message save(Blogbrowe blogbrowe) {
	// if (blogbroweService.saveBlogbrowe(blogbrowe) > 0) {
	// return Message.success();
	// }
	// return Message.error();
	// }

	/**
	 * 删除博客浏览日志
	 */
	@RequiresPermissions("blog:blogbrowe:remove")
	@PostMapping("/remove/{blogBroweId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogBroweId") Integer blogBroweId) {
		if (blogbroweService.deleteBlogbroweById(blogBroweId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除博客浏览日志
	 */
	@RequiresPermissions("blog:blogbrowe:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogBroweIds) {
		int rows = blogbroweService.batchDeleteBlogbrowe(blogBroweIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

}
