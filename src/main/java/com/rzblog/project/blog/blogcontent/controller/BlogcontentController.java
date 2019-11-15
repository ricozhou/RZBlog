package com.rzblog.project.blog.blogcontent.controller;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.BlogcontentRecommend;
import com.rzblog.project.blog.blogcontent.service.IBlogcontentService;
import com.rzblog.project.blog.blogcontent.utils.BlogUtil;
import com.rzblog.project.blog.blogset.domain.Blogset;
import com.rzblog.project.blog.blogset.service.IBlogsetService;
import com.rzblog.project.blog.blogtags.domain.Blogtags;
import com.rzblog.project.blog.blogtags.service.IBlogtagsService;
import com.rzblog.project.blog.softcontent.domain.Softcontent;
import com.rzblog.project.blog.softcontent.service.ISoftcontentService;
import com.rzblog.project.common.file.FileType;
import com.rzblog.project.common.file.domain.FileDao;
import com.rzblog.project.common.file.service.IFileService;
import com.rzblog.project.common.file.utilt.FileUtils;
import com.rzblog.project.common.image.utils.WaterMarkUtils;
import com.rzblog.project.system.website.domain.Website;
import com.rzblog.project.system.website.service.IWebsiteService;
import com.alibaba.fastjson.JSONObject;
import com.rzblog.common.constant.CommonConstant;
import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.FileExtensionConstant;
import com.rzblog.common.constant.FileMessageConstant;
import com.rzblog.common.constant.FileOtherConstant;
import com.rzblog.common.constant.HttpConstant;
import com.rzblog.common.constant.ReturnMessageConstant;
import com.rzblog.common.constant.project.BlogConstant;
import com.rzblog.common.utils.FileUploadUtils;
import com.rzblog.common.utils.ImageUtils;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.framework.aspectj.lang.annotation.Log;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 文章内容 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-06-12
 */
@Controller
@RequestMapping("/admin/blog/blogcontent")
public class BlogcontentController extends BaseController {
	private String prefix = "blog/blogcontent";

	@Autowired
	private IBlogcontentService blogcontentService;
	@Autowired
	private IFileService fileService;

	@Autowired
	private IBlogsetService blogsetService;
	@Autowired
	private IBlogtagsService blogtagsService;

	@Autowired
	private ISoftcontentService softcontentService;

	@Autowired
	private IWebsiteService websiteService;

	@Log(title = "博客管理", action = "文章管理-查看")
	@GetMapping()
	@RequiresPermissions("blog:blogcontent:view")
	public String blogcontent() {
		return prefix + "/blogcontent";
	}

	/**
	 * 查询文章内容列表
	 */
	@RequiresPermissions("blog:blogcontent:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogcontent blogcontent) {
		startPage();
		List<Blogcontent> list = blogcontentService.selectBlogcontentAllListWithoutContent(blogcontent);
		return getDataTable(list);
	}

	/**
	 * 新增文章内容
	 */
	@RequiresPermissions("blog:blogcontent:add")
	@GetMapping("/add")
	public String add(Model model) {
		// 先创建一个临时文件夹
		String blogFileName = String.valueOf(UUID.randomUUID());
		FileUtils.createFolder(FilePathConfig.getUploadBlogPath() + File.separator + blogFileName);
		model.addAttribute("blogFileName", blogFileName);
		// 将设置中的博主信息发送过去
		Blogset blogset = blogsetService.selectSomeBlogsetsById(1);
		model.addAttribute("bloggersetBloggerName",
				blogset != null ? blogset.getBloggersetBloggerName() : CommonSymbolicConstant.EMPTY_STRING);
		model.addAttribute("basicsetGlobalAllowComment", blogset != null ? blogset.getBasicsetGlobalAllowComment() : 1);
		model.addAttribute("basicsetGlobalAllowReprint", blogset != null ? blogset.getBasicsetGlobalAllowReprint() : 1);
		model.addAttribute("basicsetOpenBlogDownload", blogset != null ? blogset.getBasicsetOpenBlogDownload() : 1);
		model.addAttribute("basicsetArticleEditor", blogset != null ? blogset.getBasicsetArticleEditor() : 0);
		List<Blogtags> tags = blogtagsService.selectBlogtagsList(null);
		model.addAttribute("tags", tags);
		return prefix + "/add";
	}

	/**
	 * 文章推荐设置
	 */
	@RequiresPermissions("blog:blogcontent:recommendSet")
	@GetMapping("/recommendSet")
	public String recommendSet(Model model) {
		// 将设置中的博主信息发送过去
		Blogset blogset = blogsetService.selectSomeBlogsetById(1);
		model.addAttribute("blogset", blogset);
		// 把所有文章列表发送过去
		List<Blogcontent> blogcontentRecommends = blogcontentService.selectBlogcontentRecommendWithoutContent();
		model.addAttribute("blogcontentRecommends", blogcontentRecommends);
		return prefix + "/recommendset";
	}

	/**
	 * 博客搬家
	 */
	@RequiresPermissions("blog:blogcontent:blogMove")
	@GetMapping("/blogMove")
	public String blogMove(Model model) {
		// 将设置中的博主信息发送过去
		Blogset blogset = blogsetService.selectBlogsetBlogMoveMsgById(1);
		model.addAttribute("blogset", blogset);
		model.addAttribute("loginName", ShiroUtils.getLoginName());
		return prefix + "/blogmove/blogmove";
	}

	/**
	 * 文章推荐设置保存
	 */
	@Log(title = "博客管理", action = "文章管理-推荐设置保存")
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("blog:blogcontent:recommendSetSave")
	@PostMapping("/recommendSetSave")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message recommendSetSave(BlogcontentRecommend blogcontentRecommend) {
		if (blogcontentService.recommendSetSave(blogcontentRecommend) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 修改文章内容
	 */
	@RequiresPermissions("blog:blogcontent:edit")
	@GetMapping("/edit/{cid}")
	public String edit(@PathVariable("cid") Long cid, Model model) {
		Blogcontent blogcontent = blogcontentService.selectBlogcontentById(cid);
		Softcontent softcontent = null;
		if (blogcontent.getBlogType() == 1) {
			softcontent = softcontentService.selectSoftcontentById(cid);
		}
		blogcontent.setSoftcontent(softcontent == null ? new Softcontent() : softcontent);
		model.addAttribute("blogcontent", blogcontent);

		// 将设置发送过去
		Blogset blogset = blogsetService.selectSomeBloggersetById(1);
		model.addAttribute("basicsetGlobalAllowComment", blogset != null ? blogset.getBasicsetGlobalAllowComment() : 1);
		model.addAttribute("basicsetGlobalAllowReprint", blogset != null ? blogset.getBasicsetGlobalAllowReprint() : 1);

		// 标签
		List<Blogtags> tags = blogtagsService.selectBlogtagsByCid(cid);
		model.addAttribute("tags", tags);
		return prefix + "/edit";
	}

	/**
	 * 保存文章内容
	 */
	@Log(title = "博客管理", action = "文章管理-文章保存")
	@RequiresPermissions("blog:blogcontent:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Blogcontent blogcontent, HttpServletRequest request) {
		Website website = null;
		String domain = null;
		if (blogcontent.getNowPushBaidu() == 0 && blogcontent.getStatus() == 1) {
			website = websiteService.getWebsiteSetMsg();
			// 获取网络协议
			String networkProtocol = request.getScheme();
			// 网络IP
			String ip = request.getServerName();
			// 端口号
			int port = request.getServerPort();
			if (website == null || website.getWebsiteDomainName() == null
					|| CommonSymbolicConstant.EMPTY_STRING.equals(website.getWebsiteDomainName())
					|| website.getWebsiteBaiduToken() == null
					|| CommonSymbolicConstant.EMPTY_STRING.equals(website.getWebsiteBaiduToken())
					|| networkProtocol == null || ip == null || HttpConstant.HTTP_DEFAULT_DOMAIN.equals(ip)
					|| StringUtils.checkIp(ip)) {
				blogcontent.setNowPushBaidu(1);
			}

			domain = BlogUtil.getRequestDomain(networkProtocol, ip, port);
		}

		if (blogcontent.getType() == null) {
			blogcontent.setType("article");
		}
		if (blogcontentService.saveBlogcontent(blogcontent, website, domain) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除缓存文件夹
	 */
	@PostMapping("/deleteCacheFile")
	@ResponseBody
	public Message deleteCacheFile(String blogFileName) {
		String path = FilePathConfig.getUploadBlogPath() + File.separator + blogFileName;
		if (FileUtils.deleteFile(path)) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除文章内容
	 */
	@Log(title = "博客管理", action = "文章管理-文章删除")
	@RequiresPermissions("blog:blogcontent:remove")
	@PostMapping("/remove/{cid}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("cid") Long cid, @RequestParam("delPushBaidu") boolean delPushBaidu) {
		if (blogcontentService.deleteBlogcontentById(cid, delPushBaidu) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除文章内容
	 */
	@Log(title = "博客管理", action = "文章管理-批量删除文章")
	@RequiresPermissions("blog:blogcontent:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Long[] cids, @RequestParam("delPushBaidu") boolean delPushBaidu) {
		int rows = blogcontentService.batchDeleteBlogcontent(cids, delPushBaidu);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 文本编辑上传图片
	 */
	@ResponseBody
	@PostMapping("/uploadBlogImg")
	public Message uploadimg(@RequestParam("file") MultipartFile file, HttpServletRequest request, String blogFileName,
			boolean addWaterMark) {
		String fileName = file.getOriginalFilename();
		if (!fileName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_IMAGE_JPG)
				&& !fileName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_IMAGE_PNG)
				&& !fileName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_IMAGE_GIF)
				&& !fileName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_IMAGE_JPEG)) {
			return Message.error(FileMessageConstant.FILE_MESSAGE_FORMAT_INCORRENT);
		}
		// 重命名
		fileName = FileUploadUtils.renameToUUID(fileName);
		if (blogFileName == null || CommonSymbolicConstant.EMPTY_STRING.equals(blogFileName)
				|| CommonConstant.UNDEFINED.equals(blogFileName)) {
			blogFileName = String.valueOf(UUID.randomUUID());
		} else {
			// 验证 blogFileName

		}
		FileDao sysFile = new FileDao(FileType.fileType(fileName),
				FileOtherConstant.FILE_JUMP_PATH_PREFIX3 + blogFileName + File.separator + fileName, new Date());
		// 先上传
		try {
			FileUploadUtils.uploadFile(file.getBytes(),
					FilePathConfig.getUploadBlogPath() + File.separator + blogFileName, fileName);
		} catch (Exception e) {
			return Message.error(FileMessageConstant.FILE_MESSAGE_FILE_UPLOAD_FAILED);
		}
		// 打水印
		if (addWaterMark) {
			// 查询总设置是否需要打水印
			Blogset blogset = blogsetService.selectBlogsetWaterMarkMsgById(1);
			if (blogset.getBasicsetAddWaterMark() == 0 && blogset.getBasicsetWaterMarkMsg() != null
					&& !CommonSymbolicConstant.EMPTY_STRING.equals(blogset.getBasicsetWaterMarkMsg())) {
				String srcImgPath = FilePathConfig.getUploadBlogPath() + File.separator + blogFileName + File.separator
						+ fileName; // 源图片地址
				String tarImgPath = FilePathConfig.getUploadBlogPath() + File.separator + blogFileName + File.separator
						+ fileName; // 待存储的地址
				String waterMarkContent = blogset.getBasicsetWaterMarkMsg(); // 水印内容
				// 第一个是颜色16进制，第二个是字体，第三个是字体样式，第四个是字体大小,第五个是水印方位
				String[] markSetMsg = blogset.getBasicsetWaterMarkSetMsg().split(CommonSymbolicConstant.COMMA);

				int[] rgb = ImageUtils.hexToRgb(markSetMsg[0]);
				Color color = new Color(rgb[0], rgb[1], rgb[2], 110);
				Font font = new Font(markSetMsg[1], Integer.valueOf(markSetMsg[2]), Integer.valueOf(markSetMsg[3]));
				WaterMarkUtils.addWaterMark(srcImgPath, tarImgPath, waterMarkContent, color, font,
						Integer.valueOf(markSetMsg[4]));
			}
		}
		if (fileService.save(sysFile) > 0) {
			Message message = new Message();
			message = message.success();
			message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_2, sysFile.getUrl());
			message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_6, blogFileName);
			return message;
		}
		return Message.error();
	}

	/**
	 * 批量发布文章内容
	 */
	@Log(title = "博客管理", action = "文章管理-批量发布文章")
	@RequiresPermissions("blog:blogcontent:batchRelease")
	@PostMapping("/batchRelease")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message batchRelease(@RequestParam("status") Integer status, @RequestParam("ids[]") Long[] cids) {
		int rows = blogcontentService.batchReleaseBlogcontent(status, cids);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 置顶文章
	 */
	@RequiresPermissions("blog:blogcontent:edit")
	@PostMapping("/articleTop")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message articleTop(@RequestParam("cid") Long cid, @RequestParam("articleTop") Integer articleTop) {
		if (blogcontentService.articleTop(cid, articleTop) > 0) {
			return Message.success();
		}
		return Message.error(BlogConstant.BLOG_BLOGTOP_SAVE_FAILED);
	}

	/**
	 * 主动推送百度
	 */
	@Log(title = "博客管理", action = "文章管理-主动推送百度")
	@RequiresPermissions("blog:blogcontent:pushBaidu")
	@PostMapping("/activePushBaidu")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message activePushBaidu(@RequestParam("ids[]") String[] showIds, @RequestParam("type") Integer type,
			HttpServletRequest request) {
		Website website = websiteService.getWebsiteSetMsg();

		if (website == null || website.getWebsiteDomainName() == null
				|| CommonSymbolicConstant.EMPTY_STRING.equals(website.getWebsiteDomainName())) {
			return Message.error(BlogConstant.BLOG_BLOGPUSH_DOMAIN_NOEXIT);
		}
		if (website.getWebsiteBaiduToken() == null
				|| CommonSymbolicConstant.EMPTY_STRING.equals(website.getWebsiteBaiduToken())) {
			return Message.error(BlogConstant.BLOG_BLOGPUSH_TOKEN_NOEXIT);
		}

		// 获取网络协议
		String networkProtocol = request.getScheme();
		// 网络IP
		String ip = request.getServerName();
		// 端口号
		int port = request.getServerPort();
		if (networkProtocol == null || ip == null) {
			return Message.error(BlogConstant.BLOG_BLOGPUSH_UNKNOWN_FAILED);
		}
		if (HttpConstant.HTTP_DEFAULT_DOMAIN.equals(ip) || StringUtils.checkIp(ip)) {
			// 不能是ip，localhost
			return Message.error(BlogConstant.BLOG_BLOGPUSH_LOCALENV);
		}

		String domain = BlogUtil.getRequestDomain(networkProtocol, ip, port);

		String result = blogcontentService.activePushBaidu(showIds, website, type, domain);
		JSONObject resultJson = JSONObject.parseObject(result);
		if (resultJson == null) {
			return Message.error(BlogConstant.BLOG_BLOGPUSH_UNKNOWN_FAILED);
		}
		if (resultJson.containsKey("error")) {
			return Message.error(resultJson.getString("message"));
		}
		return Message.success(result);
	}

	/**
	 * 手动推送
	 */
	@RequiresPermissions("blog:blogcontent:pushBaidu")
	@GetMapping("/linkManualPushBaidu")
	public String linkManualPushBaidu(Model model) {
		model.addAttribute("loginName", ShiroUtils.getLoginName());
		return prefix + "/linkManualPushBaidu";
	}

	/**
	 * 手动推送百度
	 */
	@Log(title = "博客管理", action = "文章管理-手动推送百度")
	@RequiresPermissions("blog:blogcontent:pushBaidu")
	@PostMapping("/manualPushBaidu")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message manualPushBaidu(@RequestParam("site") String site, @RequestParam("token") String token,
			@RequestParam("type") Integer type, @RequestParam("urls") String urls) {
		String result = blogcontentService.manualPushBaidu(site, token, type, urls);
		JSONObject resultJson = JSONObject.parseObject(result);
		if (resultJson == null) {
			return Message.error(BlogConstant.BLOG_BLOGPUSH_UNKNOWN_FAILED);
		}
		if (resultJson.containsKey("error")) {
			return Message.error(resultJson.getString("message"));
		}
		return Message.success(result);
	}
}
