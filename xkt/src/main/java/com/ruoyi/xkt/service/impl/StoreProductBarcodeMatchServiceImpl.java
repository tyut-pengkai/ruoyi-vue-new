package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductBarcodeMatch;
import com.ruoyi.xkt.mapper.StoreProductBarcodeMatchMapper;
import com.ruoyi.xkt.service.IStoreProductBarcodeMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口条形码和第三方系统条形码匹配结果Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductBarcodeMatchServiceImpl implements IStoreProductBarcodeMatchService {
    @Autowired
    private StoreProductBarcodeMatchMapper storeProductBarcodeMatchMapper;

    /**
     * 查询档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProdBarcodeMatchId 档口条形码和第三方系统条形码匹配结果主键
     * @return 档口条形码和第三方系统条形码匹配结果
     */
    @Override
    public StoreProductBarcodeMatch selectStoreProductBarcodeMatchByStoreProdBarcodeMatchId(Long storeProdBarcodeMatchId) {
        return storeProductBarcodeMatchMapper.selectStoreProductBarcodeMatchByStoreProdBarcodeMatchId(storeProdBarcodeMatchId);
    }

    /**
     * 查询档口条形码和第三方系统条形码匹配结果列表
     *
     * @param storeProductBarcodeMatch 档口条形码和第三方系统条形码匹配结果
     * @return 档口条形码和第三方系统条形码匹配结果
     */
    @Override
    public List<StoreProductBarcodeMatch> selectStoreProductBarcodeMatchList(StoreProductBarcodeMatch storeProductBarcodeMatch) {
        return storeProductBarcodeMatchMapper.selectStoreProductBarcodeMatchList(storeProductBarcodeMatch);
    }

    /**
     * 新增档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProductBarcodeMatch 档口条形码和第三方系统条形码匹配结果
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductBarcodeMatch(StoreProductBarcodeMatch storeProductBarcodeMatch) {
        storeProductBarcodeMatch.setCreateTime(DateUtils.getNowDate());
        return storeProductBarcodeMatchMapper.insertStoreProductBarcodeMatch(storeProductBarcodeMatch);
    }

    /**
     * 修改档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProductBarcodeMatch 档口条形码和第三方系统条形码匹配结果
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductBarcodeMatch(StoreProductBarcodeMatch storeProductBarcodeMatch) {
        storeProductBarcodeMatch.setUpdateTime(DateUtils.getNowDate());
        return storeProductBarcodeMatchMapper.updateStoreProductBarcodeMatch(storeProductBarcodeMatch);
    }

    /**
     * 批量删除档口条形码和第三方系统条形码匹配结果
     *
     * @param storeProdBarcodeMatchIds 需要删除的档口条形码和第三方系统条形码匹配结果主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchIds(Long[] storeProdBarcodeMatchIds) {
        return storeProductBarcodeMatchMapper.deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchIds(storeProdBarcodeMatchIds);
    }

    /**
     * 删除档口条形码和第三方系统条形码匹配结果信息
     *
     * @param storeProdBarcodeMatchId 档口条形码和第三方系统条形码匹配结果主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchId(Long storeProdBarcodeMatchId) {
        return storeProductBarcodeMatchMapper.deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchId(storeProdBarcodeMatchId);
    }
}
