package com.ruoyi.xkt.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部账户交易明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.516
 **/
@Data
public class InternalAccountTransDetailDTO {
    /**
     * 内部账户交易明细ID
     */
    private Long id;
    /**
     * 内部账户ID
     */
    private Long internalAccountId;
    /**
     * 来源单据ID
     */
    private Long srcBillId;
    /**
     * 来源单据类型[1:收款 2:付款 3:转移]
     */
    private Integer srcBillType;
    /**
     * 借贷方向[1:借(D) 2:贷(C)]
     */
    private Integer loanDirection;
    /**
     * 交易金额
     */
    private BigDecimal transAmount;
    /**
     * 交易时间
     */
    private Date transTime;
    /**
     * 经办人ID
     */
    private Long handlerId;
    /**
     * 入账状态 [1:已入账 2:未入账]
     */
    private Integer entryStatus;
    /**
     * 入账执行标识[1:已执行 2:未执行]
     */
    private Integer entryExecuted;
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
