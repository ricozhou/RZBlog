package com.rzblog.project.blog.blogtags.controller;

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

import com.rzblog.project.blog.blogtags.domain.Blogtags;
import com.rzblog.project.blog.blogtags.service.IBlogtagsService;
import com.rzblog.common.constant.CommonConstant;
import com.rzblog.framework.aspectj.lang.annotation.Log;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客标签 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-09-20
 */
@Controller
@RequestMapping("/admin/blog/blogtags")
public class BlogtagsController extends BaseController {
	private String prefix = "blog/blogtags";

	@Autowired
	private IBlogtagsService blogtagsService;

	@GetMapping()
	@RequiresPermissions("blog:blogtags:view")
	public String blogtags() {
		return prefix + "/blogtags";
	}

	/**
	 * 查询博客标签列表
	 */
	@RequiresPermissions("blog:blogtags:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogtags blogtags) {
		startPage();
		List<Blogtags> list = blogtagsService.selectBlogtagsList(blogtags);
		return getDataTable(list);
	}

	/**
	 * 新增博客标签
	 */
	@RequiresPermissions("blog:blogtags:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改博客标签
	 */
	@RequiresPermissions("blog:blogtags:edit")
	@GetMapping("/edit/{blogTagsId}")
	public String edit(@PathVariable("blogTagsId") Integer blogTagsId, Model model) {
		Blogtags blogtags = blogtagsService.selectBlogtagsById(blogTagsId);
		model.addAttribute("blogtags", blogtags);
		return prefix + "/edit";
	}

	/**
	 * 保存博客标签
	 */
	@RequiresPermissions("blog:blogtags:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogtags blogtags) {
		if (blogtagsService.saveBlogtags(blogtags) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除博客标签
	 */
	@RequiresPermissions("blog:blogtags:remove")
	@PostMapping("/remove/{blogTagsId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogTagsId") Integer blogTagsId) {
		if (blogtagsService.deleteBlogtagsById(blogTagsId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除博客标签
	 */
	@RequiresPermissions("blog:blogtags:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogTagsIds) {
		int rows = blogtagsService.batchDeleteBlogtags(blogTagsIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 校验名称
	 */
	@Log(title = "博客管理", action = "标签管理-校验标签名称")
	@PostMapping("/checkTagNameUnique")
	@ResponseBody
	public String checkTagNameUnique(Blogtags blogtags) {
		String uniqueFlag = CommonConstant.UNIQUE;
		if (blogtags != null) {
			uniqueFlag = blogtagsService.checkTagNameUnique(blogtags);
		}
		return uniqueFlag;
	}

}
