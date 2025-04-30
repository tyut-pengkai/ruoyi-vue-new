package com.ruoyi.sale.service;

import com.ruoyi.sale.domain.SysSaleOrder;

import java.util.List;

/**
 * 销售订单Service接口
 *
 * @author zwgu
 * @date 2022-02-21
 */
public interface ISysSaleOrderService {
    /**
     * 查询销售订单
     *
     * @param orderId 销售订单主键
     * @return 销售订单
     */
    public SysSaleOrder selectSysSaleOrderByOrderId(Long orderId);

    /**
     * 查询销售订单
     *
     * @param orderNo 销售订单主键
     * @return 销售订单
     */
    public SysSaleOrder selectSysSaleOrderByOrderNo(String orderNo);

    /**
     * 查询销售订单列表
     *
     * @param sysSaleOrder 销售订单
     * @return 销售订单集合
     */
    public List<SysSaleOrder> selectSysSaleOrderList(SysSaleOrder sysSaleOrder);

    /**
     * 查询销售订单列表
     *
     * @param sysSaleOrder 销售订单
     * @return 销售订单集合
     */
    public List<SysSaleOrder> selectSysSaleOrderQueryLimit(SysSaleOrder sysSaleOrder, int limit);

    /**
     * 新增销售订单
     *
     * @param sysSaleOrder 销售订单
     * @return 结果
     */
    public int insertSysSaleOrder(SysSaleOrder sysSaleOrder);

    /**
     * 修改销售订单
     *
     * @param sysSaleOrder 销售订单
     * @return 结果
     */
    public int updateSysSaleOrder(SysSaleOrder sysSaleOrder);

    /**
     * 批量删除销售订单
     *
     * @param orderIds 需要删除的销售订单主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderByOrderIds(Long[] orderIds);

    /**
     * 删除销售订单信息
     *
     * @param orderId 销售订单主键
     * @return 结果
     */
    public int deleteSysSaleOrderByOrderId(Long orderId);
}
