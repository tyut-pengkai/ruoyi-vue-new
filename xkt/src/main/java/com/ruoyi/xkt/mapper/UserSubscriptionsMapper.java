package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserSubscriptions;

import java.util.List;

/**
 * 用户关注u档口Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserSubscriptionsMapper extends BaseMapper<UserSubscriptions> {
    /**
     * 查询用户关注u档口
     *
     * @param id 用户关注u档口主键
     * @return 用户关注u档口
     */
    public UserSubscriptions selectUserSubscriptionsByUserSubsId(Long id);

    /**
     * 查询用户关注u档口列表
     *
     * @param userSubscriptions 用户关注u档口
     * @return 用户关注u档口集合
     */
    public List<UserSubscriptions> selectUserSubscriptionsList(UserSubscriptions userSubscriptions);

    /**
     * 新增用户关注u档口
     *
     * @param userSubscriptions 用户关注u档口
     * @return 结果
     */
    public int insertUserSubscriptions(UserSubscriptions userSubscriptions);

    /**
     * 修改用户关注u档口
     *
     * @param userSubscriptions 用户关注u档口
     * @return 结果
     */
    public int updateUserSubscriptions(UserSubscriptions userSubscriptions);

    /**
     * 删除用户关注u档口
     *
     * @param id 用户关注u档口主键
     * @return 结果
     */
    public int deleteUserSubscriptionsByUserSubsId(Long id);

    /**
     * 批量删除用户关注u档口
     *
     * @param userSubsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserSubscriptionsByUserSubsIds(Long[] userSubsIds);
}
