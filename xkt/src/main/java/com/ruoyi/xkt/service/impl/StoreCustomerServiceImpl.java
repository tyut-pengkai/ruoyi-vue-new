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
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.dto.storeCustomer.*;
import com.ruoyi.xkt.mapper.StoreCustomerMapper;
import com.ruoyi.xkt.service.IStoreCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    /**
     * 更改是否大小码加价
     *
     * @param addOverPriceDTO 入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateAddOverPrice(StoreCusAddOverPriceDTO addOverPriceDTO) {
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getStoreId, addOverPriceDTO.getStoreId()).eq(StoreCustomer::getId, addOverPriceDTO.getStoreCusId())
                        .eq(StoreCustomer::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口客户不存在!", HttpStatus.ERROR));
        storeCus.setAddOverPrice(addOverPriceDTO.getAddOverPrice());
        return this.storeCusMapper.updateById(storeCus);
    }

    @Override
    @Transactional
    public int create(StoreCusDTO storeCusDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeCusDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
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
        BeanUtil.copyProperties(storeCusDTO, storeCus);
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

}
