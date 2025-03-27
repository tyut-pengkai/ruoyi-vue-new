package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserAccount;

import java.util.List;

/**
 * 用户账户（支付宝、微信等）Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {
    /**
     * 查询用户账户（支付宝、微信等）
     *
     * @param userAccId 用户账户（支付宝、微信等）主键
     * @return 用户账户（支付宝、微信等）
     */
    public UserAccount selectUserAccountByUserAccId(Long userAccId);

    /**
     * 查询用户账户（支付宝、微信等）列表
     *
     * @param userAccount 用户账户（支付宝、微信等）
     * @return 用户账户（支付宝、微信等）集合
     */
    public List<UserAccount> selectUserAccountList(UserAccount userAccount);

    /**
     * 新增用户账户（支付宝、微信等）
     *
     * @param userAccount 用户账户（支付宝、微信等）
     * @return 结果
     */
    public int insertUserAccount(UserAccount userAccount);

    /**
     * 修改用户账户（支付宝、微信等）
     *
     * @param userAccount 用户账户（支付宝、微信等）
     * @return 结果
     */
    public int updateUserAccount(UserAccount userAccount);

    /**
     * 删除用户账户（支付宝、微信等）
     *
     * @param userAccId 用户账户（支付宝、微信等）主键
     * @return 结果
     */
    public int deleteUserAccountByUserAccId(Long userAccId);

    /**
     * 批量删除用户账户（支付宝、微信等）
     *
     * @param userAccIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAccountByUserAccIds(Long[] userAccIds);
}
