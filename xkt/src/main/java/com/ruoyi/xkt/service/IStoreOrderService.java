package com.ruoyi.xkt.service;

import com.github.pagehelper.Page;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.*;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 13:16
 */
public interface IStoreOrderService {
    /**
     * 创建订单
     *
     * @param storeOrderAddDTO 订单信息
     * @param beginPay         是否发起支付
     * @param payChannel       支付渠道
     * @param payPage          支付来源
     * @return
     */
    StoreOrderAddResult createOrder(StoreOrderAddDTO storeOrderAddDTO, boolean beginPay, EPayChannel payChannel,
                                    EPayPage payPage);

    /**
     * 更新订单
     *
     * @param storeOrderUpdateDTO
     * @return
     */
    StoreOrderExt modifyOrder(StoreOrderUpdateDTO storeOrderUpdateDTO);

    /**
     * 通过订单号获取订单
     *
     * @param orderNo
     * @return
     */
    StoreOrder getByOrderNo(String orderNo);

    /**
     * 获取订单详情
     *
     * @param storeOrderId
     * @return
     */
    StoreOrderInfoDTO getInfo(Long storeOrderId);

    /**
     * 分页查询订单
     *
     * @param queryDTO
     * @return
     */
    Page<StoreOrderPageItemDTO> page(StoreOrderQueryDTO queryDTO);

    /**
     * 准备支付订单
     *
     * @param storeOrderId
     * @param payChannel
     */
    StoreOrderExt preparePayOrder(Long storeOrderId, EPayChannel payChannel);

    /**
     * 订单支付成果
     * TODO 更新扣除手续费后的金额
     *
     * @param storeOrderId
     * @param totalAmount
     * @param realTotalAmount
     * @return
     */
    StoreOrderExt paySuccess(Long storeOrderId, BigDecimal totalAmount, BigDecimal realTotalAmount);

    /**
     * 取消订单
     *
     * @param opt
     */
    void cancelOrder(OrderOptDTO opt);

    /**
     * 准备发货（平台物流）
     *
     * @param storeOrderId
     * @param storeOrderDetailIds
     * @param expressId
     * @param operatorId
     * @return
     */
    StoreOrderExt shipOrderByPlatform(Long storeOrderId, List<Long> storeOrderDetailIds, Long expressId,
                                      Long operatorId);

    /**
     * 发货（档口物流）
     *
     * @param storeOrderId
     * @param storeOrderDetailIds
     * @param expressId
     * @param expressWaybillNo
     * @param operatorId
     * @return
     */
    StoreOrderExt shipOrderByStore(Long storeOrderId, List<Long> storeOrderDetailIds, Long expressId,
                                   String expressWaybillNo, Long operatorId);
}
