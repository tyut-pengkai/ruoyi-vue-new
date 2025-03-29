package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserSubscriptions;
import com.ruoyi.xkt.mapper.UserSubscriptionsMapper;
import com.ruoyi.xkt.service.IUserSubscriptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户关注u档口Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserSubscriptionsServiceImpl implements IUserSubscriptionsService {
    @Autowired
    private UserSubscriptionsMapper userSubscriptionsMapper;

    /**
     * 查询用户关注u档口
     *
     * @param userSubsId 用户关注u档口主键
     * @return 用户关注u档口
     */
    @Override
    public UserSubscriptions selectUserSubscriptionsByUserSubsId(Long userSubsId) {
        return userSubscriptionsMapper.selectUserSubscriptionsByUserSubsId(userSubsId);
    }

    /**
     * 查询用户关注u档口列表
     *
     * @param userSubscriptions 用户关注u档口
     * @return 用户关注u档口
     */
    @Override
    public List<UserSubscriptions> selectUserSubscriptionsList(UserSubscriptions userSubscriptions) {
        return userSubscriptionsMapper.selectUserSubscriptionsList(userSubscriptions);
    }

    /**
     * 新增用户关注u档口
     *
     * @param userSubscriptions 用户关注u档口
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserSubscriptions(UserSubscriptions userSubscriptions) {
        userSubscriptions.setCreateTime(DateUtils.getNowDate());
        return userSubscriptionsMapper.insertUserSubscriptions(userSubscriptions);
    }

    /**
     * 修改用户关注u档口
     *
     * @param userSubscriptions 用户关注u档口
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserSubscriptions(UserSubscriptions userSubscriptions) {
        userSubscriptions.setUpdateTime(DateUtils.getNowDate());
        return userSubscriptionsMapper.updateUserSubscriptions(userSubscriptions);
    }

    /**
     * 批量删除用户关注u档口
     *
     * @param userSubsIds 需要删除的用户关注u档口主键
     * @return 结果
     */
    @Override
    public int deleteUserSubscriptionsByUserSubsIds(Long[] userSubsIds) {
        return userSubscriptionsMapper.deleteUserSubscriptionsByUserSubsIds(userSubsIds);
    }

    /**
     * 删除用户关注u档口信息
     *
     * @param userSubsId 用户关注u档口主键
     * @return 结果
     */
    @Override
    public int deleteUserSubscriptionsByUserSubsId(Long userSubsId) {
        return userSubscriptionsMapper.deleteUserSubscriptionsByUserSubsId(userSubsId);
    }
}
