package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.PurchaseOrder;
import com.easycode.cloud.domain.vo.PurchaseOrderVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购单Mapper接口
 * 
 * @author bcp
 * @date 2023-02-23
 */
@Repository
public interface PurchaseOrderMapper 
{
    /**
     * 查询采购单
     * 
     * @param id 采购单主键
     * @return 采购单
     */
    public PurchaseOrder selectPurchaseOrderById(Long id);

    /**
     * 查询采购单列表
     * 
     * @param purchaseOrder 采购单
     * @return 采购单集合
     */
    public List<PurchaseOrder> selectPurchaseOrderList(PurchaseOrder purchaseOrder);

    /**
     * 新增采购单
     * 
     * @param purchaseOrder 采购单
     * @return 结果
     */
    public int insertPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * 修改采购单
     * 
     * @param purchaseOrder 采购单
     * @return 结果
     */
    public int updatePurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * 删除采购单
     * 
     * @param id 采购单主键
     * @return 结果
     */
    public int deletePurchaseOrderById(Long id);

    /**
     * 批量删除采购单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePurchaseOrderByIds(Long[] ids);

    /**
     * 查询采购协议总数量和最大ID
     * @param purchaseOrder
     * @return 结果
     */
    public List<PurchaseOrderVo> selectPurOrderAgNum(PurchaseOrder purchaseOrder);

    /**
     * 获取采购协议列表
     * @param purchaseOrder
     * @return 结果
     */
    public List<PurchaseOrder> selectPurOrderByOrderType(PurchaseOrder purchaseOrder);

    List<PurchaseOrder> selectOrderByOrderNo(List<String> orderNoList);
}
