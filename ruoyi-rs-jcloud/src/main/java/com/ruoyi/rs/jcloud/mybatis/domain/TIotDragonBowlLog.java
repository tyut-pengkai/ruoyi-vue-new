/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.domain;

import java.util.Date;
import java.io.Serializable;

public class TIotDragonBowlLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer bowlId;
    private Date operateTime;
    private String operateStatus;
    private Integer operateType;

    public Integer getId() {
        return id;
    }
    public Integer getBowlId() {
        return bowlId;
    }
    public Date getOperateTime() {
        return operateTime;
    }
    public String getOperateStatus() {
        return operateStatus;
    }
    public Integer getOperateType() {
        return operateType;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setBowlId(Integer bowlId) {
        this.bowlId = bowlId;
    }
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
}