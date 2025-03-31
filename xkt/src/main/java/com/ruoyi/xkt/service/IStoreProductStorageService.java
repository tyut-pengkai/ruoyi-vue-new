package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageResDTO;

import java.util.List;

/**
 * 档口商品入库Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStorageService {
    /**
     * 查询档口商品入库
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 档口商品入库
     */
    public StoreProdStorageResDTO selectByStoreProdStorId(Long storeProdStorId);

    /**
     * 查询档口商品入库列表
     *
     * @param storeProductStorage 档口商品入库
     * @return 档口商品入库集合
     */
    public List<StoreProductStorage> selectStoreProductStorageList(StoreProductStorage storeProductStorage);

    /**
     * 新增档口商品入库
     *
     * @param storeProdStorageDTO 档口商品入库
     * @return 结果
     */
    public int create(StoreProdStorageDTO storeProdStorageDTO);

    /**
     * 修改档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    public int updateStoreProductStorage(StoreProductStorage storeProductStorage);

    /**
     * 撤销档口商品入库
     *
     * @param storeProdStorId 需要删除的档口商品入库主键集合
     * @return 结果
     */
    public int deleteByStoreProdStorId(Long storeProdStorId);

    /**
     * 删除档口商品入库信息
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 结果
     */
    public int deleteStoreProductStorageByStoreProdStorId(Long storeProdStorId);

    /**
     * 分页查询
     * @param storagePageDTO 查询入参
     * @return Page
     */
    Page<StoreProdStoragePageResDTO> page(StoreProdStoragePageDTO storagePageDTO);

}
