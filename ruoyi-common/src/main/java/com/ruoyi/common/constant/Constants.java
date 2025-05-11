package com.ruoyi.common.constant;

import com.ruoyi.common.enums.AdType;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 系统语言
     */
    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 所有权限标识
     */
    public static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    public static final String SUPER_ADMIN = "admin";

    /**
     * 角色权限分隔符
     */
    public static final String ROLE_DELIMETER = ",";

    /**
     * 权限标识分隔符
     */
    public static final String PERMISSION_DELIMETER = ",";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.ruoyi" };

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "com.ruoyi.quartz.task" };

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.ruoyi.common.utils.file", "com.ruoyi.common.config", "com.ruoyi.generator" };

    public static final String UNDELETED = "0";
    public static final String DELETED = "2";

    public static final Integer SIZE_30 = 30;
    public static final Integer SIZE_31 = 31;
    public static final Integer SIZE_32 = 32;
    public static final Integer SIZE_33 = 33;
    public static final Integer SIZE_34 = 34;
    public static final Integer SIZE_35 = 35;
    public static final Integer SIZE_36 = 36;
    public static final Integer SIZE_37 = 37;
    public static final Integer SIZE_38 = 38;
    public static final Integer SIZE_39 = 39;
    public static final Integer SIZE_40 = 40;
    public static final Integer SIZE_41 = 41;
    public static final Integer SIZE_42 = 42;
    public static final Integer SIZE_43 = 43;
    /**
     * 平台内部账户ID
     */
    public static final Long PLATFORM_INTERNAL_ACCOUNT_ID = 1L;
    /**
     * 平台外部账户ID-支付宝账户
     */
    public static final Long PLATFORM_ALIPAY_EXTERNAL_ACCOUNT_ID = 1L;

    /**
     * 排序值1
     */
    public static final Integer ORDER_NUM_1 = 1;

    public static final String VERSION_LOCK_ERROR_COMMON_MSG = "系统繁忙，请稍后再试";

    /**
     * 行政区划缓存
     */
    public static final String EXPRESS_REGION_MAP_CACHE_KEY = "EXPRESS_REGION_MAP";
    public static final String EXPRESS_REGION_NAME_MAP_CACHE_KEY = "EXPRESS_REGION_NAME_MAP";
    public static final String EXPRESS_REGION_LIST_CACHE_KEY = "EXPRESS_REGION_LIST";
    public static final String EXPRESS_REGION_TREE_CACHE_KEY = "EXPRESS_REGION_TREE";
    /**
     * ES 索引 product_info
     */
    public static final String ES_IDX_PRODUCT_INFO = "product_info";
    /**
     * 最顶层商品分类ID
     */
    public static final Long TOPMOST_PRODUCT_CATEGORY_ID = 1L;

    /**
     * redis store前缀
     */
    public static final String STORE_REDIS_PREFIX = "store_";

    /**
     * 枚举的广告位类型
     */
/*    public final static List<Integer> posEnumTypeList = new ArrayList<Integer>() {{
        // 人气榜中上侧
        add(AdType.PC_HOME_POP_MID_TOP.getValue());
        // 人气榜中下侧
        add(AdType.PC_HOME_POP_MID_BOTTOM.getValue());
        // 人气榜右上侧
        add(AdType.PC_HOME_POP_RIGHT_TOP.getValue());
        // 人气榜右下侧
        add(AdType.PC_HOME_POP_RIGHT_BOTTOM.getValue());
        // 新品馆热卖榜右推广商品
        add(AdType.PC_NEW_PROD_HOT_RIGHT_PRODUCT.getValue());
        // PC下载页
        add(AdType.PC_DOWNLOAD.getValue());
    }};*/


}
