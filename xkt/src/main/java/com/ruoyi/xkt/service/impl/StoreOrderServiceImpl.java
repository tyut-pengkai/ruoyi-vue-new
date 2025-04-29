package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
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
import com.ruoyi.xkt.dto.express.ExpressPrintDTO;
import com.ruoyi.xkt.dto.express.ExpressRegionDTO;
import com.ruoyi.xkt.dto.express.ExpressShipReqDTO;
import com.ruoyi.xkt.dto.order.*;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.*;
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
    private IFinanceBillService financeBillService;
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
            throw new ServiceException(CharSequenceUtil.format("订单[{}]已完成支付，无法修改",
                    storeOrderUpdateDTO.getId()));
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
        if (!EOrderStatus.PENDING_PAYMENT.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]当前状态无法支付", order.getId()));
        }
        if (payChannel != EPayChannel.of(order.getPayChannel())) {
            throw new ServiceException("订单支付渠道不允许修改");
        }
        if (!EPayStatus.INIT.getValue().equals(order.getPayStatus())
                && !EPayStatus.PAYING.getValue().equals(order.getPayStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]状态异常无法发起支付",
                    order.getOrderNo()));
        }
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
            if (!EPayStatus.INIT.getValue().equals(orderDetail.getPayStatus())
                    && !EPayStatus.PAYING.getValue().equals(orderDetail.getPayStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]状态异常无法发起支付",
                        orderDetail.getId()));
            }
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
    public StoreOrderExt paySuccess(Long storeOrderId, String payTradeNo, BigDecimal totalAmount,
                                    BigDecimal realTotalAmount) {
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
        order.setPayTradeNo(payTradeNo);
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
    public void cancelOrder(StoreOrderCancelDTO opt) {
        StoreOrder order = getAndBaseCheck(opt.getStoreOrderId());
        EOrderStatus oStatus = EOrderStatus.of(order.getOrderStatus());
        EPayStatus pStatus = EPayStatus.of(order.getPayStatus());
        if (EOrderStatus.PENDING_PAYMENT != oStatus) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]状态为[{}]，无法取消",
                    order.getOrderNo(), oStatus.getLabel()));
        }
        if (EPayStatus.PAID == pStatus) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]已支付完成，无法取消",
                    order.getOrderNo()));
        }
        PaymentManager paymentManager = getPaymentManager(EPayChannel.of(order.getPayChannel()));
        boolean isPaid = paymentManager.isStoreOrderPaid(order.getOrderNo());
        if (isPaid) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]已支付，无法取消",
                    order.getOrderNo()));
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
    public StoreOrderExt shipOrderByPlatform(Long storeOrderId, List<Long> storeOrderDetailIds, Long expressId,
                                             Long operatorId) {
        Assert.notEmpty(storeOrderDetailIds);
        ExpressManager expressManager = getExpressManager(expressId);
        Express express = expressService.getById(expressId);
        if (!BeanValidators.exists(express) || !express.getSystemDeliverAccess()) {
            throw new ServiceException(CharSequenceUtil.format("快递[{}]不可用", expressId));
        }
        StoreOrder order = getAndBaseCheck(storeOrderId);
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]当前状态无法发货",
                    order.getOrderNo()));
        }
        List<StoreOrderDetail> containDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        for (StoreOrderDetail containDetail : containDetails) {
            if (EDeliveryType.SHIP_COMPLETE.getValue().equals(order.getDeliveryType())) {
                //如果是货齐再发，此次发货需要包含所有明细
                if (!storeOrderDetailIds.contains(containDetail.getId())) {
                    throw new ServiceException(CharSequenceUtil.format("订单[{}]不可拆单发货",
                            order.getOrderNo()));
                }
            }
//            if (EExpressType.STORE.getValue().equals(containDetail.getExpressType())) {
//                //已存在档口发货的明细
//                throw new ServiceException(CharSequenceUtil.format("订单[{}]由档口物流发货！",
//                        order.getOrderNo()));
//            }
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectByIds(storeOrderDetailIds);
        for (StoreOrderDetail orderDetail : orderDetails) {
            //校验明细状态
            if (!BeanValidators.exists(orderDetail)) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]不存在",
                        orderDetail.getId()));
            }
            if (!order.getId().equals(orderDetail.getStoreOrderId())) {
                throw new ServiceException(CharSequenceUtil.format("发货订单[{}]与明细[{}]不匹配",
                        order.getId(), orderDetail.getId()));
            }
            if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(orderDetail.getDetailStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]当前状态无法发货",
                        order.getOrderNo()));
            }
        }
        //发货
        ExpressShipReqDTO shipReq = createShipReq(order, orderDetails);
        String expressWaybillNo = expressManager.shipStoreOrder(shipReq);

        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        for (StoreOrderDetail orderDetail : orderDetails) {
            //明细->已发货
            orderDetail.setDetailStatus(EOrderStatus.SHIPPED.getValue());
            orderDetail.setExpressId(expressId);
            orderDetail.setExpressType(EExpressType.PLATFORM.getValue());
            orderDetail.setExpressStatus(EExpressStatus.PLACED.getValue());
            orderDetail.setExpressReqNo(shipReq.getExpressReqNo());
            orderDetail.setExpressWaybillNo(expressWaybillNo);
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        EOrderStatus currentOrderStatus = EOrderStatus.SHIPPED;
        Map<Long, StoreOrderDetail> detailMap = orderDetails.stream()
                .collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
        for (StoreOrderDetail containDetail : containDetails) {
            StoreOrderDetail now = detailMap.getOrDefault(containDetail.getId(), containDetail);
            if (EOrderStatus.PENDING_SHIPMENT.getValue().equals(now.getDetailStatus())) {
                //明细有一条或多条为代发货
                currentOrderStatus = EOrderStatus.PENDING_SHIPMENT;
                break;
            }
        }
        //订单 -> 代发货/已发货
        order.setOrderStatus(currentOrderStatus.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
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
            throw new ServiceException(CharSequenceUtil.format("快递[{}]不可用", expressId));
        }
        StoreOrder order = getAndBaseCheck(storeOrderId);
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]当前状态无法发货",
                    order.getOrderNo()));
        }
        List<StoreOrderDetail> containDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        for (StoreOrderDetail containDetail : containDetails) {
            if (EDeliveryType.SHIP_COMPLETE.getValue().equals(order.getDeliveryType())) {
                //如果是货齐再发，此次发货需要包含所有明细
                if (!storeOrderDetailIds.contains(containDetail.getId())) {
                    throw new ServiceException(CharSequenceUtil.format("订单[{}]不可拆单发货",
                            order.getOrderNo()));
                }
            }
//            if (EExpressType.PLATFORM.getValue().equals(containDetail.getExpressType())) {
//                //已存在平台发货的明细
//                throw new ServiceException(CharSequenceUtil.format("订单[{}]由平台物流发货！",
//                        order.getOrderNo()));
//            }
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectByIds(storeOrderDetailIds);
        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        for (StoreOrderDetail orderDetail : orderDetails) {
            if (!BeanValidators.exists(orderDetail)) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]不存在", orderDetail.getId()));
            }
            if (!order.getId().equals(orderDetail.getStoreOrderId())) {
                throw new ServiceException(CharSequenceUtil.format("发货订单[{}]与明细[{}]不匹配",
                        order.getId(), orderDetail.getId()));
            }
            if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(orderDetail.getDetailStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]当前状态无法发货",
                        order.getOrderNo()));
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
        EOrderStatus currentOrderStatus = EOrderStatus.SHIPPED;
        Map<Long, StoreOrderDetail> detailMap = orderDetails.stream()
                .collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
        for (StoreOrderDetail containDetail : containDetails) {
            StoreOrderDetail now = detailMap.getOrDefault(containDetail.getId(), containDetail);
            if (EOrderStatus.PENDING_SHIPMENT.getValue().equals(now.getDetailStatus())) {
                //明细有一条或多条为代发货
                currentOrderStatus = EOrderStatus.PENDING_SHIPMENT;
                break;
            }
        }
        //订单 -> 代发货/已发货
        order.setOrderStatus(currentOrderStatus.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.SHIP, orderDetailIdList, EOrderAction.SHIP,
                "档口物流发货", operatorId, new Date());
        return new StoreOrderExt(order, orderDetails);
    }

    @Override
    public List<ExpressPrintDTO> printOrder(List<Long> storeOrderDetailIds) {
        Assert.notEmpty(storeOrderDetailIds);
        Map<Long, List<StoreOrderDetail>> detailMap = storeOrderDetailMapper.selectByIds(storeOrderDetailIds).stream()
                .filter(o -> BeanValidators.exists(o)
                        && Objects.nonNull(o.getExpressId())
                        && StrUtil.isNotEmpty(o.getExpressWaybillNo())
                        && EExpressType.PLATFORM.getValue().equals(o.getExpressType()))
                .collect(Collectors.groupingBy(StoreOrderDetail::getExpressId));
        List<ExpressPrintDTO> rtnList = new ArrayList<>();
        for (Map.Entry<Long, List<StoreOrderDetail>> entry : detailMap.entrySet()) {
            ExpressManager expressManager = getExpressManager(entry.getKey());
            Map<Long, String> detailMap2 = entry.getValue()
                    .stream()
                    .collect(Collectors.toMap(SimpleEntity::getId, StoreOrderDetail::getExpressWaybillNo));
            List<ExpressPrintDTO> pds = expressManager.printOrder(new HashSet<>(detailMap2.values()));
            rtnList.addAll(pds);
        }
        return rtnList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt receiptOrder(Long storeOrderId, Long operatorId) {
        StoreOrder order = getAndBaseCheck(storeOrderId);
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())
                && !EOrderStatus.SHIPPED.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]当前状态无法确认收货",
                    order.getOrderNo()));
        }
        //存在售后订单
        long afterSaleOrderCount = storeOrderMapper.selectCount(Wrappers.lambdaQuery(StoreOrder.class)
                .eq(StoreOrder::getRefundOrderId, storeOrderId)
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        if (afterSaleOrderCount > 0) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]存在售后，无法确认收货",
                    order.getOrderNo()));
        }
        //订单->已完成
        order.setOrderStatus(EOrderStatus.COMPLETED.getValue());
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
            if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())
                    && !EOrderStatus.SHIPPED.getValue().equals(order.getOrderStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]当前状态无法确认收货",
                        order.getOrderNo()));
            }
            //订单明细->已完成
            orderDetail.setDetailStatus(EOrderStatus.COMPLETED.getValue());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.COMPLETE, orderDetailIdList, EOrderAction.COMPLETE,
                "确认收货", operatorId, new Date());
        StoreOrderExt orderExt = new StoreOrderExt(order, orderDetails);
        //创建转移单
        financeBillService.createOrderCompletedTransferBill(orderExt);
        return orderExt;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt completeOrder(Long storeOrderId, Long operatorId) {
        StoreOrder order = getAndBaseCheck(storeOrderId);
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())
                && !EOrderStatus.SHIPPED.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]当前状态无法完成",
                    order.getOrderNo()));
        }
        //售后订单
        List<StoreOrder> afterSaleOrderList = storeOrderMapper.selectList(Wrappers.lambdaQuery(StoreOrder.class)
                .eq(StoreOrder::getRefundOrderId, storeOrderId)
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        List<StoreOrderExt> afterSaleOrderExts = new ArrayList<>(afterSaleOrderList.size());
        if (afterSaleOrderList.size() > 0) {
            List<Long> afterSaleOrderIds = afterSaleOrderList.stream().map(SimpleEntity::getId)
                    .collect(Collectors.toList());
            Map<Long, List<StoreOrderDetail>> afterSaleOrderDetailGroupMap = storeOrderDetailMapper.selectList(
                    Wrappers.lambdaQuery(StoreOrderDetail.class)
                            .in(StoreOrderDetail::getStoreOrderId, afterSaleOrderIds)
                            .eq(SimpleEntity::getDelFlag, Constants.UNDELETED))
                    .stream()
                    .filter(o -> {
                        if (!EOrderStatus.AFTER_SALE_COMPLETED.getValue().equals(o.getDetailStatus())) {
                            throw new ServiceException(CharSequenceUtil.format("订单明细[{}]未完成售后",
                                    o.getId()));
                        }
                        if (EPayStatus.PAYING.getValue().equals(o.getPayStatus())) {
                            throw new ServiceException(CharSequenceUtil.format("订单明细[{}]未完成售后",
                                    o.getId()));
                        }
                        return true;
                    })
                    .collect(Collectors.groupingBy(StoreOrderDetail::getStoreOrderId));
            for (StoreOrder afterSaleOrder : afterSaleOrderList) {
                List<StoreOrderDetail> afterSaleOrderDetailList = afterSaleOrderDetailGroupMap
                        .get(afterSaleOrder.getId());
                afterSaleOrderExts.add(new StoreOrderExt(afterSaleOrder, afterSaleOrderDetailList));
                if (!EOrderStatus.AFTER_SALE_COMPLETED.getValue().equals(afterSaleOrder.getOrderStatus())) {
                    throw new ServiceException(CharSequenceUtil.format("订单[{}]未完成售后",
                            afterSaleOrder.getOrderNo()));
                }
                //售后订单更新一次，触发乐观锁
                int r = storeOrderMapper.updateById(afterSaleOrder);
                if (r == 0) {
                    throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
                }
            }
        }
        //订单->已完成
        order.setOrderStatus(EOrderStatus.COMPLETED.getValue());
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
            if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(order.getOrderStatus())
                    && !EOrderStatus.SHIPPED.getValue().equals(order.getOrderStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]当前状态无法确认收货",
                        order.getOrderNo()));
            }
            //订单明细->已完成
            orderDetail.setDetailStatus(EOrderStatus.COMPLETED.getValue());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            orderDetailIdList.add(orderDetail.getId());
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.COMPLETE, orderDetailIdList, EOrderAction.COMPLETE,
                "确认收货", operatorId, new Date());
        StoreOrderExt orderExt = new StoreOrderExt(order, orderDetails);
        //创建转移单
        financeBillService.createOrderCompletedTransferBill(orderExt, afterSaleOrderExts);
        return orderExt;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderExt createAfterSaleOrder(StoreOrderAfterSaleDTO afterSaleDTO) {
        Assert.notEmpty(afterSaleDTO.getStoreOrderDetailIds());
        if (afterSaleDTO.getExpressId() != null) {
            expressService.checkExpress(afterSaleDTO.getExpressId());
        }
        //获取原订单
        StoreOrder originOrder = getAndBaseCheck(afterSaleDTO.getStoreOrderId());
        if (!EOrderStatus.PENDING_SHIPMENT.getValue().equals(originOrder.getOrderStatus())
                && !EOrderStatus.SHIPPED.getValue().equals(originOrder.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]当前状态无法申请售后",
                    originOrder.getOrderNo()));
        }
        List<StoreOrderDetail> originOrderDetails = storeOrderDetailMapper.selectList(
                Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .eq(StoreOrderDetail::getStoreOrderId, originOrder.getId())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        Map<Long, StoreOrderDetail> originOrderDetailMap = originOrderDetails.stream()
                .collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
        Set<Long> originOrderDetailIds = originOrderDetailMap.keySet();
        for (Long orderDetailId : afterSaleDTO.getStoreOrderDetailIds()) {
            //检查明细归属
            if (!originOrderDetailIds.contains(orderDetailId)) {
                throw new ServiceException(CharSequenceUtil.format("订单[{}]明细[{}]不匹配",
                        originOrder.getId(), orderDetailId));
            }
        }
        //已存在的售后订单
        List<StoreOrderDetail> existsAfterSaleOrderDetails = storeOrderDetailMapper
                .selectList(Wrappers.lambdaQuery(StoreOrderDetail.class)
                        .in(StoreOrderDetail::getRefundOrderDetailId, afterSaleDTO.getStoreOrderDetailIds())
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        Set<Long> existsAfterSaleOrderDetailIds = existsAfterSaleOrderDetails.stream()
                .map(StoreOrderDetail::getRefundOrderDetailId).collect(Collectors.toSet());
        List<Long> afterSaleOrderDetailIds = afterSaleDTO.getStoreOrderDetailIds().stream()
                .filter(o -> !existsAfterSaleOrderDetailIds.contains(o)).distinct().collect(Collectors.toList());
        Assert.notEmpty(afterSaleOrderDetailIds, "已存在售后订单");
        List<StoreOrderDetail> orderDetails = new ArrayList<>(afterSaleOrderDetailIds.size());
        int orderGoodsQuantity = 0;
        BigDecimal orderGoodsAmount = BigDecimal.ZERO;
        BigDecimal orderExpressFee = BigDecimal.ZERO;
        BigDecimal orderTotalAmount = BigDecimal.ZERO;
        BigDecimal orderRealTotalAmount = BigDecimal.ZERO;
        for (Long afterSaleOrderDetailId : afterSaleOrderDetailIds) {
            StoreOrderDetail originOrderDetail = originOrderDetailMap.get(afterSaleOrderDetailId);
            StoreOrderDetail orderDetail = BeanUtil.toBean(originOrderDetail, StoreOrderDetail.class);
            orderDetails.add(orderDetail);
            orderDetail.setId(null);
            orderDetail.setStoreOrderId(null);
            orderDetail.setDetailStatus(EOrderStatus.AFTER_SALE_IN_PROGRESS.getValue());
            orderDetail.setPayStatus(EPayStatus.INIT.getValue());
            orderDetail.setExpressId(afterSaleDTO.getExpressId());
            orderDetail.setExpressType(null);
            orderDetail.setExpressStatus(EExpressStatus.INIT.getValue());
            orderDetail.setExpressReqNo(null);
            orderDetail.setExpressWaybillNo(afterSaleDTO.getExpressWaybillNo());
            orderDetail.setRefundOrderDetailId(afterSaleOrderDetailId);
            orderDetail.setRefundReasonCode(afterSaleDTO.getRefundReasonCode());
            orderDetail.setVersion(0L);
            orderDetail.setDelFlag(Constants.UNDELETED);
            //计算订单费用
            orderGoodsQuantity = orderGoodsQuantity + orderDetail.getGoodsQuantity();
            orderGoodsAmount = NumberUtil.add(orderGoodsAmount, orderDetail.getGoodsAmount());
            orderExpressFee = NumberUtil.add(orderExpressFee, orderDetail.getExpressFee());
            orderTotalAmount = NumberUtil.add(orderTotalAmount, orderDetail.getTotalAmount());
            orderRealTotalAmount = NumberUtil.add(orderRealTotalAmount, orderDetail.getRealTotalAmount());
        }
        StoreOrder order = BeanUtil.toBean(originOrder, StoreOrder.class);
        order.setId(null);
        //生成订单号
        String orderNo = voucherSequenceService.generateCode(order.getStoreId(),
                EVoucherSequenceType.STORE_ORDER.getValue(), DateUtil.today());
        order.setOrderNo(orderNo);
        order.setOrderType(EOrderType.RETURN_ORDER.getValue());
        order.setOrderStatus(EOrderStatus.AFTER_SALE_IN_PROGRESS.getValue());
        order.setPayStatus(EPayStatus.INIT.getValue());
        order.setOrderRemark(null);
        order.setGoodsQuantity(orderGoodsQuantity);
        order.setGoodsAmount(orderGoodsAmount);
        order.setExpressFee(orderExpressFee);
        order.setTotalAmount(orderTotalAmount);
        order.setRealTotalAmount(orderRealTotalAmount);
        order.setRefundOrderId(afterSaleDTO.getStoreOrderId());
        order.setRefundReasonCode(afterSaleDTO.getRefundReasonCode());
        order.setExpressId(afterSaleDTO.getExpressId());
        order.setVersion(0L);
        order.setDelFlag(Constants.UNDELETED);
        //落库
        storeOrderMapper.insert(order);
        List<Long> orderDetailIdList = new ArrayList<>(orderDetails.size());
        orderDetails.forEach(orderDetail -> {
            orderDetail.setStoreOrderId(order.getId());
            storeOrderDetailMapper.insert(orderDetail);
            orderDetailIdList.add(orderDetail.getId());
        });
        //原订单更新一次，触发乐观锁，防止退货明细重复创建
        int r = storeOrderMapper.updateById(originOrder);
        if (r == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.INSERT, orderDetailIdList, EOrderAction.INSERT,
                afterSaleDTO.getOperatorId(), new Date());
        addOperationRecords(originOrder.getId(), EOrderAction.APPLY_AFTER_SALE, afterSaleOrderDetailIds,
                EOrderAction.APPLY_AFTER_SALE, afterSaleDTO.getOperatorId(), new Date());
        return new StoreOrderExt(order, orderDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectRefundOrder(StoreOrderRefundRejectDTO refundRejectDTO) {
        Assert.notEmpty(refundRejectDTO.getStoreOrderDetailIds());
        StoreOrder order = getAndBaseCheck(refundRejectDTO.getStoreOrderId());
        if (!EOrderStatus.AFTER_SALE_IN_PROGRESS.getValue().equals(order.getOrderStatus())
                && !EOrderStatus.AFTER_SALE_REJECTED.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]状态异常", order.getId()));
        }
        List<StoreOrderDetail> details = storeOrderDetailMapper.selectList(Wrappers.lambdaQuery(StoreOrderDetail.class)
                .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        Map<Long, StoreOrderDetail> detailMap = details.stream()
                .collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
        for (Long orderDetailId : refundRejectDTO.getStoreOrderDetailIds()) {
            StoreOrderDetail orderDetail = detailMap.get(orderDetailId);
            if (!BeanValidators.exists(orderDetail)) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]不存在或与订单不匹配",
                        orderDetailId));
            }
            if (!EOrderStatus.AFTER_SALE_IN_PROGRESS.getValue().equals(orderDetail.getDetailStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]状态异常",
                        orderDetail.getId()));
            }
            orderDetail.setDetailStatus(EOrderStatus.AFTER_SALE_REJECTED.getValue());
            orderDetail.setRefundRejectReason(refundRejectDTO.getRefundRejectReason());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
        }
        order.setOrderStatus(EOrderStatus.AFTER_SALE_REJECTED.getValue());
        order.setRefundRejectReason(refundRejectDTO.getRefundRejectReason());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //操作记录
        addOperationRecords(order.getId(), EOrderAction.REJECT_AFTER_SALE, refundRejectDTO.getStoreOrderDetailIds(),
                EOrderAction.REJECT_AFTER_SALE, refundRejectDTO.getOperatorId(), new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreOrderRefund prepareRefundOrder(StoreOrderRefundConfirmDTO refundConfirmDTO) {
        StoreOrder order = getAndBaseCheck(refundConfirmDTO.getStoreOrderId());
        if (!EOrderStatus.AFTER_SALE_IN_PROGRESS.getValue().equals(order.getOrderStatus())
                && !EOrderStatus.AFTER_SALE_REJECTED.getValue().equals(order.getOrderStatus())) {
            throw new ServiceException(CharSequenceUtil.format("订单[{}]状态异常", order.getId()));
        }
        List<StoreOrderDetail> orderDetails = storeOrderDetailMapper.selectList(Wrappers
                .lambdaQuery(StoreOrderDetail.class)
                .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
//        Set<Long> originOrderDetailIds = orderDetails.stream()
//                .map(StoreOrderDetail::getRefundOrderDetailId)
//                .collect(Collectors.toSet());
//        Map<Long,StoreOrderDetail> originOrderDetailMap = storeOrderDetailMapper.selectList(Wrappers
//                .lambdaQuery(StoreOrderDetail.class)
//                .in(SimpleEntity::getId,originOrderDetailIds))
//                .stream()
//                .collect(Collectors.toMap(SimpleEntity::getId,Function.identity()));
        List<StoreOrderDetail> refundOrderDetails = new ArrayList<>();
        for (StoreOrderDetail orderDetail : orderDetails) {
            if (!EOrderStatus.AFTER_SALE_IN_PROGRESS.getValue().equals(orderDetail.getDetailStatus())) {
                continue;
            }
            if (!EPayStatus.INIT.getValue().equals(orderDetail.getPayStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]支付状态异常",
                        orderDetail.getId()));
            }
            orderDetail.setPayStatus(EPayStatus.PAYING.getValue());
            orderDetail.setDetailStatus(EOrderStatus.AFTER_SALE_COMPLETED.getValue());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
            refundOrderDetails.add(orderDetail);
        }

        EOrderStatus orderStatus = EOrderStatus.AFTER_SALE_COMPLETED;
        for (StoreOrderDetail orderDetail : orderDetails) {
            if (EOrderStatus.PLATFORM_INTERVENED.getValue().equals(orderDetail.getDetailStatus())) {
                orderStatus = EOrderStatus.PLATFORM_INTERVENED;
                break;
            }
            if (EOrderStatus.AFTER_SALE_REJECTED.getValue().equals(orderDetail.getDetailStatus())) {
                orderStatus = EOrderStatus.AFTER_SALE_REJECTED;
                break;
            }
        }
        order.setOrderStatus(orderStatus.getValue());
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //操作记录
        addOperationRecords(order.getId(),
                EOrderAction.CONFIRM_AFTER_SALE,
                refundOrderDetails.stream().map(SimpleEntity::getId).collect(Collectors.toList()),
                EOrderAction.CONFIRM_AFTER_SALE,
                refundConfirmDTO.getOperatorId(),
                new Date());
        //创建付款单
        financeBillService.createRefundOrderPaymentBill(new StoreOrderExt(order, refundOrderDetails));
        return new StoreOrderRefund(order, refundOrderDetails, getAndBaseCheck(order.getRefundOrderId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundSuccess(Long storeOrderId, List<Long> storeOrderDetailIds, Long operatorId) {
        StoreOrder order = getAndBaseCheck(storeOrderId);
        Map<Long, StoreOrderDetail> orderDetailMap = storeOrderDetailMapper.selectList(Wrappers
                .lambdaQuery(StoreOrderDetail.class)
                .eq(StoreOrderDetail::getStoreOrderId, order.getId())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED))
                .stream()
                .collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
        for (Long storeOrderDetailId : storeOrderDetailIds) {
            StoreOrderDetail orderDetail = orderDetailMap.get(storeOrderDetailId);
            if (!BeanValidators.exists(orderDetail)) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]不存在或与订单[{}]不匹配",
                        storeOrderDetailId, storeOrderId));
            }
            if (!EOrderStatus.COMPLETED.getValue().equals(orderDetail.getDetailStatus())
                    || !EPayStatus.PAYING.getValue().equals(orderDetail.getPayStatus())) {
                throw new ServiceException(CharSequenceUtil.format("订单明细[{}]状态异常", storeOrderDetailId));
            }
            orderDetail.setPayStatus(EPayStatus.PAID.getValue());
            int orderDetailSuccess = storeOrderDetailMapper.updateById(orderDetail);
            if (orderDetailSuccess == 0) {
                throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
            }
        }
        int orderSuccess = storeOrderMapper.updateById(order);
        if (orderSuccess == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
        //操作记录
        addOperationRecords(order.getId(),
                EOrderAction.REFUND,
                storeOrderDetailIds,
                EOrderAction.REFUND,
                operatorId,
                new Date());
        //付款单到账
        financeBillService.entryRefundOrderPaymentBill(order.getId());
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
            throw new ServiceException(CharSequenceUtil.format("订单[{}]不存在", storeOrderId));
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

    private ExpressShipReqDTO createShipReq(StoreOrder order, List<StoreOrderDetail> orderDetails) {
        ExpressShipReqDTO reqDTO = BeanUtil.toBean(order, ExpressShipReqDTO.class);
        //生成请求号
        reqDTO.setExpressReqNo(IdUtil.simpleUUID());
        //行政区划信息
        Map<String, ExpressRegionDTO> regionMap = expressService.getRegionMapCache();
        reqDTO.setDestinationProvinceName(Optional.ofNullable(regionMap.get(order.getDestinationProvinceCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        reqDTO.setDestinationCityName(Optional.ofNullable(regionMap.get(order.getDestinationCityCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        reqDTO.setDestinationCountyName(Optional.ofNullable(regionMap.get(order.getDestinationCountyCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        reqDTO.setOriginProvinceName(Optional.ofNullable(regionMap.get(order.getOriginProvinceCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        reqDTO.setOriginCityName(Optional.ofNullable(regionMap.get(order.getOriginCityCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        reqDTO.setOriginCountyName(Optional.ofNullable(regionMap.get(order.getOriginCountyCode()))
                .map(ExpressRegionDTO::getParentRegionName).orElse(null));
        //货物信息
        List<ExpressShipReqDTO.OrderItem> orderItems = CollUtil.emptyIfNull(orderDetails).stream()
                .map(o -> ExpressShipReqDTO.OrderItem
                        .builder()
                        //TODO 其他信息？
                        .name(o.getProdTitle())
                        .quantity(o.getGoodsQuantity())
                        .build())
                .collect(Collectors.toList());
        reqDTO.setOrderItems(orderItems);

        return reqDTO;
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
