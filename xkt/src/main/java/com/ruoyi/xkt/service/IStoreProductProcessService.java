package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductProcess;

import java.util.List;

/**
 * 档口商品工艺信息Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductProcessService {
    /**
     * 查询档口商品工艺信息
     *
     * @param storeProdProcessId 档口商品工艺信息主键
     * @return 档口商品工艺信息
     */
    public StoreProductProcess selectStoreProductProcessByStoreProdProcessId(Long storeProdProcessId);

    /**
     * 查询档口商品工艺信息列表
     *
     * @param storeProductProcess 档口商品工艺信息
     * @return 档口商品工艺信息集合
     */
    public List<StoreProductProcess> selectStoreProductProcessList(StoreProductProcess storeProductProcess);

    /**
     * 新增档口商品工艺信息
     *
     * @param storeProductProcess 档口商品工艺信息
     * @return 结果
     */
    public int insertStoreProductProcess(StoreProductProcess storeProductProcess);

    /**
     * 修改档口商品工艺信息
     *
     * @param storeProductProcess 档口商品工艺信息
     * @return 结果
     */
    public int updateStoreProductProcess(StoreProductProcess storeProductProcess);

    /**
     * 批量删除档口商品工艺信息
     *
     * @param storeProdProcessIds 需要删除的档口商品工艺信息主键集合
     * @return 结果
     */
    public int deleteStoreProductProcessByStoreProdProcessIds(Long[] storeProdProcessIds);

    /**
     * 删除档口商品工艺信息信息
     *
     * @param storeProdProcessId 档口商品工艺信息主键
     * @return 结果
     */
    public int deleteStoreProductProcessByStoreProdProcessId(Long storeProdProcessId);
}
