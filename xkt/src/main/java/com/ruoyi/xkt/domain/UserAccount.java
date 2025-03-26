package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户账户（支付宝、微信等）对象 user_account
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户账户ID
     */
    private Long userAccId;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * 用户类型（支付宝、微信等）
     */
    @Excel(name = "用户类型", readConverterExp = "支=付宝、微信等")
    private Long accountType;

    /**
     * 账户电话
     */
    @Excel(name = "账户电话")
    private String accountPhone;

    /**
     * 账户名称
     */
    @Excel(name = "账户名称")
    private String accountName;

    /**
     * 账户余额
     */
    @Excel(name = "账户余额")
    private BigDecimal balance;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userAccId", getUserAccId())
                .append("userId", getUserId())
                .append("accountType", getAccountType())
                .append("accountPhone", getAccountPhone())
                .append("accountName", getAccountName())
                .append("balance", getBalance())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
