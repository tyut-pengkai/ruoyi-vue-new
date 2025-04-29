package com.ruoyi.xkt.thirdpart.zto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 18:11
 */
@Data
public class ZtoCancelOrderParam {
    /**
     * 取消类型 1不想寄了,2下错单,3重复下单,4运费太贵,5无人联系,6取件太慢,7态度差
     */
    private String cancelType;
    /**
     * 预约件订单号（orderCode与billCode必传其一）
     */
    private String orderCode;
    /**
     * 运单号（orderCode与billCode必传其一）
     */
    private String billCode;
}
