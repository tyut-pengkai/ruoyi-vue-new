package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品图包压缩类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum StoreProdFileZipStatus {

    NON_ZIP(1, "非图包/不处理"),
    PENDING(2, "待处理"),
    PROCESSING(3, "处理中"),
    PROCESSED(4, "已处理"),
    FAILED(5, "处理异常");

    private final Integer value;
    private final String label;

    // 根据 value 获取对应的枚举实例
    public static StoreProdFileZipStatus of(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("图包状态值不能为空");
        }
        for (StoreProdFileZipStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的图包状态值: " + value);
    }
}
