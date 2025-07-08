package com.ruoyi.mmclub.domain;

import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医院管理对象 m_hospitals
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public class MHospitals extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 医院ID */
    private Long id;

    /** 医院名称 */
    @Excel(name = "医院名称")
    private String name;

    /** 医院名称 */
    @Excel(name = "医院名称")
    private String nameCn;

    /** 页面描述 */
    @Excel(name = "页面描述")
    private String description;

    /** 页面描述 */
    @Excel(name = "页面描述")
    private String descriptionCn;

    /** 医院图片 */
    @Excel(name = "医院图片")
    private String image;

    /** 医院logo图片 */
    @Excel(name = "医院logo图片")
    private String logoImage;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 地址 */
    @Excel(name = "地址")
    private String addressCn;

    /** 医生数量 */
    @Excel(name = "医生数量")
    private Long doctorCount;

    /** 平均评分 */
    @Excel(name = "平均评分")
    private BigDecimal starAvg;

    /** 评论数量 */
    @Excel(name = "评论数量")
    private Long reviewCount;

    /** 是否删除：1-未删除，0-已删除 */
    private Long deleted;

    /** 医院分类关系映射信息 */
    private List<MHospitalCategory> mHospitalCategoryList;

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

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setDescriptionCn(String descriptionCn) 
    {
        this.descriptionCn = descriptionCn;
    }

    public String getDescriptionCn() 
    {
        return descriptionCn;
    }

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    public void setLogoImage(String logoImage) 
    {
        this.logoImage = logoImage;
    }

    public String getLogoImage() 
    {
        return logoImage;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setAddressCn(String addressCn) 
    {
        this.addressCn = addressCn;
    }

    public String getAddressCn() 
    {
        return addressCn;
    }

    public void setDoctorCount(Long doctorCount) 
    {
        this.doctorCount = doctorCount;
    }

    public Long getDoctorCount() 
    {
        return doctorCount;
    }

    public void setStarAvg(BigDecimal starAvg) 
    {
        this.starAvg = starAvg;
    }

    public BigDecimal getStarAvg() 
    {
        return starAvg;
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

    public List<MHospitalCategory> getMHospitalCategoryList()
    {
        return mHospitalCategoryList;
    }

    public void setMHospitalCategoryList(List<MHospitalCategory> mHospitalCategoryList)
    {
        this.mHospitalCategoryList = mHospitalCategoryList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("nameCn", getNameCn())
            .append("description", getDescription())
            .append("descriptionCn", getDescriptionCn())
            .append("image", getImage())
            .append("logoImage", getLogoImage())
            .append("address", getAddress())
            .append("addressCn", getAddressCn())
            .append("doctorCount", getDoctorCount())
            .append("starAvg", getStarAvg())
            .append("reviewCount", getReviewCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .append("mHospitalCategoryList", getMHospitalCategoryList())
            .toString();
    }
}
