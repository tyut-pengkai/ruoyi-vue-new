package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductCategoryAttribute;
import com.ruoyi.xkt.mapper.StoreProductCategoryAttributeMapper;
import com.ruoyi.xkt.service.IStoreProductCategoryAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品类目信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductCategoryAttributeServiceImpl implements IStoreProductCategoryAttributeService {
    @Autowired
    private StoreProductCategoryAttributeMapper storeProductCategoryAttributeMapper;

    /**
     * 查询档口商品类目信息
     *
     * @param storeProdAttrId 档口商品类目信息主键
     * @return 档口商品类目信息
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductCategoryAttribute selectStoreProductCategoryAttributeByStoreProdAttrId(Long storeProdAttrId) {
        return storeProductCategoryAttributeMapper.selectStoreProductCategoryAttributeByStoreProdAttrId(storeProdAttrId);
    }

    /**
     * 查询档口商品类目信息列表
     *
     * @param storeProductCategoryAttribute 档口商品类目信息
     * @return 档口商品类目信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductCategoryAttribute> selectStoreProductCategoryAttributeList(StoreProductCategoryAttribute storeProductCategoryAttribute) {
        return storeProductCategoryAttributeMapper.selectStoreProductCategoryAttributeList(storeProductCategoryAttribute);
    }

    /**
     * 新增档口商品类目信息
     *
     * @param storeProductCategoryAttribute 档口商品类目信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductCategoryAttribute(StoreProductCategoryAttribute storeProductCategoryAttribute) {
        storeProductCategoryAttribute.setCreateTime(DateUtils.getNowDate());
        return storeProductCategoryAttributeMapper.insertStoreProductCategoryAttribute(storeProductCategoryAttribute);
    }

    /**
     * 修改档口商品类目信息
     *
     * @param storeProductCategoryAttribute 档口商品类目信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductCategoryAttribute(StoreProductCategoryAttribute storeProductCategoryAttribute) {
        storeProductCategoryAttribute.setUpdateTime(DateUtils.getNowDate());
        return storeProductCategoryAttributeMapper.updateStoreProductCategoryAttribute(storeProductCategoryAttribute);
    }

    /**
     * 批量删除档口商品类目信息
     *
     * @param storeProdAttrIds 需要删除的档口商品类目信息主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductCategoryAttributeByStoreProdAttrIds(Long[] storeProdAttrIds) {
        return storeProductCategoryAttributeMapper.deleteStoreProductCategoryAttributeByStoreProdAttrIds(storeProdAttrIds);
    }

    /**
     * 删除档口商品类目信息信息
     *
     * @param storeProdAttrId 档口商品类目信息主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductCategoryAttributeByStoreProdAttrId(Long storeProdAttrId) {
        return storeProductCategoryAttributeMapper.deleteStoreProductCategoryAttributeByStoreProdAttrId(storeProdAttrId);
    }
}
