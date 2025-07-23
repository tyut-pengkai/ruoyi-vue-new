package com.ruoyi.xkt.service;

import com.github.pagehelper.Page;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.express.ExpressShippingLabelDTO;
import com.ruoyi.xkt.dto.express.ExpressTrackDTO;
import com.ruoyi.xkt.dto.order.*;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
     * 通过ID获取订单
     *
     * @param id
     * @return
     */
    StoreOrder getById(Long id);

    /**
     * 获取订单详情
     *
     * @param storeOrderId
     * @return
     */
    StoreOrderInfoDTO getInfo(Long storeOrderId);

    /**
     * 获取订单轨迹
     *
     * @param storeOrderId
     * @return
     */
    List<ExpressTrackDTO> getOrderExpressTracks(Long storeOrderId);

    /**
     * 面单预备原单号打印项列表
     *
     * @param storeOrderId
     * @return
     */
    List<ShipLabelPreOrgPrintItemDTO> listPreOrgPrintItem(Long storeOrderId);

    /**
     * 原单号打印
     *
     * @param expressWaybillNos
     * @return
     */
    List<ExpressShippingLabelDTO> printOrgShippingLabel(Collection<String> expressWaybillNos);

    /**
     * 分页查询订单
     *
     * @param queryDTO
     * @return
     */
    Page<StoreOrderPageItemDTO> page(StoreOrderQueryDTO queryDTO);

    /**
     * 订单统计
     *
     * @param queryDTO
     * @return
     */
    StoreOrderCountDTO count(StoreOrderCountQueryDTO queryDTO);

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
     * @param payTradeNo
     * @param totalAmount
     * @param realTotalAmount
     * @return
     */
    StoreOrderExt paySuccess(Long storeOrderId, String payTradeNo, BigDecimal totalAmount, BigDecimal realTotalAmount);

    /**
     * 取消订单
     *
     * @param opt
     */
    void cancelOrder(StoreOrderCancelDTO opt);

    /**
     * 发货（平台物流）
     *
     * @param storeOrderId
     * @param storeOrderDetailIds
     * @param operatorId
     * @return
     */
    StoreOrderExt shipOrderByPlatform(Long storeOrderId, List<Long> storeOrderDetailIds, Long operatorId);

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

    /**
     * 打印面单
     *
     * @param storeOrderId
     * @param storeOrderDetailIds
     * @param expressId
     * @param needShip
     * @param operatorId
     * @return
     */
    ExpressShippingLabelDTO printOrder(Long storeOrderId, List<Long> storeOrderDetailIds, Long expressId,
                                       Boolean needShip, Long operatorId);

    /**
     * 确认收货
     *
     * @param storeOrderId
     * @param operatorId
     * @return
     */
    StoreOrderExt receiptOrder(Long storeOrderId, Long operatorId);

    /**
     * 完成订单
     *
     * @param storeOrderId
     * @param operatorId
     * @return
     */
    StoreOrderExt completeOrder(Long storeOrderId, Long operatorId);

    /**
     * 创建售后订单
     *
     * @param afterSaleDTO
     * @return
     */
    AfterSaleApplyResultDTO createAfterSaleOrder(StoreOrderAfterSaleDTO afterSaleDTO);

    /**
     * 拒绝退款
     *
     * @param refundRejectDTO
     */
    void rejectRefundOrder(StoreOrderRefundRejectDTO refundRejectDTO);

    /**
     * 准备退款
     *
     * @param refundConfirmDTO
     * @return
     */
    StoreOrderRefund prepareRefundOrder(StoreOrderRefundConfirmDTO refundConfirmDTO);

    /**
     * 退款成功
     *
     * @param storeOrderId
     * @param storeOrderDetailIds
     * @param operatorId
     */
    void refundSuccess(Long storeOrderId, List<Long> storeOrderDetailIds, Long operatorId);

    /**
     * 添加轨迹信息
     *
     * @param trackAddDTO
     */
    void addTrack(StoreOrderExpressTrackAddDTO trackAddDTO);

    /**
     * 获取需要自动关闭的订单
     *
     * @param beforeDate
     * @param count
     * @return
     */
    List<Long> listNeedAutoCloseOrder(Date beforeDate, Integer count);

    /**
     * 获取需要继续退款的订单信息
     *
     * @param count
     * @return
     */
    List<StoreOrderRefund> listNeedContinueRefundOrder(Integer count);

    /**
     * 订单是否属于用户
     *
     * @param storeOrderId
     * @param userId
     */
    void checkOrderUser(Long storeOrderId, Long userId);

    /**
     * 订单是否属于档口
     *
     * @param storeOrderId
     * @param storeId
     */
    void checkOrderStore(Long storeOrderId, Long storeId);

    /**
     * 订单明细是否属于档口
     *
     * @param storeOrderDetailIds
     * @param storeId
     */
    void checkOrderStore(Collection<Long> storeOrderDetailIds, Long storeId);

    /**
     * 申请平台介入
     *
     * @param storeOrderId
     * @param platformInvolveReason
     */
    void applyPlatformInvolve(Long storeOrderId, String platformInvolveReason);

    /**
     * 平台介入完成
     *
     * @param storeOrderId
     * @param platformInvolveResult
     */
    void completePlatformInvolve(Long storeOrderId, String platformInvolveResult);

    /**
     * 售后完成
     *
     * @param storeOrderId
     */
    void completeRefundByUser(Long storeOrderId);
}
