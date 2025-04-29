package com.ruoyi.xkt.thirdpart.yto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 18:13
 */
@Data
public class YtoCancelOrderParam {
    /**
     * 物流单号，打印拉取运单号前，物流单号和渠道唯一确定一笔快递物流订单。
     */
    private String logisticsNo;
    /**
     * 取消原因
     */
    private String cancelDesc;
}
