package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userNotice.*;

import java.util.List;

/**
 * 用户所有通知Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserNoticeService {

    /**
     * 查询用户所有通知列表
     *
     * @param pageDTO 查询入参
     * @return Page<UserNoticeResDTO>
     */
    Page<UserNoticeResDTO> pcPage(UserNoticePageDTO pageDTO);

    /**
     * 获取APP用户消息列表
     *
     * @return List<UserNoticeAppResDTO>
     */
    List<UserNoticeAppListResDTO> appList();

    /**
     * 获取APP某一个类型消息列表
     *
     * @param pageDTO 分页入参
     * @return List<UserNoticeAppResDTO>
     */
    Page<UserNoticeAppResDTO> appTypePage(UserNoticeAppTypePageDTO pageDTO);

    /**
     * app 全部已读
     *
     * @return Integer
     */
    Integer appBatchRead();

    /**
     * app 某一个具体分类已读
     *
     * @param targetNoticeType type
     * @return
     */
    Integer appTypeRead(Integer targetNoticeType);
}
