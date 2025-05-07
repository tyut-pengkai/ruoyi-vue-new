package com.ruoyi.xkt.thirdpart.yto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 18:11
 */
@Data
public class YtoSubTrackParam {
    /**
     * 客户端id, 由物流公司提供，物流公司开通新渠道后获得
     */
    private String client_id;
    /**
     * 请求体
     */
    private String logistics_interface;
    /**
     * 类型(一般填写online)
     */
    private String msg_type;

    @Data
    public static class LogisticsInterface{
        /**
         * 客户端id, 由物流公司提供，物流公司开通新渠道后获得
         */
        private String clientId;
        /**
         * 运单号
         */
        private String waybillNo;
        /**
         * 订单号
         */
        private String txLogisticId;
    }

}
