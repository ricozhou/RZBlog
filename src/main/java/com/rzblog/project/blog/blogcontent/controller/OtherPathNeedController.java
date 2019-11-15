package com.rzblog.project.blog.blogcontent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rzblog.framework.web.controller.BaseController;

/**
 * 为了一些非常特殊的需求设置
 * 
 * @author ricozhou
 * @date 2018-10-19
 */
@Controller
@RequestMapping("/otherpath/pathneed")
public class OtherPathNeedController extends BaseController {
	private String prefix = "otherpath/pathneed";

	/**
	 * 在爬取今日头条文章时可以爬取到，但是由于今日头条下拉加载下一页，
	 * get请求url需要参数签名，解密了签名算法后写出为html在浏览器执行获取_signature可以使用，
	 * 但是使用htmlunit直接执行TAV.sign方法得到的_signature却无法使用不知为何，
	 * 使用htmlunit读取本地html也是如此，大概是签名需要一些浏览器的信息，
	 * 为了能够获取可以使用的_signature只能在自己的项目中加上一个路径访问此html然后获取有效的_signature， 曲线救国
	 */
	@GetMapping("/blogmovesign")
	public String blogmovesign(Model model) {
		//参数存入map中，立刻存入立刻取出立刻删除，保证每一次请求每个用户唯一参数
		model.addAttribute("user_id", "101528687217");
		model.addAttribute("max_behot_time", "1539352612");

		return prefix + "/blogmovesign";
	}

}
