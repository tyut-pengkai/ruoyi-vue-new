package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnbindType implements BaseEnum {
    FRONTEND_UNBIND("0", "用户前台解绑"),
    CALL_API_UNBIND("1", "用户软件解绑(API)"),
    ADMIN_UNBIND("2", "管理员后台解绑");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

