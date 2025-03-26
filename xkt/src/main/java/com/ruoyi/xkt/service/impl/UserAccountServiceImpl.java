package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserAccount;
import com.ruoyi.xkt.mapper.UserAccountMapper;
import com.ruoyi.xkt.service.IUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户账户（支付宝、微信等）Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserAccountServiceImpl implements IUserAccountService {
    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 查询用户账户（支付宝、微信等）
     *
     * @param userAccId 用户账户（支付宝、微信等）主键
     * @return 用户账户（支付宝、微信等）
     */
    @Override
    public UserAccount selectUserAccountByUserAccId(Long userAccId) {
        return userAccountMapper.selectUserAccountByUserAccId(userAccId);
    }

    /**
     * 查询用户账户（支付宝、微信等）列表
     *
     * @param userAccount 用户账户（支付宝、微信等）
     * @return 用户账户（支付宝、微信等）
     */
    @Override
    public List<UserAccount> selectUserAccountList(UserAccount userAccount) {
        return userAccountMapper.selectUserAccountList(userAccount);
    }

    /**
     * 新增用户账户（支付宝、微信等）
     *
     * @param userAccount 用户账户（支付宝、微信等）
     * @return 结果
     */
    @Override
    public int insertUserAccount(UserAccount userAccount) {
        userAccount.setCreateTime(DateUtils.getNowDate());
        return userAccountMapper.insertUserAccount(userAccount);
    }

    /**
     * 修改用户账户（支付宝、微信等）
     *
     * @param userAccount 用户账户（支付宝、微信等）
     * @return 结果
     */
    @Override
    public int updateUserAccount(UserAccount userAccount) {
        userAccount.setUpdateTime(DateUtils.getNowDate());
        return userAccountMapper.updateUserAccount(userAccount);
    }

    /**
     * 批量删除用户账户（支付宝、微信等）
     *
     * @param userAccIds 需要删除的用户账户（支付宝、微信等）主键
     * @return 结果
     */
    @Override
    public int deleteUserAccountByUserAccIds(Long[] userAccIds) {
        return userAccountMapper.deleteUserAccountByUserAccIds(userAccIds);
    }

    /**
     * 删除用户账户（支付宝、微信等）信息
     *
     * @param userAccId 用户账户（支付宝、微信等）主键
     * @return 结果
     */
    @Override
    public int deleteUserAccountByUserAccId(Long userAccId) {
        return userAccountMapper.deleteUserAccountByUserAccId(userAccId);
    }
}
