package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductService;
import com.ruoyi.xkt.mapper.StoreProductServiceMapper;
import com.ruoyi.xkt.service.IStoreProductServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口商品服务Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductServiceServiceImpl implements IStoreProductServiceService {
    @Autowired
    private StoreProductServiceMapper storeProductServiceMapper;

    /**
     * 查询档口商品服务
     *
     * @param storeProdSvcId 档口商品服务主键
     * @return 档口商品服务
     */
    @Override
    public StoreProductService selectStoreProductServiceByStoreProdSvcId(Long storeProdSvcId) {
        return storeProductServiceMapper.selectStoreProductServiceByStoreProdSvcId(storeProdSvcId);
    }

    /**
     * 查询档口商品服务列表
     *
     * @param storeProductService 档口商品服务
     * @return 档口商品服务
     */
    @Override
    public List<StoreProductService> selectStoreProductServiceList(StoreProductService storeProductService) {
        return storeProductServiceMapper.selectStoreProductServiceList(storeProductService);
    }

    /**
     * 新增档口商品服务
     *
     * @param storeProductService 档口商品服务
     * @return 结果
     */
    @Override
    public int insertStoreProductService(StoreProductService storeProductService) {
        storeProductService.setCreateTime(DateUtils.getNowDate());
        return storeProductServiceMapper.insertStoreProductService(storeProductService);
    }

    /**
     * 修改档口商品服务
     *
     * @param storeProductService 档口商品服务
     * @return 结果
     */
    @Override
    public int updateStoreProductService(StoreProductService storeProductService) {
        storeProductService.setUpdateTime(DateUtils.getNowDate());
        return storeProductServiceMapper.updateStoreProductService(storeProductService);
    }

    /**
     * 批量删除档口商品服务
     *
     * @param storeProdSvcIds 需要删除的档口商品服务主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductServiceByStoreProdSvcIds(Long[] storeProdSvcIds) {
        return storeProductServiceMapper.deleteStoreProductServiceByStoreProdSvcIds(storeProdSvcIds);
    }

    /**
     * 删除档口商品服务信息
     *
     * @param storeProdSvcId 档口商品服务主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductServiceByStoreProdSvcId(Long storeProdSvcId) {
        return storeProductServiceMapper.deleteStoreProductServiceByStoreProdSvcId(storeProdSvcId);
    }
}
