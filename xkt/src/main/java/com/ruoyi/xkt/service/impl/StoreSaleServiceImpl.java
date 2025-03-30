package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.domain.StoreSaleDetail;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusGeneralSaleDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDTO;
import com.ruoyi.xkt.mapper.StoreCustomerMapper;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import com.ruoyi.xkt.service.IStoreSaleService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * 获取当前档口客户的销售业绩
     *
     * @param days    查询时间diff
     * @param storeId 档口ID
     * @param storeCusId 档口客户ID
     * @return StoreCusGeneralSaleDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCusGeneralSaleDTO getCusGeneralSale(Integer days, Long storeId, Long storeCusId) {
        // 获取档口客户
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusId).eq(StoreCustomer::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口客户不存在!", HttpStatus.ERROR));
        // 当前时间
        Date nowDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 往前推60天
        Date pastDate = Date.from(LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 查询当前档口客户在这段时间内的销售业绩情况
        List<StoreSale> saleList = this.storeSaleMapper.selectList(new LambdaQueryWrapper<StoreSale>()
                .eq(StoreSale::getStoreId, storeId).eq(StoreSale::getStoreCusId, storeCusId)
                .eq(StoreSale::getDelFlag, "0").between(StoreSale::getVoucherDate, pastDate, nowDate));
        // 初始化返回对象
        StoreCusGeneralSaleDTO generalSaleDTO = StoreCusGeneralSaleDTO.builder().storeId(storeId).storeCusId(storeCusId)
                .cusName(storeCus.getCusName()).saleAmount(BigDecimal.ZERO).debtAmount(BigDecimal.ZERO)
                .saleCount(0L).build();
        if (CollectionUtils.isEmpty(saleList)) {
            return generalSaleDTO;
        }
        // 总的销售金额
        BigDecimal saleAmount = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总的销售数量
        Long saleCount = saleList.stream().map(x -> x.getQuantity() == null ? 0L : x.getQuantity()).reduce(0L, Long::sum);
        // 总的欠款金额
        BigDecimal debtAmount = saleList.stream().filter(x -> Objects.equals(x.getPayWay(), "DEBT"))
                .map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        return generalSaleDTO.setSaleAmount(saleAmount).setSaleCount(saleCount).setDebtAmount(debtAmount);
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
        String code = this.sequenceService.generateCode(storeSaleDTO.getStoreId(), "STORE_SALE", DateUtils.getDate());
        storeSale.setCode(code).setVoucherDate(DateUtils.getNowDate());
        int count = storeSaleMapper.insert(storeSale);
        // 处理订单明细
        List<StoreSaleDetail> saleDetailList = storeSaleDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreSaleDetail.class)
                .setSaleType(storeSaleDTO.getSaleType()).setStoreSaleId(storeSale.getId())).collect(Collectors.toList());
        this.storeSaleDetailMapper.insert(saleDetailList);
        return count;
    }


















































    /**
     * 查询档口销售出库
     *
     * @param storeSaleId 档口销售出库主键
     * @return 档口销售出库
     */
    @Override
    public StoreSale selectStoreSaleByStoreSaleId(Long storeSaleId) {
        return storeSaleMapper.selectStoreSaleByStoreSaleId(storeSaleId);
    }

    /**
     * 查询档口销售出库列表
     *
     * @param storeSale 档口销售出库
     * @return 档口销售出库
     */
    @Override
    public List<StoreSale> selectStoreSaleList(StoreSale storeSale) {
        return storeSaleMapper.selectStoreSaleList(storeSale);
    }


    /**
     * 修改档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreSale(StoreSale storeSale) {
        storeSale.setUpdateTime(DateUtils.getNowDate());
        return storeSaleMapper.updateStoreSale(storeSale);
    }

    /**
     * 批量删除档口销售出库
     *
     * @param storeSaleIds 需要删除的档口销售出库主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleByStoreSaleIds(Long[] storeSaleIds) {
        return storeSaleMapper.deleteStoreSaleByStoreSaleIds(storeSaleIds);
    }

    /**
     * 删除档口销售出库信息
     *
     * @param storeSaleId 档口销售出库主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleByStoreSaleId(Long storeSaleId) {
        return storeSaleMapper.deleteStoreSaleByStoreSaleId(storeSaleId);
    }


}
