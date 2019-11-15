package com.rzblog.project.blog.blogoverview.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.project.blog.blogoverview.domain.Blogoverview;
import com.rzblog.project.blog.blogoverview.service.IBlogoverviewService;

/**
 * 博客网站 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-06-12
 */
@Controller
@RequestMapping("/admin/blog/blogoverview")
public class BlogoverviewController extends BaseController {
	private String prefix = "blog/blogoverview";
	@Autowired
	private IBlogoverviewService blogoverviewService;

	@GetMapping()
	@RequiresPermissions("blog:blogoverview:view")
	public String blogoverview(Blogoverview blogoverview,Model model) {

		model.addAttribute("blogoverview", blogoverviewService.selectBlogoverview(blogoverview));
		return prefix + "/blogoverview";
	}
}
