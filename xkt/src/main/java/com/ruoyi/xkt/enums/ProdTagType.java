package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 档口标签类型
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum ProdTagType {

    // 月销千件
    MONTH_SALES_THOUSAND(10, "月销千件"),
    // 销量榜
    SALES_RANK(20, "销量榜"),
    // 当月爆款
    MONTH_HOT(30, "当月爆款"),
    // 档口热卖
    STORE_HOT(40, "档口热卖"),
    // 图搜榜
    IMG_SEARCH_RANK(50, "图搜榜"),
    // 收藏榜
    COLLECTION_RANK(60, "收藏榜"),
    // 下载量
    DOWNLOAD_RANK(70, "下载量"),
    // 三日上新
    THREE_DAY_NEW(80, "三日上新"),
    // 七日上新
    SEVEN_DAY_NEW(90, "七日上新"),
    // 风格
    STYLE(100, "风格"),


    ;


    private final Integer value;
    private final String label;

    public static ProdTagType of(Integer value) {
        for (ProdTagType e : ProdTagType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("档口标签类型不存在!", HttpStatus.ERROR);
    }
}
