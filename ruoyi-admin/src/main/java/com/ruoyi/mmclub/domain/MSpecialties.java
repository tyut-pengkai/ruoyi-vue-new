package com.ruoyi.mmclub.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医生专业管理对象 m_specialties
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public class MSpecialties extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 专业名称(韩文) */
    @Excel(name = "专业名称(韩文)")
    private String name;

    /** 专业名称(中文) */
    @Excel(name = "专业名称(中文)")
    private String nameCn;

    /** 专业领域代码 */
    @Excel(name = "专业领域代码")
    private String specialtyCode;

    /** 专业领域描述 */
    @Excel(name = "专业领域描述")
    private String description;

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

    public void setSpecialtyCode(String specialtyCode) 
    {
        this.specialtyCode = specialtyCode;
    }

    public String getSpecialtyCode() 
    {
        return specialtyCode;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
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
            .append("specialtyCode", getSpecialtyCode())
            .append("description", getDescription())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .toString();
    }
}
