package com.rzblog.project.blog.softcontent.controller;

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
import com.rzblog.project.blog.softcontent.domain.Softcontent;
import com.rzblog.project.blog.softcontent.service.ISoftcontentService;
import com.rzblog.framework.web.controller.BaseController;
import com.rzblog.framework.web.page.TableDataInfo;
import com.rzblog.framework.web.domain.Message;

/**
 * 博客模块软件管理 信息操作处理
 * 
 * @author ricozhou
 * @date 2019-02-10
 */
@Controller
@RequestMapping("/blog/softcontent")
public class SoftcontentController extends BaseController
{
    private String prefix = "blog/softcontent";
	
	@Autowired
	private ISoftcontentService softcontentService;
	
	@GetMapping()
	@RequiresPermissions("blog:softcontent:view")
	public String softcontent()
	{
	    return prefix + "/softcontent";
	}
	
	/**
	 * 查询博客模块软件管理列表
	 */
	@RequiresPermissions("blog:softcontent:list")
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(Softcontent softcontent)
	{
		startPage();
        List<Softcontent> list = softcontentService.selectSoftcontentList(softcontent);
		return getDataTable(list);
	}
	
	/**
	 * 新增博客模块软件管理
	 */
	@RequiresPermissions("blog:softcontent:add")
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}

	/**
	 * 修改博客模块软件管理
	 */
	@RequiresPermissions("blog:softcontent:edit")
	@GetMapping("/edit/{cid}")
	public String edit(@PathVariable("cid") Long cid, Model model)
	{
		Softcontent softcontent = softcontentService.selectSoftcontentById(cid);
		model.addAttribute("softcontent", softcontent);
	    return prefix + "/edit";
	}
	
	/**
	 * 保存博客模块软件管理
	 */
	@RequiresPermissions("blog:softcontent:save")
	@PostMapping("/save")
	@ResponseBody
	public Message save(Softcontent softcontent)
	{
		if (softcontentService.saveSoftcontent(softcontent) > 0)
		{
			return Message.success();
		}
		return Message.error();
	}
	
	/**
	 * 删除博客模块软件管理
	 */
	@RequiresPermissions("blog:softcontent:remove")
	@PostMapping( "/remove/{cid}")
	@ResponseBody
	public Message remove(@PathVariable("cid") Long cid)
	{
		if (softcontentService.deleteSoftcontentById(cid) > 0)
		{
		    return Message.success();
		}
		return Message.error();
	}
	
	/**
	 * 批量删除博客模块软件管理
	 */
	@RequiresPermissions("blog:softcontent:batchRemove")
	@PostMapping( "/batchRemove")
	@ResponseBody
	public Message remove(@RequestParam("ids[]") Long[] cids)
	{
		int rows = softcontentService.batchDeleteSoftcontent(cids);
		if (rows > 0)
        {
            return Message.success();
        }
        return Message.error();
	}
	
}
