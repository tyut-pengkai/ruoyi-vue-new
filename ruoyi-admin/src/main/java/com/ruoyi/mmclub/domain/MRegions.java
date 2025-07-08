package com.ruoyi.mmclub.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 地区管理对象 m_regions
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public class MRegions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 地区名称(韩文) */
    @Excel(name = "地区名称(韩文)")
    private String name;

    /** 地区名称(中文) */
    @Excel(name = "地区名称(中文)")
    private String nameCn;

    /** 上级地区ID */
    @Excel(name = "上级地区ID")
    private Long parentId;

    /** 地区层级 */
    @Excel(name = "地区层级")
    private Long level;

    /** 地区代码 */
    @Excel(name = "地区代码")
    private String regionCode;

    /** 排序顺序 */
    @Excel(name = "排序顺序")
    private Long sortOrder;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 删除标识：1-未删除，0-已删除 */
    private Long deleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setNameCn(String nameCn) 
    {
        this.nameCn = nameCn;
    }

    public String getNameCn() 
    {
        return nameCn;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setLevel(Long level) 
    {
        this.level = level;
    }

    public Long getLevel() 
    {
        return level;
    }

    public void setRegionCode(String regionCode) 
    {
        this.regionCode = regionCode;
    }

    public String getRegionCode() 
    {
        return regionCode;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setDeleted(Long deleted) 
    {
        this.deleted = deleted;
    }

    public Long getDeleted() 
    {
        return deleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("nameCn", getNameCn())
            .append("parentId", getParentId())
            .append("level", getLevel())
            .append("regionCode", getRegionCode())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .toString();
    }
}
