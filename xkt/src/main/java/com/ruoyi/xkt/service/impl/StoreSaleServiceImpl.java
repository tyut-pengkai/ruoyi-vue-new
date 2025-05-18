package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.dailySale.StoreTodaySaleDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusGeneralSaleDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePayStatusDTO;
import com.ruoyi.xkt.enums.EVoucherSequenceType;
import com.ruoyi.xkt.enums.PaymentStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductStockService;
import com.ruoyi.xkt.service.IStoreSaleService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口销售出库Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreSaleServiceImpl implements IStoreSaleService {

    final StoreSaleMapper storeSaleMapper;
    final StoreCustomerMapper storeCusMapper;
    final IVoucherSequenceService sequenceService;
    final StoreSaleDetailMapper storeSaleDetailMapper;
    final StoreSaleRefundRecordMapper refundRecordMapper;
    final StoreSaleRefundRecordDetailMapper refundRecordDetailMapper;
    final IStoreProductStockService storeProdStockService;

    /**
     * 获取当前档口客户的销售业绩
     *
     * @param days       查询时间diff
     * @param storeId    档口ID
     * @param storeCusId 档口客户ID
     * @return StoreCusGeneralSaleDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCusGeneralSaleDTO getCusGeneralSale(Integer days, Long storeId, Long storeCusId) {
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusId).eq(StoreCustomer::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口客户不存在!", HttpStatus.ERROR));
        // 当前时间
        Date nowDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 往前推60天
        Date pastDate = Date.from(LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 查询当前档口客户在这段时间内的销售业绩情况
        List<StoreSale> saleList = this.storeSaleMapper.selectList(new LambdaQueryWrapper<StoreSale>()
                .eq(StoreSale::getStoreId, storeId).eq(StoreSale::getStoreCusId, storeCusId)
                .eq(StoreSale::getDelFlag, Constants.UNDELETED).between(StoreSale::getVoucherDate, pastDate, nowDate));
        // 初始化返回对象
        StoreCusGeneralSaleDTO generalSaleDTO = StoreCusGeneralSaleDTO.builder().storeId(storeId).storeCusId(storeCusId)
                .storeCusName(storeCus.getCusName()).saleAmount(BigDecimal.ZERO).debtAmount(BigDecimal.ZERO)
                .saleCount(0L).build();
        if (CollectionUtils.isEmpty(saleList)) {
            return generalSaleDTO;
        }
        // 总的销售金额
        BigDecimal saleAmount = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总的销售数量
        Long saleCount = saleList.stream().map(x -> x.getQuantity() == null ? 0L : x.getQuantity()).reduce(0L, Long::sum);
        // 总的欠款金额
        BigDecimal debtAmount = saleList.stream().filter(x -> Objects.equals(x.getPaymentStatus(), PaymentStatus.DEBT.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        return generalSaleDTO.setSaleAmount(saleAmount).setSaleCount(saleCount).setDebtAmount(debtAmount);
    }

    /**
     * 销售出库列表查询
     *
     * @param pageDTO 入参
     * @return Page<StoreSalePageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreSalePageResDTO> page(StoreSalePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreSalePageResDTO> list = storeSaleMapper.selectPage(pageDTO);
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 清除店铺顾客债务信息
     * <p>
     * 该方法旨在根据提供的店铺销售支付状态信息来清除或更新店铺顾客的债务记录
     * 它通常在完成销售交易、债务偿还或其他需要调整顾客债务的情况下调用
     *
     * @param payStatusDTO 包含店铺销售支付状态的DTO对象，用于确定是否需要清除顾客债务
     * @return 返回一个整数，表示受影响的债务记录数量或状态更新结果
     */
    @Override
    @Transactional
    public Integer clearStoreCusDebt(StoreSalePayStatusDTO payStatusDTO) {
        List<StoreSale> storeSaleList = Optional.ofNullable(this.storeSaleMapper.selectList(new LambdaQueryWrapper<StoreSale>()
                        .in(StoreSale::getId, payStatusDTO.getStoreSaleIdList()).eq(StoreSale::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("没有找到对应的销售出库单!", HttpStatus.ERROR));
        // 勾选订单是否有已结算的
        List<StoreSale> settledList = storeSaleList.stream().filter(x -> Objects.equals(x.getPaymentStatus(), PaymentStatus.SETTLED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(settledList)) {
            throw new ServiceException("当前订单已结算!" + settledList.stream().map(StoreSale::getCode).collect(Collectors.toList()), HttpStatus.ERROR);
        }
        storeSaleList.forEach(x -> x.setPaymentStatus(PaymentStatus.SETTLED.getValue()));
        List<BatchResult> list = this.storeSaleMapper.updateById(storeSaleList);
        return list.size();
    }

    /**
     * 获取当前档口今日销售数据
     *
     * @param storeId 档口ID
     * @return StoreTodaySaleDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreTodaySaleDTO getTodaySale(Long storeId) {
        // 今天 yyyy-MM-dd
        Date date = java.sql.Date.valueOf(LocalDate.now());
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getVoucherDate, date));
        if (CollectionUtils.isEmpty(saleDetailList)) {
            return new StoreTodaySaleDTO();
        }
        Integer saleQuantity = saleDetailList.stream().filter(x -> x.getQuantity() > 0).mapToInt(StoreSaleDetail::getQuantity).sum();
        BigDecimal saleAmount = saleDetailList.stream().filter(x -> x.getQuantity() > 0)
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer refundQuantity = saleDetailList.stream().filter(x -> x.getQuantity() < 0).mapToInt(StoreSaleDetail::getQuantity).sum();
        BigDecimal refundAmount = saleDetailList.stream().filter(x -> x.getQuantity() < 0)
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal amount = saleDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new StoreTodaySaleDTO().setStoreId(storeId).setSaleQuantity(saleQuantity).setSaleAmount(saleAmount)
                .setRefundQuantity(refundQuantity).setRefundAmount(refundAmount).setAmount(amount);
    }

    /**
     * 新增档口销售出库
     *
     * @param storeSaleDTO 档口销售出库
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreSale(StoreSaleDTO storeSaleDTO) {
        StoreSale storeSale = BeanUtil.toBean(storeSaleDTO, StoreSale.class);
        // 生成code
        String code = this.sequenceService.generateCode(storeSaleDTO.getStoreId(), EVoucherSequenceType.STORE_SALE.getValue(), DateUtils.parseDateToStr(DateUtils.YYYYMMDD, new Date()));
        // 总的数量
        Integer quantity = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 销售数量
        Integer saleQuantity = storeSaleDTO.getDetailList().stream().filter(x -> x.getQuantity() > 0).mapToInt(StoreSaleDTO.SaleDetailVO::getQuantity).sum();
        // 退货数量
        Integer refundQuantity = storeSaleDTO.getDetailList().stream().filter(x -> x.getQuantity() < 0).mapToInt(StoreSaleDTO.SaleDetailVO::getQuantity).sum();
        // 总的金额
        BigDecimal amount = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 属性赋值
        storeSale.setCode(code).setVoucherDate(DateUtils.getNowDate()).setQuantity(quantity).setAmount(amount)
                .setSaleQuantity(saleQuantity).setRefundQuantity(refundQuantity).setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername());
        int count = storeSaleMapper.insert(storeSale);
        // 处理订单明细
        List<StoreSaleDetail> saleDetailList = storeSaleDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreSaleDetail.class)
                .setSaleType(storeSaleDTO.getSaleType()).setStoreSaleId(storeSale.getId()).setStoreId(storeSale.getStoreId()))
                .collect(Collectors.toList());
        this.storeSaleDetailMapper.insert(saleDetailList);
        // 先汇总当前这笔订单商品明细的销售数量，包括销售及退货 key： prodArtNum + storeProdId + storeProdColorId + colorName, value: map(key:size,value:count)
        Map<String, Map<Integer, Integer>> saleCountMap = storeSaleDTO.getDetailList().stream().collect(Collectors
                .groupingBy(x -> x.getProdArtNum() + ":" + x.getStoreProdId() + ":" + x.getStoreProdColorId() + ":" + x.getColorName(), Collectors
                        .groupingBy(StoreSaleDTO.SaleDetailVO::getSize, Collectors
                                .mapping(StoreSaleDTO.SaleDetailVO::getQuantity, Collectors.reducing(0, Integer::sum)))));
        // 组装库存调整入参调整库存
        this.storeProdStockService.updateStock(storeSale.getStoreId(), this.getStockDiffList(saleCountMap, -1), 1);
        return count;
    }


    /**
     * 修改档口销售出库
     *
     * @param storeSaleDTO 档口销售出库
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreSale(StoreSaleDTO storeSaleDTO) {
        // 当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        StoreSale storeSale = Optional.ofNullable(this.storeSaleMapper.selectOne(new LambdaQueryWrapper<StoreSale>()
                        .eq(StoreSale::getId, storeSaleDTO.getStoreSaleId()).eq(StoreSale::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        // 档口销售出库明细列表
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreSaleId, storeSaleDTO.getStoreSaleId()).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED));
        // 若为返单，则将之前数据记录到返单记录表中
        if (Objects.equals(storeSaleDTO.getRefund(), Boolean.TRUE)) {
            // 订单记录到StoreSaleRefundRecord
            StoreSaleRefundRecord refundRecord = BeanUtil.toBean(storeSale, StoreSaleRefundRecord.class).setStoreSaleId(storeSale.getId())
                    .setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername());
            this.refundRecordMapper.insert(refundRecord);
            // 明细记录到StoreSaleRefundRecordDetail
            List<StoreSaleRefundRecordDetail> refundDetailRecordList = saleDetailList.stream().map(x -> BeanUtil.toBean(x, StoreSaleRefundRecordDetail.class)
                    .setStoreSaleRefundRecordId(refundRecord.getId())).collect(Collectors.toList());
            this.refundRecordDetailMapper.insert(refundDetailRecordList);
        }
        // 更新档口销售数据
        BeanUtil.copyProperties(storeSaleDTO, storeSale);
        // 总的数量
        Integer quantity = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 销售数量
        Integer saleQuantity = storeSaleDTO.getDetailList().stream().filter(x -> x.getQuantity() > 0).mapToInt(StoreSaleDTO.SaleDetailVO::getQuantity).sum();
        // 退货数量
        Integer refundQuantity = storeSaleDTO.getDetailList().stream().filter(x -> x.getQuantity() < 0).mapToInt(StoreSaleDTO.SaleDetailVO::getQuantity).sum();
        // 总的金额
        BigDecimal amount = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        int count = this.storeSaleMapper.updateById(storeSale.setQuantity(quantity).setAmount(amount).setSaleQuantity(saleQuantity).setRefundQuantity(refundQuantity)
                .setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername()));
        // 先将所有明细置为无效，再新增
        this.storeSaleDetailMapper.updateById(saleDetailList.stream().peek(x -> x.setDelFlag(Constants.DELETED)).collect(Collectors.toList()));
        // 再新增档口销售出库明细数据
        List<StoreSaleDetail> detailList = storeSaleDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreSaleDetail.class)
                .setSaleType(storeSaleDTO.getSaleType()).setStoreSaleId(storeSale.getId()).setStoreId(storeSale.getStoreId()))
                .collect(Collectors.toList());
        this.storeSaleDetailMapper.insert(detailList);
        // 汇总编辑的存货总数量
        final List<StoreSaleDetail> totalList = new ArrayList<StoreSaleDetail>(saleDetailList) {{
            addAll(detailList);
        }};
        // 先汇总当前这笔订单商品明细的销售数量，包括销售及退货 key： prodArtNum + storeProdId + storeProdColorId + colorName, value: map(key:size,value:count)
        Map<String, Map<Integer, Integer>> saleCountMap = totalList.stream().collect(Collectors
                .groupingBy(x -> x.getProdArtNum() + ":" + x.getStoreProdId() + ":" + x.getStoreProdColorId() + ":" + x.getColorName(), Collectors
                        .groupingBy(StoreSaleDetail::getSize, Collectors
                                .mapping(StoreSaleDetail::getQuantity, Collectors.reducing(0, Integer::sum)))));
        // 调整库存
        this.storeProdStockService.updateStock(storeSale.getStoreId(), this.getStockDiffList(saleCountMap, 1), 1);
        return count;
    }

    /**
     * 查询档口销售出库
     *
     * @param storeSaleId 档口销售出库主键
     * @return 档口销售出库
     */
    @Override
    @Transactional(readOnly = true)
    public StoreSaleDTO selectStoreSaleByStoreSaleId(Long storeSaleId) {
        StoreSale storeSale = Optional.ofNullable(this.storeSaleMapper.selectOne(new LambdaQueryWrapper<StoreSale>()
                        .eq(StoreSale::getId, storeSaleId).eq(StoreSale::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        StoreSaleDTO storeSaleDTO = BeanUtil.toBean(storeSale, StoreSaleDTO.class);
        // 查询销售出库明细
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreSaleId, storeSaleId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED));
        storeSaleDTO.setDetailList(saleDetailList.stream().map(x -> BeanUtil.toBean(x, StoreSaleDTO.SaleDetailVO.class)).collect(Collectors.toList()));
        return storeSaleDTO;
    }

    /**
     * 删除档口销售出库信息
     *
     * @param storeSaleId 档口销售出库主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreSaleByStoreSaleId(Long storeSaleId) {
        // 删除档口销售出库数据
        StoreSale storeSale = Optional.ofNullable(this.storeSaleMapper.selectOne(new LambdaQueryWrapper<StoreSale>()
                        .eq(StoreSale::getId, storeSaleId).eq(StoreSale::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        storeSale.setDelFlag(Constants.DELETED);
        int count = this.storeSaleMapper.updateById(storeSale);
        // 删除档口销售出库明细数据
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreSaleId, storeSaleId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED));
        this.storeSaleDetailMapper.updateById(saleDetailList.stream().peek(x -> x.setDelFlag(Constants.DELETED)).collect(Collectors.toList()));
        // 先汇总当前这笔订单商品明细的销售数量，包括销售及退货 key： prodArtNum + storeProdId + storeProdColorId + colorName, value: map(key:size,value:count)
        Map<String, Map<Integer, Integer>> saleCountMap = saleDetailList.stream().collect(Collectors
                .groupingBy(x -> x.getProdArtNum() + ":" + x.getStoreProdId() + ":" + x.getStoreProdColorId() + ":" + x.getColorName(), Collectors
                        .groupingBy(StoreSaleDetail::getSize, Collectors
                                .mapping(StoreSaleDetail::getQuantity, Collectors.reducing(0, Integer::sum)))));
        // 组装库存调整库存
        this.storeProdStockService.updateStock(storeSale.getStoreId(), this.getStockDiffList(saleCountMap, 1), 1);
        return count;
    }


    /**
     * 获取库存变更列表
     *
     * @param saleCountMap     销售出库的map数量
     * @param multiplierFactor 1 返回当前库存 -1 减少库存
     * @return List<StoreProdStockUpdateDTO>
     */
    private List<StoreProdStockDTO> getStockDiffList(Map<String, Map<Integer, Integer>> saleCountMap, int multiplierFactor) {
        return MapUtils.isEmpty(saleCountMap) ? new ArrayList<>() : saleCountMap.entrySet().stream()
                .map(entry -> {
                    String[] keys = entry.getKey().split(":");
                    String prodArtNum = keys[0];
                    Long storeProdId = Long.parseLong(keys[1]);
                    Long storeProdColorId = Long.parseLong(keys[2]);
                    String colorName = keys[3];
                    StoreProdStockDTO dto = new StoreProdStockDTO().setProdArtNum(prodArtNum)
                            .setStoreProdId(storeProdId).setStoreProdColorId(storeProdColorId).setColorName(colorName);
                    entry.getValue().forEach((size, diffQuantity) -> {
                        // 库存变更数量乘以正负1
                        diffQuantity = diffQuantity * multiplierFactor;
                        switch (size) {
                            case 30:
                                dto.setSize30(diffQuantity);
                                break;
                            case 31:
                                dto.setSize31(diffQuantity);
                                break;
                            case 32:
                                dto.setSize32(diffQuantity);
                                break;
                            case 33:
                                dto.setSize33(diffQuantity);
                                break;
                            case 34:
                                dto.setSize34(diffQuantity);
                                break;
                            case 35:
                                dto.setSize35(diffQuantity);
                                break;
                            case 36:
                                dto.setSize36(diffQuantity);
                                break;
                            case 37:
                                dto.setSize37(diffQuantity);
                                break;
                            case 38:
                                dto.setSize38(diffQuantity);
                                break;
                            case 39:
                                dto.setSize39(diffQuantity);
                                break;
                            case 40:
                                dto.setSize40(diffQuantity);
                                break;
                            case 41:
                                dto.setSize41(diffQuantity);
                                break;
                            case 42:
                                dto.setSize42(diffQuantity);
                                break;
                            case 43:
                                dto.setSize43(diffQuantity);
                                break;
                            default:
                                // 处理不在预期范围内的尺码
                                throw new IllegalArgumentException("Unexpected size: " + size);
                        }
                    });
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
