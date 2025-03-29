package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserAuthentication;
import com.ruoyi.xkt.mapper.UserAuthenticationMapper;
import com.ruoyi.xkt.service.IUserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户代发认证Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserAuthenticationServiceImpl implements IUserAuthenticationService {
    @Autowired
    private UserAuthenticationMapper userAuthenticationMapper;

    /**
     * 查询用户代发认证
     *
     * @param userAuthId 用户代发认证主键
     * @return 用户代发认证
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthentication selectUserAuthenticationByUserAuthId(Long userAuthId) {
        return userAuthenticationMapper.selectUserAuthenticationByUserAuthId(userAuthId);
    }

    /**
     * 查询用户代发认证列表
     *
     * @param userAuthentication 用户代发认证
     * @return 用户代发认证
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAuthentication> selectUserAuthenticationList(UserAuthentication userAuthentication) {
        return userAuthenticationMapper.selectUserAuthenticationList(userAuthentication);
    }

    /**
     * 新增用户代发认证
     *
     * @param userAuthentication 用户代发认证
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserAuthentication(UserAuthentication userAuthentication) {
        userAuthentication.setCreateTime(DateUtils.getNowDate());
        return userAuthenticationMapper.insertUserAuthentication(userAuthentication);
    }

    /**
     * 修改用户代发认证
     *
     * @param userAuthentication 用户代发认证
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserAuthentication(UserAuthentication userAuthentication) {
        userAuthentication.setUpdateTime(DateUtils.getNowDate());
        return userAuthenticationMapper.updateUserAuthentication(userAuthentication);
    }

    /**
     * 批量删除用户代发认证
     *
     * @param userAuthIds 需要删除的用户代发认证主键
     * @return 结果
     */
    @Override
    public int deleteUserAuthenticationByUserAuthIds(Long[] userAuthIds) {
        return userAuthenticationMapper.deleteUserAuthenticationByUserAuthIds(userAuthIds);
    }

    /**
     * 删除用户代发认证信息
     *
     * @param userAuthId 用户代发认证主键
     * @return 结果
     */
    @Override
    public int deleteUserAuthenticationByUserAuthId(Long userAuthId) {
        return userAuthenticationMapper.deleteUserAuthenticationByUserAuthId(userAuthId);
    }
}
