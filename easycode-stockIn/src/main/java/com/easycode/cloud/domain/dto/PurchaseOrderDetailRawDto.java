package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.WmsPurchaseOrderDetailRaw;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 采购单据处理dto
 * @author hbh
 */
@Alias("PurchaseOrderDetailRawDto")
public class PurchaseOrderDetailRawDto extends WmsPurchaseOrderDetailRaw {

    /**
     * 采购明细id(业务表)
     */
    private Long detailId;

    /**
     * wms已收数量
     */
    private BigDecimal wmsReceivedQty;

    /**
     * 总需求数量
     */
    private BigDecimal totalQty;

    /**
     * 公司id
     */
    private Long companyId;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getWmsReceivedQty() {
        return wmsReceivedQty;
    }

    public void setWmsReceivedQty(BigDecimal wmsReceivedQty) {
        this.wmsReceivedQty = wmsReceivedQty;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
}
