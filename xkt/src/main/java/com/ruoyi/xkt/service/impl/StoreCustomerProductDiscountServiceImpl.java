package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeCusProdDiscount.StoreCusProdDiscountDTO;
import com.ruoyi.xkt.mapper.StoreCustomerMapper;
import com.ruoyi.xkt.mapper.StoreCustomerProductDiscountMapper;
import com.ruoyi.xkt.mapper.StoreProductColorMapper;
import com.ruoyi.xkt.service.IStoreCustomerProductDiscountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口客户优惠Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreCustomerProductDiscountServiceImpl implements IStoreCustomerProductDiscountService {

    final StoreCustomerProductDiscountMapper cusProdDiscountMapper;
    final StoreCustomerMapper storeCusMapper;
    final StoreProductColorMapper storeProdColorMapper;

    /**
     * 修改档口客户优惠
     *
     * @param cusProdDisDTO 档口客户优惠
     * @return 结果
     */
    @Override
    @Transactional
    public Integer updateStoreCusProdDiscount(StoreCusProdDiscountDTO cusProdDisDTO) {
        List<StoreCustomer> storeCusList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, cusProdDisDTO.getStoreId()).eq(StoreCustomer::getDelFlag, "0")
                .eq(StoreCustomer::getCusName, cusProdDisDTO.getStoreCusName()));
        if (ObjectUtils.isNotEmpty(storeCusList) && storeCusList.size() > 1) {
            throw new ServiceException("客户名称重复，请修改客户名称!", HttpStatus.CONFLICT);
        }
        StoreCustomer storeCus = CollectionUtils.isNotEmpty(storeCusList) ? storeCusList.get(0) : this.createStoreCustomer(cusProdDisDTO);
        // 获取当前档口客户已有的优惠
        List<StoreCustomerProductDiscount> cusProdDisList = Optional.ofNullable(cusProdDiscountMapper.selectList(new LambdaQueryWrapper<StoreCustomerProductDiscount>()
                .eq(StoreCustomerProductDiscount::getStoreCusName, cusProdDisDTO.getStoreCusName()).eq(StoreCustomerProductDiscount::getDelFlag, "0")
                .eq(StoreCustomerProductDiscount::getStoreId, cusProdDisDTO.getStoreId()))).orElse(new ArrayList<>());
        // 已存在优惠但优惠额度低于当前优惠，则更新该部分优惠
        List<StoreCustomerProductDiscount> updateList = cusProdDisList.stream()
                // 找到所有优惠低于当前优惠额度的列表
                .filter(x -> cusProdDisDTO.getDiscount().compareTo(ObjectUtils.defaultIfNull(x.getDiscount(), BigDecimal.ZERO)) > 0)
                // 更新最新的优惠
                .peek(x -> x.setDiscount(cusProdDisDTO.getDiscount())).collect(Collectors.toList());
        // 已有优惠的id
        List<Long> existDiscountProdColorIdList = cusProdDisList.stream().map(StoreCustomerProductDiscount::getStoreProdColorId).collect(Collectors.toList());
        // 档口所有的商品
        List<StoreProductColor> storeProdColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, cusProdDisDTO.getStoreId()).eq(StoreProductColor::getDelFlag, "0"));
        // 绑定其它商品的优惠
        List<StoreCustomerProductDiscount> addDiscountList = storeProdColorList.stream().filter(x -> !existDiscountProdColorIdList.contains(x.getId()))
                .map(x -> BeanUtil.toBean(x, StoreCustomerProductDiscount.class).setDiscount(cusProdDisDTO.getDiscount()).setStoreProdColorId(x.getId())
                        .setStoreId(cusProdDisDTO.getStoreId()).setStoreCusId(storeCus.getId()).setStoreCusName(cusProdDisDTO.getStoreCusName()))
                .collect(Collectors.toList());
        // 档口所有商品优惠
        if (CollectionUtils.isNotEmpty(addDiscountList)) {
            updateList.addAll(addDiscountList);
        }
        // 更新及新增当前客户优惠
        return this.cusProdDiscountMapper.insertOrUpdate(updateList).size();
    }



    /**
     * 查询档口客户优惠
     *
     * @param storeCusProdDiscId 档口客户优惠主键
     * @return 档口客户优惠
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCustomerProductDiscount selectStoreCustomerProductDiscountByStoreCusProdDiscId(Long storeCusProdDiscId) {
        return cusProdDiscountMapper.selectStoreCustomerProductDiscountByStoreCusProdDiscId(storeCusProdDiscId);
    }

    /**
     * 查询档口客户优惠列表
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 档口客户优惠
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreCustomerProductDiscount> selectStoreCustomerProductDiscountList(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        return cusProdDiscountMapper.selectStoreCustomerProductDiscountList(storeCustomerProductDiscount);
    }

    /**
     * 新增档口客户优惠
     *
     * @param storeCustomerProductDiscount 档口客户优惠
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreCustomerProductDiscount(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        storeCustomerProductDiscount.setCreateTime(DateUtils.getNowDate());
        return cusProdDiscountMapper.insertStoreCustomerProductDiscount(storeCustomerProductDiscount);
    }

    /**
     * 批量删除档口客户优惠
     *
     * @param storeCusProdDiscIds 需要删除的档口客户优惠主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(Long[] storeCusProdDiscIds) {
        return cusProdDiscountMapper.deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(storeCusProdDiscIds);
    }

    /**
     * 删除档口客户优惠信息
     *
     * @param storeCusProdDiscId 档口客户优惠主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreCustomerProductDiscountByStoreCusProdDiscId(Long storeCusProdDiscId) {
        return cusProdDiscountMapper.deleteStoreCustomerProductDiscountByStoreCusProdDiscId(storeCusProdDiscId);
    }

    /**
     * 新增档口客户
     * @param cusProdDisDTO 新增档口客户入参
     * @return StoreCustomer
     */
    private StoreCustomer createStoreCustomer(StoreCusProdDiscountDTO cusProdDisDTO) {
        StoreCustomer storeCus = new StoreCustomer();
        storeCus.setCusName(cusProdDisDTO.getStoreCusName());
        storeCus.setPhone(cusProdDisDTO.getPhone());
        storeCus.setStoreId(cusProdDisDTO.getStoreId());
        this.storeCusMapper.insert(storeCus);
        return storeCus;
    }

}
