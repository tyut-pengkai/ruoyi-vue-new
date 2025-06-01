package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum HomepageType {

    // 轮播大图
    SLIDING_PICTURE(1, "轮播大图"),
    // 轮播小图
    SLIDING_PICTURE_SMALL(2, "轮播小图"),
    // 店家推荐
    STORE_RECOMMENDED(3, "店家推荐"),
    // 人气爆款
    POPULAR_SALES(4, "人气爆款"),
    // 当季新品
    SEASON_NEW_PRODUCTS(5, "当季新品"),
    // 销量排行
    SALES_RANKING(6, "销量排行"),

    ;

    private final Integer value;
    private final String label;

    public static HomepageType of(Integer value) {
        for (HomepageType e : HomepageType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
