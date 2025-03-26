package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.UserNoticeSetting;

import java.util.List;

/**
 * 用户通知接收设置Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserNoticeSettingService {
    /**
     * 查询用户通知接收设置
     *
     * @param userNoticeSetId 用户通知接收设置主键
     * @return 用户通知接收设置
     */
    public UserNoticeSetting selectUserNoticeSettingByUserNoticeSetId(Long userNoticeSetId);

    /**
     * 查询用户通知接收设置列表
     *
     * @param userNoticeSetting 用户通知接收设置
     * @return 用户通知接收设置集合
     */
    public List<UserNoticeSetting> selectUserNoticeSettingList(UserNoticeSetting userNoticeSetting);

    /**
     * 新增用户通知接收设置
     *
     * @param userNoticeSetting 用户通知接收设置
     * @return 结果
     */
    public int insertUserNoticeSetting(UserNoticeSetting userNoticeSetting);

    /**
     * 修改用户通知接收设置
     *
     * @param userNoticeSetting 用户通知接收设置
     * @return 结果
     */
    public int updateUserNoticeSetting(UserNoticeSetting userNoticeSetting);

    /**
     * 批量删除用户通知接收设置
     *
     * @param userNoticeSetIds 需要删除的用户通知接收设置主键集合
     * @return 结果
     */
    public int deleteUserNoticeSettingByUserNoticeSetIds(Long[] userNoticeSetIds);

    /**
     * 删除用户通知接收设置信息
     *
     * @param userNoticeSetId 用户通知接收设置主键
     * @return 结果
     */
    public int deleteUserNoticeSettingByUserNoticeSetId(Long userNoticeSetId);
}
