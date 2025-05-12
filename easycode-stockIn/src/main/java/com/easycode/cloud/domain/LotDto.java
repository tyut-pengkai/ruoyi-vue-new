package com.easycode.cloud.domain;

import java.math.BigDecimal;

/**
 * 批次dto
 * @author bcp
 */
public class LotDto {
    /**
     * 批次号
     */
    private String lotNo;

    /**
     * 批次数量
     */
    private BigDecimal lotNoNum;

    /**
     * 校验是否成功
     */
    private String isVerify;

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public BigDecimal getLotNoNum() {
        return lotNoNum;
    }

    public void setLotNoNum(BigDecimal lotNoNum) {
        this.lotNoNum = lotNoNum;
    }

    public String getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(String isVerify) {
        this.isVerify = isVerify;
    }
}
