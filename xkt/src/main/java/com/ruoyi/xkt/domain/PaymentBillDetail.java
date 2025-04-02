package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 支付单据明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.550
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentBillDetail extends SimpleEntity {
    /**
     * 支付单据ID
     */
    private Long paymentBillId;
    /**
     * 关联类型[1:代发订单明细]
     */
    private Integer relType;
    /**
     * 关联ID
     */
    private Long relId;
    /**
     * 业务金额
     */
    private BigDecimal businessAmount;
    /**
     * 交易金额
     */
    private BigDecimal transAmount;
    /**
     * 备注
     */
    private String remark;
}
