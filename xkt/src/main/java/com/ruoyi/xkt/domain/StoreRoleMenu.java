package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口子角色菜单对象 store_role_menu
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreRoleMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口子角色菜单ID
     */
    private Long storeRoleMenuId;

    /**
     * 档口角色ID
     */
    @Excel(name = "档口角色ID")
    private Long storeRoleId;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 菜单名称
     */
    @Excel(name = "菜单名称")
    private String menuName;

    /**
     * 菜单使用状态（0正常 1停用）
     */
    @Excel(name = "菜单使用状态", readConverterExp = "0=正常,1=停用")
    private String menuStatus;

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
                .append("storeRoleMenuId", getStoreRoleMenuId())
                .append("storeRoleId", getStoreRoleId())
                .append("storeId", getStoreId())
                .append("menuName", getMenuName())
                .append("menuStatus", getMenuStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
