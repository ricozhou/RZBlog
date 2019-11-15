package com.rzblog.project.tool.swagger;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rzblog.framework.web.controller.BaseController;

import io.swagger.annotations.ApiOperation;

/**
 * swagger 接口
 * 
 * @author ricozhou
 */
@Controller
@RequestMapping("/admin/tool/swagger")
public class SwaggerController extends BaseController
{
	@ApiOperation(value = "swagger页面" ,  notes="swagger页面")
    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index()
    {
		System.out.println(redirect("/swagger-ui.html"));
        return redirect("/swagger-ui.html");
    }
}
