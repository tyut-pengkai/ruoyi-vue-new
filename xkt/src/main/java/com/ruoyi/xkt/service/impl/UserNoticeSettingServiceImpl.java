package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserNoticeSetting;
import com.ruoyi.xkt.mapper.UserNoticeSettingMapper;
import com.ruoyi.xkt.service.IUserNoticeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户通知接收设置Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserNoticeSettingServiceImpl implements IUserNoticeSettingService {
    @Autowired
    private UserNoticeSettingMapper userNoticeSettingMapper;

    /**
     * 查询用户通知接收设置
     *
     * @param userNoticeSetId 用户通知接收设置主键
     * @return 用户通知接收设置
     */
    @Override
    public UserNoticeSetting selectUserNoticeSettingByUserNoticeSetId(Long userNoticeSetId) {
        return userNoticeSettingMapper.selectUserNoticeSettingByUserNoticeSetId(userNoticeSetId);
    }

    /**
     * 查询用户通知接收设置列表
     *
     * @param userNoticeSetting 用户通知接收设置
     * @return 用户通知接收设置
     */
    @Override
    public List<UserNoticeSetting> selectUserNoticeSettingList(UserNoticeSetting userNoticeSetting) {
        return userNoticeSettingMapper.selectUserNoticeSettingList(userNoticeSetting);
    }

    /**
     * 新增用户通知接收设置
     *
     * @param userNoticeSetting 用户通知接收设置
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserNoticeSetting(UserNoticeSetting userNoticeSetting) {
        userNoticeSetting.setCreateTime(DateUtils.getNowDate());
        return userNoticeSettingMapper.insertUserNoticeSetting(userNoticeSetting);
    }

    /**
     * 修改用户通知接收设置
     *
     * @param userNoticeSetting 用户通知接收设置
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserNoticeSetting(UserNoticeSetting userNoticeSetting) {
        userNoticeSetting.setUpdateTime(DateUtils.getNowDate());
        return userNoticeSettingMapper.updateUserNoticeSetting(userNoticeSetting);
    }

    /**
     * 批量删除用户通知接收设置
     *
     * @param userNoticeSetIds 需要删除的用户通知接收设置主键
     * @return 结果
     */
    @Override
    public int deleteUserNoticeSettingByUserNoticeSetIds(Long[] userNoticeSetIds) {
        return userNoticeSettingMapper.deleteUserNoticeSettingByUserNoticeSetIds(userNoticeSetIds);
    }

    /**
     * 删除用户通知接收设置信息
     *
     * @param userNoticeSetId 用户通知接收设置主键
     * @return 结果
     */
    @Override
    public int deleteUserNoticeSettingByUserNoticeSetId(Long userNoticeSetId) {
        return userNoticeSettingMapper.deleteUserNoticeSettingByUserNoticeSetId(userNoticeSetId);
    }
}
