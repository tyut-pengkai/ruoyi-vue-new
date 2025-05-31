package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.userIndex.UserOverallResDTO;

/**
 * 用户首页IService
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserIndexService {

    /**
     * 获取用户首页数据总览
     *
     * @return UserOverallResDTO
     */
    UserOverallResDTO getOverall();
}
