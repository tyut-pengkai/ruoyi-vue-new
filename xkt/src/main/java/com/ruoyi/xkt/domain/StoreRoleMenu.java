package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
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
public class StoreRoleMenu extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口子角色菜单ID
     */
    @TableId
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
