package com.ruoyi.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 租户上下文 - 基于ThreadLocal实现租户ID的传递
 *
 * Reason: 使用TransmittableThreadLocal支持线程池场景下的上下文传递
 */
public class TenantContext {

    private static final Logger log = LoggerFactory.getLogger(TenantContext.class);

    /**
     * 支持父子线程传递的ThreadLocal
     * Reason: 若依框架使用了异步任务和线程池，普通ThreadLocal会丢失上下文
     */
    private static final TransmittableThreadLocal<Long> TENANT_ID_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 忽略租户过滤的标志（用于超级管理员或系统任务）
     */
    private static final TransmittableThreadLocal<Boolean> IGNORE_TENANT = new TransmittableThreadLocal<>();

    /**
     * 设置当前租户ID
     *
     * @param tenantId 租户ID
     */
    public static void setTenantId(Long tenantId) {
        if (tenantId == null) {
            log.warn("尝试设置NULL租户ID，已拒绝");
            return;
        }
        TENANT_ID_HOLDER.set(tenantId);
        log.debug("TenantContext设置租户ID: {}", tenantId);
    }

    /**
     * 获取当前租户ID
     *
     * @return 租户ID
     */
    public static Long getTenantId() {
        return TENANT_ID_HOLDER.get();
    }

    /**
     * 清除租户上下文
     * Reason: 请求结束后必须清除，避免线程池复用时的上下文污染
     */
    public static void clear() {
        TENANT_ID_HOLDER.remove();
        IGNORE_TENANT.remove();
        log.trace("TenantContext已清除");
    }

    /**
     * 设置忽略租户过滤（超级管理员专用）
     *
     * @param ignore 是否忽略租户过滤
     */
    public static void setIgnore(boolean ignore) {
        IGNORE_TENANT.set(ignore);
        if (ignore) {
            log.warn("租户过滤已禁用，当前处于全局模式！请谨慎操作");
        } else {
            log.info("租户过滤已启用");
        }
    }

    /**
     * 是否忽略租户过滤
     *
     * @return true-忽略（全局模式），false-正常过滤
     */
    public static boolean isIgnore() {
        Boolean ignore = IGNORE_TENANT.get();
        return ignore != null && ignore;
    }

    /**
     * 获取当前租户ID（如果不存在则抛出异常）
     *
     * @return 租户ID
     * @throws com.ruoyi.common.exception.ServiceException 租户ID不存在时抛出
     */
    public static Long getRequiredTenantId() {
        Long tenantId = getTenantId();
        if (tenantId == null && !isIgnore()) {
            log.error("【安全警告】租户上下文丢失，当前操作被拦截");
            throw new RuntimeException("租户上下文丢失，请重新登录");
        }
        return tenantId;
    }
}
