package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知公告类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum UserNoticeType {

    // 系统消息
    SYSTEM_MSG(1, "系统消息"),
    // 代发订单
    DELIVERY_ORDER(2, "代发订单"),
    // 关注档口
    FOCUS_STORE(3, "关注档口"),
    // 收藏商品
    FAVORITE_PRODUCT(4, "收藏商品"),
    // 推广营销
    ADVERT(5, "推广营销"),
    // 商品动态
    PRODUCT_DYNAMIC(6, "商品动态"),

    ;

    private final Integer value;
    private final String label;

    public static UserNoticeType of(Integer value) {
        for (UserNoticeType e : UserNoticeType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("通知公告类型不存在!", HttpStatus.ERROR);
    }
}
