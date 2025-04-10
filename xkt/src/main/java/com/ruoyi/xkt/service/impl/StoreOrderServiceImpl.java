package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.domain.XktBaseEntity;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.express.ExpressContactDTO;
import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderAddResult;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.order.StoreOrderOperationRecordAddDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IExpressService;
import com.ruoyi.xkt.service.IOperationRecordService;
import com.ruoyi.xkt.service.IStoreOrderService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-02 13:19
 */
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private StoreOrderDetailMapper storeOrderDetailMapper;
    @Autowired
    private StoreProductMapper storeProductMapper;
    @Autowired
    private StoreProductColorSizeMapper storeProductColorSizeMapper;
    @Autowired
    private StoreProductColorPriceMapper storeProductColorPriceMapper;
    @Autowired
    private IExpressService expressService;
    @Autowired
    private IOperationRecordService operationRecordService;
    @Autowired
    private IVoucherSequenceService voucherSequenceService;
    @Autowired
    private List<PaymentManager> paymentManagers;

    @Transactional
    @Override
    public StoreOrderAddResult createOrder(StoreOrderAddDTO storeOrderAddDTO, boolean beginPay, EPayChannel payChannel,
                                           EPayPage payPage) {
        Long orderUserId = storeOrderAddDTO.getOrderUserId();
        Long storeId = storeOrderAddDTO.getStoreId();
        Long expressId = storeOrderAddDTO.getExpressId();
        //校验
        expressService.checkExpress(expressId);
        checkDelivery(storeOrderAddDTO.getDeliveryType(), storeOrderAddDTO.getDeliveryEndTime());
        Map<Long, StoreProductColorSize> spcsMap = checkOrderDetailThenRtnSpcsMap(storeId,
                storeOrderAddDTO.getDetailList());
        //快递费配置
        ExpressFeeConfig expressFeeConfig = expressService.getExpressFeeConfig(expressId,
                storeOrderAddDTO.getDestinationProvinceCode(), storeOrderAddDTO.getDestinationCityCode(),
                storeOrderAddDTO.getDestinationCountyCode());
        Assert.isTrue(BeanValidators.exists(expressFeeConfig), "无快递费用配置");
        boolean isFirstExpressItem = true;
        //生成订单明细
        List<StoreOrderDetail> orderDetailList = new ArrayList<>(storeOrderAddDTO.getDetailList().size());
        int orderGoodsQuantity = 0;
        BigDecimal orderGoodsAmount = BigDecimal.ZERO;
        BigDecimal orderExpressFee = BigDecimal.ZERO;
        for (StoreOrderAddDTO.Detail detail : storeOrderAddDTO.getDetailList()) {
            StoreProductColorSize spcs = spcsMap.get(detail.getStoreProdColorSizeId());
            StoreOrderDetail orderDetail = new StoreOrderDetail();
            orderDetailList.add(orderDetail);
            orderDetail.setStoreProdColorSizeId(spcs.getId());
            orderDetail.setStoreProdId(spcs.getStoreProdId());
            orderDetail.setDetailStatus(EOrderStatus.PENDING_PAYMENT.getValue());
            orderDetail.setPayStatus(beginPay ? EPayStatus.PAYING.getValue() : EPayStatus.INIT.getValue());
            orderDetail.setExpressStatus(EExpressStatus.INIT.getValue());
            //计算明细费用
            BigDecimal goodsPrice = calcPrice(orderUserId, spcs);
            Integer goodsQuantity = detail.getGoodsQuantity();
            BigDecimal goodsAmount = NumberUtil.mul(goodsPrice, BigDecimal.valueOf(goodsQuantity));
            BigDecimal expressFee;
            if (isFirstExpressItem) {
                if (goodsQuantity == 1) {
                    expressFee = expressFeeConfig.getFirstItemAmount();
                } else {
                    BigDecimal nextAmount = NumberUtil.mul(expressFeeConfig.getNextItemAmount(),
                            BigDecimal.valueOf(goodsQuantity - 1));
                    expressFee = NumberUtil.add(expressFeeConfig.getFirstItemAmount(), nextAmount);
                }
                isFirstExpressItem = false;
            } else {
                expressFee = NumberUtil.mul(expressFeeConfig.getNextItemAmount(), BigDecimal.valueOf(goodsQuantity));
            }
            BigDecimal totalAmount = NumberUtil.add(goodsAmount, expressFee);
            orderDetail.setGoodsPrice(goodsPrice);
            orderDetail.setGoodsQuantity(goodsQuantity);
            orderDetail.setGoodsAmount(goodsAmount);
            orderDetail.setExpressFee(expressFee);
            orderDetail.setTotalAmount(totalAmount);
            orderDetail.setDelFlag(Constants.UNDELETED);
            orderDetail.setVersion(0L);
            //计算订单费用
            orderGoodsQuantity = orderGoodsQuantity + goodsQuantity;
            orderGoodsAmount = NumberUtil.add(orderGoodsAmount, goodsAmount);
            orderExpressFee = NumberUtil.add(orderExpressFee, expressFee);
        }
        //生成订单号
        String orderNo = voucherSequenceService.generateCode(storeId, EVoucherSequenceType.STORE_ORDER.getValue(),
                DateUtil.today());
        StoreOrder order = new StoreOrder();
        order.setStoreId(storeId);
        order.setOrderUserId(storeOrderAddDTO.getOrderUserId());
        order.setOrderNo(orderNo);
        order.setOrderType(EOrderType.SALES_ORDER.getValue());
        order.setOrderStatus(EOrderStatus.PENDING_PAYMENT.getValue());
        order.setPayStatus(beginPay ? EPayStatus.PAYING.getValue() : EPayStatus.INIT.getValue());
        order.setOrderRemark(storeOrderAddDTO.getOrderRemark());
        order.setGoodsQuantity(orderGoodsQuantity);
        order.setGoodsAmount(orderGoodsAmount);
        order.setExpressFee(orderExpressFee);
        order.setTotalAmount(NumberUtil.add(orderGoodsAmount, orderExpressFee));
        order.setExpressId(expressId);
        //发货人信息
        ExpressContactDTO expressContactDTO = expressService.getStoreContact(storeId);
        order.setOriginContactName(expressContactDTO.getContactName());
        order.setOriginContactPhoneNumber(expressContactDTO.getContactPhoneNumber());
        order.setOriginProvinceCode(expressContactDTO.getProvinceCode());
        order.setOriginCityCode(expressContactDTO.getCityCode());
        order.setOriginCountyCode(expressContactDTO.getCountyCode());
        order.setOriginDetailAddress(expressContactDTO.getDetailAddress());
        //收货人信息
        order.setDestinationContactName(storeOrderAddDTO.getDestinationContactName());
        order.setDestinationContactPhoneNumber(storeOrderAddDTO.getDestinationContactPhoneNumber());
        order.setDestinationProvinceCode(storeOrderAddDTO.getDestinationProvinceCode());
        order.setDestinationCityCode(storeOrderAddDTO.getDestinationCityCode());
        order.setDestinationCountyCode(storeOrderAddDTO.getDestinationCountyCode());
        order.setDestinationDetailAddress(storeOrderAddDTO.getDestinationDetailAddress());
        order.setDeliveryType(storeOrderAddDTO.getDeliveryType());
        order.setDeliveryEndTime(storeOrderAddDTO.getDeliveryEndTime());
        order.setVoucherDate(DateUtil.date());
        order.setVersion(0L);
        order.setDelFlag(Constants.UNDELETED);
        //落库
        storeOrderMapper.insert(order);
        Long orderId = order.getId();
        List<Long> orderDetailIdList = new ArrayList<>(orderDetailList.size());
        orderDetailList.forEach(storeOrderDetail -> {
            storeOrderDetail.setStoreOrderId(orderId);
            storeOrderDetailMapper.insert(storeOrderDetail);
            orderDetailIdList.add(storeOrderDetail.getId());
        });
        //操作记录
        addOperationRecords(orderId, orderDetailIdList, orderUserId, new Date(), EOrderAction.ADD_ORDER);
        StoreOrderInfo orderInfo = new StoreOrderInfo(order, orderDetailList);
        String rtnStr = null;
        if (beginPay) {
            //发起支付
            PaymentManager paymentManager = getPaymentManager(payChannel);
            rtnStr = paymentManager.payOrder(orderInfo, payPage);
        }
        return new StoreOrderAddResult(orderInfo, rtnStr);
    }

    @Override
    public StoreOrder getByOrderNo(String orderNo) {
        Assert.notNull(orderNo);
        return storeOrderMapper.selectOne(Wrappers.lambdaQuery(StoreOrder.class)
                .eq(StoreOrder::getOrderNo, orderNo));
    }

    @Transactional
    @Override
    public StoreOrderInfo preparePayOrder(Long storeOrderId) {
        Assert.notNull(storeOrderId);
        StoreOrder order = storeOrderMapper.selectById(storeOrderId);
        Assert.isTrue(EOrderType.SALES_ORDER.getValue().equals(order.getOrderType()),
                "非销售订单无法发起支付");
        Assert.isTrue(BeanValidators.exists(order), "订单不存在");
        checkPreparePayStatus(order.getPayStatus());
        order.setPayStatus(EPayStatus.PAYING.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, storeOrderId)
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        for (StoreOrderDetail orderDetail : orderDetails) {
            checkPreparePayStatus(orderDetail.getPayStatus());
            orderDetail.setPayStatus(EPayStatus.PAYING.getValue());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
        }
        return new StoreOrderInfo(order, orderDetails);
    }

    @Transactional
    @Override
    public StoreOrderInfo paySuccess(Long storeOrderId) {
        Assert.notNull(storeOrderId);
        StoreOrder order = storeOrderMapper.selectById(storeOrderId);
        Assert.isTrue(EOrderType.SALES_ORDER.getValue().equals(order.getOrderType()),
                "订单类型异常");
        Assert.isTrue(BeanValidators.exists(order), "订单不存在");
        Assert.isTrue(order.getPayStatus().equals(EPayStatus.PAYING.getValue()), "订单支付状态异常");
        order.setPayStatus(EPayStatus.PAID.getValue());
        //TODO 暂时使用总金额
        order.setRealTotalAmount(order.getTotalAmount());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, storeOrderId)
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        for (StoreOrderDetail orderDetail : orderDetails) {
            Assert.isTrue(orderDetail.getPayStatus().equals(EPayStatus.PAYING.getValue()),
                    "订单明细支付状态异常");
            orderDetail.setPayStatus(EPayStatus.PAID.getValue());
            orderDetail.setRealTotalAmount(orderDetail.getTotalAmount());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(storeOrderId, orderDetailIdList, null, new Date(), EOrderAction.PAID_ORDER);
        return new StoreOrderInfo(order, orderDetails);
    }

    private void checkPreparePayStatus(Integer payStatus) {
        if (!EPayStatus.INIT.getValue().equals(payStatus) && !EPayStatus.PAYING.getValue().equals(payStatus)) {
            throw new ServiceException("订单状态异常无法发起支付");
        }
    }

    /**
     * 添加操作记录
     *
     * @param orderId
     * @param orderDetailIds
     * @param operatorId
     * @param operationTime
     * @param action
     */
    private void addOperationRecords(Long orderId, List<Long> orderDetailIds, Long operatorId, Date operationTime,
                                     EOrderAction action) {
        List<StoreOrderOperationRecordAddDTO> addDTOList = new ArrayList<>(1 +
                CollUtil.emptyIfNull(orderDetailIds).size());
        StoreOrderOperationRecordAddDTO addDTO = new StoreOrderOperationRecordAddDTO();
        addDTO.setTargetId(orderId);
        addDTO.setTargetType(EOrderTargetTypeAction.ORDER.getValue());
        addDTO.setAction(action.getValue());
        addDTO.setOperatorId(operatorId);
        addDTO.setOperationTime(operationTime);
        addDTOList.add(addDTO);
        for (Long orderDetailId : orderDetailIds) {
            StoreOrderOperationRecordAddDTO detailAddDTO = new StoreOrderOperationRecordAddDTO();
            detailAddDTO.setTargetId(orderDetailId);
            detailAddDTO.setTargetType(EOrderTargetTypeAction.ORDER_DETAIL.getValue());
            addDTO.setAction(action.getValue());
            addDTO.setOperatorId(operatorId);
            addDTO.setOperationTime(operationTime);
            addDTOList.add(addDTO);
        }
        operationRecordService.addOrderOperationRecords(addDTOList);
    }

    /**
     * 计算商品单价
     *
     * @param orderUserId
     * @param storeProductColorSize
     * @return
     */
    private BigDecimal calcPrice(Long orderUserId, StoreProductColorSize storeProductColorSize) {

        StoreProductColorPrice productColorPrice = storeProductColorPriceMapper.selectOne(Wrappers
                .lambdaQuery(StoreProductColorPrice.class)
                .eq(StoreProductColorPrice::getStoreProdId, storeProductColorSize.getStoreProdId())
                .eq(StoreProductColorPrice::getStoreColorId, storeProductColorSize.getStoreColorId())
                .eq(XktBaseEntity::getDelFlag, Constants.UNDELETED));
        Assert.notNull(productColorPrice, "无法获取商品定价");
        BigDecimal price = productColorPrice.getPrice();
        if (ProductSizeStatus.UN_STANDARD.getValue().equals(storeProductColorSize.getStandard())) {
            //非标准尺码
//            StoreProduct product = storeProductMapper.selectById(storeProductColorSize.getStoreProdId());
//            BigDecimal addPrice = BigDecimal.valueOf(NumberUtil.nullToZero(product.getOverPrice()));
//            price = NumberUtil.add(price, addPrice);
            //非标准尺码不存在线上下单
            throw new ServiceException("商品尺码异常");
        }
        return price;
    }

    /**
     * 检查发货配置
     *
     * @param deliveryType
     * @param deliveryEndTime
     */
    private void checkDelivery(Integer deliveryType, Date deliveryEndTime) {
        EDeliveryType deliveryTypeEnum = EDeliveryType.of(deliveryType);
        Assert.notNull(deliveryTypeEnum, "发货方式异常");
        if (deliveryEndTime != null
                && deliveryTypeEnum != EDeliveryType.PARTIAL_SHIPMENT) {
            throw new ServiceException("有货先发才能设置最晚发货时间");
        }
    }

    /**
     * 检查订单明细
     *
     * @param storeId
     * @param detailList
     * @return 商品颜色尺码集合
     */
    private Map<Long, StoreProductColorSize> checkOrderDetailThenRtnSpcsMap(Long storeId,
                                                                            List<StoreOrderAddDTO.Detail> detailList) {
        Assert.notNull(storeId, "档口不能为空");
        Assert.notEmpty(detailList, "商品不能为空");
        Set<Long> spcsIds = detailList.stream()
                .map(StoreOrderAddDTO.Detail::getStoreProdColorSizeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        //下单商品颜色尺码
        Map<Long, StoreProductColorSize> spcsMap = storeProductColorSizeMapper.selectByIds(spcsIds).stream()
                .collect(Collectors.toMap(StoreProductColorSize::getId, o -> o));
        List<Long> spIdList = spcsMap.values().stream()
                .map(StoreProductColorSize::getStoreProdId)
                .collect(Collectors.toList());
        //下单商品
        Map<Long, StoreProduct> spMap = storeProductMapper.selectByIds(spIdList).stream()
                .collect(Collectors.toMap(StoreProduct::getId, o -> o));
        Set<Long> spcsIdCheckSet = new HashSet<>(detailList.size());
        for (StoreOrderAddDTO.Detail detail : detailList) {
            Assert.notNull(detail.getStoreProdColorSizeId(), "商品颜色尺码异常");
            Integer goodsQuantity = detail.getGoodsQuantity();
            if (Objects.isNull(goodsQuantity) || goodsQuantity <= 0) {
                throw new IllegalArgumentException("商品数量异常");
            }
            StoreProductColorSize spcs = spcsMap.get(detail.getStoreProdColorSizeId());
            Assert.isTrue(BeanValidators.exists(spcs), "商品颜色尺码不存在");
            StoreProduct sp = spMap.get(spcs.getStoreProdId());
            Assert.isTrue(BeanValidators.exists(sp), "商品不存在");
            Assert.isTrue(storeId.equals(sp.getStoreId()), "系统不支持跨档口下单");
            Assert.isTrue(EProductStatus.accessOrder(sp.getProdStatus()), "商品状态异常");
            //相同商品颜色尺码只能存在一条明细
            Assert.isFalse(spcsIdCheckSet.contains(spcs.getId()), "商品明细异常");
            spcsIdCheckSet.add(spcs.getId());
        }
        return spcsMap;
    }

    /**
     * 根据支付渠道匹配支付类
     *
     * @param payChannel
     * @return
     */
    private PaymentManager getPaymentManager(EPayChannel payChannel) {
        if (payChannel == null) {
            throw new ServiceException("请先选择支付渠道");
        }
        for (PaymentManager paymentManager : paymentManagers) {
            if (paymentManager.channel() == payChannel) {
                return paymentManager;
            }
        }
        throw new ServiceException("未知支付渠道");
    }
}
