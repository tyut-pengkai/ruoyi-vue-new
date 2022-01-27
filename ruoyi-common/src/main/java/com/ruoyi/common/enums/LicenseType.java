package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LicenseType implements BaseEnum {

    NO_LICENCE("0", "未授权"), //
    TRAIL("1", "体验版"), //1个软件位
    PERSONAL("2", "个人版"), //
    TEAM("3", "团队版"),
    COMPANY("4", "企业版");
    @EnumValue // 用于dao层序列化与反序列化
    @JsonValue // 用于controller层序列化
    private final String code;
    private final String info;

    public static LicenseType valueOfCode(String code) {
        for (LicenseType lt : LicenseType.values()) {
            if (lt.getCode().equals(code)) {
                return lt;
            }
        }
        return NO_LICENCE;
    }

}
