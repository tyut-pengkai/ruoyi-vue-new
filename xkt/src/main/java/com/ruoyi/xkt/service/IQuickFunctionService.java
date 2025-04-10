package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.quickFunction.QuickFuncDTO;

import java.util.List;

/**
 * 档口快捷功能Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IQuickFunctionService {
    /**
     * 获取当前绑定的快捷功能
     *
     * @param roleId 当前角色ID
     * @param bizId  bizId
     * @return List<StoreQuickFuncDTO.DetailDTO>
     */
    List<QuickFuncDTO.DetailDTO> getCheckedMenuList(Long roleId, Long bizId);

    /**
     * 更新绑定的快捷功能
     *
     * @param storeQuickFuncDTO 绑定快捷功能的DTO
     * @return
     */
    void updateCheckedList(QuickFuncDTO storeQuickFuncDTO);

}
