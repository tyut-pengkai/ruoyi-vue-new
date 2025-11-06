package com.ruoyi.common.constant;

import io.jsonwebtoken.Claims;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    public static final String SYS_NORMAL_STATUS = "0";

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

    // 系统所有的尺码
    public static final List<Integer> SIZE_LIST = new ArrayList<>(Arrays
            .asList(SIZE_30, SIZE_31, SIZE_32, SIZE_33, SIZE_34, SIZE_35, SIZE_36, SIZE_37, SIZE_38, SIZE_39, SIZE_40, SIZE_41, SIZE_42, SIZE_43));
    // 系统标准尺码 34-40
    public static final List<Integer> STANDARD_SIZE_LIST = new ArrayList<>(Arrays.asList(SIZE_34, SIZE_35, SIZE_36, SIZE_37, SIZE_38, SIZE_39, SIZE_40));

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
    /**
     * 档口默认权重 0
     */
    public static final Integer WEIGHT_DEFAULT_ZERO = 0;

    public static final String VERSION_LOCK_ERROR_COMMON_MSG = "系统繁忙，请稍后再试";

    /**
     * 行政区划缓存
     */
    public static final String EXPRESS_REGION_MAP_CACHE_KEY = "EXPRESS_REGION_MAP";
    public static final String EXPRESS_REGION_NAME_MAP_CACHE_KEY = "EXPRESS_REGION_NAME_MAP";
    public static final String EXPRESS_REGION_LIST_CACHE_KEY = "EXPRESS_REGION_LIST";
    public static final String EXPRESS_REGION_TREE_CACHE_KEY = "EXPRESS_REGION_TREE";

    // 销售出库
    public static final String VOUCHER_SEQ_STORE_SALE_PREFIX = "SD";
    // 销售出库类型
    public static final String VOUCHER_SEQ_STORE_SALE_TYPE = "STORE_SALE";
    // 采购入库
    public static final String VOUCHER_SEQ_STORAGE_PREFIX = "RK";
    // 采购入库类型
    public static final String VOUCHER_SEQ_STORAGE_TYPE = "STORAGE";
    // 需求单
    public static final String VOUCHER_SEQ_DEMAND_PREFIX = "XQ";
    // 需求单类型
    public static final String VOUCHER_SEQ_DEMAND_TYPE = "DEMAND";
    // 代发订单
    public static final String VOUCHER_SEQ_STORE_ORDER_PREFIX = "DF";
    // 代发订单类型
    public static final String VOUCHER_SEQ_STORE_ORDER_TYPE = "STORE_ORDER";
    // %04d
    public static final String VOUCHER_SEQ_FORMAT = "%04d";

    // 档口上传推广图或上传推广商品截止时间 晚上 22:20:00
    public static final String ADVERT_STORE_UPLOAD_DEADLINE = "22:20:00";

    // 步橘网条码长度
    public static final Integer BU_JU_SN_LENGTH = 21;
    // 天友条码长度
    public static final Integer TIAN_YOU_SN_LENGTH = 13;
    // 发货宝条码长度
    public static final Integer FA_HUO_BAO_SN_LENGTH = 17;
    // 步橘网条码前缀长度
    public static final Integer BU_JU_SN_PREFIX_LENGTH = 13;
    // 天友条码前缀长度
    public static final Integer TIAN_YOU_SN_PREFIX_LENGTH = 6;
    // 天友条码 公共部分 前缀长度
    public static final Integer TIAN_YOU_SN_COMMON_PREFIX_LENGTH = 4;
    // 发货宝条码前缀长度
    public static final Integer FA_HUO_BAO_SN_PREFIX_LENGTH = 10;
    // 发货宝条码 公共部分 前缀长度
    public static final Integer FA_HUO_BAO_SN_COMMON_PREFIX_LENGTH = 8;
    // 每个档口默认 现金客户
    public static final String STORE_CUS_CASH = "现金客户";

    // 年费暂定3999
    public static final BigDecimal STORE_ANNUAL_AMOUNT = new BigDecimal(3999);
    // 会员费暂定5999
    public static final BigDecimal STORE_MEMBER_AMOUNT = new BigDecimal(5999);

    /**
     * 上市季节年份
     */
    public static final String RELEASE_YEAR_SEASON_DICT = "release_year_season";
    /**
     * ES 索引 product_info
     */
    public static final String ES_IDX_PRODUCT_INFO = "product_info";
    /**
     * 最顶层商品分类ID
     */
    public static final Long TOPMOST_PRODUCT_CATEGORY_ID = 1L;

    /**
     * 最受欢迎的8个推广位
     */
    public static final String ADVERT_POPULAR = "ADVERT_POPULAR";
    /**
     * 支付超时最大时间
     */
    public static final Integer PAY_EXPIRE_MAX_HOURS = 24 * 7;
    /**
     * 以图搜图图片类目
     */
    public static final int IMG_SEARCH_CATEGORY_ID = 4;
    /**
     * 以图搜图匹配分数阈值
     */
    public static final float IMG_SEARCH_MATCH_SCORE_THRESHOLD = (float) 0.6;
    /**
     * 以图搜图默认返回数
     */
    public static final int IMG_SEARCH_DEFAULT_REQUEST_NUM = 30;
    /**
     * 以图搜图接口最大返回数
     */
    public static final int IMG_SEARCH_MAX_PAGE_NUM = 100;
    /**
     * APP 首页广告位置 插入广告的索引位置集合 获取精选热卖推广，将广告嵌入到列表中 每一页20条，5条广告嵌入到 3  7  11  15  19
     */
    public static final Set<Integer> APP_INSERT_POSITIONS = new HashSet<>(Arrays.asList(2, 6, 10, 14, 18));
    /**
     * 以图搜款搜索结果，广告插入位置 5 9 13 17 20
     */
    public static final Set<Integer> PIC_SEARCH_INSERT_POSITIONS = new HashSet<>(Arrays.asList(4, 8, 12, 16, 19));
    /**
     * 档口搜索结果，广告插入位置 2 9 18 27 36
     */
    public static final Set<Integer> STORE_RECOMMEND_INSERT_POSITIONS = new HashSet<>(Arrays.asList(2, 9, 18, 27, 36));
    /**
     * PC 搜索结果，广告插入位置 2 6 10 14 18
     */
    public static final Set<Integer> PC_SEARCH_RESULT_INSERT_POSITIONS = new HashSet<>(Arrays.asList(2, 6, 10, 14, 18));

    public static final String ALIPAY_DEFAULT_FORMAT = "json";

    public static final BigDecimal ZERO_POINT_ONE = BigDecimal.valueOf(0.1);

    public static final BigDecimal ALI_SERVICE_FEE_RATE = BigDecimal.valueOf(0.006);

    public static final String  INNER_MATERIAL = "内里材质";
    public static final String  OUTER_MATERIAL = "里料材质";

    public static final String UPPER_MATERIAL = "upperMaterial";
    public static final String UPPER_MATERIAL_NAME = "帮面材质";
    public static final String SHAFT_LINING_MATERIAL = "shaftLiningMaterial";
    public static final String SHAFT_LINING_MATERIAL_NAME = "靴筒内里材质";
    public static final String SHAFT_MATERIAL = "shaftMaterial";
    public static final String SHAFT_MATERIAL_NAME = "靴筒面材质";
    public static final String SHOE_UPPER_LINING_MATERIAL = "shoeUpperLiningMaterial";
    public static final String SHOE_UPPER_LINING_MATERIAL_NAME = "鞋面内里材质";
    public static final String SHOE_STYLE_NAME = "shoeStyleName";
    public static final String SHOE_STYLE_NAME_NAME = "靴款品名";
    public static final String SHAFT_HEIGHT = "shaftHeight";
    public static final String SHAFT_HEIGHT_NAME = "筒高";
    public static final String INSOLE_MATERIAL = "insoleMaterial";
    public static final String INSOLE_MATERIAL_NAME = "鞋垫材质";
    public static final String RELEASE_YEAR_SEASON = "releaseYearSeason";
    public static final String RELEASE_YEAR_SEASON_NAME = "上市年份季节";
    public static final String HEEL_HEIGHT = "heelHeight";
    public static final String HEEL_HEIGHT_NAME = "后跟高";
    public static final String HEEL_TYPE = "heelType";
    public static final String HEEL_TYPE_NAME = "跟底款式";
    public static final String TOE_STYLE = "toeStyle";
    public static final String TOE_STYLE_NAME = "鞋头款式";
    public static final String SUITABLE_SEASON = "suitableSeason";
    public static final String SUITABLE_SEASON_NAME = "适合季节";
    public static final String COLLAR_DEPTH = "collarDepth";
    public static final String COLLAR_DEPTH_NAME = "开口深度";
    public static final String OUTSOLE_MATERIAL = "outsoleMaterial";
    public static final String OUTSOLE_MATERIAL_NAME = "鞋底材质";
    public static final String STYLE = "style";
    public static final String STYLE_NAME = "风格";
    public static final String DESIGN = "design";
    public static final String DESIGN_NAME = "款式";
    public static final String LEATHER_FEATURES = "leatherFeatures";
    public static final String LEATHER_FEATURES_NAME = "皮质特征";
    public static final String MANUFACTURING_PROCESS = "manufacturingProcess";
    public static final String MANUFACTURING_PROCESS_NAME = "鞋制作工艺";
    public static final String PATTERN = "pattern";
    public static final String PATTERN_NAME = "图案";
    public static final String CLOSURE_TYPE = "closureType";
    public static final String CLOSURE_TYPE_NAME = "闭合方式";
    public static final String OCCASION = "occasion";
    public static final String OCCASION_NAME = "适用场景";
    public static final String THICKNESS = "thickness";
    public static final String THICKNESS_NAME = "厚薄";
    public static final String FASHION_ELEMENTS = "fashionElements";
    public static final String FASHION_ELEMENTS_NAME = "流行元素";
    public static final String SUITABLE_PERSON = "suitablePerson";
    public static final String SUITABLE_PERSON_NAME = "适用对象";

    /**
     * app 类目属性的key value匹配值
     */
    public static final Map<String, String> CATE_RELATE_MAP = new ConcurrentHashMap<String, String>() {{
        put(UPPER_MATERIAL, UPPER_MATERIAL_NAME);
        put(SHAFT_LINING_MATERIAL, SHAFT_LINING_MATERIAL_NAME);
        put(SHAFT_MATERIAL, SHAFT_MATERIAL_NAME);
        put(SHOE_UPPER_LINING_MATERIAL, SHOE_UPPER_LINING_MATERIAL_NAME);
        put(SHOE_STYLE_NAME, SHOE_STYLE_NAME_NAME);
        put(SHAFT_HEIGHT, SHAFT_HEIGHT_NAME);
        put(INSOLE_MATERIAL, INSOLE_MATERIAL_NAME);
        put(RELEASE_YEAR_SEASON, RELEASE_YEAR_SEASON_NAME);
        put(HEEL_HEIGHT, HEEL_HEIGHT_NAME);
        put(HEEL_TYPE, HEEL_TYPE_NAME);
        put(TOE_STYLE, TOE_STYLE_NAME);
        put(SUITABLE_SEASON, SUITABLE_SEASON_NAME);
        put(COLLAR_DEPTH, COLLAR_DEPTH_NAME);
        put(OUTSOLE_MATERIAL, OUTSOLE_MATERIAL_NAME);
        put(STYLE, STYLE_NAME);
        put(DESIGN, DESIGN_NAME);
        put(LEATHER_FEATURES, LEATHER_FEATURES_NAME);
        put(MANUFACTURING_PROCESS, MANUFACTURING_PROCESS_NAME);
        put(PATTERN, PATTERN_NAME);
        put(CLOSURE_TYPE, CLOSURE_TYPE_NAME);
        put(OCCASION, OCCASION_NAME);
        put(THICKNESS, THICKNESS_NAME);
        put(FASHION_ELEMENTS, FASHION_ELEMENTS_NAME);
        put(SUITABLE_PERSON, SUITABLE_PERSON_NAME);
    }};

}
