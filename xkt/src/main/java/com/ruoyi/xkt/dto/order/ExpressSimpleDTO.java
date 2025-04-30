package com.ruoyi.xkt.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-30 13:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressSimpleDTO {
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 物流类型[1:平台物流 2:档口物流]
     */
    private Integer expressType;
    /**
     * 物流状态[1:初始 2:下单中 3:已下单 4:取消中 5:已揽件 6:拦截中 99:已结束]
     */
    private Integer expressStatus;
    /**
     * 物流请求单号
     */
    private String expressReqNo;
    /**
     * 物流运单号（快递单号），档口/用户自己填写时可能存在多个，使用“,”分割
     */
    private String expressWaybillNo;

}
