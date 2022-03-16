package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType implements BaseEnum {

    FRONTEND("0", "前台公告"),
    BACKEND("1", "后台公告");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
