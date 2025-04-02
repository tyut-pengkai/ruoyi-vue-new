package com.ruoyi.xkt.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付单据明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.550
 **/
@Data
public class PaymentBillDetailDTO {
    /**
     * 支付单据明细ID
     */
    private Long id;
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
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
