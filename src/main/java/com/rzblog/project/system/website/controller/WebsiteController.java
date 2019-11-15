package com.rzblog.project.system.website.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.rzblog.project.system.website.domain.Website;
import com.rzblog.project.system.website.service.IWebsiteService;
import com.rzblog.common.constant.project.SystemConstant;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;

/**
 * 网站设置 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-09-10
 */
@Controller
@RequestMapping("/admin/system/website")
public class WebsiteController extends BaseController {
	private String prefix = "system/website";

	@Autowired
	private IWebsiteService websiteService;

	@GetMapping()
	@RequiresPermissions("system:website:view")
	public String website(Model model) {
		List<Website> list = websiteService.selectWebsiteList(null);
		if (list != null && list.size() > 0) {
			model.addAttribute("website", list.get(0));
		}

		return prefix + "/website";
	}

	/**
	 * 保存网站设置
	 */
	@RequiresPermissions("system:website:save")
	@PostMapping("/websitesave")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message websitesave(Website website) {
		if (websiteService.saveWebsite(website) > 0) {
			return Message.success(SystemConstant.SYSTEM_MESSAGE_WEBSTIE_SAVE_SUCCESS);
		}
		return Message.error(SystemConstant.SYSTEM_MESSAGE_WEBSTIE_SAVE_FAILED);
	}

	/**
	 * 保存网站设置邮件设置
	 */
	@RequiresPermissions("system:website:save")
	@PostMapping("/mailsave")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message mailsave(Website website) {
		if (websiteService.saveMail(website) > 0) {
			return Message.success(SystemConstant.SYSTEM_MESSAGE_MAIL_SAVE_SUCCESS);
		}
		return Message.error(SystemConstant.SYSTEM_MESSAGE_MAIL_SAVE_FAILED);
	}

	/**
	 * 获取相关信息
	 */
	@RequiresPermissions("system:website:save")
	@GetMapping("/getWebSiteMsg")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Website getWebSiteMsg(Website website) {
		return websiteService.getWebsiteSetMsg();
	}
}
