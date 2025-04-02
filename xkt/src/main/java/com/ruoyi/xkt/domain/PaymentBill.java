package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 支付单据
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.533
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentBill extends SimpleEntity {
    /**
     * 单号
     */
    private String billNo;
    /**
     * 单据类型[1:收款 2:付款 3:转移]
     */
    private Integer billType;
    /**
     * 单据状态[1:初始 2:执行中 3:执行成功 4:执行失败]
     */
    private Integer billStatus;
    /**
     * 来源类型[1:代发订单支付 2:代发订单完成 3:提现]
     */
    private Integer srcType;
    /**
     * 来源ID
     */
    private Long srcId;
    /**
     * 关联类型[1:代发订单]
     */
    private Integer relType;
    /**
     * 关联ID
     */
    private Long relId;
    /**
     * 收入内部账户ID
     */
    private Long inputInternalAccountId;
    /**
     * 支出内部账户ID
     */
    private Long outputInternalAccountId;
    /**
     * 收入外部账户ID
     */
    private Long inputExternalAccountId;
    /**
     * 支出外部账户ID
     */
    private Long outputExternalAccountId;
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
     * 版本号
     */
    @Version
    private Long version;
}
