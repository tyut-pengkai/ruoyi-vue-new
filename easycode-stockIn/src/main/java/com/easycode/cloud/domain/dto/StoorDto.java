package com.easycode.cloud.domain.dto;

import org.apache.ibatis.type.Alias;

@Alias("StoorDto")
public class StoorDto {


//     <result property="id"    column="id"    />
//        <result property="materialNo"    column="material_no"    />
//        <result property="locationId"    column="location_id"    />
//        <result property="materialName"    column="material_name"    />
//        <result property="detailType"    column="detail_type"    />
    private Long id;
    private String materialNo;
    private String locationCode;
    private String materialName;
    private String areaCode;

    private String oldMaterialNo;

    private String positionId;

    private String positionNo;

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getPositionId() {
        return positionId;
    }
    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
