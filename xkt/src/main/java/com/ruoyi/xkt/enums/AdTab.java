package com.ruoyi.xkt.enums;

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

    // 首页
    HOME(1, "首页"),
    // 新品馆
    NEW_PROD(2, "新品馆"),
    // 以图搜款
    PIC_SEARCH(3, "以图搜款"),
    // 搜索结果
    SEARCH_RESULT(4, "搜索结果"),
    // 用户中心
    USER_CENTER(5, "用户中心"),
    // 下载页
    DOWNLOAD(6, "下载页"),
    // 分类页
    CATEGORY(7, "分类页"),

    ;

    private final Integer value;
    private final String label;

    public static AdTab of(Integer value) {
        for (AdTab e : AdTab.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
