package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 租户套餐对象 sys_tenant_package
 *
 * @author ruoyi
 * @date 2025-12-19
 */
public class SysTenantPackage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 套餐ID */
    private Long packageId;

    /** 套餐名称 */
    private String packageName;

    /** 套餐编码 */
    private String packageCode;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 菜单ID列表（非数据库字段） */
    private Long[] menuIds;

    public void setPackageId(Long packageId)
    {
        this.packageId = packageId;
    }

    public Long getPackageId()
    {
        return packageId;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageCode(String packageCode)
    {
        this.packageCode = packageCode;
    }

    public String getPackageCode()
    {
        return packageCode;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Long[] getMenuIds()
    {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds)
    {
        this.menuIds = menuIds;
    }
}
