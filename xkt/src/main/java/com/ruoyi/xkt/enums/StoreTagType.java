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

    // 实力质造、旗舰工厂

    // 月销千件
    MONTH_SALES_THOUSAND(20, "月销千件"),
    // 销量榜
    SALES_RANK(30, "销量榜"),
    // 爆款频出
    HOT_RANK(40, "爆款频出"),
    // 新款频出
    NEW_PRODUCT(50, "新款频出"),
    // 优质代发
    QUALITY_AGENT(60, "优质代发"),
    // 关注榜
    ATTENTION_RANK(70, "关注榜"),
    // 七日上新
    SEVEN_DAY_NEW_RANK(80, "七日上新"),
    // 库存榜
    STOCK_RANK(90, "库存榜"),

    /*
    // 经营年限榜
    OPERATE_YEARS_RANK(100, "经营年限榜"),
    // 基础信息榜
    BASIC_RANK(100, "基础榜"),
    // 收藏榜
    COLLECTION_RANK(5, "收藏榜"),*/

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
