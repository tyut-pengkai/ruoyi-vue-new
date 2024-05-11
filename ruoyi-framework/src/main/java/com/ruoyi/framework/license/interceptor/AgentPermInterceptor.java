package com.ruoyi.framework.license.interceptor;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.license.anno.AgentPermCheck;
import com.ruoyi.framework.web.service.PermissionService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 代理权限检查拦截器
 *
 * @author ruoyi
 */
@Component
public class AgentPermInterceptor implements HandlerInterceptor {

    @Resource
    private ISysAgentUserService sysAgentService;
    @Resource
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AgentPermCheck annotation = method.getAnnotation(AgentPermCheck.class);
            if (annotation != null) {
                if (permissionService.hasAnyRoles("sadmin,admin")) {
                    return true;
                }
                // 检查是否具有代理身份且身份正常
                SysAgent agent = sysAgentService.selectSysAgentByUserId(SecurityUtils.getUserId());
                sysAgentService.checkAgent(agent, annotation.checkEnableAddSubagent());
                // 检查代理权限
                if(StringUtils.isNotBlank(annotation.value())) {
                    if(!permissionService.hasAgentPermi(annotation.value())) {
                        throw new ServiceException("您没有该操作的权限（代理系统）");
                    }
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
