package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppUserExpireChangeType implements BaseEnum {
    RECHARGE("1", "用户充值"),
    OTHOR_IN("2", "其他增加"),
    OTHOR_OUT("3", "其他扣减"),
    UNBIND("4", "用户解绑"),
    CALL_API("5", "API扣减"),
    ADMIN_UPDATE("6", "管理调整"),
    APP_LOGIN("7", "软件登录");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}

