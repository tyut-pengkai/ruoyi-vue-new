package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.dto.store.StoreCreateDTO;
import com.ruoyi.xkt.dto.store.StoreUpdateDTO;

import java.util.List;

/**
 * 档口Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreService {

    /**
     * 更新档口数据
     * @param storeUpdateDTO 更新数据入参
     * @return int
     */
    public int updateStore(StoreUpdateDTO  storeUpdateDTO);













    /**
     * 注册时新增档口信息
     * @param createDTO 新增DTO
     * @return int
     */
    public int create(StoreCreateDTO createDTO);

    /**
     * 查询档口
     *
     * @param storeId 档口主键
     * @return 档口
     */
    public Store selectStoreByStoreId(Long storeId);

    /**
     * 查询档口列表
     *
     * @param store 档口
     * @return 档口集合
     */
    public List<Store> selectStoreList(Store store);

    /**
     * 新增档口
     *
     * @param store 档口
     * @return 结果
     */
    public int insertStore(Store store);


    /**
     * 批量删除档口
     *
     * @param storeIds 需要删除的档口主键集合
     * @return 结果
     */
    public int deleteStoreByStoreIds(Long[] storeIds);

    /**
     * 删除档口信息
     *
     * @param storeId 档口主键
     * @return 结果
     */
    public int deleteStoreByStoreId(Long storeId);
}
