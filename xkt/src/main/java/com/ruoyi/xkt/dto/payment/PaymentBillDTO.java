package com.ruoyi.xkt.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付单据
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.533
 **/
@Data
public class PaymentBillDTO {
    /**
     * 支付单据ID
     */
    private Long id;
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
    /**
     * 版本号
     */
    private Long version;
}
