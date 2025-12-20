package com.ruoyi.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.ruoyi.common.config.TenantConfig;
import com.ruoyi.common.core.context.TenantContext;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 租户拦截器 - 从LoginUser中提取tenant_id并设置到TenantContext
 *
 * Reason: 每个HTTP请求都需要识别当前租户，并将租户信息存入ThreadLocal
 *
 * 工作流程：
 * 1. 请求进入 → preHandle()
 * 2. 从SecurityContext获取LoginUser
 * 3. 判断是否是超级管理员
 *    - 是：设置忽略租户过滤（全局模式）
 *    - 否：提取tenant_id并设置到TenantContext
 * 4. 请求结束 → afterCompletion() → 清除TenantContext
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);

    /**
     * 请求处理前的拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestUri = request.getRequestURI();

        // 判断是否是忽略URL（如登录、验证码接口）
        if (TenantConfig.isIgnoreUrl(requestUri)) {
            log.trace("忽略URL租户过滤: {}", requestUri);
            return true;
        }

        try {
            // 从SecurityContext获取当前登录用户
            LoginUser loginUser = SecurityUtils.getLoginUser();

            if (loginUser != null) {
                Long userId = loginUser.getUserId();

                // 判断是否是超级管理员
                if (TenantConfig.isSuperAdmin(userId)) {
                    // 超级管理员：忽略租户过滤（全局模式）
                    TenantContext.setIgnore(true);
                    log.warn("【超级管理员】用户 {} (ID:{}) 进入全局模式，可查看所有租户数据",
                             loginUser.getUsername(), userId);
                }
                else if (loginUser.getTenantId() != null) {
                    // 普通用户：设置租户ID
                    TenantContext.setTenantId(loginUser.getTenantId());
                    log.debug("【租户用户】用户 {} (ID:{}) 进入租户 {} 模式",
                             loginUser.getUsername(), userId, loginUser.getTenantId());
                }
                else {
                    // 未分配租户的普通用户（不允许访问）
                    log.error("【安全警告】用户 {} (ID:{}) 未分配租户且非超级管理员，拒绝访问",
                             loginUser.getUsername(), userId);
                    throw new ServiceException("用户未分配租户，请联系管理员");
                }
            } else {
                // 未登录状态（可能是匿名访问或登录接口）
                log.trace("未获取到LoginUser，跳过租户拦截: {}", requestUri);
            }

        } catch (ServiceException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            // 其他异常（可能是登录接口或匿名访问）
            log.trace("获取租户上下文失败: {}, URI: {}", e.getMessage(), requestUri);
        }

        return true;
    }

    /**
     * 请求完成后的清理
     * Reason: 必须清除ThreadLocal，避免线程池复用时的上下文污染
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        TenantContext.clear();
        log.trace("请求完成，TenantContext已清除");
    }
}
