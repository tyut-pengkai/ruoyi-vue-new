package com.ruoyi.mmclub.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医院分类管理对象 m_categorys
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public class MCategorys extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 分类名称（英文） */
    @Excel(name = "分类名称", readConverterExp = "英=文")
    private String categoryName;

    /** 分类名称（中文） */
    @Excel(name = "分类名称", readConverterExp = "中=文")
    private String categoryNameCn;

    /** 分类编码 */
    @Excel(name = "分类编码")
    private String categoryCode;

    /** 上级分类ID */
    @Excel(name = "上级分类ID")
    private Long parentId;

    /** 分类级别 */
    @Excel(name = "分类级别")
    private Long level;

    /** 排序顺序 */
    @Excel(name = "排序顺序")
    private Long sortOrder;

    /** 分类描述 */
    @Excel(name = "分类描述")
    private String description;

    /** 分类图标URL */
    @Excel(name = "分类图标URL")
    private String iconUrl;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 是否删除：1-未删除，0-已删除 */
    private Long deleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }

    public void setCategoryNameCn(String categoryNameCn) 
    {
        this.categoryNameCn = categoryNameCn;
    }

    public String getCategoryNameCn() 
    {
        return categoryNameCn;
    }

    public void setCategoryCode(String categoryCode) 
    {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() 
    {
        return categoryCode;
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

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setIconUrl(String iconUrl) 
    {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() 
    {
        return iconUrl;
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
            .append("categoryName", getCategoryName())
            .append("categoryNameCn", getCategoryNameCn())
            .append("categoryCode", getCategoryCode())
            .append("parentId", getParentId())
            .append("level", getLevel())
            .append("sortOrder", getSortOrder())
            .append("description", getDescription())
            .append("iconUrl", getIconUrl())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .toString();
    }
}
