package com.rzblog.framework.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rzblog.common.constant.UserConstants;
import com.rzblog.common.utils.OtherUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.framework.aspectj.lang.annotation.RoleInterception;
import com.rzblog.framework.web.domain.Message;
import com.rzblog.project.monitor.operlog.service.IOperLogService;
import com.rzblog.project.system.role.service.IRoleService;

/**
 * 角色拦截记录处理
 * 
 * @author ricozhou
 */
@Aspect
@Component
// @EnableAsync
public class RoleAspect {
	private static final Logger log = LoggerFactory.getLogger(RoleAspect.class);
	@Autowired
	private IRoleService roleService;

	@Autowired
	private IOperLogService operLogService;

	// 配置织入点(拦截此注解)
	@Pointcut("@annotation(com.rzblog.framework.aspectj.lang.annotation.RoleInterception)")
	public void rolePointCut() {
	}

	// 增强拦截（拦截带有该注解的controller方法，直接返回，不再进行其他操作）
	@Around("rolePointCut()")
	public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
		// 获得注解
		RoleInterception controllerRole = getAnnotationRole(pjp);
		if (controllerRole == null) {
			// 执行原方法并返回
			return pjp.proceed();
		}

		// 测试管理员禁用
		if (OtherUtils.isTestManager(roleService.selectRoleKeys(ShiroUtils.getUserId()))) {
			// 直接返回
			return Message.error(UserConstants.USER_MESSAGE_TEATADMIN_DISABLED_USE);
		} else {
			// 执行原方法并返回
			return pjp.proceed();
		}
	}

	/**
	 * 是否存在注解，如果存在就获取
	 */
	private RoleInterception getAnnotationRole(JoinPoint joinPoint) throws Exception {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(RoleInterception.class);
		}
		return null;
	}
}
