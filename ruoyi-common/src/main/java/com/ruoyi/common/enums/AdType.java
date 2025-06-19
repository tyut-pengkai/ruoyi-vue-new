package com.ruoyi.common.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdType {

    // 顶部横向大图
    PC_HOME_TOP_LEFT_BANNER(1, "顶部横向大图"),
    // 顶部纵向小图
    PC_HOME_TOP_RIGHT_BANNER(2, "顶部纵向小图"),
    // 首页销售榜榜一
    PC_HOME_SALE_RANK_ONE(3, "销售榜榜一"),
    // 首页销售榜榜二
    PC_HOME_SALE_RANK_TWO(4, "销售榜榜二"),
    // 首页销售榜榜三
    PC_HOME_SALE_RANK_THREE(5, "销售榜榜三"),
    // 首页销售榜榜四
    PC_HOME_SALE_RANK_FOUR(6, "销售榜榜四"),
    // 风格榜
    PC_HOME_STYLE_RANK(7, "风格榜"),
    // 人气榜左侧大图
    PC_HOME_POP_LEFT_BANNER(8, "人气榜左大图"),
    // 人气榜中间图
    PC_HOME_POP_MID(9, "人气榜中间图"),
    // 人气榜右侧图
    PC_HOME_POP_RIGHT(10, "人气榜右侧图"),


    // 首页档口横幅
    PC_HOME_SINGLE_BANNER(11, "首页档口横幅"),
    // 首页商品列表
    PC_HOME_PRODUCT_LIST(12, "首页商品列表"),


    // 首页两侧固定挂耳
    PC_HOME_FIXED_EAR(13, "首页两侧固定挂耳"),
    // 首页搜索框下名称
    PC_HOME_SEARCH_DOWN_NAME(14, "首页搜索框下档口名称"),
    // 首页搜索框中推荐商品
    PC_HOME_SEARCH_PRODUCT(15, "首页搜索框中推荐商品"),


    // 新品馆顶部横向大图
    PC_NEW_PROD_TOP_LEFT_BANNER(101, "新品馆顶部横向大图"),
    // 新品馆顶部纵向小图
    PC_NEW_PROD_TOP_RIGHT(102, "新品馆顶部纵向小图"),
    // 新品馆品牌榜
    PC_NEW_PROD_BRAND_BANNER(103, "新品馆品牌榜"),
    // 新品馆热卖榜左大图
    PC_NEW_PROD_HOT_LEFT_BANNER(104, "新品馆热卖榜左大图"),
    // 新品馆热卖榜右推广商品
    PC_NEW_PROD_HOT_RIGHT_PRODUCT(105, "新品馆热卖榜右推广商品"),
    // 新品馆横幅
    PC_NEW_PROD_SINGLE_BANNER(106, "新品馆横幅"),
    // 新品馆商品列表
    PC_NEW_PROD_PRODUCT_LIST(107, "新品馆商品列表"),

    // 档口馆 顶部轮播图
    PC_STORE_TOP_BANNER(201, "档口馆顶部轮播图"),
    // 档口馆 横幅
    PC_STORE_MID_BANNER(202, "档口馆横幅"),
    // 档口馆 推荐档口
    PC_STORE_RECOMMEND(203, "档口馆推荐档口"),


    // 首页以图搜款框商品、以图搜款结果商品、点击以图搜款界面
    PIC_SEARCH_PRODUCT(300, "以图搜款商品"),


    // PC搜索结果
    PC_SEARCH_RESULT(401, "电脑端搜索结果"),
    // PC用户中心 18个位置
    PC_USER_CENTER(402, "电脑端用户中心"),
    // PC下载页
    PC_DOWNLOAD(403, "电脑端下载页"),


    // APP首页顶部轮播图
    APP_HOME_TOP_BANNER(501, "APP首页顶部轮播图"),
    // APP首页品牌好货
    APP_HOME_RECOMMEND_PRODUCT(502, "APP首页品牌好货"),
    // APP首页热卖推荐 轮播商品
    APP_HOME_HOT_RECOMMEND_FIXED_PROD(503, "APP首页热卖推荐固定轮播商品"),
    // APP首页热卖推荐 商品列表
    APP_HOME_HOT_RECOMMEND_PROD(504, "APP首页热卖推荐"),
    // APP首页人气榜 商品列表
    APP_HOME_POP_RECOMMEND_PROD(505, "APP首页人气榜"),
    // APP首页新品榜 商品列表
    APP_HOME_NEW_PROD_RECOMMEND_PROD(506, "APP首页新品榜"),
    // APP搜索结果
    APP_SEARCH_RESULT(507, "APP搜索结果"),


    // APP分类页轮播图
    APP_CATEGORY_TOP_BANNER(601, "APP分类页轮播图"),
    // APP个人中心猜你喜欢
    APP_USER_CENTER_GUESS_YOU_LIKE(602, "APP我的猜你喜欢"),


    ;

    private final Integer value;
    private final String label;

    public static AdType of(Integer value) {
        for (AdType e : AdType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广推广类型不存在!", HttpStatus.ERROR);
    }
}
