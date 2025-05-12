package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.dto.FinProductScanDetailDto;

import java.util.List;

/**
 * 成品扫描收货对象
 *
 * @author Administrator
 */
public class FinProductScanVo {

    /**
     * 生产订单号/计划号
     */
    private String prdOrderNo;

    /**
     * 收货类型
     */
    private String ruleType;

    /**
     * 物料名称
     */
    private String materialDesc;

    /**
     * 物料号
     */
    private String materialNo;

    /**
     * 工厂代码
     */
    private String factoryCode;

    /**
     * 成品收货明细
     */
    List<FinProductScanDetailDto> finProductScanDetailDtoList;

    public String getPrdOrderNo() {
        return prdOrderNo;
    }

    public void setPrdOrderNo(String prdOrderNo) {
        this.prdOrderNo = prdOrderNo;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public List<FinProductScanDetailDto> getFinProductScanDetailDtoList() {
        return finProductScanDetailDtoList;
    }

    public void setFinProductScanDetailDtoList(List<FinProductScanDetailDto> finProductScanDetailDtoList) {
        this.finProductScanDetailDtoList = finProductScanDetailDtoList;
    }
}
