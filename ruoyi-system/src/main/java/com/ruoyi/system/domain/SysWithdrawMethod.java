package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收款方式对象 sys_withdraw_method
 * 
 * @author zwgu
 * @date 2024-06-03
 */
public class SysWithdrawMethod extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 收款平台 支付宝 微信 银行卡 */
    @Excel(name = "收款平台 支付宝 微信 银行卡")
    private String receiveMethod;

    /** 收款账号 */
    @Excel(name = "收款账号")
    private String receiveAccount;

    /** 实名 */
    @Excel(name = "实名")
    private String realName;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setReceiveMethod(String receiveMethod) 
    {
        this.receiveMethod = receiveMethod;
    }

    public String getReceiveMethod() 
    {
        return receiveMethod;
    }
    public void setReceiveAccount(String receiveAccount) 
    {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveAccount() 
    {
        return receiveAccount;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("receiveMethod", getReceiveMethod())
            .append("receiveAccount", getReceiveAccount())
            .append("realName", getRealName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
