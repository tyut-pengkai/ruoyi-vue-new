package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowHisPageDTO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowsingHisPageResDTO;

/**
 * 用户搜索历史Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserBrowsingHistoryService {

    /**
     * 查询用户浏览历史分页
     *
     * @param pageDTO 查询入参
     * @return Page<UserBrowsingHisResDTO>
     */
    Page<UserBrowsingHisPageResDTO> page(UserBrowHisPageDTO pageDTO);

}
