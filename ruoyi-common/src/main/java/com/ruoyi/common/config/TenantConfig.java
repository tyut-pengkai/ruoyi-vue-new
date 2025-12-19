package com.ruoyi.common.config;

import java.util.Arrays;
import java.util.List;

/**
 * 租户配置类
 *
 * Reason: 集中管理租户相关配置，包括超级管理员列表
 */
public class TenantConfig {

    /**
     * 超级管理员用户ID列表
     * Reason: 使用固定用户ID判断，简单可靠，避免角色权限体系的复杂度
     *
     * 超级管理员拥有以下特权：
     * 1. 登录后自动进入全局模式
     * 2. 可以查看所有租户的数据
     * 3. 所有操作都会记录审计日志
     */
    public static final List<Long> SUPER_ADMIN_USER_IDS = Arrays.asList(
        1L  // admin 用户
    );

    /**
     * 忽略租户过滤的URL路径（不需要租户隔离的接口）
     * Reason: 登录、验证码等公共接口不需要租户上下文
     */
    public static final List<String> IGNORE_URLS = Arrays.asList(
        "/login",
        "/captchaImage",
        "/logout",
        "/register"
    );

    /**
     * 需要进行租户隔离的表名列表
     * Reason: 明确定义需要拦截的表，避免误拦截
     */
    public static final List<String> TENANT_TABLES = Arrays.asList(
        // 若依框架表
        "sys_user",
        "sys_dept",
        "sys_role",
        "sys_post",
        "sys_notice",
        // HairLink业务表
        "hl_customer",
        "hl_member_card",
        "hl_service",
        "hl_staff",
        "hl_order"
    );

    /**
     * 不需要租户隔离的表（白名单）
     * Reason: 这些表是全局共享的，所有租户共用
     */
    public static final List<String> IGNORE_TABLES = Arrays.asList(
        "sys_menu",
        "sys_dict_type",
        "sys_dict_data",
        "sys_config",
        "sys_user_role",
        "sys_role_menu",
        "sys_role_dept",
        "sys_tenant",
        "sys_logininfor",
        "sys_oper_log",
        "sys_job",
        "sys_job_log"
    );

    /**
     * 判断是否是超级管理员
     *
     * @param userId 用户ID
     * @return true-超级管理员，false-普通用户
     */
    public static boolean isSuperAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        return SUPER_ADMIN_USER_IDS.contains(userId);
    }

    /**
     * 判断URL是否需要忽略租户过滤
     *
     * @param url 请求URL
     * @return true-忽略，false-需要过滤
     */
    public static boolean isIgnoreUrl(String url) {
        if (url == null) {
            return false;
        }
        return IGNORE_URLS.stream().anyMatch(url::contains);
    }

    /**
     * 判断表是否需要租户隔离
     *
     * @param tableName 表名
     * @return true-需要隔离，false-不需要
     */
    public static boolean needTenantFilter(String tableName) {
        if (tableName == null) {
            return false;
        }

        String lowerTableName = tableName.toLowerCase();

        // 白名单表不需要过滤
        if (IGNORE_TABLES.stream().anyMatch(lowerTableName::equals)) {
            return false;
        }

        // 租户表需要过滤
        return TENANT_TABLES.stream().anyMatch(lowerTableName::equals);
    }
}
