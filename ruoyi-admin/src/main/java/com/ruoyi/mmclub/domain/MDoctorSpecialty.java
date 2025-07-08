package com.ruoyi.mmclub.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医生专业领域关系映射对象 m_doctor_specialty
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public class MDoctorSpecialty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 医生ID */
    @Excel(name = "医生ID")
    private Long doctorId;

    /** 专业领域ID */
    @Excel(name = "专业领域ID")
    private Long specialtyId;

    /** 专业水平：1-初级，2-中级，3-高级，4-专家级 */
    @Excel(name = "专业水平：1-初级，2-中级，3-高级，4-专家级")
    private Long proficiencyLevel;

    /** 该领域从业年限 */
    @Excel(name = "该领域从业年限")
    private Long yearsExperience;

    /** 相关认证/资质 */
    @Excel(name = "相关认证/资质")
    private String certification;

    /** 排序顺序 */
    @Excel(name = "排序顺序")
    private Long sortOrder;

    /** 状态：0-禁用，1-启用 */
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
    public void setDoctorId(Long doctorId) 
    {
        this.doctorId = doctorId;
    }

    public Long getDoctorId() 
    {
        return doctorId;
    }
    public void setSpecialtyId(Long specialtyId) 
    {
        this.specialtyId = specialtyId;
    }

    public Long getSpecialtyId() 
    {
        return specialtyId;
    }
    public void setProficiencyLevel(Long proficiencyLevel) 
    {
        this.proficiencyLevel = proficiencyLevel;
    }

    public Long getProficiencyLevel() 
    {
        return proficiencyLevel;
    }
    public void setYearsExperience(Long yearsExperience) 
    {
        this.yearsExperience = yearsExperience;
    }

    public Long getYearsExperience() 
    {
        return yearsExperience;
    }
    public void setCertification(String certification) 
    {
        this.certification = certification;
    }

    public String getCertification() 
    {
        return certification;
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
            .append("doctorId", getDoctorId())
            .append("specialtyId", getSpecialtyId())
            .append("proficiencyLevel", getProficiencyLevel())
            .append("yearsExperience", getYearsExperience())
            .append("certification", getCertification())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .toString();
    }
}
