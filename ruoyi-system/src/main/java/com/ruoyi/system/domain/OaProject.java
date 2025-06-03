package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目管理主对象 oa_project
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public class OaProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 公司来源(0:手动/1:合作方库) */
    @Excel(name = "公司来源(0:手动/1:合作方库)")
    private Integer companyInfoSource;

    /** 合作方ID */
    @Excel(name = "合作方ID")
    private Long partnerId;

    /** 手动输入公司名 */
    @Excel(name = "手动输入公司名")
    private String customCompany;

    /** 项目日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date projectDate;

    /** 项目地址 */
    @Excel(name = "项目地址")
    private String address;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 备案人ID */
    @Excel(name = "备案人ID")
    private Long recordUserId;

    /** 备案公司 */
    @Excel(name = "备案公司")
    private String recordCompany;

    /** 状态(0:意向中/1:已中标/2:已验收) */
    @Excel(name = "状态(0:意向中/1:已中标/2:已验收)")
    private Integer status;

    /** 所属部门ID */
    @Excel(name = "所属部门ID")
    private Long ownerDeptId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createdBy;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updatedBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setCompanyInfoSource(Integer companyInfoSource) 
    {
        this.companyInfoSource = companyInfoSource;
    }

    public Integer getCompanyInfoSource() 
    {
        return companyInfoSource;
    }

    public void setPartnerId(Long partnerId) 
    {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() 
    {
        return partnerId;
    }

    public void setCustomCompany(String customCompany) 
    {
        this.customCompany = customCompany;
    }

    public String getCustomCompany() 
    {
        return customCompany;
    }

    public void setProjectDate(Date projectDate) 
    {
        this.projectDate = projectDate;
    }

    public Date getProjectDate() 
    {
        return projectDate;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setContactPerson(String contactPerson) 
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() 
    {
        return contactPerson;
    }

    public void setRecordUserId(Long recordUserId) 
    {
        this.recordUserId = recordUserId;
    }

    public Long getRecordUserId() 
    {
        return recordUserId;
    }

    public void setRecordCompany(String recordCompany) 
    {
        this.recordCompany = recordCompany;
    }

    public String getRecordCompany() 
    {
        return recordCompany;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setOwnerDeptId(Long ownerDeptId) 
    {
        this.ownerDeptId = ownerDeptId;
    }

    public Long getOwnerDeptId() 
    {
        return ownerDeptId;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setCreatedBy(Long createdBy) 
    {
        this.createdBy = createdBy;
    }

    public Long getCreatedBy() 
    {
        return createdBy;
    }

    public void setUpdatedBy(Long updatedBy) 
    {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedBy() 
    {
        return updatedBy;
    }

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectName", getProjectName())
            .append("companyInfoSource", getCompanyInfoSource())
            .append("partnerId", getPartnerId())
            .append("customCompany", getCustomCompany())
            .append("projectDate", getProjectDate())
            .append("address", getAddress())
            .append("contactPerson", getContactPerson())
            .append("recordUserId", getRecordUserId())
            .append("recordCompany", getRecordCompany())
            .append("status", getStatus())
            .append("ownerDeptId", getOwnerDeptId())
            .append("createdTime", getCreatedTime())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
