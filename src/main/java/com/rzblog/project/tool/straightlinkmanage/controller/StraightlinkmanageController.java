package com.rzblog.project.tool.straightlinkmanage.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
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

import com.rzblog.common.constant.CommonConstant;
import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.FileExtensionConstant;
import com.rzblog.common.constant.FileMessageConstant;
import com.rzblog.common.constant.ReturnMessageConstant;
import com.rzblog.common.utils.FileUploadUtils;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.framework.aspectj.lang.annotation.Log;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.domain.Message;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.project.common.file.utilt.FileUtils;
import com.rzblog.project.tool.straightlinkmanage.domain.Straightlinkmanage;
import com.rzblog.project.tool.straightlinkmanage.service.IStraightlinkmanageService;

/**
 * 直链管理详情 信息操作处理
 * 
 * @author ricozhou
 * @date 2018-10-16
 */
@Controller
@RequestMapping("/admin/tool/straightlinkmanage")
public class StraightlinkmanageController extends BaseController {
	private String prefix = "tool/straightlinkmanage";

	@Autowired
	private IStraightlinkmanageService straightlinkmanageService;

	@GetMapping()
	@RequiresPermissions("tool:straightlinkmanage:view")
	public String straightlinkmanage() {
		return prefix + "/straightlinkmanage";
	}

	/**
	 * 查询直链管理详情列表
	 */
	@RequiresPermissions("tool:straightlinkmanage:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Straightlinkmanage straightlinkmanage) {
		startPage();
		List<Straightlinkmanage> list = straightlinkmanageService.selectStraightlinkmanageList(straightlinkmanage);
		return getDataTable(list);
	}

	/**
	 * 新增直链管理详情
	 */
	@RequiresPermissions("tool:straightlinkmanage:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改直链管理详情
	 */
	@RequiresPermissions("tool:straightlinkmanage:edit")
	@GetMapping("/edit/{straightlinkmanageId}")
	public String edit(@PathVariable("straightlinkmanageId") Integer straightlinkmanageId, Model model) {
		Straightlinkmanage straightlinkmanage = straightlinkmanageService
				.selectStraightlinkmanageById(straightlinkmanageId);
		model.addAttribute("straightlinkmanage", straightlinkmanage);
		return prefix + "/edit";
	}

	/**
	 * 保存直链管理详情
	 */
	@RequiresPermissions("tool:straightlinkmanage:save")
	@PostMapping("/save")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message save(Straightlinkmanage straightlinkmanage) {
		if (straightlinkmanageService.saveStraightlinkmanage(straightlinkmanage) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 删除直链管理详情
	 */
	@RequiresPermissions("tool:straightlinkmanage:remove")
	@PostMapping("/remove/{straightlinkmanageId}")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@PathVariable("straightlinkmanageId") Integer straightlinkmanageId) {
		if (straightlinkmanageService.deleteStraightlinkmanageById(straightlinkmanageId) > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 批量删除直链管理详情
	 */
	@RequiresPermissions("tool:straightlinkmanage:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	// 拦截指定角色（测试管理员）
	@RoleInterception()
	public Message remove(@RequestParam("ids[]") Integer[] straightlinkmanageIds) {
		int rows = straightlinkmanageService.batchDeleteStraightlinkmanage(straightlinkmanageIds);
		if (rows > 0) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 上传文件
	 */
	@Log(title = "系统工具", action = "直链管理-文件上传")
	@RequiresPermissions("tool:straightlinkmanage:uploadFile")
	@ResponseBody
	@PostMapping("/uploadFile")
	public Message uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			String straightlinkFileUrlKey) {
		// 更新
		// 判断文件大小,不超过500m
		try {
			FileUploadUtils.assertAllowedSetSize(file, FileUploadUtils.DEFAULT_MAX_SIZE_100M);
		} catch (FileSizeLimitExceededException e1) {
			e1.printStackTrace();
			return Message.error(FileMessageConstant.FILE_MESSAGE_SIZE_GREATER_ONE_HUNDRED_M);
		}
		// 原始名字
		String fileName = file.getOriginalFilename();
		// 重命名
		fileName = FileUploadUtils.renameToUUID(fileName);
		if (straightlinkFileUrlKey == null || CommonSymbolicConstant.EMPTY_STRING.equals(straightlinkFileUrlKey)
				|| CommonConstant.UNDEFINED.equals(straightlinkFileUrlKey)) {
			straightlinkFileUrlKey = StringUtils.getUUID();
		} else {
			// 验证 straightlinkFileUrlKey

		}
		// 每一个直链单独给一个文件夹
		try {
			FileUploadUtils.uploadFile(file.getBytes(),
					FilePathConfig.getStraightlinkManagePath() + File.separator + straightlinkFileUrlKey,
					file.getOriginalFilename());
			// // 保存的时候再解压删除
			// // 如果是压缩文件还要解压
			// File file2 = new File(FilePathConfig.getStraightlinkManagePath()
			// + File.separator + straightlinkFileUrlKey
			// + File.separator + file.getOriginalFilename());
			// if
			// (fileName.endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_COMPRESSEDFILE_ZIP))
			// {
			// FileUtils.unZipFiles(file2,
			// FilePathConfig.getStraightlinkManagePath() + File.separator +
			// straightlinkFileUrlKey, false);
			// file2.delete();
			// }
			// 返回文件名
			Message message = new Message();
			message = message.success();
			message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_2, fileName);
			message.put(ReturnMessageConstant.RETURN_MESSAGE_KEY_6, straightlinkFileUrlKey);
			return message;
		} catch (Exception e) {
			return Message.error(FileMessageConstant.FILE_MESSAGE_FILE_UPLOAD_FAILED);
		}
	}

	/**
	 * 删除缓存文件夹
	 */
	@PostMapping("/deleteCacheFile")
	@ResponseBody
	public Message deleteCacheFile(String straightlinkFileUrlKey) {
		String path = FilePathConfig.getStraightlinkManagePath() + File.separator + straightlinkFileUrlKey;
		if (FileUtils.deleteFile(path)) {
			return Message.success();
		}
		return Message.error();
	}

	/**
	 * 导出主页
	 */
	@RequiresPermissions("tool:straightlinkmanage:save")
	@GetMapping("/exportPageCode/{straightlinkFileUrlKey}")
	public void exportPageCode(HttpServletResponse response,
			@PathVariable("straightlinkFileUrlKey") String straightlinkFileUrlKey) throws IOException {
		response.reset();
		List<String> filePathList = new ArrayList<String>();
		// 添加地址
		String folderPath = FilePathConfig.getStraightlinkManagePath() + File.separator + straightlinkFileUrlKey;
		filePathList.add(folderPath);
		byte[] data = FileUtils.getAllFileToByte(filePathList, false, straightlinkFileUrlKey);
		String fileName = straightlinkFileUrlKey + FileExtensionConstant.FILE_EXTENSION_POINT_COMPRESSEDFILE_ZIP;
		// 处理中文乱码
		try {
			fileName = FileUtils.getNewString(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.reset();
		response.setHeader(FileMessageConstant.FILE_CONTENT_DISPOSITION,
				FileMessageConstant.FILE_ATTACHMENT_FILENAME + fileName);
		response.addHeader(FileMessageConstant.FILE_CONTENT_LENGTH, CommonSymbolicConstant.EMPTY_STRING + data.length);
		response.setContentType(FileMessageConstant.FILE_CONTENT_TYPE);

		IOUtils.write(data, response.getOutputStream());
		response.getOutputStream().close();
	}
}
