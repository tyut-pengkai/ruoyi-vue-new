package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

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
    MAIN_PIC_450(4, "商品下载图片包450px"),
    MAIN_PIC_750(5, "商品下载图片包750px"),
    ID_CARD_FACE(6, "身份证人脸"),
    ID_CARD_EMBLEM(7, "身份证国徽"),
    BUSINESS_LICENSE(8, "档口营业执照"),

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

    public static List<Integer> picPackValues() {
        return Arrays.asList(DOWNLOAD.getValue(), MAIN_PIC_450.getValue(), MAIN_PIC_750.getValue());
    }
}
