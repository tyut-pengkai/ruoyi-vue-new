package com.ruoyi.common.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销类型
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdType {

    // 顶部横向大图
    PC_HOME_TOP_LEFT_BANNER(1, "顶部横向大图", "/url"),
    // 顶部纵向小图
    PC_HOME_TOP_RIGHT_BANNER(2, "顶部纵向小图", "/url"),
    // 人气榜左侧大图
    PC_HOME_POP_LEFT_BANNER(3, "人气榜左大图", "/url"),
    // 人气榜中上侧
    PC_HOME_POP_MID_TOP(4, "人气榜中上侧", "/url"),
    // 人气榜中下侧
    PC_HOME_POP_MID_BOTTOM(5, "人气榜中下侧", "/url"),
    // 人气榜右上侧
    PC_HOME_POP_RIGHT_TOP(6, "人气榜右上侧", "/url"),
    // 人气榜右下侧
    PC_HOME_POP_RIGHT_BOTTOM(7, "人气榜右下侧", "/url"),
    // 首页档口横幅
    PC_HOME_SINGLE_BANNER(8, "首页档口横幅", "/url"),
    // 首页商品列表
    PC_HOME_PRODUCT_LIST(9, "首页商品列表", "/url"),
    // 首页两侧固定挂耳
    PC_HOME_FIXED_EAR(10, "首页两侧固定挂耳", "/url"),
    // 首页搜索框下名称
    PC_HOME_SEARCH_DOWN_NAME(11, "首页搜索框下名称", "/url"),
    // 首页搜索框商品
    PC_HOME_SEARCH_PRODUCT(12, "首页搜索框商品", "/url"),
    // 首页搜索框档口
    PC_HOME_SEARCH_STORE(13, "首页搜索框档口", "/url"),
    // 首页以图搜款商品
    PC_HOME_PIC_SEARCH_PRODUCT(14, "首页以图搜款商品", "/url"),


    // 新品馆顶部横向大图
    PC_NEW_PROD_TOP_LEFT_BANNER(30, "新品馆顶部横向大图", "/url"),
    // 新品馆顶部纵向大图
    PC_NEW_PROD_TOP_RIGHT(31, "新品馆顶部纵向大图", "/url"),
    // 新品馆品牌榜
    PC_NEW_PROD_BRAND_BANNER(32, "新品馆品牌榜", "/url"),
    // 新品馆热卖榜左大图
    PC_NEW_PROD_HOT_LEFT_BANNER(33, "新品馆热卖榜左大图", "/url"),
    // 新品馆热卖榜右推广商品
    PC_NEW_PROD_HOT_RIGHT_PRODUCT(34, "新品馆热卖榜右推广商品", "/url"),
    // 新品馆横幅
    PC_NEW_PROD_SINGLE_BANNER(35, "新品馆横幅", "/url"),
    // 新品馆商品列表
    PC_NEW_PROD_PRODUCT_LIST(36, "新品馆商品列表", "/url"),

    // PC搜索结果
    PC_SEARCH_RESULT(40, "PC搜索结果", "/url"),
    // PC用户中心
    PC_USER_CENTER(41, "PC用户中心", "/url"),
    // PC下载页
    PC_DOWNLOAD(42, "PC下载页", "/url"),


    // APP首页顶部轮播图
    APP_HOME_TOP_BANNER(50, "APP首页顶部轮播图", "/url"),
    // APP首页推荐商品区
    APP_HOME_RECOMMEND_PRODUCT(51, "APP首页推荐商品区", "/url"),
    // APP首页热卖推荐
    APP_HOME_HOT_RECOMMEND(52, "APP首页热卖推荐", "/url"),
    // APP首页人气榜
    APP_HOME_POP_RECOMMEND(53, "APP首页人气榜", "/url"),
    // APP首页新品榜
    APP_HOME_NEW_PROD_RECOMMEND(54, "APP首页新品榜", "/url"),
    // APP搜索结果
    APP_SEARCH_RESULT(55, "APP搜索结果", "/url"),


    // APP分类页轮播图
    APP_CATEGORY_TOP_BANNER(70, "APP分类页轮播图", "/url"),
    // APP个人中心猜你喜欢
    APP_USER_CENTER_GUESS_YOU_LIKE(71, "APP个人中心猜你喜欢", "/url"),


    // 以图搜款结果推广商品
    PIC_SEARCH_RESULT_PRODUCT(80, "以图搜款结果推广商品", "/url"),



    ;

    private final Integer value;
    private final String label;
    private final String demoUrl;

    public static AdType of(Integer value) {
        for (AdType e : AdType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广推广类型不存在!", HttpStatus.ERROR);
    }
}
