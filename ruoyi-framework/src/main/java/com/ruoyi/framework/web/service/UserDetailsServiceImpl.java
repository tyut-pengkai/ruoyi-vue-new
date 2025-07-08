package com.ruoyi.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import java.util.Set;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        if (user == null) {
            throw new ServiceException("用户信息为空，无法创建登录用户");
        }
        
        if (user.getUserId() == null) {
            throw new ServiceException("用户ID为空，无法创建登录用户");
        }
        
        if (user.getUserName() == null) {
            throw new ServiceException("用户名为空，无法创建登录用户");
        }
        
        Long deptId = user.getDeptId();
        if (deptId == null) {
            deptId = 103L;
            log.warn("用户 {} 的部门ID为空，使用默认部门ID: {}", user.getUserName(), deptId);
        }
        
        if (permissionService == null) {
            log.error("权限服务未注入，无法创建登录用户");
            throw new ServiceException("系统配置错误：权限服务未初始化");
        }
        
        Set<String> permissions;
        try {
            permissions = permissionService.getMenuPermission(user);
            if (permissions == null) {
                log.warn("用户 {} 的权限为空，使用空权限集合", user.getUserName());
                permissions = new java.util.HashSet<>();
            }
        } catch (Exception e) {
            log.error("获取用户 {} 权限失败: {}", user.getUserName(), e.getMessage());
            permissions = new java.util.HashSet<>();
        }
        
        return new LoginUser(user.getUserId(), deptId, user, permissions);
    }
}
