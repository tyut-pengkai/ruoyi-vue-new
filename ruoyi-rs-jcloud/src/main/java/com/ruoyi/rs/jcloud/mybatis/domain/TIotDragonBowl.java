/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.domain;

import java.io.Serializable;

public class TIotDragonBowl implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private String status;
    private String fixedTimeOpen1;
    private String fixedTimeOpen2;
    private String fixedTimeOpen3;
    private String fixedTimeClose1;
    private String fixedTimeClose2;
    private String fixedTimeClose3;
    private String remark;

    public Integer getId() {
        return id;
    }
    public Integer getUserId() {
        return userId;
    }
    public String getStatus() {
        return status;
    }
    public String getFixedTimeOpen1() {
        return fixedTimeOpen1;
    }
    public String getFixedTimeOpen2() {
        return fixedTimeOpen2;
    }
    public String getFixedTimeOpen3() {
        return fixedTimeOpen3;
    }
    public String getFixedTimeClose1() {
        return fixedTimeClose1;
    }
    public String getFixedTimeClose2() {
        return fixedTimeClose2;
    }
    public String getFixedTimeClose3() {
        return fixedTimeClose3;
    }
    public String getRemark() {
        return remark;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setFixedTimeOpen1(String fixedTimeOpen1) {
        this.fixedTimeOpen1 = fixedTimeOpen1;
    }
    public void setFixedTimeOpen2(String fixedTimeOpen2) {
        this.fixedTimeOpen2 = fixedTimeOpen2;
    }
    public void setFixedTimeOpen3(String fixedTimeOpen3) {
        this.fixedTimeOpen3 = fixedTimeOpen3;
    }
    public void setFixedTimeClose1(String fixedTimeClose1) {
        this.fixedTimeClose1 = fixedTimeClose1;
    }
    public void setFixedTimeClose2(String fixedTimeClose2) {
        this.fixedTimeClose2 = fixedTimeClose2;
    }
    public void setFixedTimeClose3(String fixedTimeClose3) {
        this.fixedTimeClose3 = fixedTimeClose3;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}