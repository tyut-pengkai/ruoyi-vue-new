package com.ruoyi.mmclub.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医生管理对象 m_doctors
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public class MDoctors extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 医生ID */
    private Long id;

    /** 医院ID */
    @Excel(name = "医院ID")
    private Long hospitalId;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    private String name;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    private String nameCn;

    /** 头像照片URL */
    @Excel(name = "头像照片URL")
    private String profilePhoto;

    /** 评论数量 */
    @Excel(name = "评论数量")
    private Long reviewCount;

    /** 是否删除：1-未删除，0-已删除 */
    private Long deleted;

    /** 医生专业领域关系映射信息 */
    private List<MDoctorSpecialty> mDoctorSpecialtyList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setHospitalId(Long hospitalId) 
    {
        this.hospitalId = hospitalId;
    }

    public Long getHospitalId() 
    {
        return hospitalId;
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

    public void setProfilePhoto(String profilePhoto) 
    {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhoto() 
    {
        return profilePhoto;
    }

    public void setReviewCount(Long reviewCount) 
    {
        this.reviewCount = reviewCount;
    }

    public Long getReviewCount() 
    {
        return reviewCount;
    }

    public void setDeleted(Long deleted) 
    {
        this.deleted = deleted;
    }

    public Long getDeleted() 
    {
        return deleted;
    }

    public List<MDoctorSpecialty> getMDoctorSpecialtyList()
    {
        return mDoctorSpecialtyList;
    }

    public void setMDoctorSpecialtyList(List<MDoctorSpecialty> mDoctorSpecialtyList)
    {
        this.mDoctorSpecialtyList = mDoctorSpecialtyList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("hospitalId", getHospitalId())
            .append("name", getName())
            .append("nameCn", getNameCn())
            .append("profilePhoto", getProfilePhoto())
            .append("reviewCount", getReviewCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .append("mDoctorSpecialtyList", getMDoctorSpecialtyList())
            .toString();
    }
}
