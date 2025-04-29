package com.ruoyi.xkt.dto.express;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 17:57
 */
@Data
public class ExpressCancelReqDTO {
    /**
     * 请求号
     */
    private String expressReqNo;
    /**
     * 运单号
     */
    private String expressWaybillNo;
}
