package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchOperationAppUserType implements BaseEnum {

    ALL("0", "全部用户"),
    VIP("1", "VIP用户"),
    NO_VIP("2", "非VIP用户"),
    ;

    @EnumValue // 用于dao层序列化与反序列化
    @JsonValue // 用于controller层序列化
    private final String code;
    private final String info;

}
