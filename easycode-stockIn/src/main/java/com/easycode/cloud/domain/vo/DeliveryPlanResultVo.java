package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

/**
 * 采购单明细对象 wms_purchase_order_detail
 *
 * @author weifu
 * @date 2023-06-16
 */
@Alias("DeliveryPlanResultVo")
public class DeliveryPlanResultVo extends BaseEntity
{
    /**
     * 数据
     */
    private List<Map<String, String>> dataResultList;

    /**
     * 动态列
     */
    private List<Map<String, String>> headResultList;

    List<PurchaseOrderDetailVo> list;


    public List<Map<String, String>> getDataResultList() {
        return dataResultList;
    }

    public void setDataResultList(List<Map<String, String>> dataResultList) {
        this.dataResultList = dataResultList;
    }

    public List<Map<String, String>> getHeadResultList() {
        return headResultList;
    }

    public void setHeadResultList(List<Map<String, String>> headResultList) {
        this.headResultList = headResultList;
    }

    public List<PurchaseOrderDetailVo> getList() {
        return list;
    }

    public void setList(List<PurchaseOrderDetailVo> list) {
        this.list = list;
    }
}
