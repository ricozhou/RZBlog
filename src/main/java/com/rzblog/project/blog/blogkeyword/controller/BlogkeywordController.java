package com.rzblog.project.blog.blogkeyword.controller;

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
import com.rzblog.project.blog.blogkeyword.domain.Blogkeyword;
import com.rzblog.project.blog.blogkeyword.service.IBlogkeywordService;
import com.rzblog.common.constant.CommonConstant;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 关键词管理 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-11-08
 */
@Controller
@RequestMapping("/admin/blog/blogkeyword")
public class BlogkeywordController extends BaseController {
	private String prefix = "blog/blogkeyword";

	@Autowired
	private IBlogkeywordService blogkeywordService;

	@GetMapping()
	@RequiresPermissions("blog:blogkeyword:view")
	public String blogkeyword() {
		return prefix + "/blogkeyword";
	}

	/**
	 * 查询关键词管理列表
	 */
	@RequiresPermissions("blog:blogkeyword:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogkeyword blogkeyword) {
		startPage();
		List<Blogkeyword> list = blogkeywordService.selectBlogkeywordList(blogkeyword);
		return getDataTable(list);
	}

	/**
	 * 新增关键词管理
	 */
	@RequiresPermissions("blog:blogkeyword:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改关键词管理
	 */
	@RequiresPermissions("blog:blogkeyword:edit")
	@GetMapping("/edit/{blogKeywordId}")
	public String edit(@PathVariable("blogKeywordId") Integer blogKeywordId, Model model) {
		Blogkeyword blogkeyword = blogkeywordService.selectBlogkeywordById(blogKeywordId);
		model.addAttribute("blogkeyword", blogkeyword);
		return prefix + "/edit";
	}

	/**
	 * 保存关键词管理
	 */
	@RequiresPermissions("blog:blogkeyword:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogkeyword blogkeyword) {
		if (blogkeywordService.saveBlogkeyword(blogkeyword) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除关键词管理
	 */
	@RequiresPermissions("blog:blogkeyword:remove")
	@PostMapping("/remove/{blogKeywordId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogKeywordId") Integer blogKeywordId) {
		if (blogkeywordService.deleteBlogkeywordById(blogKeywordId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除关键词管理
	 */
	@RequiresPermissions("blog:blogkeyword:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogKeywordIds) {
		int rows = blogkeywordService.batchDeleteBlogkeyword(blogKeywordIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 校验名称
	 */
	@PostMapping("/checkKeywordNameUnique")
	@ResponseBody
	public String checkKeywordNameUnique(Blogkeyword blogkeyword) {
		String uniqueFlag = CommonConstant.UNIQUE;
		if (blogkeyword != null) {
			uniqueFlag = blogkeywordService.checkKeywordNameUnique(blogkeyword);
		}
		return uniqueFlag;
	}
}
