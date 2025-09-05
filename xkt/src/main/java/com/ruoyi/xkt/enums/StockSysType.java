package com.ruoyi.xkt.enums;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存系统枚举
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum StockSysType {

    // 步橘网
    BU_JU(1, "步橘网"),
    // 天友
    TIAN_YOU(2, "天友"),
    // 发货宝
    FA_HUO_BAO(3, "发货宝"),


    ;

    private final Integer value;
    private final String label;

    public static StockSysType of(Integer value) {
        for (StockSysType e : StockSysType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new ServiceException("库存系统枚举不存在!", HttpStatus.ERROR);
    }
}
