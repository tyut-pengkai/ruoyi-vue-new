package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.UserBrowsingHistory;

import java.util.List;

/**
 * 用户浏览历史Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserBrowsingHistoryMapper {
    /**
     * 查询用户浏览历史
     *
     * @param userBrowHisId 用户浏览历史主键
     * @return 用户浏览历史
     */
    public UserBrowsingHistory selectUserBrowsingHistoryByUserBrowHisId(Long userBrowHisId);

    /**
     * 查询用户浏览历史列表
     *
     * @param userBrowsingHistory 用户浏览历史
     * @return 用户浏览历史集合
     */
    public List<UserBrowsingHistory> selectUserBrowsingHistoryList(UserBrowsingHistory userBrowsingHistory);

    /**
     * 新增用户浏览历史
     *
     * @param userBrowsingHistory 用户浏览历史
     * @return 结果
     */
    public int insertUserBrowsingHistory(UserBrowsingHistory userBrowsingHistory);

    /**
     * 修改用户浏览历史
     *
     * @param userBrowsingHistory 用户浏览历史
     * @return 结果
     */
    public int updateUserBrowsingHistory(UserBrowsingHistory userBrowsingHistory);

    /**
     * 删除用户浏览历史
     *
     * @param userBrowHisId 用户浏览历史主键
     * @return 结果
     */
    public int deleteUserBrowsingHistoryByUserBrowHisId(Long userBrowHisId);

    /**
     * 批量删除用户浏览历史
     *
     * @param userBrowHisIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBrowsingHistoryByUserBrowHisIds(Long[] userBrowHisIds);
}
