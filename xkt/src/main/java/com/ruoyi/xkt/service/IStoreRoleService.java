package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeRole.StoreRoleDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleListDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleResDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleUpdateStatusDTO;

import java.util.List;

/**
 * 档口子角色Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreRoleService {

    /**
     * 新增档口子角色
     *
     * @param storeRoleDTO 档口子角色
     * @return 结果
     */
    public int insertStoreRole(StoreRoleDTO storeRoleDTO);

    /**
     * 更新档口子角色
     *
     * @param storeRoleDTO 档口子角色信息
     * @return
     */
    Integer update(StoreRoleDTO storeRoleDTO);

    /**
     * 查询档口子角色菜单详情
     *
     * @param storeRoleId 档口子角色ID
     * @return
     */
    StoreRoleDTO selectByStoreRoleId(Long storeRoleId);

    /**
     * 档口子角色分页查询
     *
     * @param pageDTO 档口子角色分页查询入参
     * @return
     */
    List<StoreRoleResDTO> list(StoreRoleListDTO pageDTO);

    /**
     * 更新档口子角色状态
     *
     * @param updateStatusDTO 子角色状态更新入参
     * @return
     */
    Integer updateRoleStatus(StoreRoleUpdateStatusDTO updateStatusDTO);

}
