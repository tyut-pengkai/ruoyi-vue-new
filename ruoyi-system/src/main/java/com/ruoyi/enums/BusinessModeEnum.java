package com.ruoyi.enums;


import com.ruoyi.common.utils.StringUtils;

public enum BusinessModeEnum implements BaseEnum {
    WMS(1, "仓库管理", "WMS"),
    ;
    final Integer key;
    final String name;
    final String code;

    BusinessModeEnum(Integer key, String name, String code) {
        this.key = key;
        this.name = name;
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    public static BusinessModeEnum getEnumByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (BusinessModeEnum enums : BusinessModeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }
}
