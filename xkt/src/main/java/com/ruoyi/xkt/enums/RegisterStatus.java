package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登记状态
 * @author 刘江
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum RegisterStatus {

    IN_EXISTENCE(1, "存续（在营、开业、在册）"),
    REVOKED_NOT_DEREGISTER(2, "吊销，未注销"),
    REVOKED_DEREGISTER(3, "吊销，已注销"),
    DEREGISTER(4, "注销"),
    REVOKED(5, "撤销"),
    MOVED_OUT(6, "迁出"),
    OTHER(7, "其它"),

    ;

    private final Integer value;
    private final String label;

    public static RegisterStatus of(Integer value) {
        for (RegisterStatus e : RegisterStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
