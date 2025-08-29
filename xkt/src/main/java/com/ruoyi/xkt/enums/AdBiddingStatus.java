package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
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

    // 未出价
    UN_BIDDING(0, "未出价"),
    // 已出价
    BIDDING(1, "已出价"),
    // 竞价成功
    BIDDING_SUCCESS(2, "竞价成功"),
    // 竞价失败
    BIDDING_FAIL(3, "竞价失败"),

    ;

    private final Integer value;
    private final String label;

    public static AdBiddingStatus of(Integer value) {
        for (AdBiddingStatus e : AdBiddingStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("营销推广竞价状态不存在!", HttpStatus.ERROR);
    }
}
