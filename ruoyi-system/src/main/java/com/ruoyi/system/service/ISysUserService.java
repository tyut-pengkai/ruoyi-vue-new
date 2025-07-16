package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.UserInfo;
import com.ruoyi.common.core.domain.model.UserInfoEdit;
import com.ruoyi.common.core.domain.model.UserListItem;
import com.ruoyi.common.core.domain.model.UserQuery;

import java.util.Date;
import java.util.List;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ISysUserService {
    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfo getUserById(Long userId);

    /**
     * 获取用户信息
     *
     * @param userName
     * @return
     */
    UserInfo getUserByUsername(String userName);

    /**
     * 获取用户信息
     *
     * @param phoneNumber
     * @return
     */
    UserInfo getUserByPhoneNumber(String phoneNumber);

    /**
     * 获取用户基本信息
     *
     * @param userId
     * @return
     */
    SysUser getBaseUser(Long userId);

    /**
     * 用户列表
     *
     * @param query
     * @return
     */
    List<UserListItem> listUser(UserQuery query);

    /**
     * 创建用户
     *
     * @param userEdit
     * @return
     */
    Long createUser(UserInfoEdit userEdit);

    /**
     * 修改用户
     *
     * @param userEdit
     * @return
     */
    Long updateUser(UserInfoEdit userEdit);

    /**
     * 批量修改用户状态
     *
     * @param userIds
     * @param status
     * @return
     */
    int batchUpdateUserStatus(List<Long> userIds, String status);

    /**
     * 批量删除用户
     *
     * @param userIds
     * @return
     */
    int batchDeleteUser(List<Long> userIds);

    /**
     * 重置密码
     *
     * @param userId
     * @param password
     */
    void resetPassword(Long userId, String password);

    /**
     * 更新登录信息
     *
     * @param userId
     * @param loginIp
     * @param loginDate
     */
    void updateLoginInfo(Long userId, String loginIp, Date loginDate);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserProfile(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * 修改用户手机号
     *
     * @param userId
     * @param phoneNumber
     */
    void updateUserPhoneNumber(Long userId, String phoneNumber);

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 更新关联档口ID
     *
     * @param userId
     * @param roleId
     */
    void refreshRelStore(Long userId, Long roleId);
}
