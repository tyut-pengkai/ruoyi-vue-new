package com.easycode.cloud.service;

import com.easycode.cloud.domain.PrdReturnOrder;
import com.easycode.cloud.domain.dto.PrdReturnOrderDto;

import java.util.List;

/**
 * 生产订单发货退料单Service接口
 * 
 * @author bcp
 * @date 2023-03-11
 */
public interface IPrdReturnOrderService 
{
    /**
     * 查询生产订单发货退料单
     * 
     * @param id 生产订单发货退料单主键
     * @return 生产订单发货退料单
     */
    public PrdReturnOrder selectPrdReturnOrderById(Long id);

    /**
     * 查询生产订单发货退料单列表
     * 
     * @param prdReturnOrderDto 生产订单发货退料单
     * @return 生产订单发货退料单集合
     */
    public List<PrdReturnOrder> selectPrdReturnOrderList(PrdReturnOrderDto prdReturnOrderDto);

    /**
     * 新增生产订单发货退料单
     * 
     * @param prdReturnOrderDto 生产订单发货退料单dto
     * @return 结果
     */
    public PrdReturnOrder insertPrdReturnOrder(PrdReturnOrderDto prdReturnOrderDto);

    /**
     * 修改生产订单发货退料单
     * 
     * @param prdReturnOrder 生产订单发货退料单
     * @return 结果
     */
    public int updatePrdReturnOrder(PrdReturnOrder prdReturnOrder);

    /**
     * 批量删除生产订单发货退料单
     * 
     * @param ids 需要删除的生产订单发货退料单主键集合
     * @return 结果
     */
    public int deletePrdReturnOrderByIds(Long[] ids);

    /**
     * 删除生产订单发货退料单信息
     * 
     * @param id 生产订单发货退料单主键
     * @return 结果
     */
    public int deletePrdReturnOrderById(Long id);

    /**
     * 关闭生产订单发货退料单
     * @param id 主键id
     * @return 结果
     */
    int close(Long id);

    /**
     * 批量激活生产补料退货单
     * @param ids 生产补料退货单ids
     * @return 结果
     */
    int activePrdReturnOrderByIds(Long[] ids);
}
