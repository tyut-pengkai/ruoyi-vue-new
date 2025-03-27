package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserNotice;

import java.util.List;

/**
 * 用户所有通知Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserNoticeMapper extends BaseMapper<UserNotice> {
    /**
     * 查询用户所有通知
     *
     * @param userNoticeId 用户所有通知主键
     * @return 用户所有通知
     */
    public UserNotice selectUserNoticeByUserNoticeId(Long userNoticeId);

    /**
     * 查询用户所有通知列表
     *
     * @param userNotice 用户所有通知
     * @return 用户所有通知集合
     */
    public List<UserNotice> selectUserNoticeList(UserNotice userNotice);

    /**
     * 新增用户所有通知
     *
     * @param userNotice 用户所有通知
     * @return 结果
     */
    public int insertUserNotice(UserNotice userNotice);

    /**
     * 修改用户所有通知
     *
     * @param userNotice 用户所有通知
     * @return 结果
     */
    public int updateUserNotice(UserNotice userNotice);

    /**
     * 删除用户所有通知
     *
     * @param userNoticeId 用户所有通知主键
     * @return 结果
     */
    public int deleteUserNoticeByUserNoticeId(Long userNoticeId);

    /**
     * 批量删除用户所有通知
     *
     * @param userNoticeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserNoticeByUserNoticeIds(Long[] userNoticeIds);
}
