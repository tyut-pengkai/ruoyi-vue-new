package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserBillingStatement;

import java.util.List;

/**
 * 用户对账明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserBillingStatementMapper extends BaseMapper<UserBillingStatement> {
    /**
     * 查询用户对账明细
     *
     * @param id 用户对账明细主键
     * @return 用户对账明细
     */
    public UserBillingStatement selectUserBillingStatementByUserBillStatId(Long id);

    /**
     * 查询用户对账明细列表
     *
     * @param userBillingStatement 用户对账明细
     * @return 用户对账明细集合
     */
    public List<UserBillingStatement> selectUserBillingStatementList(UserBillingStatement userBillingStatement);

    /**
     * 新增用户对账明细
     *
     * @param userBillingStatement 用户对账明细
     * @return 结果
     */
    public int insertUserBillingStatement(UserBillingStatement userBillingStatement);

    /**
     * 修改用户对账明细
     *
     * @param userBillingStatement 用户对账明细
     * @return 结果
     */
    public int updateUserBillingStatement(UserBillingStatement userBillingStatement);

    /**
     * 删除用户对账明细
     *
     * @param id 用户对账明细主键
     * @return 结果
     */
    public int deleteUserBillingStatementByUserBillStatId(Long id);

    /**
     * 批量删除用户对账明细
     *
     * @param userBillStatIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBillingStatementByUserBillStatIds(Long[] userBillStatIds);
}
