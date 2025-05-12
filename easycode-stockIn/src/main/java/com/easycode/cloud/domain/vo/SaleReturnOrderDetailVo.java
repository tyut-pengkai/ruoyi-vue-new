package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.SaleReturnOrderDetail;
import org.apache.ibatis.type.Alias;

/**
 * 销售发货退货单明细对象 wms_sale_center_return_order_detail
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("SaleReturnOrderDetailVo")
public class SaleReturnOrderDetailVo extends SaleReturnOrderDetail
{
    /** 物料类型 */
    private String type;

    /** 物料类型 */
    private String locationCode;

    /** 物料类型 */
    private String positionNo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }
}
