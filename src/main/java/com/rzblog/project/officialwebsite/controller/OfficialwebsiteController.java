package com.rzblog.project.officialwebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.project.blog.blogset.service.IBlogsetService;

/**
 * 官方网站 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-06-12
 */
@Controller
@RequestMapping("/")
public class OfficialwebsiteController extends BaseController {
	@Autowired
	private IBlogsetService blogsetService;

	// 到主界面
	@GetMapping()
	public String blog(Model model) {
		// 把设置数据发送到前端
		model.addAttribute("blogset", blogsetService.getBlogsetMsg());
		return "blog/index/list/main";
	}
}
