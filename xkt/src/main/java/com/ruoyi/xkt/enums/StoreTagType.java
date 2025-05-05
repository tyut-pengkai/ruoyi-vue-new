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
public enum StoreTagType {

    // 销量榜
    SALES_RANK(1, "销量榜"),
    // 爆款频出
    HOT_RANK(2, "爆款频出"),
    // 新款频出
    NEW_PRODUCT(3, "新款频出"),
    // 关注榜
    ATTENTION_RANK(4, "关注榜"),
    // 收藏榜
    COLLECTION_RANK(5, "收藏榜"),
    // 库存榜
    STOCK_RANK(6, "库存榜"),
    // 经营年限榜
    OPERATE_YEARS_RANK(7, "经营年限榜"),
    // 七日上新
    SEVEN_DAY_NEW_RANK(8, "七日上新"),
    // 基础信息榜
    BASIC_RANK(100, "基础榜"),


    ;


    private final Integer value;
    private final String label;

    public static StoreTagType of(Integer value) {
        for (StoreTagType e : StoreTagType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("档口标签类型不存在!", HttpStatus.ERROR);
    }
}
