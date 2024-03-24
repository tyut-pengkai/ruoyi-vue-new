package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchOperationType implements BaseEnum {

    QUERY("0", "查询"),
    BAN("1", "冻结"),
    UNBAN("2", "解冻"),
    UNBIND("3", "解绑"),
    DELETE("4", "删除"),
    ADD_TIME("5", "加时"),
    SUB_TIME("6", "扣时"),
    ADD_POINT("7", "加点"),
    SUB_POINT("8", "扣点"),
    REMARK("9", "备注"),
    ON_SALE("10", "上架"),
    OFF_SALE("11", "下架");

    @EnumValue // 用于dao层序列化与反序列化
    @JsonValue // 用于controller层序列化
    private final String code;
    private final String info;

}
