package com.rzblog.project.blog.blogcolumn.controller;

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
import com.rzblog.project.blog.blogcolumn.domain.Blogcolumn;
import com.rzblog.project.blog.blogcolumn.service.IBlogcolumnService;
import com.rzblog.common.constant.CommonConstant;
import com.rzblog.framework.aspectj.lang.annotation.Log;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客专栏管理 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-09-15
 */
@Controller
@RequestMapping("/admin/blog/blogcolumn")
public class BlogcolumnController extends BaseController {
	private String prefix = "blog/blogcolumn";

	@Autowired
	private IBlogcolumnService blogcolumnService;

	@GetMapping()
	@RequiresPermissions("blog:blogcolumn:view")
	public String blogcolumn() {
		return prefix + "/blogcolumn";
	}

	/**
	 * 查询博客专栏管理列表
	 */
	@RequiresPermissions("blog:blogcolumn:list")
	@GetMapping("/list")
	@ResponseBody
	public List<Blogcolumn> list(Blogcolumn blogcolumn) {
		List<Blogcolumn> list = blogcolumnService.selectBlogcolumnListAll(blogcolumn);
		return list;
	}

	/**
	 * 删除博客专栏管理
	 */
	@RequiresPermissions("blog:blogcolumn:remove")
	@PostMapping("/remove/{blogColumnId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogColumnId") Integer blogColumnId,
			@RequestParam("delArticle") boolean delArticle) {
		// 同时清除专栏内所有文章
		// 更新，不再同时删除专栏内文章,给选择参数
		if (blogcolumnService.deleteBlogcolumnById(blogColumnId, delArticle) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 新增博客专栏管理
	 */
	@RequiresPermissions("blog:blogcolumn:add")
	@GetMapping("/add/{flag}/{parentId}")
	public String add(@PathVariable("flag") Integer flag, @PathVariable("parentId") Integer parentId, Model model) {
		model.addAttribute("flag", flag);
		model.addAttribute("parentId", parentId);
		return prefix + "/add";
	}

	/**
	 * 修改博客专栏管理
	 */
	@RequiresPermissions("blog:blogcolumn:edit")
	@GetMapping("/edit/{flag}/{blogColumnId}")
	public String edit(@PathVariable("flag") Integer flag, @PathVariable("blogColumnId") Integer blogColumnId,
			Model model) {
		Blogcolumn blogcolumn = blogcolumnService.selectBlogcolumnById(blogColumnId);
		model.addAttribute("blogcolumn", blogcolumn);
		model.addAttribute("flag", flag);
		return prefix + "/edit";
	}

	/**
	 * 保存博客专栏管理
	 */
	@RequiresPermissions("blog:blogcolumn:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogcolumn blogcolumn) {
		if (blogcolumnService.saveBlogcolumn(blogcolumn) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 校验名称
	 */
	@Log(title = "博客管理", action = "专栏管理-校验专栏名称")
	@PostMapping("/checkColumnNameUnique")
	@ResponseBody
	public String checkColumnNameUnique(Blogcolumn blogcolumn) {
		String uniqueFlag = CommonConstant.UNIQUE;
		if (blogcolumn != null) {
			uniqueFlag = blogcolumnService.checkColumnNameUnique(blogcolumn);
		}
		return uniqueFlag;
	}

	/**
	 * 校验url
	 */
	@Log(title = "博客管理", action = "专栏管理-校验专栏url")
	@PostMapping("/checkUrlUnique")
	@ResponseBody
	public String checkUrlUnique(Blogcolumn blogcolumn) {
		String uniqueFlag = CommonConstant.UNIQUE;
		if (blogcolumn != null) {
			uniqueFlag = blogcolumnService.checkUrlUnique(blogcolumn);
		}
		return uniqueFlag;
	}

	/**
	 * 查询所有的次专栏信息
	 */
	@GetMapping("/selectBlogCcolumnList")
	@ResponseBody
	public List<Blogcolumn> selectBlogCcolumnList() {
		List<Blogcolumn> list = blogcolumnService.selectBlogCcolumnList();
		return list;
	}

	/**
	 * 选择专栏图标
	 */
	@GetMapping("/icon")
	public String icon() {
		return prefix + "/icon";
	}
}
