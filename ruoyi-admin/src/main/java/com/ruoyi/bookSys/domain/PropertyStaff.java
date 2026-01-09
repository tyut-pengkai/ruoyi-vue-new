package com.ruoyi.bookSys.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物业人员对象 property_staff
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public class PropertyStaff extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工ID */
    private Long staffId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码哈希 */
    @Excel(name = "密码哈希")
    private String passwordHash;

    /** 姓名 */
    @Excel(name = "姓名")
    private String fullName;

    /** 角色(1:管理员/2:保安/3:前台) */
    @Excel(name = "角色(1:管理员/2:保安/3:前台)")
    private Integer role;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 部门 */
    @Excel(name = "部门")
    private String department;

    /** 是否在职 */
    @Excel(name = "是否在职")
    private Integer status;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLogin;

    /** 访客记录信息 */
    private List<VisitRecords> visitRecordsList;

    public void setStaffId(Long staffId) 
    {
        this.staffId = staffId;
    }

    public Long getStaffId() 
    {
        return staffId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPasswordHash(String passwordHash) 
    {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() 
    {
        return passwordHash;
    }

    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }

    public void setRole(Integer role) 
    {
        this.role = role;
    }

    public Integer getRole() 
    {
        return role;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setLastLogin(Date lastLogin) 
    {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogin() 
    {
        return lastLogin;
    }

    public List<VisitRecords> getVisitRecordsList()
    {
        return visitRecordsList;
    }

    public void setVisitRecordsList(List<VisitRecords> visitRecordsList)
    {
        this.visitRecordsList = visitRecordsList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("staffId", getStaffId())
            .append("username", getUsername())
            .append("passwordHash", getPasswordHash())
            .append("fullName", getFullName())
            .append("role", getRole())
            .append("phone", getPhone())
            .append("department", getDepartment())
            .append("status", getStatus())
            .append("lastLogin", getLastLogin())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("visitRecordsList", getVisitRecordsList())
            .toString();
    }
}
