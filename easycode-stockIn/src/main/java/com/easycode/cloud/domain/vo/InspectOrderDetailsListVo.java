package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 送检单明细对象 wms_inspect_order_details
 *
 * @author weifu
 * @date 2023-03-29
 */
@Alias("InspectOrderDetailsListVo")
public class InspectOrderDetailsListVo extends BaseEntity
{

    private String orderNo;

    private List<InspectOrderDetailsVo> detailsList;

    public List<InspectOrderDetailsVo> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<InspectOrderDetailsVo> detailsList) {
        this.detailsList = detailsList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
