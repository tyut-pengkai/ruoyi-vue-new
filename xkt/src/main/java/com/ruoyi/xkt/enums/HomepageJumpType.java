package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 刘江
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum HomepageJumpType {

    // 跳转档口
    JUMP_STORE(1, "跳转档口"),
    // 跳转商品
    JUMP_PRODUCT(2, "跳转商品"),
    // 不跳转
    NO_JUMP(10, "不跳转"),

    ;

    private final Integer value;
    private final String label;

    public static HomepageJumpType of(Integer value) {
        for (HomepageJumpType e : HomepageJumpType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
