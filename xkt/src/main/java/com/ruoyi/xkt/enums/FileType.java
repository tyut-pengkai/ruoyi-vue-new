package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum FileType {

    MAIN_PIC(1, "商品主图"),
    MAIN_PIC_VIDEO(2, "商品主图视频"),
    DOWNLOAD(3, "商品下载图片包"),
    ID_CARD_FACE(4, "身份证人脸"),
    ID_CARD_EMBLEM(5, "身份证国徽"),
    BUSINESS_LICENSE(6, "档口营业执照"),

    ;

    private final Integer value;
    private final String label;

    public static FileType of(Integer value) {
        for (FileType e : FileType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
