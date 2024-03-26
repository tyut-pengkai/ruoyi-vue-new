package com.ruoyi.common.constant;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.util.ObfuscatedString;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {

    /**
     * 记录软件用户下线原因，key为token
     */
    public static final Map<String, String> LAST_ERROR_REASON_MAP = new HashMap<>();

    /**
     * 密钥库密码
     */
    public static final String STORE_PASS = new ObfuscatedString(new long[]{0x61FCA6F12D0B529BL, 0xEB31834955913DAL, 0x162A9C12A2B377EEL}).toString() /* => "Rly*05wtyapVZAft" */;

    /**
     * 服务器机器码
     */
    public static String SERVER_SN;

    /**
     * IP列表
     */
    public static List<String> IP_ADDRESS;

    /**
     * 授权信息
     */
    public static LicenseContent LICENSE_CONTENT;

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

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

    public static final String APP_USER_ID = "app_user_id";

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
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = {"com.ruoyi"};

    public static final String LOCK_USER_CHANGE = "lock_user_change:";

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.ruoyi.common.utils.file", "com.ruoyi.common.config"};

    /**
     * 暗桩，标记被破解
     */
    public static boolean IS_CRCD = false;
}
