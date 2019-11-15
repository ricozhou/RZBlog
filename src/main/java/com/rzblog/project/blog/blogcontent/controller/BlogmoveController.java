package com.rzblog.project.blog.blogcontent.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.FileExtensionConstant;
import com.rzblog.common.constant.FileMessageConstant;
import com.rzblog.common.constant.ReturnMessageConstant;
import com.rzblog.common.constant.WebSocketConstants;
import com.rzblog.common.constant.project.BlogConstant;
import com.rzblog.common.utils.FileUploadUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.framework.aspectj.lang.annotation.Log;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.websocket.service.WebSocketPushHandler;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.blog.blogcontent.service.IBlogmoveService;

/**
 * 博客搬家详情 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-10-19
 */
@Controller
@RequestMapping("/admin/blog/blogcontent/blogmove")
public class BlogmoveController extends BaseController {
	private String prefix = "blog/blogcontent/blogmove";

	@Autowired
	private IBlogmoveService blogmoveService;

	@GetMapping("/blogmovelog")
	public String blogmove() {
		return prefix + "/blogmovelog";
	}

	/**
	 * 博客搬家启动
	 * 
	 * @throws IOException
	 */
	@RequiresPermissions("blog:blogcontent:blogMoveRun")
	@PostMapping("/blogMoveRun")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message blogMoveRun(Blogmove blogMove, Model model) throws IOException {
		System.out.println("-----------------------:" + blogMove);
		// 校验状态
		if (blogmoveService.checkMoveStatus(ShiroUtils.getLoginName()) == 0) {
			return Message.error(BlogConstant.BLOG_BLOGMOVE_RUNNING_TO_STOP);
		}
		// 验证参数
		String statusMsg = "-->> 博客搬家爬虫准备完毕，共需爬取 " + blogMove.getMoveWebsiteId() + " " + blogMove.getMoveNum()
				+ " 条文章...";
		if (blogMove.getMoveMode() == 2) {
			statusMsg = "-->> 博客搬家准备完毕，共需读取WORD文档  " + blogMove.getMoveNum() + " 篇...";
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(statusMsg));
		blogMove.setMoveMessage(statusMsg);
		Integer blogMoveId = blogmoveService.blogMoveRun(blogMove);
		if (blogMoveId > -1) {
			return Message.success(String.valueOf(blogMoveId));
		}
		return Message.error(BlogConstant.BLOG_BLOGMOVE_RUN_FAILED);
	}

	/**
	 * 博客搬家中止
	 * 
	 * @throws IOException
	 */
	@RequiresPermissions("blog:blogcontent:blogMoveRun")
	@PostMapping("/blogMoveStop")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message blogMoveStop(Blogmove blogMove, Model model) throws IOException {
		// 校验状态
		if (blogmoveService.checkMoveStatus(ShiroUtils.getLoginName()) == 1) {
			return Message.error(BlogConstant.BLOG_BLOGMOVE_STOPPED);
		}
		if (blogmoveService.blogMoveStop(blogMove)) {
			return Message.success();
		}
		return Message.error(BlogConstant.BLOG_BLOGMOVE_STOP_FAILED);
	}

	/**
	 * 查询博客搬家详情列表
	 */
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Blogmove blogmove) {
		startPage();
		List<Blogmove> list = blogmoveService.selectBlogmoveList(blogmove);
		return getDataTable(list);
	}

	/**
	 * 删除博客搬家详情
	 */
	@RequiresPermissions("blog:blogcontent:blogMoveLogRemove")
	@PostMapping("/remove/{blogMoveId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("blogMoveId") Integer blogMoveId) {
		if (blogmoveService.deleteBlogmoveById(blogMoveId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除博客搬家详情
	 */
	@RequiresPermissions("blog:blogcontent:blogMoveLogBatchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] blogMoveIds) {
		int rows = blogmoveService.batchDeleteBlogmove(blogMoveIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 搬家日志查看
	 */
	@GetMapping("/viewMoveLog/{blogMoveId}")
	public String viewMoveLog(@PathVariable("blogMoveId") Integer blogMoveId, Model model) {
		Blogmove blogmove = blogmoveService.selectBlogmoveById(blogMoveId);
		model.addAttribute("blogmovelog", blogmove.getMoveMessage());
		return prefix + "/viewmovelog";
	}

	/**
	 * 上传文件doc
	 */
	@Log(title = "文章管理", action = "博客搬家-上传文件")
	@ResponseBody
	@PostMapping("/uploadBlogMoveFile")
	public Message uploadToolFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
		String fileName;
		String fileOName;
		String fileNames = null;
		String fileONames = null;
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				// 原始名字
				fileOName = files[i].getOriginalFilename();
				if (i == 0) {
					fileONames = fileOName;
				} else {
					fileONames += CommonSymbolicConstant.COMMA + fileOName;
				}

				// word和pdf
				if (!fileOName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_FILE_DOC)
						&& !fileOName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_FILE_DOCX)
						&& !fileOName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_FILE_PDF)) {
					return Message.error(FileMessageConstant.FILE_MESSAGE_FORMAT_INCORRENT);
				}

				// 重命名
				fileName = FileUploadUtils.renameToUUID(fileOName);
				// 先上传
				try {
					FileUploadUtils.uploadFile(files[i].getBytes(), FilePathConfig.getUploadCachePath(), fileName);
					if (i == 0) {
						fileNames = fileName;
					} else {
						fileNames += CommonSymbolicConstant.COMMA + fileName;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(fileNames);
			// 返回文件名
			Message message = new Message();
			message = message.success();
			message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_2, fileNames);
			message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_7, fileONames);
			return message;
		}
		return Message.error(FileMessageConstant.FILE_MESSAGE_FILE_UPLOAD_FAILED);

	}

}
