package com.ruoyi.xkt.thirdpart.zto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-15 19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZtoPrintOrderParam {
    /**
     * 打印类型(0:JPG, 1:PDF.默认为PDF)
     */
    private Integer printType;
    /**
     * 运单号
     */
    private String billCode;
    /**
     * 是否打印中通logo(true:打印, false:不打印.默认不打印)
     */
    private Boolean printLogo;
}
