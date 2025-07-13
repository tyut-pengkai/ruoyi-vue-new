package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.userHomepage.UserHomepageOverviewDTO;

/**
 * 用户首页 服务层
 *
 * @author ruoyi
 */
public interface IUserHomepageService {

    /**
     * 获取用户首页统计概览
     *
     * @return UserHomepageOverviewDTO
     */
    UserHomepageOverviewDTO overview();
}
