package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageResDTO;
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

    @Override
    @Transactional
    public int create(StoreCusDTO storeCusDTO) {
        return this.storeCusMapper.insert(BeanUtil.toBean(storeCusDTO, StoreCustomer.class));
    }

    @Override
    @Transactional
    public void deleteStoreCus(Long storeCusId) {
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusId).eq(StoreCustomer::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口客户不存在!"));
        storeCus.setDelFlag("2");
        this.storeCusMapper.updateById(storeCus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreCusPageResDTO> selectPage(StoreCusPageDTO storeCusPageDTO) {
        LambdaQueryWrapper<StoreCustomer> queryWrapper = new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, storeCusPageDTO.getStoreId()).eq(StoreCustomer::getDelFlag, "0");
        if (StringUtils.isNotBlank(storeCusPageDTO.getCusName())) {
            queryWrapper.like(StoreCustomer::getCusName, storeCusPageDTO.getCusName());
        }
//        PageHelper.startPage((int)storeCusPageDTO.getPageNum(), (int)storeCusPageDTO.getPageSize());
        Page<StoreCustomer> page = new Page<>(storeCusPageDTO.getPageNum(), storeCusPageDTO.getPageSize());
        Page<StoreCustomer> cusList = this.storeCusMapper.selectPage(page, queryWrapper);
//        List<StoreCusPageResDTO> cusPageResDTOS = this.storeCusMapper.selectList(cusList, StoreCusPageResDTO.class);
//        List<StoreCustomer> cusList = this.storeCusMapper.selectList(queryWrapper);
//        System.err.println(cusList);
//        return null;
        return CollectionUtils.isEmpty(cusList.getRecords()) ? new ArrayList<>() : BeanUtil.copyToList(cusList.getRecords(), StoreCusPageResDTO.class);
    }

    @Override
    @Transactional
    public int updateStoreCus(StoreCusDTO storeCusDTO) {
        Optional.ofNullable(storeCusDTO.getStoreCusId()).orElseThrow(() -> new ServiceException("档口客户ID不可为空!"));
        StoreCustomer storeCus = Optional.ofNullable(this.storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusDTO.getStoreCusId()).eq(StoreCustomer::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口客户不存在!"));
        BeanUtil.copyProperties(storeCusDTO, storeCus);
        return storeCusMapper.updateStoreCustomer(storeCus);
    }


    /**
     * 查询档口客户
     *
     * @param storeCusId 档口客户主键
     * @return 档口客户
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCusDTO selectStoreCustomerByStoreCusId(Long storeCusId) {
        StoreCustomer storeCus = Optional.ofNullable(storeCusMapper.selectOne(new LambdaQueryWrapper<StoreCustomer>()
                        .eq(StoreCustomer::getId, storeCusId).eq(StoreCustomer::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口客户不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(storeCus, StoreCusDTO.class);
    }

    /**
     * 查询档口客户列表
     *
     * @param storeCustomer 档口客户
     * @return 档口客户
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreCustomer> selectStoreCustomerList(StoreCustomer storeCustomer) {
        return storeCusMapper.selectStoreCustomerList(storeCustomer);
    }

    /**
     * 新增档口客户
     *
     * @param storeCustomer 档口客户
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreCustomer(StoreCustomer storeCustomer) {
        storeCustomer.setCreateTime(DateUtils.getNowDate());
        return storeCusMapper.insertStoreCustomer(storeCustomer);
    }


    /**
     * 批量删除档口客户
     *
     * @param storeCusIds 需要删除的档口客户主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreCustomerByStoreCusIds(Long[] storeCusIds) {
        return storeCusMapper.deleteStoreCustomerByStoreCusIds(storeCusIds);
    }

    /**
     * 删除档口客户信息
     *
     * @param storeCusId 档口客户主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreCustomerByStoreCusId(Long storeCusId) {
        return storeCusMapper.deleteStoreCustomerByStoreCusId(storeCusId);
    }


}
