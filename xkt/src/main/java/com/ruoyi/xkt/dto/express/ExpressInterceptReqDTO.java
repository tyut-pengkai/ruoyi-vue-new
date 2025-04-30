package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-29 17:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressInterceptReqDTO {
    /**
     * 请求号
     */
    private String expressReqNo;
    /**
     * 运单号
     */
    private String expressWaybillNo;
}
