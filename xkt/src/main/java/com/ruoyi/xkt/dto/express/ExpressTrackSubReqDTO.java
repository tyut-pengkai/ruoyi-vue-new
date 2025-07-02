package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-16 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressTrackSubReqDTO {
    /**
     * 请求号
     */
    private String expressReqNo;
    /**
     * 物流运单号（快递单号）
     */
    private String expressWaybillNo;
    /**
     * 发货人-电话
     */
    private String originContactPhoneNumber;
    /**
     * 收货人-电话
     */
    private String destinationContactPhoneNumber;
}
