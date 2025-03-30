package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bigDecimal.CollectorsUtil;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusGeneralSaleDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePayStatusDTO;
import com.ruoyi.xkt.mapper.*;
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
    final StoreSaleRefundRecordMapper refundRecordMapper;
    final StoreSaleRefundRecordDetailMapper refundRecordDetailMapper;

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
        BigDecimal debtAmount = saleList.stream().filter(x -> Objects.equals(x.getPaymentStatus(), "DEBT"))
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
     * 批量结算客户欠款
     *
     * @param payStatusDTO 入参
     * @return int
     */
    @Override
    @Transactional
    public void clearStoreCusDebt(StoreSalePayStatusDTO payStatusDTO) {
        List<StoreSale> storeSaleList = Optional.ofNullable(this.storeSaleMapper.selectList(new LambdaQueryWrapper<StoreSale>()
                .in(StoreSale::getId, payStatusDTO.getStoreSaleIdList()).eq(StoreSale::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("没有找到对应的销售出库单!", HttpStatus.ERROR));
        // 勾选订单是否有已结算的
        List<StoreSale> settledList = storeSaleList.stream().filter(x -> Objects.equals(x.getPaymentStatus(), "SETTLED")).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(settledList)) {
            throw new ServiceException("当前订单已结算!" + settledList.stream().map(StoreSale::getCode).collect(Collectors.toList()), HttpStatus.ERROR);
        }
        storeSaleList.forEach(x -> x.setPaymentStatus("SETTLED"));
        this.storeSaleMapper.updateById(storeSaleList);
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
        // 总的数量
        Integer quantity = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 总的金额
        BigDecimal amount = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 属性赋值
        storeSale.setCode(code).setVoucherDate(DateUtils.getNowDate()).setQuantity(quantity).setAmount(amount)
                .setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername());
        int count = storeSaleMapper.insert(storeSale);
        // 处理订单明细
        List<StoreSaleDetail> saleDetailList = storeSaleDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreSaleDetail.class)
                .setSaleType(storeSaleDTO.getSaleType()).setStoreSaleId(storeSale.getId())).collect(Collectors.toList());
        this.storeSaleDetailMapper.insert(saleDetailList);


        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存



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
                        .eq(StoreSale::getId, storeSaleDTO.getStoreSaleId()).eq(StoreSale::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        // 档口销售出库明细列表
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreSaleId, storeSaleDTO.getStoreSaleId()).eq(StoreSaleDetail::getDelFlag, "0"));
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
        // 总的金额
        BigDecimal amount = storeSaleDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        int count = this.storeSaleMapper.updateById(storeSale.setQuantity(quantity).setAmount(amount)
                .setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername()));
        // 先将所有明细置为无效，再新增
        this.storeSaleDetailMapper.updateById(saleDetailList.stream().peek(x -> x.setDelFlag("2")).collect(Collectors.toList()));

        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存


        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存


        // 再新增档口销售出库明细数据
        List<StoreSaleDetail> detailList = storeSaleDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreSaleDetail.class)
                .setSaleType(storeSaleDTO.getSaleType()).setStoreSaleId(storeSale.getId())).collect(Collectors.toList());
        this.storeSaleDetailMapper.insert(detailList);
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
                        .eq(StoreSale::getId, storeSaleId).eq(StoreSale::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        StoreSaleDTO storeSaleDTO = BeanUtil.toBean(storeSale, StoreSaleDTO.class);
        // 查询销售出库明细
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreSaleId, storeSaleId).eq(StoreSaleDetail::getDelFlag, "0"));
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
    public int deleteStoreSaleByStoreSaleId(Long storeSaleId) {

        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存

        // TODO 客户销售金额扣减、商品销售金额扣减

        // 删除档口销售出库数据
        StoreSale storeSale = Optional.ofNullable(this.storeSaleMapper.selectOne(new LambdaQueryWrapper<StoreSale>()
                        .eq(StoreSale::getId, storeSaleId).eq(StoreSale::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        storeSale.setDelFlag("2");
        int count = this.storeSaleMapper.updateById(storeSale);
        // 删除档口销售出库明细数据
        List<StoreSaleDetail> saleDetailList = this.storeSaleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreSaleId, storeSaleId).eq(StoreSaleDetail::getDelFlag, "0"));
        this.storeSaleDetailMapper.updateById(saleDetailList.stream().peek(x -> x.setDelFlag("2")).collect(Collectors.toList()));
        return count;
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
     * 查询档口销售出库列表
     *
     * @param storeSale 档口销售出库
     * @return 档口销售出库集合
     */
    @Override
    public List<StoreSale> selectStoreSaleList(StoreSale storeSale) {
        return null;
    }



}
