package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.UserQuickFunction;

import java.util.List;

/**
 * 用户快捷功能Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserQuickFunctionService {
    /**
     * 查询用户快捷功能
     *
     * @param userQuickFuncId 用户快捷功能主键
     * @return 用户快捷功能
     */
    public UserQuickFunction selectUserQuickFunctionByUserQuickFuncId(Long userQuickFuncId);

    /**
     * 查询用户快捷功能列表
     *
     * @param userQuickFunction 用户快捷功能
     * @return 用户快捷功能集合
     */
    public List<UserQuickFunction> selectUserQuickFunctionList(UserQuickFunction userQuickFunction);

    /**
     * 新增用户快捷功能
     *
     * @param userQuickFunction 用户快捷功能
     * @return 结果
     */
    public int insertUserQuickFunction(UserQuickFunction userQuickFunction);

    /**
     * 修改用户快捷功能
     *
     * @param userQuickFunction 用户快捷功能
     * @return 结果
     */
    public int updateUserQuickFunction(UserQuickFunction userQuickFunction);

    /**
     * 批量删除用户快捷功能
     *
     * @param userQuickFuncIds 需要删除的用户快捷功能主键集合
     * @return 结果
     */
    public int deleteUserQuickFunctionByUserQuickFuncIds(Long[] userQuickFuncIds);

    /**
     * 删除用户快捷功能信息
     *
     * @param userQuickFuncId 用户快捷功能主键
     * @return 结果
     */
    public int deleteUserQuickFunctionByUserQuickFuncId(Long userQuickFuncId);
}
