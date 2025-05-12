package com.easycode.cloud.domain.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("StockInOrederVo")
public class StockInOrederVo {

    private String materialNo;
    private String materialName;
    private String oldMaterialNo;
    private String lot;
    private String po;
    private String ldap;

    private Date expDay;

    public Date getExpDay() {
        return expDay;
    }

    public void setExpDay(Date expDay) {
        this.expDay = expDay;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

}
