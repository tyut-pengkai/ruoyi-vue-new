package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillType {

    TIME("0", "计时模式"), POINT("1", "计点模式");

    private final String code;
    private final String info;

}
