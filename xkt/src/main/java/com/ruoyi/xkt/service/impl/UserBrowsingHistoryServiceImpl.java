package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserBrowsingHistory;
import com.ruoyi.xkt.mapper.UserBrowsingHistoryMapper;
import com.ruoyi.xkt.service.IUserBrowsingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户浏览历史Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserBrowsingHistoryServiceImpl implements IUserBrowsingHistoryService {
    @Autowired
    private UserBrowsingHistoryMapper userBrowsingHistoryMapper;

    /**
     * 查询用户浏览历史
     *
     * @param userBrowHisId 用户浏览历史主键
     * @return 用户浏览历史
     */
    @Override
    public UserBrowsingHistory selectUserBrowsingHistoryByUserBrowHisId(Long userBrowHisId) {
        return userBrowsingHistoryMapper.selectUserBrowsingHistoryByUserBrowHisId(userBrowHisId);
    }

    /**
     * 查询用户浏览历史列表
     *
     * @param userBrowsingHistory 用户浏览历史
     * @return 用户浏览历史
     */
    @Override
    public List<UserBrowsingHistory> selectUserBrowsingHistoryList(UserBrowsingHistory userBrowsingHistory) {
        return userBrowsingHistoryMapper.selectUserBrowsingHistoryList(userBrowsingHistory);
    }

    /**
     * 新增用户浏览历史
     *
     * @param userBrowsingHistory 用户浏览历史
     * @return 结果
     */
    @Override
    public int insertUserBrowsingHistory(UserBrowsingHistory userBrowsingHistory) {
        userBrowsingHistory.setCreateTime(DateUtils.getNowDate());
        return userBrowsingHistoryMapper.insertUserBrowsingHistory(userBrowsingHistory);
    }

    /**
     * 修改用户浏览历史
     *
     * @param userBrowsingHistory 用户浏览历史
     * @return 结果
     */
    @Override
    public int updateUserBrowsingHistory(UserBrowsingHistory userBrowsingHistory) {
        userBrowsingHistory.setUpdateTime(DateUtils.getNowDate());
        return userBrowsingHistoryMapper.updateUserBrowsingHistory(userBrowsingHistory);
    }

    /**
     * 批量删除用户浏览历史
     *
     * @param userBrowHisIds 需要删除的用户浏览历史主键
     * @return 结果
     */
    @Override
    public int deleteUserBrowsingHistoryByUserBrowHisIds(Long[] userBrowHisIds) {
        return userBrowsingHistoryMapper.deleteUserBrowsingHistoryByUserBrowHisIds(userBrowHisIds);
    }

    /**
     * 删除用户浏览历史信息
     *
     * @param userBrowHisId 用户浏览历史主键
     * @return 结果
     */
    @Override
    public int deleteUserBrowsingHistoryByUserBrowHisId(Long userBrowHisId) {
        return userBrowsingHistoryMapper.deleteUserBrowsingHistoryByUserBrowHisId(userBrowHisId);
    }
}
