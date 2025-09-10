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
     * 登录用户信息
     */
    public static final String LOGIN_USER_KEY = "login_users:";


    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 短信登录/注册验证码 redis key
     */
    public static final String SMS_LOGIN_CAPTCHA_CODE_KEY = "sms_login_captcha_codes:";

    /**
     * 资产绑定验证码 redis key
     */
    public static final String SMS_ASSET_CAPTCHA_CODE_KEY = "sms_asset_captcha_codes:";

    /**
     * 短信验证码CD中号码
     */
    public static final String SMS_ASSET_CAPTCHA_CODE_CD_PHONE_NUM_KEY = "sms_asset_captcha_code_cd_phone_nums:";
    public static final String SMS_LOGIN_CAPTCHA_CODE_CD_PHONE_NUM_KEY = "sms_login_captcha_code_cd_phone_nums:";
    public static final String SMS_REGISTER_CAPTCHA_CODE_CD_PHONE_NUM_KEY = "sms_register_captcha_code_cd_phone_nums:";

    /**
     * 扫码登录浏览器ID
     */
    public static final String SCAN_CODE_LOGIN_BROWSER_ID_KEY = "scan_code_login_browser_ids:";

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
     * 用户STS
     */
    public static final String USER_STS_KEY = "user_sts:";

    /**
     * 退款处理中标识
     */
    public static final String STORE_ORDER_REFUND_PROCESSING_MARK = "store_order_refund_processing_mark_";

    /**
     * 档口
     */
    public static final String STORE_KEY = "store:";

    /**
     * 推广购买截止时间
     */
    public static final String ADVERT_DEADLINE_KEY = "advert_deadline:";

    /**
     * 推广上传推广图或上传商品截止时间
     */
    public static final String ADVERT_UPLOAD_FILTER_TIME_KEY = "advert_upload_filter_time:";

    /**
     * PC 广告
     */
    public static final String PC_ADVERT = "pc_advert:";

    /**
     * PC 首页 顶部通栏
     */
    public static final String PC_ADVERT_INDEX_TOP = "pc_index_top";
    /**
     * PC 搜索结果 广告列表
     */
    public static final String PC_SEARCH_RESULT_ADVERT = "pc_search_result_advert";
    /**
     * PC 首页 顶部左侧轮播图
     */
    public static final String PC_ADVERT_INDEX_TOP_LEFT = "pc_index_top_left";
    /**
     * PC 首页 顶部右侧轮播图
     */
    public static final String PC_ADVERT_INDEX_TOP_RIGHT = "pc_index_top_right";
    /**
     * PC 首页 中部 销售榜
     */
    public static final String PC_ADVERT_INDEX_MID_SALE = "pc_index_mid_sale";
    /**
     * PC 首页 中部 风格榜
     */
    public static final String PC_ADVERT_INDEX_MID_STYLE = "pc_index_mid_style";
    /**
     * PC 首页 底部 人气榜
     */
    public static final String PC_ADVERT_INDEX_BOTTOM_POPULAR = "pc_index_bottom_popular";
    /**
     * PC 首页 两侧固定挂耳
     */
    public static final String PC_ADVERT_INDEX_FIXED_EAR = "pc_index_fixed_ear";
    /**
     * PC 首页 搜索框下档口名称
     */
    public static final String PC_ADVERT_INDEX_SEARCH_UNDERLINE_STORE_NAME = "pc_index_search_underline_store_name";
    /**
     * PC 首页 搜索框中推荐商品
     */
    public static final String PC_ADVERT_INDEX_SEARCH_RECOMMEND_PROD = "pc_index_search_recommend_prod";
    /**
     * PC 新品馆 顶部左侧轮播图
     */
    public static final String PC_ADVERT_NEW_TOP_LEFT = "pc_new_top_left";
    /**
     * PC 新品馆 顶部右侧图
     */
    public static final String PC_ADVERT_NEW_TOP_RIGHT = "pc_new_top_right";
    /**
     * PC 新品馆 中部品牌榜
     */
    public static final String PC_ADVERT_NEW_MID_BRAND = "pc_new_mid_brand";
    /**
     * PC 新品馆 中部热卖榜左侧
     */
    public static final String PC_ADVERT_NEW_MID_HOT_LEFT = "pc_new_mid_hot_left";
    /**
     * PC 新品馆 中部热卖榜右侧
     */
    public static final String PC_ADVERT_NEW_MID_HOT_RIGHT = "pc_new_mid_hot_right";
    /**
     * PC 新品馆 底部横幅
     */
    public static final String PC_ADVERT_NEW_BOTTOM_BANNER = "pc_new_bottom_banner";
    /**
     * PC 档口馆 顶部banner
     */
    public static final String PC_ADVERT_STORE_TOP_BANNER = "pc_store_top_banner";
    /**
     * PC 档口馆 中间横幅
     */
    public static final String PC_ADVERT_STORE_MID_BANNER = "pc_store_mid_banner";
    /**
     * 以图搜款
     */
    public static final String PIC_SEARCH = "pic_search";
    /**
     * PC 用户中心
     */
    public static final String PC_USER_CENTER = "pc_user_center";
    /**
     * PC 下载页
     */
    public static final String PC_DOWNLOAD = "pc_download";
    /**
     * PC 首页 为你推荐
     */
    public static final String PC_INDEX_RECOMMEND = "pc_index_recommend";
    /**
     * PC 新品馆 为你推荐
     */
    public static final String PC_NEW_RECOMMEND = "pc_new_recommend";

    /**
     * APP 广告
     */
    public static final String APP_ADVERT = "app_advert:";
    /**
     * APP 首页顶部轮播图
     */
    public static final String APP_INDEX_TOP_BANNER = "app_index_top_banner";
    /**
     * APP 首页中部品牌好货
     */
    public static final String APP_INDEX_MID_BRAND = "app_index_mid_brand";
    /**
     * APP 首页热卖精选 右侧固定位置
     */
    public static final String APP_INDEX_HOT_SALE_RIGHT_FIX = "app_index_hot_sale_right_fix";
    /**
     * APP 首页精选热卖推广
     */
    public static final String APP_INDEX_HOT_SALE_ADVERT = "app_index_hot_sale_advert";
    /**
     * APP 首页人气爆品推广
     */
    public static final String APP_INDEX_POPULAR_SALE_ADVERT = "app_index_popular_sale_advert";
    /**
     * APP 首页 新品榜
     */
    public static final String APP_INDEX_NEW_PROD = "app_index_new_prod";
    /**
     * APP 搜索
     */
    public static final String APP_SEARCH = "app_search";
    /**
     * APP 分类页
     */
    public static final String APP_CATE = "app_cate";
    /**
     * APP 我的猜你喜欢
     */
    public static final String APP_OWN_GUESS_LIKE = "app_own_guess_like";
    /**
     * 商品图搜次数统计
     */
    public static final String PRODUCT_STATISTICS_IMG_SEARCH_COUNT = "product_statistics_img_search_count";
    /**
     * 商品浏览量统计
     */
    public static final String PRODUCT_STATISTICS_VIEW_COUNT = "product_statistics_view_count";
    /**
     * 商品下载量统计
     */
    public static final String PRODUCT_STATISTICS_DOWNLOAD_COUNT = "product_statistics_download_count";
    /**
     * 图搜热款
     */
    public static final String IMG_SEARCH_PRODUCT_HOT = "img_search_product_hot";
    /**
     * 所有的图搜热款
     */
    public static final String IMG_SEARCH_PRODUCT_HOT_TOTAL = "img_search_product_hot_total";
    /**
     * 档口馆 档口推荐列表
     */
    public static final String PC_STORE_RECOMMEND_LIST = "pc_store_recommend_list";
    /**
     * 档口馆 档口推荐广告
     */
    public static final String PC_STORE_RECOMMEND_ADVERT = "pc_store_recommend_advert";
    /**
     * 用户搜索历史
     */
    public static final String USER_SEARCH_HISTORY = "user_search_history:";
    /**
     * 用户浏览足迹
     */
    public static final String USER_BROWSING_HISTORY = "user_browsing_history:";
    /**
     * 系统前20条热搜
     */
    public static final String SEARCH_HOT_KEY = "search_hot_key";
    /**
     * 所有的推广
     */
    public static final String ADVERT_KEY = "advert:";
    /**
     * 档口会员
     */
    public static final String STORE_MEMBER = "store_member:";
    /**
     * OCR缓存
     */
    public static final String OCR_CACHE = "ocr_cache:";
    /**
     * 档口商品销售top100
     */
    public static final String TOP_50_SALE_PROD = "top_50_sale_prod";
    /**
     * 商品分类销量前100
     */
    public static final String CATE_TOP_50_SALE_PROD = "cate_top_50_sale_prod";
    /**
     * 图包用户请求次数缓存
     */
    public static final String PIC_PACK_USER_REQ_COUNT_CACHE = "pic_pack_user_req_count_cache:";
    /**
     * 充值单号缓存
     */
    public static final String RECHARGE_BILL_NO_CACHE = "recharge_bill_no_cache:";


}
