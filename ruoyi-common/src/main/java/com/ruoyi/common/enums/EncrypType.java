package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EncrypType implements BaseEnum {

    NONE("0", "明文传输"),
    BASE64("1", "BASE64"),
    AES_CBC_PKCS5Padding("2", "AES_CBC_PKCS5Padding"),
    AES_CBC_ZeroPadding("3", "AES_CBC_ZeroPadding"),
    AES_CBC_NoPadding("4", "AES_CBC_NoPadding");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
