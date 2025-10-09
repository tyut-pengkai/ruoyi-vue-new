package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.quickFunction.QuickFuncResDTO;
import com.ruoyi.xkt.dto.quickFunction.QuickFuncUpdateDTO;

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
     * @return List<StoreQuickFuncDTO.DetailDTO>
     */
    QuickFuncResDTO getCheckedMenuList();

    /**
     * 更新绑定的快捷功能
     *
     * @param updateDTO 绑定快捷功能的DTO
     * @return
     */
    Integer update(QuickFuncUpdateDTO updateDTO);

}
