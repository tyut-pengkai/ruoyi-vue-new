package com.ruoyi.manager;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.enums.BaseEnum;
import com.ruoyi.enums.BusinessModeEnum;

import java.util.*;
import java.util.stream.Collectors;

public class EnumManager {
    private static final List<Map<String, Object>> ENUM_LIST = new ArrayList<>();
    private static final Map<String, BaseEnum[]> ENUMS = new HashMap<>();

    static {
        buildEnumMap("BusinessModeEnum", BusinessModeEnum.values());
    }

    public static List<Map<String, Object>> getEnumsList() {
        return ENUM_LIST;
    }

    private static void buildEnumMap(String enumName, BaseEnum[] bs) {
        for (BaseEnum b : bs) {
            Map<String, Object> enumMap = new HashMap<>();
            enumMap.put("enumName", enumName);
            enumMap.put("enumValue", b.toString());
            enumMap.put("key", b.getKey());
            enumMap.put("name", b.getName());
            enumMap.put("code", b.getCode());
            ENUM_LIST.add(enumMap);
        }
        ENUMS.put(enumName, bs);
    }

    public static List<Map<String, Object>> getEnum(String enumName) {
        BaseEnum[] baseEnum = ENUMS.get(enumName);
        if (baseEnum == null) {
            throw new ServiceException(enumName + "枚举不存在、请联系管理员！");
        }
        return Arrays.stream(baseEnum).map(be -> {
            Map<String, Object> res = new HashMap<>();
            res.put("key", be.getKey());
            res.put("name", be.getName());
            res.put("code", be.getCode());
            return res;
        }).collect(Collectors.toList());
    }
}
