package com.ruoyi.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScriptLanguage implements BaseEnum {

    JAVA_SCRIPT("1", "JavaScript"),
    PYTHON3("2", "Python3"),
    PYTHON2("3", "Python2"),
    PHP("4", "PHP");
    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

}
