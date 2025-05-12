package com.easycode.cloud.domain.dto;

import com.weifu.cloud.common.core.web.domain.BaseEntity;
import com.weifu.cloud.domain.vo.DeliveryOrderDetailVo;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 供货要求
 * @author hbh
 */
@Alias("DeliveryRequirementCheckDto")
public class DeliveryRequirementCheckDto extends BaseEntity {

    List<DeliveryOrderDetailVo> deliveryOrderDetailVos;

    public List<DeliveryOrderDetailVo> getDeliveryOrderDetailVos() {
        return deliveryOrderDetailVos;
    }

    public void setDeliveryOrderDetailVos(List<DeliveryOrderDetailVo> deliveryOrderDetailVos) {
        this.deliveryOrderDetailVos = deliveryOrderDetailVos;
    }
}
