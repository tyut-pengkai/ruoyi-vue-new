package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 成本中心退货单对象 wms_cost_center_return_order
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("CostCenterReturnOrderVo")
public class CostCenterReturnOrderVo extends CostCenterReturnOrder
{
    private static final long serialVersionUID = 1L;

    private List<CostCenterReturnOrderDetail> detailList;

    private String startTime;

    private String materialNo;

    private String endTime;

    public List<CostCenterReturnOrderDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CostCenterReturnOrderDetail> detailList) {
        this.detailList = detailList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("factoryCode", getFactoryCode())
                .append("factoryName", getFactoryName())
                .append("costCenterCode", getCostCenterCode())
                .append("costCenterDesc", getCostCenterDesc())
                .append("moveType", getMoveType())
                .append("customerCode", getCustomerCode())
                .append("innerOrderNo", getInnerOrderNo())
                .append("orderStatus", getOrderStatus())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
