package com.ruoyi.xkt.dto.account;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部账户
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.493
 **/
@Data
public class InternalAccountDTO {
    /**
     * 内部账户ID
     */
    private Long id;
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    private Integer ownerType;
    /**
     * 归属ID（平台=-1，档口=store_id）
     */
    private Long ownerId;
    /**
     * 账户状态[1:正常 2:冻结]
     */
    private Integer accountStatus;
    /**
     * 交易密码
     */
    private String transactionPassword;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 可用余额
     */
    private BigDecimal usableBalance;
    /**
     * 支付中金额
     */
    private BigDecimal paymentAmount;
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
