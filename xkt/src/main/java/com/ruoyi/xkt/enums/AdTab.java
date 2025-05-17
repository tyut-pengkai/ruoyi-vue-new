package com.ruoyi.xkt.enums;

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
public enum AdTab {

    // PC 首页
    PC_HOME(1, "首页"),
    // PC 新品馆
    PC_NEW_PROD(2, "新品馆"),
    // PC 以图搜款
    PC_PIC_SEARCH(3, "以图搜款"),
    // PC 搜索结果
    PC_SEARCH_RESULT(4, "搜索结果/用户中心/下载页"),
    // PC 档口馆
    PC_STORE(5, "档口馆"),

    // APP 首页
    APP_HOME(7, "APP首页"),
    // APP 分类页
    APP_CATEGORY(8, "APP分类页"),
    // APP 搜索结果
    APP_SEARCH_RESULT(9, "APP热卖精选 右侧固定商品"),
    // APP 我的 猜你喜欢
    APP_USER_CENTER_GUESS_YOU_LIKE(10, "APP我的 猜你喜欢"),

    ;

    private final Integer value;
    private final String label;

    public static AdTab of(Integer value) {
        for (AdTab e : AdTab.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("推广营销TAB不存在!", HttpStatus.ERROR);
    }
}
