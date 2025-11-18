package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreSaleDetail;
import com.ruoyi.xkt.dto.storeSaleDetail.StoreTodaySaleDTO;
import com.ruoyi.xkt.dto.storeSaleDetail.StoreTodaySaleSummaryDTO;
import com.ruoyi.xkt.enums.SaleType;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.service.IStoreSaleDetailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口销售明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreSaleDetailServiceImpl implements IStoreSaleDetailService {

    final StoreSaleDetailMapper saleDetailMapper;

    /**
     * 获取当前档口今日销售数据
     *
     * @param storeId 档口ID
     * @return StoreTodaySaleDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreTodaySaleDTO getTodaySale(Long storeId) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 今天 yyyy-MM-dd
        Date date = java.sql.Date.valueOf(LocalDate.now());
        List<StoreSaleDetail> saleDetailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getVoucherDate, date));
        if (CollectionUtils.isEmpty(saleDetailList)) {
            return new StoreTodaySaleDTO();
        }
        Integer saleQuantity = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        BigDecimal saleAmount = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer refundQuantity = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        BigDecimal refundAmount = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal amount = saleDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer quantity = saleDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        return new StoreTodaySaleDTO().setStoreId(storeId).setSaleQuantity(saleQuantity).setSaleAmount(saleAmount)
                .setRefundQuantity(refundQuantity).setRefundAmount(refundAmount).setAmount(amount).setQuantity(quantity);
    }

    /**
     * 查询今日统计数据
     *
     * @param storeId 档口ID
     * @return StoreTodaySaleSummaryDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreTodaySaleSummaryDTO getTodaySaleSummary(Long storeId) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreSaleDetail> saleDetailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getVoucherDate, java.sql.Date.valueOf(LocalDate.now()))
                .eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(saleDetailList)) {
            return new StoreTodaySaleSummaryDTO();
        }
        // 销售出库金额
        BigDecimal saleAmount = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 退货出库金额
        BigDecimal refundAmount = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 销售出库数量
        Integer saleQuantity = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 退货出库数量
        Integer refundQuantity = saleDetailList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 退货率
        String refundRate = saleQuantity > 0 ? String.format("%.2f%%", Math.abs(refundQuantity) * 100.0 / saleQuantity) : "0.00%";
        // 采购客户数量
        Long cusQuantity = saleDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getStoreCusId(), 0)).distinct().count();
        return new StoreTodaySaleSummaryDTO().setSaleAmount(saleAmount).setRefundAmount(refundAmount).setSaleQuantity(saleQuantity)
                .setRefundQuantity(refundQuantity).setRefundRate(refundRate).setCusQuantity(cusQuantity)
                .setProdSaleList(getProdSaleSummary(saleDetailList)).setCusSaleList(getCusSaleSummary(saleDetailList));
    }

    /**
     * 今日客户销售数据
     *
     * @param saleDetailList 今日销售数据
     * @return List<StoreTodaySaleSummaryDTO.STSSCusSaleDTO>
     */
    private List<StoreTodaySaleSummaryDTO.STSSCusSaleDTO> getCusSaleSummary(List<StoreSaleDetail> saleDetailList) {
        // 客户销售汇总map
        Map<String, List<StoreSaleDetail>> cusSaleGroupMap = saleDetailList.stream().collect(Collectors.groupingBy(StoreSaleDetail::getStoreCusName));
        List<StoreTodaySaleSummaryDTO.STSSCusSaleDTO> retCusSaleList = new ArrayList<>();
        cusSaleGroupMap.forEach((cusName, cusSaleList) -> {
            BigDecimal cusSaleAmount = cusSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cusRefundAmount = cusSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cusTotalAmount = cusSaleAmount.add(cusRefundAmount);
            Integer cusSaleQuantity = cusSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            Integer cusRefundQuantity = cusSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            Integer cusTotalQuantity = cusSaleQuantity + cusRefundQuantity;
            String refundRate = cusSaleQuantity > 0 ? String.format("%.2f%%", Math.abs(cusRefundQuantity) * 100.0 / cusSaleQuantity) : "0.00%";
            retCusSaleList.add(new StoreTodaySaleSummaryDTO.STSSCusSaleDTO().setStoreCusName(cusName).setSaleAmount(cusSaleAmount)
                    .setRefundAmount(cusRefundAmount).setTotalAmount(cusTotalAmount).setSaleQuantity(cusSaleQuantity)
                    .setRefundQuantity(cusRefundQuantity).setTotalQuantity(cusTotalQuantity).setRefundRate(refundRate));
        });
        // 按照总的销售金额 由高到低 排
        retCusSaleList.sort(Comparator.comparing(StoreTodaySaleSummaryDTO.STSSCusSaleDTO::getTotalAmount).reversed());
        return retCusSaleList;
    }

    /**
     * 商品今日统计数据
     *
     * @param saleDetailList 今日销售数据
     * @return List<StoreTodaySaleSummaryDTO.STSSProdSaleDTO>
     */
    private List<StoreTodaySaleSummaryDTO.STSSProdSaleDTO> getProdSaleSummary(List<StoreSaleDetail> saleDetailList) {
        // 货号维度 销量map
        Map<String, List<StoreSaleDetail>> artNumSaleGroupMap = saleDetailList.stream().collect(Collectors.groupingBy(StoreSaleDetail::getProdArtNum));
        // 货号 颜色 维度，销量map
        Map<String, Map<String, List<StoreSaleDetail>>> artNumColorSaleGroupMap = saleDetailList.stream().collect(Collectors
                .groupingBy(StoreSaleDetail::getProdArtNum, Collectors.groupingBy(StoreSaleDetail::getColorName)));
        List<StoreTodaySaleSummaryDTO.STSSProdSaleDTO> retProdSaleList = new ArrayList<>();
        artNumSaleGroupMap.forEach((prodArtNum, prodArtNumSaleList) -> {
            BigDecimal saleAmount = prodArtNumSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal refundAmount = prodArtNumSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            Integer saleQuantity = prodArtNumSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            Integer refundQuantity = prodArtNumSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                    .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            String refundRate = saleQuantity > 0 ? String.format("%.2f%%", Math.abs(refundQuantity) * 100.0 / saleQuantity) : "0.00%";
            Map<String, List<StoreSaleDetail>> colorSaleMap = artNumColorSaleGroupMap.get(prodArtNum);
            colorSaleMap.forEach((colorName, colorSaleList) -> {
                BigDecimal colorSaleAmount = colorSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                        .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal colorRefundAmount = colorSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                        .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
                Integer colorSaleQuantity = colorSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.GENERAL_SALE.getValue()))
                        .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                Integer colorRefundQuantity = colorSaleList.stream().filter(x -> Objects.equals(x.getSaleType(), SaleType.SALE_REFUND.getValue()))
                        .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                retProdSaleList.add(new StoreTodaySaleSummaryDTO.STSSProdSaleDTO().setProdArtNum(prodArtNum).setColorName(colorName)
                        .setColorSaleAmount(colorSaleAmount).setColorRefundAmount(colorRefundAmount).setColorSaleQuantity(colorSaleQuantity)
                        .setColorRefundQuantity(colorRefundQuantity).setColorTotalQuantity(colorSaleQuantity + colorRefundQuantity)
                        .setColorTotalAmount(colorSaleAmount.add(colorRefundAmount)).setSaleAmount(saleAmount).setRefundAmount(refundAmount)
                        .setSaleQuantity(saleQuantity).setRefundQuantity(refundQuantity).setRefundRate(refundRate));
            });
        });
        // 按照总金额倒序排
        retProdSaleList.sort(Comparator.comparing(StoreTodaySaleSummaryDTO.STSSProdSaleDTO::getSaleAmount).reversed()
                .thenComparing(StoreTodaySaleSummaryDTO.STSSProdSaleDTO::getProdArtNum));
        return retProdSaleList;
    }


}
