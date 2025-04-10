package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeCusProdDiscount.*;
import com.ruoyi.xkt.mapper.StoreCustomerMapper;
import com.ruoyi.xkt.mapper.StoreCustomerProductDiscountMapper;
import com.ruoyi.xkt.mapper.StoreProductColorMapper;
import com.ruoyi.xkt.service.IStoreCustomerProductDiscountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

    final StoreCustomerProductDiscountMapper cusProdDiscMapper;
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
                .eq(StoreCustomer::getStoreId, cusProdDisDTO.getStoreId()).eq(StoreCustomer::getDelFlag, Constants.UNDELETED)
                .eq(StoreCustomer::getCusName, cusProdDisDTO.getStoreCusName()));
        if (ObjectUtils.isNotEmpty(storeCusList) && storeCusList.size() > 1) {
            throw new ServiceException("客户名称重复，请修改客户名称!", HttpStatus.CONFLICT);
        }
        StoreCustomer storeCus = CollectionUtils.isNotEmpty(storeCusList) ? storeCusList.get(0) : this.createStoreCustomer(cusProdDisDTO);
        // 获取当前档口客户已有的优惠
        List<StoreCustomerProductDiscount> cusProdDisList = Optional.ofNullable(cusProdDiscMapper.selectList(new LambdaQueryWrapper<StoreCustomerProductDiscount>()
                .eq(StoreCustomerProductDiscount::getStoreCusName, cusProdDisDTO.getStoreCusName()).eq(StoreCustomerProductDiscount::getDelFlag, Constants.UNDELETED)
                .eq(StoreCustomerProductDiscount::getStoreId, cusProdDisDTO.getStoreId()))).orElse(new ArrayList<>());
        // 已存在优惠但优惠额度低于当前优惠，则更新该部分优惠
        List<StoreCustomerProductDiscount> updateList = cusProdDisList.stream()
                // 找到所有优惠低于当前优惠额度的列表
                .filter(x -> cusProdDisDTO.getDiscount().compareTo(ObjectUtils.defaultIfNull(x.getDiscount(), 0)) > 0)
                // 更新最新的优惠
                .peek(x -> x.setDiscount(cusProdDisDTO.getDiscount())).collect(Collectors.toList());
        // 已有优惠的id
        List<Long> existDiscountProdColorIdList = cusProdDisList.stream().map(StoreCustomerProductDiscount::getStoreProdColorId).collect(Collectors.toList());
        // 档口所有的商品
        List<StoreProductColor> storeProdColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, cusProdDisDTO.getStoreId()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
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
        return this.cusProdDiscMapper.insertOrUpdate(updateList).size();
    }

    /**
     * 客户销售管理 批量减价、批量抹零减价、新增客户定价优惠
     *
     * @param batchDiscDTO 批量减价入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer batchDiscount(StoreCusProdBatchDiscountDTO batchDiscDTO) {
        // 获取当前客户已有优惠列表
        List<StoreCustomerProductDiscount> prodCusDiscList = this.cusProdDiscMapper.selectList(new LambdaQueryWrapper<StoreCustomerProductDiscount>()
                .in(StoreCustomerProductDiscount::getStoreCusId, batchDiscDTO.getDiscountList().stream()
                        .map(StoreCusProdBatchDiscountDTO.DiscountItemDTO::getStoreCusId).distinct().collect(Collectors.toList()))
                .eq(StoreCustomerProductDiscount::getDelFlag, Constants.UNDELETED)
                .eq(StoreCustomerProductDiscount::getStoreId, batchDiscDTO.getStoreId())
                .in(StoreCustomerProductDiscount::getStoreProdColorId, batchDiscDTO.getDiscountList().stream()
                        .map(StoreCusProdBatchDiscountDTO.DiscountItemDTO::getStoreProdColorId).distinct().collect(Collectors.toList())));
        // 数据库已有的客户优惠map
        Map<String, StoreCustomerProductDiscount> prodCusDiscMap = prodCusDiscList.stream().collect(Collectors
                .toMap(item -> item.getStoreCusId().toString() + item.getStoreProdColorId().toString(), Function.identity()));
        // 最新的优惠金额
        List<StoreCustomerProductDiscount> updateList = new ArrayList<>();
        // 入参优惠列表
        batchDiscDTO.getDiscountList().forEach(itemDTO -> {
            // 如果已存在优惠则叠加
            if (prodCusDiscMap.containsKey(itemDTO.getStoreCusId().toString() + itemDTO.getStoreProdColorId().toString())) {
                StoreCustomerProductDiscount prodColorDisc = prodCusDiscMap.get(itemDTO.getStoreCusId().toString() + itemDTO.getStoreProdColorId().toString());
                // 如果是批量优惠、批量减价则进行叠加 反之为新增客户优惠，直接覆盖
                if (batchDiscDTO.getIsInsert()) {
                    prodColorDisc.setDiscount(ObjectUtils.defaultIfNull(itemDTO.getDiscount(), 0));
                } else {
                    // 优惠金额进行累加
                    prodColorDisc.setDiscount(ObjectUtils.defaultIfNull(prodColorDisc.getDiscount(), 0) + ObjectUtils.defaultIfNull(itemDTO.getDiscount(), 0));
                }
                updateList.add(prodColorDisc);
                // 不存在优惠则新增
            } else {
                // 新增优惠
                updateList.add(new StoreCustomerProductDiscount() {{
                    setDiscount(itemDTO.getDiscount());
                    setStoreId(batchDiscDTO.getStoreId());
                    setStoreCusName(itemDTO.getStoreCusName());
                    setStoreCusId(itemDTO.getStoreCusId());
                    setStoreProdColorId(itemDTO.getStoreProdColorId());
                    setStoreProdId(itemDTO.getStoreProdId());
                }});
            }
        });
        if (CollectionUtils.isEmpty(updateList)) {
            return 0;
        }
        return this.cusProdDiscMapper.updateById(updateList).size();
    }

    /**
     * 查询客户销售管理列表
     *
     * @param pageDTO 分页入参
     * @return Page<StoreCusProdDiscPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreCusProdDiscPageResDTO> selectPage(StoreCusProdDiscPageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreCusProdDiscPageResDTO> list = this.cusProdDiscMapper.selectDiscPage(pageDTO);
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 客户销售管理，新增客户优惠时，判断是否已存在优惠
     *
     * @param existDTO 优惠是否存在DTO
     * @return String
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreCusProdDiscExistResDTO> discountExist(StoreCusProdDiscExistDTO existDTO) {
        List<StoreCustomerProductDiscount> discountList = this.cusProdDiscMapper.selectList(new LambdaQueryWrapper<StoreCustomerProductDiscount>()
                .eq(StoreCustomerProductDiscount::getStoreId, existDTO.getStoreId())
                .in(StoreCustomerProductDiscount::getStoreProdColorId, existDTO.getDiscountList().stream().map(StoreCusProdDiscExistDTO.DiscountItemDTO::getStoreProdColorId).collect(Collectors.toList()))
                .in(StoreCustomerProductDiscount::getStoreCusId, existDTO.getDiscountList().stream().map(StoreCusProdDiscExistDTO.DiscountItemDTO::getStoreCusId).collect(Collectors.toList()))
                .eq(StoreCustomerProductDiscount::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(discountList)) {
            return new ArrayList<>();
        }
        // 判断存在哪些优惠，给与提示
        Map<String, StoreCustomerProductDiscount> existDiscMap = discountList.stream().collect(Collectors
                .toMap(x -> x.getStoreCusId().toString() + x.getStoreProdColorId().toString(), Function.identity()));
        // 根据入参一次确认
        List<StoreCusProdDiscExistResDTO> existResList = new ArrayList<>();
        existDTO.getDiscountList().forEach(x -> {
            final String existKey = x.getStoreCusId().toString() + x.getStoreProdColorId().toString();
            if (existDiscMap.containsKey(existKey)) {
                existResList.add(new StoreCusProdDiscExistResDTO() {{
                    setExitDiscount(existDiscMap.get(existKey).getDiscount());
                    setDiscount(x.getDiscount());
                    setColorName(x.getColorName());
                    setProdArtNum(x.getProdArtNum());
                    setStoreCusName(x.getStoreCusName());
                }});
            }
        });
        return existResList;
    }

    /**
     * 新增档口客户
     *
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
