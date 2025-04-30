package com.ruoyi.sale.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.mapper.SysSaleOrderMapper;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.system.domain.vo.SysSaleOrderLimitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单Service业务层处理
 *
 * @author zwgu
 * @date 2022-02-21
 */
@Service
public class SysSaleOrderServiceImpl implements ISysSaleOrderService {
    @Autowired
    private SysSaleOrderMapper sysSaleOrderMapper;

    /**
     * 查询销售订单
     *
     * @param orderId 销售订单主键
     * @return 销售订单
     */
    @Override
    public SysSaleOrder selectSysSaleOrderByOrderId(Long orderId) {
        return sysSaleOrderMapper.selectSysSaleOrderByOrderId(orderId);
    }

    /**
     * 查询销售订单
     *
     * @param orderNo 销售订单主键
     * @return 销售订单
     */
    @Override
    public SysSaleOrder selectSysSaleOrderByOrderNo(String orderNo) {
        return sysSaleOrderMapper.selectSysSaleOrderByOrderNo(orderNo);
    }


    /**
     * 查询销售订单列表
     *
     * @param sysSaleOrder 销售订单
     * @return 销售订单
     */
    @Override
    public List<SysSaleOrder> selectSysSaleOrderList(SysSaleOrder sysSaleOrder) {
        return sysSaleOrderMapper.selectSysSaleOrderList(sysSaleOrder);
    }

    /**
     * 查询销售订单列表
     *
     * @param sysSaleOrder 销售订单
     * @return 销售订单
     */
    @Override
    public List<SysSaleOrder> selectSysSaleOrderQueryLimit(SysSaleOrder sysSaleOrder, int limit) {
        SysSaleOrderLimitVo order = new SysSaleOrderLimitVo(sysSaleOrder, limit);
        return sysSaleOrderMapper.selectSysSaleOrderQueryLimit(order);
    }

    /**
     * 新增销售订单
     *
     * @param sysSaleOrder 销售订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSysSaleOrder(SysSaleOrder sysSaleOrder) {
        sysSaleOrder.setCreateTime(DateUtils.getNowDate());
        int rows = sysSaleOrderMapper.insertSysSaleOrder(sysSaleOrder);
        insertSysSaleOrderItem(sysSaleOrder);
        return rows;
    }

    /**
     * 修改销售订单
     *
     * @param sysSaleOrder 销售订单
     * @return 结果
     */
    @Override
    public int updateSysSaleOrder(SysSaleOrder sysSaleOrder) {
        sysSaleOrder.setUpdateTime(DateUtils.getNowDate());
//        sysSaleOrderMapper.deleteSysSaleOrderItemByOrderId(sysSaleOrder.getOrderId());
//        insertSysSaleOrderItem(sysSaleOrder);
        return sysSaleOrderMapper.updateSysSaleOrder(sysSaleOrder);
    }

    /**
     * 批量删除销售订单
     *
     * @param orderIds 需要删除的销售订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysSaleOrderByOrderIds(Long[] orderIds) {
//        sysSaleOrderMapper.deleteSysSaleOrderItemByOrderIds(orderIds);
        return sysSaleOrderMapper.deleteSysSaleOrderByOrderIds(orderIds);
    }

    /**
     * 删除销售订单信息
     *
     * @param orderId 销售订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysSaleOrderByOrderId(Long orderId) {
//        sysSaleOrderMapper.deleteSysSaleOrderItemByOrderId(orderId);
        return sysSaleOrderMapper.deleteSysSaleOrderByOrderId(orderId);
    }

    /**
     * 新增销售订单详情信息
     *
     * @param sysSaleOrder 销售订单对象
     */
    public void insertSysSaleOrderItem(SysSaleOrder sysSaleOrder) {
        List<SysSaleOrderItem> sysSaleOrderItemList = sysSaleOrder.getSysSaleOrderItemList();
        Long orderId = sysSaleOrder.getOrderId();
        if (StringUtils.isNotNull(sysSaleOrderItemList)) {
            List<SysSaleOrderItem> list = new ArrayList<SysSaleOrderItem>();
            for (SysSaleOrderItem sysSaleOrderItem : sysSaleOrderItemList) {
                sysSaleOrderItem.setOrderId(orderId);
                list.add(sysSaleOrderItem);
            }
            if (!list.isEmpty()) {
                sysSaleOrderMapper.batchSysSaleOrderItem(list);
            }
        }
    }
}
