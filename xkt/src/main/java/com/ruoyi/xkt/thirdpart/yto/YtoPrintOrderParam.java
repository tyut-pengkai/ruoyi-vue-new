package com.ruoyi.xkt.thirdpart.yto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-15 19:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YtoPrintOrderParam {
    /**
     * 圆通运单号
     */
    private String waybillNo;
    /**
     * 备注，最多130个字符
     */
    private String remark;
}
