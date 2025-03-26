package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
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
public class StoreRoleAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口子角色账号ID
     */
    private Long storeRoleAccId;

    /**
     * 档口子角色ID
     */
    @Excel(name = "档口子角色ID")
    private Long storeRoleId;

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
     * 当前档口子账号状态（0正常 1停用）
     */
    @Excel(name = "当前档口子账号状态", readConverterExp = "0=正常,1=停用")
    private String accStatus;

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
                .append("storeRoleAccId", getStoreRoleAccId())
                .append("storeRoleId", getStoreRoleId())
                .append("storeId", getStoreId())
                .append("userId", getUserId())
                .append("accStatus", getAccStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
