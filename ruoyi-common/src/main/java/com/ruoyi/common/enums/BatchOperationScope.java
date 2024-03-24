package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchOperationScope implements BaseEnum {

    ALL("0", "全部范围"),
    //    VIP("1", "未到期或点数＞0"),
//    NO_VIP("2", "已到期或点数＜=0"),
    USED("3", "已使用"),
    UNUSED("4", "未使用"),
    BANNED("5", "已冻结"),
    UNBANNED("6", "未冻结"),
    ON_SALE("7", "已上架"),
    OFF_SALE("8", "未上架"),
    ;

    @EnumValue // 用于dao层序列化与反序列化
    @JsonValue // 用于controller层序列化
    private final String code;
    private final String info;

}
