package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LimitOper implements BaseEnum {
    TIPS("0", "提示用户"),
    EARLIEST_LOGOUT("1", "注销最早登录的用户");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
