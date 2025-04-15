package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.domain.XktBaseEntity;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.express.ExpressContactDTO;
import com.ruoyi.xkt.dto.express.ExpressRegionDTO;
import com.ruoyi.xkt.dto.order.*;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IExpressService;
import com.ruoyi.xkt.service.IOperationRecordService;
import com.ruoyi.xkt.service.IStoreOrderService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.ORDER_NUM_1;

/**
 * @author liangyq
 * @date 2025-04-02 13:19
 */
@Slf4j
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private StoreOrderDetailMapper storeOrderDetailMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StoreProductMapper storeProductMapper;
    @Autowired
    private StoreProductColorSizeMapper storeProductColorSizeMapper;
    @Autowired
    private StoreProductColorPriceMapper storeProductColorPriceMapper;
    @Autowired
    private StoreColorMapper storeColorMapper;
    @Autowired
    private StoreProductFileMapper storeProductFileMapper;
    @Autowired
    private IExpressService expressService;
    @Autowired
    private IOperationRecordService operationRecordService;
    @Autowired
    private IVoucherSequenceService voucherSequenceService;
    @Autowired
    private List<PaymentManager> paymentManagers;
    @Autowired
    private List<ExpressManager> expressManagers;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderAddResult createOrder(StoreOrderAddDTO storeOrderAddDTO, boolean beginPay, EPayChannel payChannel,
                                           EPayPage payPage) {
        Long orderUserId = storeOrderAddDTO.getOrderUserId();
        Long storeId = storeOrderAddDTO.getStoreId();
        Long expressId = storeOrderAddDTO.getExpressId();
        //校验
        Assert.notNull(payChannel);
        expressService.checkExpress(expressId);
        checkDelivery(storeOrderAddDTO.getDeliveryType(), storeOrderAddDTO.getDeliveryEndTime());
        OrderDetailCheckRtn detailCheckRtn = checkOrderDetailThenRtnUsedMap(storeId, storeOrderAddDTO.getDetailList());
        Map<Long, StoreProductColorSize> spcsMap = detailCheckRtn.getSpcsMap();
        Map<Long, StoreProduct> spMap = detailCheckRtn.getSpMap();
        Map<Long, StoreColor> scMap = detailCheckRtn.getScMap();
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
        for (StoreOrderDetailAddDTO detail : storeOrderAddDTO.getDetailList()) {
            StoreProductColorSize spcs = spcsMap.get(detail.getStoreProdColorSizeId());
            StoreOrderDetail orderDetail = new StoreOrderDetail();
            orderDetailList.add(orderDetail);
            orderDetail.setStoreProdColorSizeId(spcs.getId());
            //快照部分
            StoreProduct sp = spMap.get(spcs.getStoreProdId());
            StoreColor sc = scMap.get(spcs.getStoreColorId());
            orderDetail.setStoreProdId(sp.getId());
            orderDetail.setProdName(sp.getProdName());
            orderDetail.setProdArtNum(sp.getProdArtNum());
            orderDetail.setProdTitle(sp.getProdTitle());
            orderDetail.setStoreColorId(sc.getId());
            orderDetail.setColorName(sc.getColorName());
            orderDetail.setSize(spcs.getSize());
            //状态
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
        order.setPayChannel(payChannel.getValue());
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
        addOperationRecords(orderId, EOrderAction.INSERT, orderDetailIdList, EOrderAction.INSERT, orderUserId,
                new Date());
        StoreOrderExt orderExt = new StoreOrderExt(order, orderDetailList);
        String rtnStr = null;
        if (beginPay) {
            //发起支付
            PaymentManager paymentManager = getPaymentManager(payChannel);
            rtnStr = paymentManager.payStoreOrder(orderExt, payPage);
        }
        return new StoreOrderAddResult(orderExt, rtnStr);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt modifyOrder(StoreOrderUpdateDTO storeOrderUpdateDTO) {
        //原订单
        StoreOrder order = getAndBaseCheck(storeOrderUpdateDTO.getId());
        Long orderUserId = storeOrderUpdateDTO.getOrderUserId();
        Long storeId = storeOrderUpdateDTO.getStoreId();
        Long expressId = storeOrderUpdateDTO.getExpressId();
        //校验
        if (!EOrderStatus.PENDING_PAYMENT.getValue().equals(order.getOrderStatus())
                && !EPayStatus.PAID.getValue().equals(order.getPayStatus())) {
            throw new ServiceException("订单[" + storeOrderUpdateDTO.getId() + "]已完成支付，无法修改");
        }
        expressService.checkExpress(expressId);
        checkDelivery(storeOrderUpdateDTO.getDeliveryType(), storeOrderUpdateDTO.getDeliveryEndTime());
        OrderDetailCheckRtn detailCheckRtn = checkOrderDetailThenRtnUsedMap(storeId,
                storeOrderUpdateDTO.getDetailList());
        Map<Long, StoreProductColorSize> spcsMap = detailCheckRtn.getSpcsMap();
        Map<Long, StoreProduct> spMap = detailCheckRtn.getSpMap();
        Map<Long, StoreColor> scMap = detailCheckRtn.getScMap();
        //快递费配置
        ExpressFeeConfig expressFeeConfig = expressService.getExpressFeeConfig(expressId,
                storeOrderUpdateDTO.getDestinationProvinceCode(), storeOrderUpdateDTO.getDestinationCityCode(),
                storeOrderUpdateDTO.getDestinationCountyCode());
        Assert.isTrue(BeanValidators.exists(expressFeeConfig), "无快递费用配置");
        boolean isFirstExpressItem = true;
        //生成订单明细
        List<StoreOrderDetail> orderDetailList = new ArrayList<>(storeOrderUpdateDTO.getDetailList().size());
        int orderGoodsQuantity = 0;
        BigDecimal orderGoodsAmount = BigDecimal.ZERO;
        BigDecimal orderExpressFee = BigDecimal.ZERO;
        for (StoreOrderDetailAddDTO detail : storeOrderUpdateDTO.getDetailList()) {
            StoreProductColorSize spcs = spcsMap.get(detail.getStoreProdColorSizeId());
            StoreOrderDetail orderDetail = new StoreOrderDetail();
            orderDetailList.add(orderDetail);
            orderDetail.setStoreProdColorSizeId(spcs.getId());
            //快照部分
            StoreProduct sp = spMap.get(spcs.getStoreProdId());
            StoreColor sc = scMap.get(spcs.getStoreColorId());
            orderDetail.setStoreProdId(sp.getId());
            orderDetail.setProdName(sp.getProdName());
            orderDetail.setProdArtNum(sp.getProdArtNum());
            orderDetail.setProdTitle(sp.getProdTitle());
            orderDetail.setStoreColorId(sc.getId());
            orderDetail.setColorName(sc.getColorName());
            orderDetail.setSize(spcs.getSize());
            //状态
            orderDetail.setDetailStatus(EOrderStatus.PENDING_PAYMENT.getValue());
            orderDetail.setPayStatus(order.getPayStatus());
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
        order.setStoreId(storeId);
        order.setOrderUserId(orderUserId);
        order.setOrderRemark(storeOrderUpdateDTO.getOrderRemark());
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
        order.setDestinationContactName(storeOrderUpdateDTO.getDestinationContactName());
        order.setDestinationContactPhoneNumber(storeOrderUpdateDTO.getDestinationContactPhoneNumber());
        order.setDestinationProvinceCode(storeOrderUpdateDTO.getDestinationProvinceCode());
        order.setDestinationCityCode(storeOrderUpdateDTO.getDestinationCityCode());
        order.setDestinationCountyCode(storeOrderUpdateDTO.getDestinationCountyCode());
        order.setDestinationDetailAddress(storeOrderUpdateDTO.getDestinationDetailAddress());
        order.setDeliveryType(storeOrderUpdateDTO.getDeliveryType());
        order.setDeliveryEndTime(storeOrderUpdateDTO.getDeliveryEndTime());
        //落库
        int r = storeOrderMapper.updateById(order);
        if (r == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //删除原明细
        StoreOrderDetail delete = new StoreOrderDetail();
        delete.setDelFlag(Constants.DELETED);
        storeOrderDetailMapper.update(delete, Wrappers.lambdaUpdate(StoreOrderDetail.class)
                .eq(StoreOrderDetail::getStoreOrderId, order.getId()));
        //重新生成明细
        Long orderId = order.getId();
        List<Long> orderDetailIdList = new ArrayList<>(orderDetailList.size());
        orderDetailList.forEach(storeOrderDetail -> {
            storeOrderDetail.setStoreOrderId(orderId);
            storeOrderDetailMapper.insert(storeOrderDetail);
            orderDetailIdList.add(storeOrderDetail.getId());
        });
        //操作记录
        addOperationRecords(orderId, EOrderAction.UPDATE, orderDetailIdList, EOrderAction.INSERT, orderUserId,
                new Date());
        return new StoreOrderExt(order, orderDetailList);
    }

    @Override
    public StoreOrder getByOrderNo(String orderNo) {
        Assert.notNull(orderNo);
        return storeOrderMapper.selectOne(Wrappers.lambdaQuery(StoreOrder.class)
                .eq(StoreOrder::getOrderNo, orderNo));
    }

    @Override
    public StoreOrderInfoDTO getInfo(Long storeOrderId) {
        StoreOrder order = storeOrderMapper.selectById(storeOrderId);
        if (order == null) {
            return null;
        }
        List<StoreOrderDetail> details = storeOrderDetailMapper.selectList(Wrappers.lambdaQuery(StoreOrderDetail.class)
                .eq(StoreOrderDetail::getStoreOrderId, storeOrderId)
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        StoreOrderInfoDTO orderInfo = BeanUtil.toBean(order, StoreOrderInfoDTO.class);
        List<StoreOrderDetailInfoDTO> detailInfos = BeanUtil.copyToList(details,
                StoreOrderDetailInfoDTO.class);
        orderInfo.setOrderDetails(detailInfos);
        //档口信息
        Store store = storeMapper.selectById(order.getStoreId());
        if (store != null) {
            orderInfo.setStoreName(store.getStoreName());
            orderInfo.setBrandName(store.getBrandName());
        }
        //物流信息
        Express express = expressService.getById(order.getExpressId());
        orderInfo.setExpressName(Optional.ofNullable(express).map(Express::getExpressName).orElse(null));
        Map<String, ExpressRegionDTO> regionMap = expressService.getRegionMapCache();
        orderInfo.setDestinationProvinceName(Optional.ofNullable(regionMap.get(orderInfo.getDestinationProvinceCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        orderInfo.setDestinationCityName(Optional.ofNullable(regionMap.get(orderInfo.getDestinationCityCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        orderInfo.setDestinationCountyName(Optional.ofNullable(regionMap.get(orderInfo.getDestinationCountyCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        orderInfo.setOriginProvinceName(Optional.ofNullable(regionMap.get(orderInfo.getOriginProvinceCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        orderInfo.setOriginCityName(Optional.ofNullable(regionMap.get(orderInfo.getOriginCityCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        orderInfo.setOriginCountyName(Optional.ofNullable(regionMap.get(orderInfo.getOriginCountyCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        //商品信息
        List<Long> spIds = detailInfos.stream().map(StoreOrderDetailInfoDTO::getStoreProdId).distinct()
                .collect(Collectors.toList());
        Map<Long, String> mainPicMap = storeProductFileMapper.selectMainPicByStoreProdIdList(spIds,
                FileType.MAIN_PIC.getValue(), ORDER_NUM_1).stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl,
                        (o, n) -> n));
        for (StoreOrderDetailInfoDTO detailInfo : detailInfos) {
            detailInfo.setFirstMainPicUrl(mainPicMap.get(detailInfo.getStoreProdId()));
        }
        return orderInfo;
    }

    @Override
    public Page<StoreOrderPageItemDTO> page(StoreOrderQueryDTO queryDTO) {
        Page<StoreOrderPageItemDTO> page = PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        storeOrderMapper.listStoreOrderPageItem(queryDTO);
        if (CollUtil.isNotEmpty(page.getResult())) {
            List<StoreOrderPageItemDTO> list = page.getResult();
            Set<Long> soIds = list.stream().map(StoreOrderPageItemDTO::getId).collect(Collectors.toSet());
            List<StoreOrderDetail> orderDetailList = storeOrderDetailMapper.selectList(Wrappers
                    .lambdaQuery(StoreOrderDetail.class).eq(SimpleEntity::getDelFlag, Constants.UNDELETED)
                    .in(StoreOrderDetail::getStoreOrderId, soIds));
            List<Long> spIds = orderDetailList.stream().map(StoreOrderDetail::getStoreProdId).distinct()
                    .collect(Collectors.toList());
            Map<Long, String> mainPicMap = storeProductFileMapper.selectMainPicByStoreProdIdList(spIds,
                    FileType.MAIN_PIC.getValue(), ORDER_NUM_1).stream()
                    .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl,
                            (o, n) -> n));
            Map<Long, List<StoreOrderPageItemDTO.Detail>> orderDetailGroup = BeanUtil.copyToList(orderDetailList,
                    StoreOrderPageItemDTO.Detail.class)
                    .stream()
                    .collect(Collectors.groupingBy(StoreOrderDetailDTO::getStoreOrderId));
            Map<String, ExpressRegionDTO> regionMap = expressService.getRegionMapCache();
            for (StoreOrderPageItemDTO order : list) {
                //物流信息
                order.setDestinationProvinceName(Optional.ofNullable(regionMap.get(order.getDestinationProvinceCode()))
                        .map(ExpressRegionDTO::getParentRegionName).orElse(null));
                order.setDestinationCityName(Optional.ofNullable(regionMap.get(order.getDestinationCityCode()))
                        .map(ExpressRegionDTO::getParentRegionName).orElse(null));
                order.setDestinationCountyName(Optional.ofNullable(regionMap.get(order.getDestinationCountyCode()))
                        .map(ExpressRegionDTO::getParentRegionName).orElse(null));
                order.setOriginProvinceName(Optional.ofNullable(regionMap.get(order.getOriginProvinceCode()))
                        .map(ExpressRegionDTO::getParentRegionName).orElse(null));
                order.setOriginCityName(Optional.ofNullable(regionMap.get(order.getOriginCityCode()))
                        .map(ExpressRegionDTO::getParentRegionName).orElse(null));
                order.setOriginCountyName(Optional.ofNullable(regionMap.get(order.getOriginCountyCode()))
                        .map(ExpressRegionDTO::getParentRegionName).orElse(null));
                order.setOrderDetails(orderDetailGroup.get(order.getId()));
                for (StoreOrderPageItemDTO.Detail detail : order.getOrderDetails()) {
                    //首图
                    detail.setFirstMainPicUrl(mainPicMap.get(detail.getStoreProdId()));
                }
            }
        }
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt preparePayOrder(Long storeOrderId, EPayChannel payChannel) {
        Assert.notNull(payChannel);
        StoreOrder order = getAndBaseCheck(storeOrderId);
        Assert.isTrue(EOrderType.SALES_ORDER.getValue().equals(order.getOrderType()),
                "非销售订单无法发起支付");
        if (payChannel != EPayChannel.of(order.getPayChannel())) {
            throw new ServiceException("订单支付渠道不允许修改");
        }
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
        return new StoreOrderExt(order, orderDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt paySuccess(Long storeOrderId, BigDecimal totalAmount, BigDecimal realTotalAmount) {
        StoreOrder order = getAndBaseCheck(storeOrderId);
        Assert.isTrue(EOrderType.SALES_ORDER.getValue().equals(order.getOrderType()),
                "订单类型异常");
        if (!order.getPayStatus().equals(EPayStatus.PAYING.getValue())
                || !order.getOrderStatus().equals(EOrderStatus.PENDING_PAYMENT.getValue())) {
            log.error("订单状态异常，更新支付结果失败: id = {}", storeOrderId);
            throw new ServiceException("订单状态异常");
        }
        if (NumberUtil.equals(order.getTotalAmount(), totalAmount)) {
            log.error("订单支付金额异常，更新支付结果失败: id = {} totalAmount = {} realTotalAmount = {}",
                    storeOrderId, totalAmount, realTotalAmount);
            throw new ServiceException("订单支付金额异常");
        }
        order.setOrderStatus(EOrderStatus.PENDING_SHIPMENT.getValue());
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
            if (!orderDetail.getPayStatus().equals(EPayStatus.PAYING.getValue())
                    || !orderDetail.getDetailStatus().equals(EOrderStatus.PENDING_PAYMENT.getValue())) {
                log.error("订单明细状态异常，更新支付结果失败: id = {}", storeOrderId);
                throw new ServiceException("订单明细状态异常");
            }
            orderDetail.setDetailStatus(EOrderStatus.PENDING_SHIPMENT.getValue());
            orderDetail.setPayStatus(EPayStatus.PAID.getValue());
            orderDetail.setRealTotalAmount(orderDetail.getTotalAmount());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(storeOrderId, EOrderAction.PAY, orderDetailIdList, EOrderAction.PAY, null,
                new Date());
        return new StoreOrderExt(order, orderDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelOrder(OrderOptDTO opt) {
        StoreOrder order = getAndBaseCheck(opt.getStoreOrderId());
        EOrderStatus oStatus = EOrderStatus.of(order.getOrderStatus());
        EPayStatus pStatus = EPayStatus.of(order.getPayStatus());
        if (EOrderStatus.PENDING_PAYMENT != oStatus) {
            throw new ServiceException("订单[" + order.getOrderNo() + "]状态为[" + oStatus.getLabel() + "]，无法取消");
        }
        if (EPayStatus.PAID == pStatus) {
            throw new ServiceException("订单[" + order.getOrderNo() + "]已支付完成，无法取消");
        }
        PaymentManager paymentManager = getPaymentManager(EPayChannel.of(order.getPayChannel()));
        boolean isPaid = paymentManager.isStoreOrderPaid(order.getOrderNo());
        if (isPaid) {
            throw new ServiceException("订单[" + order.getOrderNo() + "]已支付，无法取消");
        }
        //订单已取消
        order.setOrderStatus(EOrderStatus.CANCELLED.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        for (StoreOrderDetail orderDetail : orderDetails) {
            //明细已取消
            orderDetail.setDetailStatus(EOrderStatus.CANCELLED.getValue());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.CANCEL, orderDetailIdList, EOrderAction.CANCEL, opt.getRemark(),
                opt.getOperatorId(), new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt prepareShipOrderByPlatform(Long storeOrderId, List<Long> storeOrderDetailIds, Long expressId,
                                                    Long operatorId) {
        Assert.notEmpty(storeOrderDetailIds);
//        ExpressManager expressManager = getExpressManager(expressId);
        Express express = expressService.getById(expressId);
        if (!BeanValidators.exists(express) || !express.getSystemDeliverAccess()) {
            throw new ServiceException("快递[" + expressId + "]不可用");
        }
        StoreOrder order = getAndBaseCheck(storeOrderId);
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())
                && !EOrderStatus.SHIPPED.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException("订单[" + order.getId() + "]当前状态无法发货");
        }
        List<StoreOrderDetail> containDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        for (StoreOrderDetail containDetail : containDetails) {
            if (EDeliveryType.SHIP_COMPLETE.getValue().equals(order.getDeliveryType())) {
                //如果是货齐再发，此次发货需要包含所有明细
                if (!storeOrderDetailIds.contains(containDetail.getId())) {
                    throw new ServiceException("订单[" + order.getId() + "]不可拆单发货");
                }
            }
            if (EExpressType.STORE.getValue().equals(containDetail.getExpressType())) {
                //已存在档口发货的明细
                throw new ServiceException("订单[" + order.getId() + "]由档口物流发货！");
            }
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectByIds(storeOrderDetailIds);
        //订单->已发货
        order.setOrderStatus(EOrderStatus.SHIPPED.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //生成请求号
        String expressReqNo = IdUtil.simpleUUID();
        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        for (StoreOrderDetail orderDetail : orderDetails) {
            if (!BeanValidators.exists(orderDetail)) {
                throw new ServiceException("订单明细[" + orderDetail.getId() + "]不存在");
            }
            if (!order.getId().equals(orderDetail.getStoreOrderId())) {
                throw new ServiceException("发货订单[" + order.getId() + "]与明细[" + orderDetail.getId() + "]不匹配");
            }
            if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(orderDetail.getDetailStatus())) {
                throw new ServiceException("订单明细[" + order.getId() + "]当前状态无法发货");
            }
            //明细->已发货
            orderDetail.setDetailStatus(EOrderStatus.SHIPPED.getValue());
            orderDetail.setExpressId(expressId);
            orderDetail.setExpressType(EExpressType.PLATFORM.getValue());
            orderDetail.setExpressStatus(EExpressStatus.PLACING.getValue());
            orderDetail.setExpressReqNo(expressReqNo);
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.SHIP, orderDetailIdList, EOrderAction.SHIP,
                "平台物流发货", operatorId, new Date());
        return new StoreOrderExt(order, orderDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt shipOrderByStore(Long storeOrderId, List<Long> storeOrderDetailIds, Long expressId,
                                          String expressWaybillNo, Long operatorId) {
        Assert.notEmpty(storeOrderDetailIds);
        Express express = expressService.getById(expressId);
        if (!BeanValidators.exists(express) || !express.getStoreDeliverAccess()) {
            throw new ServiceException("快递[" + expressId + "]不可用");
        }
        StoreOrder order = getAndBaseCheck(storeOrderId);
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())
                && !EOrderStatus.SHIPPED.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException("订单[" + order.getId() + "]当前状态无法发货");
        }
        List<StoreOrderDetail> containDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        for (StoreOrderDetail containDetail : containDetails) {
            if (EDeliveryType.SHIP_COMPLETE.getValue().equals(order.getDeliveryType())) {
                //如果是货齐再发，此次发货需要包含所有明细
                if (!storeOrderDetailIds.contains(containDetail.getId())) {
                    throw new ServiceException("订单[" + order.getId() + "]不可拆单发货");
                }
            }
            if (EExpressType.PLATFORM.getValue().equals(containDetail.getExpressType())) {
                //已存在平台发货的明细
                throw new ServiceException("订单[" + order.getId() + "]由平台物流发货！");
            }
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectByIds(storeOrderDetailIds);
        //订单->已发货
        order.setOrderStatus(EOrderStatus.SHIPPED.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        for (StoreOrderDetail orderDetail : orderDetails) {
            if (!BeanValidators.exists(orderDetail)) {
                throw new ServiceException("订单明细[" + orderDetail.getId() + "]不存在");
            }
            if (!order.getId().equals(orderDetail.getStoreOrderId())) {
                throw new ServiceException("发货订单[" + order.getId() + "]与明细[" + orderDetail.getId() + "]不匹配");
            }
            if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(orderDetail.getDetailStatus())) {
                throw new ServiceException("订单明细[" + order.getId() + "]当前状态无法发货");
            }
            //明细->已发货
            orderDetail.setDetailStatus(EOrderStatus.SHIPPED.getValue());
            orderDetail.setExpressId(expressId);
            orderDetail.setExpressType(EExpressType.STORE.getValue());
            orderDetail.setExpressStatus(EExpressStatus.COMPLETED.getValue());
            orderDetail.setExpressWaybillNo(expressWaybillNo);
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.SHIP, orderDetailIdList, EOrderAction.SHIP,
                "档口物流发货", operatorId, new Date());
        return new StoreOrderExt(order, orderDetails);
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
     * @param orderAction
     * @param orderDetailIds
     * @param detailAction
     * @param operatorId
     * @param operationTime
     */
    private void addOperationRecords(Long orderId, EOrderAction orderAction, List<Long> orderDetailIds,
                                     EOrderAction detailAction, Long operatorId, Date operationTime) {
        addOperationRecords(orderId, orderAction, orderDetailIds, detailAction, null, operatorId, operationTime);
    }

    /**
     * 添加操作记录
     *
     * @param orderId
     * @param orderAction
     * @param orderDetailIds
     * @param detailAction
     * @param remark
     * @param operatorId
     * @param operationTime
     */
    private void addOperationRecords(Long orderId, EOrderAction orderAction, List<Long> orderDetailIds,
                                     EOrderAction detailAction, String remark, Long operatorId, Date operationTime) {
        List<StoreOrderOperationRecordAddDTO> addDTOList = new ArrayList<>();
        if (orderId != null) {
            StoreOrderOperationRecordAddDTO addDTO = new StoreOrderOperationRecordAddDTO();
            addDTO.setTargetId(orderId);
            addDTO.setTargetType(EOrderTargetTypeAction.ORDER.getValue());
            addDTO.setAction(orderAction.getValue());
            addDTO.setRemark(remark);
            addDTO.setOperatorId(operatorId);
            addDTO.setOperationTime(operationTime);
            addDTOList.add(addDTO);
        }
        if (CollUtil.isNotEmpty(orderDetailIds)) {
            for (Long orderDetailId : orderDetailIds) {
                StoreOrderOperationRecordAddDTO detailAddDTO = new StoreOrderOperationRecordAddDTO();
                detailAddDTO.setTargetId(orderDetailId);
                detailAddDTO.setTargetType(EOrderTargetTypeAction.ORDER_DETAIL.getValue());
                detailAddDTO.setAction(detailAction.getValue());
                detailAddDTO.setRemark(remark);
                detailAddDTO.setOperatorId(operatorId);
                detailAddDTO.setOperationTime(operationTime);
                addDTOList.add(detailAddDTO);
            }
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
        if (deliveryEndTime != null) {
            if (deliveryTypeEnum != EDeliveryType.PARTIAL_SHIPMENT) {
                throw new ServiceException("有货先发才能设置最晚发货时间");
            }
            if (deliveryEndTime.before(new Date())) {
                throw new ServiceException("最晚发货时间不能早于当前时间");
            }
        }
    }

    /**
     * 检查订单明细
     *
     * @param storeId
     * @param detailList
     * @return 商品颜色尺码集合
     */
    private OrderDetailCheckRtn checkOrderDetailThenRtnUsedMap(Long storeId,
                                                               List<StoreOrderDetailAddDTO> detailList) {
        Assert.notNull(storeId, "档口不能为空");
        Assert.notEmpty(detailList, "商品不能为空");
        Set<Long> spcsIds = detailList.stream()
                .map(StoreOrderDetailAddDTO::getStoreProdColorSizeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        //下单商品颜色尺码
        Map<Long, StoreProductColorSize> spcsMap = storeProductColorSizeMapper.selectByIds(spcsIds).stream()
                .collect(Collectors.toMap(StoreProductColorSize::getId, o -> o));
        //下单商品档口颜色
        Map<Long, StoreColor> scMap = storeColorMapper.selectByIds(spcsMap.values().stream()
                .map(StoreProductColorSize::getStoreColorId)
                .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(StoreColor::getId, Function.identity()));
        //下单商品
        List<Long> spIdList = spcsMap.values().stream()
                .map(StoreProductColorSize::getStoreProdId)
                .collect(Collectors.toList());
        Map<Long, StoreProduct> spMap = storeProductMapper.selectByIds(spIdList).stream()
                .collect(Collectors.toMap(StoreProduct::getId, o -> o));
        Set<Long> spcsIdCheckSet = new HashSet<>(detailList.size());
        for (StoreOrderDetailAddDTO detail : detailList) {
            Assert.notNull(detail.getStoreProdColorSizeId(), "商品颜色尺码异常");
            Integer goodsQuantity = detail.getGoodsQuantity();
            if (Objects.isNull(goodsQuantity) || goodsQuantity <= 0) {
                throw new IllegalArgumentException("商品数量异常");
            }
            StoreProductColorSize spcs = spcsMap.get(detail.getStoreProdColorSizeId());
            Assert.isTrue(BeanValidators.exists(spcs), "商品颜色尺码不存在");
            StoreColor sc = scMap.get(spcs.getStoreColorId());
            Assert.isTrue(BeanValidators.exists(sc), "商品颜色不存在");
            StoreProduct sp = spMap.get(spcs.getStoreProdId());
            Assert.isTrue(BeanValidators.exists(sp), "商品不存在");
            Assert.isTrue(storeId.equals(sp.getStoreId()), "系统不支持跨档口下单");
            Assert.isTrue(EProductStatus.accessOrder(sp.getProdStatus()), "商品状态异常");
            //相同商品颜色尺码只能存在一条明细
            Assert.isFalse(spcsIdCheckSet.contains(spcs.getId()), "商品明细异常");
            spcsIdCheckSet.add(spcs.getId());
        }
        return new OrderDetailCheckRtn(spcsMap, spMap, scMap);
    }

    private StoreOrder getAndBaseCheck(Long storeOrderId) {
        Assert.notNull(storeOrderId);
        StoreOrder order = storeOrderMapper.selectById(storeOrderId);
        if (!BeanValidators.exists(order)) {
            throw new ServiceException("订单[" + storeOrderId + "]不存在");
        }
        return order;
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

    /**
     * 匹配物流类
     *
     * @param expressId
     * @return
     */
    private ExpressManager getExpressManager(Long expressId) {
        Assert.notNull(expressId);
        for (ExpressManager expressManager : expressManagers) {
            if (expressManager.channel().getExpressId().equals(expressId)) {
                return expressManager;
            }
        }
        throw new ServiceException("未知物流渠道");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class OrderDetailCheckRtn {
        private Map<Long, StoreProductColorSize> spcsMap;
        private Map<Long, StoreProduct> spMap;
        private Map<Long, StoreColor> scMap;
    }
}
