package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.store.StoreCreateDTO;
import com.ruoyi.xkt.dto.store.StoreUpdateDTO;

/**
 * 档口Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreService {

    /**
     * 更新档口数据
     *
     * @param storeUpdateDTO 更新数据入参
     * @return int
     */
    public int updateStore(StoreUpdateDTO storeUpdateDTO);

    /**
     * 注册时新增档口信息
     *
     * @param createDTO 新增DTO
     * @return int
     */
    public int create(StoreCreateDTO createDTO);

}
