package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.store.*;

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
    int updateStore(StoreUpdateDTO storeUpdateDTO);

    /**
     * 注册时新增档口信息
     *
     * @param createDTO 新增DTO
     * @return int
     */
    int create(StoreCreateDTO createDTO);

    /**
     * 档口分页数据
     *
     * @param pageDTO 查询入参
     * @return
     */
    Page<StorePageResDTO> page(StorePageDTO pageDTO);

    /**
     * 更新档口启用/停用状态
     *
     * @param delFlagDTO 入参
     * @return
     */
    Integer updateDelFlag(StoreUpdateDelFlagDTO delFlagDTO);

    /**
     * 审核档口
     *
     * @param auditDTO 审核入参
     * @return Integer
     */
    Integer approve(StoreAuditDTO auditDTO);

    /**
     * 获取档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    StoreBasicResDTO getInfo(Long storeId);

    /**
     * 审核时获取档口信息
     *
     * @param storeId 档口ID
     * @return StoreApproveResDTO
     */
    StoreApproveResDTO getApproveInfo(Long storeId);

}
