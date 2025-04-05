package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;

import java.util.List;

/**
 * 档口所有颜色Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreColorService {

    /**
     * 查询档口所有颜色列表
     *
     * @param storeId 档口ID
     * @return List<StoreColorDTO>
     */
    List<StoreColorDTO> list(Long storeId);

}
