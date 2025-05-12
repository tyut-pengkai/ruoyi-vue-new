package com.easycode.cloud.domain.dto;


import com.easycode.common.core.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 送货单明细-送货要求关系对象 wms_lk_delivery_detail_requirement
 *
 * @author fsc
 * @date 2023-05-10
 */
@Alias("DeliveryDetailRequirementDto")
public class DeliveryDetailRequirementDto extends BaseEntity
{
    /** 主键 */
    private List<WmsLkDeliveryDetailRequirement> requirementList;

    public List<WmsLkDeliveryDetailRequirement> getRequirementList() {
        return requirementList;
    }

    public void setRequirementList(List<WmsLkDeliveryDetailRequirement> requirementList) {
        this.requirementList = requirementList;
    }
}
