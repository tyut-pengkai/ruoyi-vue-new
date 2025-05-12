package com.easycode.cloud.domain.vo;


import com.weifu.cloud.domian.WmsSupplierInfo;
import org.apache.ibatis.type.Alias;

/**
 * 供应商管理 出参数据vo
 * @author bcp
 */
@Alias("WmsSupplierVo")
public class WmsSupplierVo extends WmsSupplierInfo {

    /** 工厂代码对应的id */
    private Long factoryId;

    /** 货主（工厂代码）*/
    private String factoryCode;

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

}
