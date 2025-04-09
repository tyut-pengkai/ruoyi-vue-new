package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeQuickFunction.StoreQuickFuncDTO;

import java.util.List;

/**
 * 档口快捷功能Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreQuickFunctionService {
    /**
     * 获取当前档口绑定的快捷功能
     *
     * @param storeId 当前档口ID
     * @return List<StoreQuickFuncDTO.DetailDTO>
     */
    List<StoreQuickFuncDTO.DetailDTO> getCheckedMenuList(Long storeId);

    /**
     * 更新档口绑定的快捷功能
     *
     * @param storeQuickFuncDTO 绑定档口快捷功能的DTO
     * @return
     */
    void updateCheckedList(StoreQuickFuncDTO storeQuickFuncDTO);

}
