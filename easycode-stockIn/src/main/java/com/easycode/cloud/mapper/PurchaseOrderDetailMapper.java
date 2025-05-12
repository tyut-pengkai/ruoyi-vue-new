package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.vo.PurchaseOrderDetailVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购单明细Mapper接口
 *
 * @author fangshucheng
 * @date 2023-06-16
 */
@Repository
public interface PurchaseOrderDetailMapper
{

    /**
     * 查询送货计划送货日期及数量
     *
     * @param purchaseOrderDetail 采购单
     * @return 采购单集合
     */
    public List<PurchaseOrderDetailVo> queryDeliveryPlan(PurchaseOrderDetailVo purchaseOrderDetail);

    /**
     * 根据送货单id查询关联采购单明细交货已完成的数据
     *
     * @param ids 采购单
     * @return 采购单集合
     */
    public List<PurchaseOrderDetailVo> queryPurchaseDetailFinishList(Long[] ids);

}
