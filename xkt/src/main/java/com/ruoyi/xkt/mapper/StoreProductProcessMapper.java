package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductProcess;

import java.util.List;

/**
 * 档口商品工艺信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductProcessMapper extends BaseMapper<StoreProductProcess> {
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
     * 删除档口商品工艺信息
     *
     * @param storeProdProcessId 档口商品工艺信息主键
     * @return 结果
     */
    public int deleteStoreProductProcessByStoreProdProcessId(Long storeProdProcessId);

    /**
     * 批量删除档口商品工艺信息
     *
     * @param storeProdProcessIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductProcessByStoreProdProcessIds(Long[] storeProdProcessIds);

    void updateDelFlagByStoreProdId(Long storeProdId);

    StoreProductProcess selectByStoreProdId(Long storeProdId);
}
