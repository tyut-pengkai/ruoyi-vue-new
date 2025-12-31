package com.ruoyi.bookSys.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 访客信息对象 visitors
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public class Visitors extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 访客ID */
    private Long visitorId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String fullName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 性别(1:男/2:女/0:未知) */
    @Excel(name = "性别(1:男/2:女/0:未知)")
    private Integer gender;

    /** 照片路径 */
    @Excel(name = "照片路径")
    private String photoPath;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String carPlate;

    /** 是否黑名单 */
    @Excel(name = "是否黑名单")
    private Integer blacklistFlag;

    /** 黑名单原因 */
    @Excel(name = "黑名单原因")
    private String blacklistReason;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String notes;

    /** 来访次数 */
    @Excel(name = "来访次数")
    private Long visitCount;

    /** 访客记录信息 */
    private List<VisitRecords> visitRecordsList;

    public void setVisitorId(Long visitorId) 
    {
        this.visitorId = visitorId;
    }

    public Long getVisitorId() 
    {
        return visitorId;
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

    public void setGender(Integer gender) 
    {
        this.gender = gender;
    }

    public Integer getGender() 
    {
        return gender;
    }

    public void setPhotoPath(String photoPath) 
    {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() 
    {
        return photoPath;
    }

    public void setCarPlate(String carPlate) 
    {
        this.carPlate = carPlate;
    }

    public String getCarPlate() 
    {
        return carPlate;
    }

    public void setBlacklistFlag(Integer blacklistFlag) 
    {
        this.blacklistFlag = blacklistFlag;
    }

    public Integer getBlacklistFlag() 
    {
        return blacklistFlag;
    }

    public void setBlacklistReason(String blacklistReason) 
    {
        this.blacklistReason = blacklistReason;
    }

    public String getBlacklistReason() 
    {
        return blacklistReason;
    }

    public void setNotes(String notes) 
    {
        this.notes = notes;
    }

    public String getNotes() 
    {
        return notes;
    }

    public void setVisitCount(Long visitCount) 
    {
        this.visitCount = visitCount;
    }

    public Long getVisitCount() 
    {
        return visitCount;
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
            .append("visitorId", getVisitorId())
            .append("fullName", getFullName())
            .append("phone", getPhone())
            .append("idCard", getIdCard())
            .append("gender", getGender())
            .append("photoPath", getPhotoPath())
            .append("carPlate", getCarPlate())
            .append("blacklistFlag", getBlacklistFlag())
            .append("blacklistReason", getBlacklistReason())
            .append("notes", getNotes())
            .append("visitCount", getVisitCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("visitRecordsList", getVisitRecordsList())
            .toString();
    }
}
