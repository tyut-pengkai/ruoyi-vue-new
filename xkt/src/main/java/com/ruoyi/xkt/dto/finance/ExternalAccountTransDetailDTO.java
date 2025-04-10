package com.ruoyi.xkt.dto.finance;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 外部账户交易明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.470
 **/
@Data
public class ExternalAccountTransDetailDTO {
    /**
     * 外部账户交易明细ID
     */
    private Long id;
    /**
     * 外部账户ID
     */
    private Long externalAccountId;
    /**
     * 来源单据ID
     */
    private Long srcBillId;
    /**
     * 来源单据类型[2:付款]
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
