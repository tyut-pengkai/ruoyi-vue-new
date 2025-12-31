package com.ruoyi.bookSys.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 住户信息对象 residents
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public class Residents extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 住户ID */
    private Long residentId;

    /** 楼栋号 */
    @Excel(name = "楼栋号")
    private String buildingNumber;

    /** 单元号 */
    @Excel(name = "单元号")
    private String unitNumber;

    /** 房间号 */
    @Excel(name = "房间号")
    private String roomNumber;

    /** 住户姓名 */
    @Excel(name = "住户姓名")
    private String fullName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 是否为业主(1:业主/0:租客) */
    @Excel(name = "是否为业主(1:业主/0:租客)")
    private Integer isOwner;

    /** 状态(1:正常/0:冻结) */
    @Excel(name = "状态(1:正常/0:冻结)")
    private Integer status;

    /** 访客记录信息 */
    private List<VisitRecords> visitRecordsList;

    public void setResidentId(Long residentId) 
    {
        this.residentId = residentId;
    }

    public Long getResidentId() 
    {
        return residentId;
    }

    public void setBuildingNumber(String buildingNumber) 
    {
        this.buildingNumber = buildingNumber;
    }

    public String getBuildingNumber() 
    {
        return buildingNumber;
    }

    public void setUnitNumber(String unitNumber) 
    {
        this.unitNumber = unitNumber;
    }

    public String getUnitNumber() 
    {
        return unitNumber;
    }

    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }

    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setIsOwner(Integer isOwner) 
    {
        this.isOwner = isOwner;
    }

    public Integer getIsOwner() 
    {
        return isOwner;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
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
            .append("residentId", getResidentId())
            .append("buildingNumber", getBuildingNumber())
            .append("unitNumber", getUnitNumber())
            .append("roomNumber", getRoomNumber())
            .append("fullName", getFullName())
            .append("phone", getPhone())
            .append("idCard", getIdCard())
            .append("isOwner", getIsOwner())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("visitRecordsList", getVisitRecordsList())
            .toString();
    }
}
