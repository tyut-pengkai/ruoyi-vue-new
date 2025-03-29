package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserNotice;
import com.ruoyi.xkt.mapper.UserNoticeMapper;
import com.ruoyi.xkt.service.IUserNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户所有通知Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserNoticeServiceImpl implements IUserNoticeService {
    @Autowired
    private UserNoticeMapper userNoticeMapper;

    /**
     * 查询用户所有通知
     *
     * @param userNoticeId 用户所有通知主键
     * @return 用户所有通知
     */
    @Override
    @Transactional(readOnly = true)
    public UserNotice selectUserNoticeByUserNoticeId(Long userNoticeId) {
        return userNoticeMapper.selectUserNoticeByUserNoticeId(userNoticeId);
    }

    /**
     * 查询用户所有通知列表
     *
     * @param userNotice 用户所有通知
     * @return 用户所有通知
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserNotice> selectUserNoticeList(UserNotice userNotice) {
        return userNoticeMapper.selectUserNoticeList(userNotice);
    }

    /**
     * 新增用户所有通知
     *
     * @param userNotice 用户所有通知
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserNotice(UserNotice userNotice) {
        userNotice.setCreateTime(DateUtils.getNowDate());
        return userNoticeMapper.insertUserNotice(userNotice);
    }

    /**
     * 修改用户所有通知
     *
     * @param userNotice 用户所有通知
     * @return 结果
     */
    @Override
    public int updateUserNotice(UserNotice userNotice) {
        userNotice.setUpdateTime(DateUtils.getNowDate());
        return userNoticeMapper.updateUserNotice(userNotice);
    }

    /**
     * 批量删除用户所有通知
     *
     * @param userNoticeIds 需要删除的用户所有通知主键
     * @return 结果
     */
    @Override
    public int deleteUserNoticeByUserNoticeIds(Long[] userNoticeIds) {
        return userNoticeMapper.deleteUserNoticeByUserNoticeIds(userNoticeIds);
    }

    /**
     * 删除用户所有通知信息
     *
     * @param userNoticeId 用户所有通知主键
     * @return 结果
     */
    @Override
    public int deleteUserNoticeByUserNoticeId(Long userNoticeId) {
        return userNoticeMapper.deleteUserNoticeByUserNoticeId(userNoticeId);
    }
}
