package com.ruoyi.common.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants {
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_UUID_KEY = "captcha_uuid:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * APP缓存 SYS_APP_KEY
     */
    public static final String SYS_APP_KEY = "sys_app:";

    /**
     * APP版本缓存 SYS_APP_VERSION_KEY
     */
    public static final String SYS_APP_VERSION_KEY = "sys_app_version:";

    /**
     * APP_USER缓存 SYS_APP_USER_KEY
     */
    public static final String SYS_APP_USER_KEY = "sys_app_user:";

    /**
     * 销售订单 redis key
     */
    public static final String SALE_ORDER_EXPIRE_KEY = "sale_order_expire:";

    /**
     * IP转换到地址 redis key
     */
    public static final String IP_TO_ADDRESS_KEY = "ip_to_address:";

    /**
     * 全局文件下载链接
     */
    public static final String GLOBAL_FILE_DOWNLOAD_KEY = "global_file_download:";

    /**
     * logincode
     */
    public static final String SYS_LOGIN_CODE_KEY = "sys_login_code:";

    /**
     * card
     */
    public static final String SYS_CARD_KEY = "sys_card:";

    /**
     * user
     */
    public static final String SYS_USER_KEY = "sys_user:";

    /**
     * checkDomain
     */
    public static final String SYS_CHECK_DOMAIN_CAPTCHA_KEY = "sys_check_domain:";
}
