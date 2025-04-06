package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeRoleAccount.*;

import java.util.List;

/**
 * 档口子角色账号Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreRoleAccountService {

    /**
     * 新增档口子账号
     *
     * @param accDTO 子账号入参
     * @return
     */
    Integer insert(StoreRoleAccDTO accDTO);

    /**
     * 编辑档口子账号
     *
     * @param accUpdateDTO 档口子账号编辑入参
     * @return
     */
    Integer update(StoreRoleAccUpdateDTO accUpdateDTO);

    /**
     * 档口子账号列表
     *
     * @param listDTO
     * @return
     */
    List<StoreRoleAccResDTO> list(StoreRoleAccListDTO listDTO);

    /**
     * 获取档口子账号详情
     *
     * @param storeRoleAccId 档口子账号ID
     * @return
     */
    StoreRoleAccDetailResDTO selectByStoreRoleAccId(Long storeRoleAccId);

    /**
     * 停用/启用档口子账号
     * @param updateStatusDTO
     * @return
     */
    Integer updateAccountStatus(StoreRoleAccUpdateStatusDTO updateStatusDTO);

}
