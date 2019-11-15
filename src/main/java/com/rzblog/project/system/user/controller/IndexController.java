package com.rzblog.project.system.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rzblog.framework.config.RZBlogConfig;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.project.system.menu.domain.Menu;
import com.rzblog.project.system.menu.service.IMenuService;
import com.rzblog.project.system.user.domain.User;
import com.rzblog.project.system.website.domain.Website;
import com.rzblog.project.system.website.service.IWebsiteService;
import com.rzblog.project.tool.baseset.service.IBasesetService;

/**
 * 首页 业务处理
 * 
 * @author ricozhou
 */
@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController {
	@Autowired
	private IMenuService menuService;

	@Autowired
	private RZBlogConfig rZBlogConfig;
	@Autowired
	private IBasesetService basesetService;
	@Autowired
	private IWebsiteService websiteService;

	@Value("${shiro.user.loginUrl}")
	public String loginUrl;

	// 系统首页
	@GetMapping("/index")
	public String index(Model model) throws Exception {
		// 取身份信息
		User user = null;
		try {
			user = getUser();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(111);
			System.out.println(111);
			return redirect(loginUrl);
		}
		// 根据用户id取出菜单
		List<Menu> menus = menuService.selectMenusByUserId(user.getUserId());
		model.addAttribute("menus", menus);
		model.addAttribute("user", user);
		model.addAttribute("copyrightYear", rZBlogConfig.getCopyrightYear());
		model.addAttribute("baseset", basesetService.getBaseset());

		Website website = websiteService.getWebsiteSetMsg();
		model.addAttribute("websiteTitleName", website.getWebsiteTitleName());
		model.addAttribute("websiteIco", website.getWebsiteIco());
		model.addAttribute("websiteCopyrightInformation", website.getWebsiteCopyrightInformation());
		return "index";
	}

	// 系统介绍
	@GetMapping("/system/main")
	public String main(Model model) {
		model.addAttribute("version", rZBlogConfig.getVersion());
		model.addAttribute("homeIntroduction", basesetService.getHomeIntroduction());
		model.addAttribute("websiteTitleName", websiteService.getSomeWebsiteSetMsg().getWebsiteTitleName());
		return "main";
	}

}
