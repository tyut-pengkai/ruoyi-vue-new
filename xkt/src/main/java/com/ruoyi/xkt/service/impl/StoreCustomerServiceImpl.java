package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusFuzzyResDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageResDTO;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 档口客户Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreCustomerServiceImpl implements IStoreCustomerService {

    final StoreCustomerMapper storeCusMapper;
    final StoreSaleMapper storeSaleMapper;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreSaleRefundRecordMapper saleRefundRecordMapper;
    final StoreSaleRefundRecordDetailMapper saleRefundRecordDetailMapper;

    /**
     * 模糊查询客户名称列表
     *
     * @param storeId 档口ID
     * @param cusName 客户名称
     * @return List<StoreCusFuzzyResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreCusFuzzyResDTO> fuzzyQueryList(Long storeId, String cusName) {
        LambdaQueryWrapper<StoreCustomer> queryWrapper = new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, storeId).eq(StoreCustomer::getDelFlag, Constants.UNDELETED);
        if (StringUtils.isNotBlank(cusName)) {
            queryWrapper.like(StoreCustomer::getCusName, cusName);
        }
        List<StoreCustomer> storeCusList = this.storeCusMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(storeCusList) ? new ArrayList<>() : storeCusList.stream()
                .map(x -> BeanUtil.toBean(x, StoreCusFuzzyResDTO.class).setStoreCusId(x.getId())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int create(StoreCusDTO storeCusDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeCusDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        if (StringUtils.isNotBlank(storeCusDTO.getPhone())) {
            final String regex = "^1[3-9]\\d{9}$";
            if (!Pattern.matches(regex, storeCusDTO.getPhone().trim())) {
                throw new ServiceException("请输入正确的手机号!", HttpStatus.ERROR);
            }
        }
        return this.storeCusMapper.insert(BeanUtil.toBean(storeCusDTO, StoreCustomer.class));
    }

    @Override
    @Transactional
    public void deleteStoreCus(Long storeCusId) {
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusId).eq(StoreCustomer::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口客户不存在!"));
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeCus.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 不允许删现金客户
        if (Objects.equals(storeCus.getCusName(), Constants.STORE_CUS_CASH)) {
            throw new ServiceException("请勿删除现金客户!", HttpStatus.ERROR);
        }
        storeCus.setDelFlag(Constants.DELETED);
        this.storeCusMapper.updateById(storeCus);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreCusPageResDTO> selectPage(StoreCusPageDTO storeCusPageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeCusPageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        LambdaQueryWrapper<StoreCustomer> queryWrapper = new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, storeCusPageDTO.getStoreId()).eq(StoreCustomer::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreCustomer::getCreateTime);
        if (StringUtils.isNotBlank(storeCusPageDTO.getCusName())) {
            queryWrapper.like(StoreCustomer::getCusName, storeCusPageDTO.getCusName());
        }
        PageHelper.startPage(storeCusPageDTO.getPageNum(), storeCusPageDTO.getPageSize());
        List<StoreCustomer> cusList = this.storeCusMapper.selectList(queryWrapper);
        // 使用公共方法转换 PageInfo 到 Page
        return Page.convert(new PageInfo<>(cusList), BeanUtil.copyToList(cusList, StoreCusPageResDTO.class));
    }


    @Override
    @Transactional
    public int updateStoreCus(StoreCusDTO storeCusDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeCusDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        Optional.ofNullable(storeCusDTO.getStoreCusId()).orElseThrow(() -> new ServiceException("档口客户ID不可为空!"));
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusDTO.getStoreCusId()).eq(StoreCustomer::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口客户不存在!"));
        if (StringUtils.isNotBlank(storeCusDTO.getPhone())) {
            final String regex = "^1[3-9]\\d{9}$";
            if (!Pattern.matches(regex, storeCusDTO.getPhone().trim())) {
                throw new ServiceException("请输入正确的手机号!", HttpStatus.ERROR);
            }
        }
        BeanUtil.copyProperties(storeCusDTO, storeCus);
        // 如果名称变更了，则需要调整关联表中的客户名称
        if (!Objects.equals(storeCusDTO.getCusName(), storeCus.getCusName())) {
            this.updateRelatedCusName(storeCusDTO.getCusName(), storeCus.getId(), storeCus.getStoreId());
        }
        return storeCusMapper.updateById(storeCus);
    }


    /**
     * 查询档口客户
     *
     * @param storeCusId 档口客户主键
     * @return 档口客户
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCusDTO selectByStoreCusId(Long storeCusId) {
        StoreCustomer storeCus = Optional.ofNullable(storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusId).eq(StoreCustomer::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口客户不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(storeCus, StoreCusDTO.class);
    }

    /**
     * 更新关联表的客户名称
     *
     * @param cusName    最新的客户名臣
     * @param storeCusId 档口客户ID
     * @param storeId    档口ID
     */
    private void updateRelatedCusName(String cusName, Long storeCusId, Long storeId) {
        // 档口销售表
        List<StoreSale> saleList = storeSaleMapper.selectList(new LambdaQueryWrapper<StoreSale>()
                .eq(StoreSale::getStoreId, storeId).eq(StoreSale::getDelFlag, Constants.UNDELETED)
                .eq(StoreSale::getStoreCusId, storeCusId));
        if (CollectionUtils.isNotEmpty(saleList)) {
            saleList.forEach(x -> x.setStoreCusName(cusName));
            storeSaleMapper.updateById(saleList);
        }
        // 档口销售明细表
        List<StoreSaleDetail> saleDetailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getStoreCusId, storeCusId));
        if (CollectionUtils.isNotEmpty(saleDetailList)) {
            saleDetailList.forEach(x -> x.setStoreCusName(cusName));
            this.saleDetailMapper.updateById(saleDetailList);
        }
        // 档口销售返单表
        List<StoreSaleRefundRecord> refundList = this.saleRefundRecordMapper.selectList(new LambdaQueryWrapper<StoreSaleRefundRecord>()
                .eq(StoreSaleRefundRecord::getStoreId, storeId).eq(StoreSaleRefundRecord::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleRefundRecord::getStoreCusId, storeCusId));
        if (CollectionUtils.isNotEmpty(refundList)) {
            refundList.forEach(x -> x.setStoreCusName(cusName));
            this.saleRefundRecordMapper.updateById(refundList);
        }
        // 档口销售返单明细表
        List<StoreSaleRefundRecordDetail> refundDetailList = this.saleRefundRecordDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleRefundRecordDetail>()
                .eq(StoreSaleRefundRecordDetail::getStoreId, storeId).eq(StoreSaleRefundRecordDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleRefundRecordDetail::getStoreCusId, storeCusId));
        if (CollectionUtils.isNotEmpty(refundDetailList)) {
            refundDetailList.forEach(x -> x.setStoreCusName(cusName));
            this.saleRefundRecordDetailMapper.updateById(refundDetailList);
        }
    }

}
