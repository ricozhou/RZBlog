package com.rzblog.framework.shiro.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * @author ricozhou
 */
@Component
public class PermissionService
{
    public String hasPermi(String permission)
    {
        return isPermittedOperator(permission) ? "" : "hidden";
    }

    private boolean isPermittedOperator(String permission)
    {
        if (SecurityUtils.getSubject().isPermitted(permission))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
