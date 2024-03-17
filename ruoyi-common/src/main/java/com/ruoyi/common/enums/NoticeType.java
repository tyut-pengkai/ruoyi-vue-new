package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType implements BaseEnum {

    FRONTEND("0", "商城公告(前台)"),
    BACKEND("1", "用户公告(后台)"),
    AGENT("2", "代理公告(后台)"),
    ALL_FOR_USER("3", "全局用户公告(后台)");

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;
}
