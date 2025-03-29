package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserBillingStatement;
import com.ruoyi.xkt.mapper.UserBillingStatementMapper;
import com.ruoyi.xkt.service.IUserBillingStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户对账明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserBillingStatementServiceImpl implements IUserBillingStatementService {
    @Autowired
    private UserBillingStatementMapper userBillingStatementMapper;

    /**
     * 查询用户对账明细
     *
     * @param userBillStatId 用户对账明细主键
     * @return 用户对账明细
     */
    @Override
    @Transactional(readOnly = true)
    public UserBillingStatement selectUserBillingStatementByUserBillStatId(Long userBillStatId) {
        return userBillingStatementMapper.selectUserBillingStatementByUserBillStatId(userBillStatId);
    }

    /**
     * 查询用户对账明细列表
     *
     * @param userBillingStatement 用户对账明细
     * @return 用户对账明细
     */
    @Override
    public List<UserBillingStatement> selectUserBillingStatementList(UserBillingStatement userBillingStatement) {
        return userBillingStatementMapper.selectUserBillingStatementList(userBillingStatement);
    }

    /**
     * 新增用户对账明细
     *
     * @param userBillingStatement 用户对账明细
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserBillingStatement(UserBillingStatement userBillingStatement) {
        userBillingStatement.setCreateTime(DateUtils.getNowDate());
        return userBillingStatementMapper.insertUserBillingStatement(userBillingStatement);
    }

    /**
     * 修改用户对账明细
     *
     * @param userBillingStatement 用户对账明细
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserBillingStatement(UserBillingStatement userBillingStatement) {
        userBillingStatement.setUpdateTime(DateUtils.getNowDate());
        return userBillingStatementMapper.updateUserBillingStatement(userBillingStatement);
    }

    /**
     * 批量删除用户对账明细
     *
     * @param userBillStatIds 需要删除的用户对账明细主键
     * @return 结果
     */
    @Override
    public int deleteUserBillingStatementByUserBillStatIds(Long[] userBillStatIds) {
        return userBillingStatementMapper.deleteUserBillingStatementByUserBillStatIds(userBillStatIds);
    }

    /**
     * 删除用户对账明细信息
     *
     * @param userBillStatId 用户对账明细主键
     * @return 结果
     */
    @Override
    public int deleteUserBillingStatementByUserBillStatId(Long userBillStatId) {
        return userBillingStatementMapper.deleteUserBillingStatementByUserBillStatId(userBillStatId);
    }
}
