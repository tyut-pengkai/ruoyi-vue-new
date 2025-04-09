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

    // 不跳转
    NO_JUMP(1, "不跳转"),
    // 跳转店铺
    JUMP_STORE(2, "跳转店铺"),
    // 跳转商品
    JUMP_PRODUCT(3, "跳转商品"),

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
