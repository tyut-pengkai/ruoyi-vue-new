package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscDeleteDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageResDTO;

/**
 * 用户关注档口Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserSubscriptionsService {

    /**
     * 新增用户关注档口
     *
     * @param subscDTO 新增用户关注档口入参
     * @return Integer
     */
    Integer create(UserSubscDTO subscDTO);

    /**
     * 用户批量取消关注档口
     *
     * @param deleteDTO 取消关注档口入参
     * @return Integer
     */
    Integer delete(UserSubscDeleteDTO deleteDTO);

    /**
     * 用户关注档口列表
     *
     * @param pageDTO 查询入参
     * @return Page<UserSubscPageResDTO>
     */
    Page<UserSubscPageResDTO> page(UserSubscPageDTO pageDTO);

}
