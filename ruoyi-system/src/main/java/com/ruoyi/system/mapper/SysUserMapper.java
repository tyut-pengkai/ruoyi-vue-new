package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.UserInfo;
import com.ruoyi.common.core.domain.model.UserListItem;
import com.ruoyi.common.core.domain.model.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfo getUserInfoById(@Param("userId") Long userId);

    /**
     * 获取用户信息
     *
     * @param userName
     * @return
     */
    UserInfo getUserInfoByUsername(@Param("userName") String userName);

    /**
     * 根据手机号获取用户
     *
     * @param phoneNumber
     * @return
     */
    UserInfo getUserInfoByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /**
     * 用户列表
     *
     * @param query
     * @return
     */
    List<UserListItem> listUser(UserQuery query);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);

    /**
     * 获取用户管理的档口ID
     *
     * @param userId
     * @return
     */
    Long getManageStoreId(@Param("userId") Long userId);
}
