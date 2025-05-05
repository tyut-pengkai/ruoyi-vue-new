package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销竞价状态
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdBiddingStatus {

    // 已出价
    BIDDING(1, "已出价"),
    // 竞价失败
    BIDDING_FAIL(2, "竞价失败"),
    // 竞价成功
    BIDDING_SUCCESS(3, "竞价成功"),

    ;

    private final Integer value;
    private final String label;

    public static AdBiddingStatus of(Integer value) {
        for (AdBiddingStatus e : AdBiddingStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
