package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.UserAuthentication;

import java.util.List;

/**
 * 用户代发认证Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserAuthenticationMapper {
    /**
     * 查询用户代发认证
     *
     * @param userAuthId 用户代发认证主键
     * @return 用户代发认证
     */
    public UserAuthentication selectUserAuthenticationByUserAuthId(Long userAuthId);

    /**
     * 查询用户代发认证列表
     *
     * @param userAuthentication 用户代发认证
     * @return 用户代发认证集合
     */
    public List<UserAuthentication> selectUserAuthenticationList(UserAuthentication userAuthentication);

    /**
     * 新增用户代发认证
     *
     * @param userAuthentication 用户代发认证
     * @return 结果
     */
    public int insertUserAuthentication(UserAuthentication userAuthentication);

    /**
     * 修改用户代发认证
     *
     * @param userAuthentication 用户代发认证
     * @return 结果
     */
    public int updateUserAuthentication(UserAuthentication userAuthentication);

    /**
     * 删除用户代发认证
     *
     * @param userAuthId 用户代发认证主键
     * @return 结果
     */
    public int deleteUserAuthenticationByUserAuthId(Long userAuthId);

    /**
     * 批量删除用户代发认证
     *
     * @param userAuthIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAuthenticationByUserAuthIds(Long[] userAuthIds);
}
