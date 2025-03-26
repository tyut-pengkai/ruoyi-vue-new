package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserQuickFunction;
import com.ruoyi.xkt.mapper.UserQuickFunctionMapper;
import com.ruoyi.xkt.service.IUserQuickFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户快捷功能Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserQuickFunctionServiceImpl implements IUserQuickFunctionService {
    @Autowired
    private UserQuickFunctionMapper userQuickFunctionMapper;

    /**
     * 查询用户快捷功能
     *
     * @param userQuickFuncId 用户快捷功能主键
     * @return 用户快捷功能
     */
    @Override
    public UserQuickFunction selectUserQuickFunctionByUserQuickFuncId(Long userQuickFuncId) {
        return userQuickFunctionMapper.selectUserQuickFunctionByUserQuickFuncId(userQuickFuncId);
    }

    /**
     * 查询用户快捷功能列表
     *
     * @param userQuickFunction 用户快捷功能
     * @return 用户快捷功能
     */
    @Override
    public List<UserQuickFunction> selectUserQuickFunctionList(UserQuickFunction userQuickFunction) {
        return userQuickFunctionMapper.selectUserQuickFunctionList(userQuickFunction);
    }

    /**
     * 新增用户快捷功能
     *
     * @param userQuickFunction 用户快捷功能
     * @return 结果
     */
    @Override
    public int insertUserQuickFunction(UserQuickFunction userQuickFunction) {
        userQuickFunction.setCreateTime(DateUtils.getNowDate());
        return userQuickFunctionMapper.insertUserQuickFunction(userQuickFunction);
    }

    /**
     * 修改用户快捷功能
     *
     * @param userQuickFunction 用户快捷功能
     * @return 结果
     */
    @Override
    public int updateUserQuickFunction(UserQuickFunction userQuickFunction) {
        userQuickFunction.setUpdateTime(DateUtils.getNowDate());
        return userQuickFunctionMapper.updateUserQuickFunction(userQuickFunction);
    }

    /**
     * 批量删除用户快捷功能
     *
     * @param userQuickFuncIds 需要删除的用户快捷功能主键
     * @return 结果
     */
    @Override
    public int deleteUserQuickFunctionByUserQuickFuncIds(Long[] userQuickFuncIds) {
        return userQuickFunctionMapper.deleteUserQuickFunctionByUserQuickFuncIds(userQuickFuncIds);
    }

    /**
     * 删除用户快捷功能信息
     *
     * @param userQuickFuncId 用户快捷功能主键
     * @return 结果
     */
    @Override
    public int deleteUserQuickFunctionByUserQuickFuncId(Long userQuickFuncId) {
        return userQuickFunctionMapper.deleteUserQuickFunctionByUserQuickFuncId(userQuickFuncId);
    }
}
