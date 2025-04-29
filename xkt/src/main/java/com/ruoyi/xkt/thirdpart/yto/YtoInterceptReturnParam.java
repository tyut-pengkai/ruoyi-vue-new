package com.ruoyi.xkt.thirdpart.yto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 18:13
 */
@Data
public class YtoInterceptReturnParam {
    /**
     * 运单号码
     */
    private String waybillNo;
    /**
     * 问题描述
     */
    private String wantedDesc;
}
