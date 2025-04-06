package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口子角色账号对象 store_role_account
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreRoleAccount extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口子角色账号ID
     */
    @TableId
    private Long id;

    /**
     * 档口子角色ID
     */
    @Excel(name = "档口子角色ID")
    private Long storeRoleId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 子账户名称
     */
    private String accountName;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;


    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人名称
     */
    private String operatorName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeRoleId", getStoreRoleId())
                .append("storeId", getStoreId())
                .append("userId", getUserId())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
