package com.ruoyi.sale.mapper;

import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.system.domain.vo.SysSaleOrderLimitVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 销售订单Mapper接口
 *
 * @author zwgu
 * @date 2022-02-21
 */
@Repository
public interface SysSaleOrderMapper {
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
    public List<SysSaleOrder> selectSysSaleOrderQueryLimit(SysSaleOrderLimitVo sysSaleOrder);

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
     * 删除销售订单
     *
     * @param orderId 销售订单主键
     * @return 结果
     */
    public int deleteSysSaleOrderByOrderId(Long orderId);

    /**
     * 批量删除销售订单
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderByOrderIds(Long[] orderIds);

    /**
     * 批量删除销售订单详情
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderItemByOrderIds(Long[] orderIds);

    /**
     * 批量新增销售订单详情
     *
     * @param sysSaleOrderItemList 销售订单详情列表
     * @return 结果
     */
    public int batchSysSaleOrderItem(List<SysSaleOrderItem> sysSaleOrderItemList);

    /**
     * 通过销售订单主键删除销售订单详情信息
     *
     * @param orderId 销售订单ID
     * @return 结果
     */
    public int deleteSysSaleOrderItemByOrderId(Long orderId);
}
