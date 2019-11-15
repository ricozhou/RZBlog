package com.rzblog.project.blog.blogset.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rzblog.project.blog.blogset.domain.Blogset;
import com.rzblog.project.blog.blogset.domain.Blogsiderbar;
import com.rzblog.project.blog.blogset.service.IBlogsetService;
import com.rzblog.project.blog.blogset.service.IBlogsiderbarService;
import com.rzblog.common.constant.CommonConstant;
import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.FileMessageConstant;
import com.rzblog.common.constant.FileOtherConstant;
import com.rzblog.common.constant.ReturnMessageConstant;
import com.rzblog.common.constant.project.BlogConstant;
import com.rzblog.common.utils.FileUploadUtils;
import com.rzblog.framework.aspectj.lang.annotation.Log;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客设置详情 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-09-13
 */
@Controller
@RequestMapping("/admin/blog/blogset")
public class BlogsetController extends BaseController {
	private String prefix = "blog/blogset";

	@Autowired
	private IBlogsetService blogsetService;

	@Autowired
	private IBlogsiderbarService blogsiderbarService;

	@GetMapping()
	@RequiresPermissions("blog:blogset:view")
	public String blogset(Model model) {
		List<Blogset> list = blogsetService.selectBlogsetList(null);
		if (list != null && list.size() > 0) {
			model.addAttribute("blogset", list.get(0));
		}
		// 所有的侧边栏信息
		List<Blogsiderbar> blogsiderbarList = blogsiderbarService.selectBlogsiderbarList(null);
		model.addAttribute("blogsiderbarList", blogsiderbarList);
		return prefix + "/blogset";
	}

	/**
	 * 保存博客基础设置详情
	 */
	@RequiresPermissions("blog:blogset:save")
	@PostMapping("/saveBasicset")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message saveBasicset(Blogset blogset) {
		if (blogsetService.saveBasicset(blogset) > 0) {
			return Message.success();
		}
		return Message.error(BlogConstant.BLOG_BASICSET_SAVE_FAILED);
	}

	/**
	 * 保存博主设置详情
	 */
	@RequiresPermissions("blog:blogset:save")
	@PostMapping("/saveBloggerset")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message saveBloggerset(Blogset blogset) {
		if (blogsetService.saveBloggerset(blogset) > 0) {
			return Message.success();
		}
		return Message.error(BlogConstant.BLOG_BLOGGERSET_SAVE_FAILED);
	}

	/**
	 * 保存博客设置详情
	 */
	@RequiresPermissions("blog:blogset:save")
	@PostMapping("/saveBlogset")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message saveBlogset(Blogset blogset) {
		if (blogsetService.saveBlogset(blogset) > 0) {
			return Message.success();
		}
		return Message.error(BlogConstant.BLOG_BLOGSET_SAVE_FAILED);
	}

	/**
	 * 保存博客设置详情
	 */
	@RequiresPermissions("blog:blogset:save")
	@PostMapping("/saveStyleset")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message saveStyleset(Blogset blogset) {
		if (blogsetService.saveStyleset(blogset) > 0) {
			return Message.success();
		}
		return Message.error(BlogConstant.BLOG_BLOGSET_SAVE_FAILED);
	}

	/**
	 * 上传文件，直接获取内容存储到数据库
	 */
	@Log(title = "博客管理", action = "博客设置-文件上传")
	@ResponseBody
	@PostMapping("/uploadImgFile")
	public Message uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, String blogFileName)
			throws IOException {
		String basePath = FilePathConfig.getUploadBlogPath();
		// 判断文件大小，格式等等
		try {
			FileUploadUtils.assertAllowedSetSize(file, 2 * 1024 * 1024);
		} catch (FileSizeLimitExceededException e1) {
			e1.printStackTrace();
			return Message.error(FileMessageConstant.FILE_MESSAGE_SIZE_GREATER_SIZE);
		}
		// 原始名字
		String fileName = file.getOriginalFilename();

		// 重命名
		fileName = FileUploadUtils.renameToUUID(fileName);
		if (blogFileName == null || CommonSymbolicConstant.EMPTY_STRING.equals(blogFileName)
				|| CommonConstant.UNDEFINED.equals(blogFileName)) {
			blogFileName = "blogset";
		} else {
			// 验证 blogFileName

		}
		// 先上传
		try {
			FileUploadUtils.uploadFile(file.getBytes(), basePath + File.separator + blogFileName, fileName);
		} catch (Exception e) {
			return Message.error();
		}
		// String imgbase64String = OtherConstant.OTHER_DATAIMAGE
		// +
		// fileName.substring(fileName.lastIndexOf(CommonSymbolicConstant.POINT)
		// + 1)
		// + OtherConstant.OTHER_BASE64 + new
		// String(Base64.encodeBase64(file.getBytes()));
		// 如果直接返回路径
		String imgbase64String = FileOtherConstant.FILE_JUMP_PATH_PREFIX3 + blogFileName + File.separator + fileName;
		Message message = new Message();
		message = message.success();
		message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_1, imgbase64String);
		// message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_2,
		// fileName);
		return message;

	}

}
