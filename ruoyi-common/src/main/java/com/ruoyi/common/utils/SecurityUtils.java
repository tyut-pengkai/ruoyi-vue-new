package com.ruoyi.common.utils;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.UserExt;
import com.ruoyi.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Optional;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
@Slf4j
public class SecurityUtils {

    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 当前档口ID
     *
     * @return
     */
    public static Long getStoreId() {
        try {
            return getLoginUser().getCurrentStoreId();
        } catch (Exception e) {
            throw new ServiceException("获取档口ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 是否有指定档口的管理权限
     *
     * @param storeId
     * @return
     */
    public static boolean isStoreManager(Long storeId) {
        if (storeId == null) {
            return false;
        }
        try {
            Authentication auth = getAuthentication();
            if (auth != null) {
                LoginUser user = (LoginUser) auth.getPrincipal();
                return user.getUser().getManagedStoreIds().contains(storeId);
            }
        } catch (Exception e) {
            log.error("判断档口管理权限异常", e);
        }
        return false;
    }

    /**
     * 是否有指定档口的管理权限
     *
     * @param storeId
     * @return
     */
    public static boolean isStoreSub(Long storeId) {
        if (storeId == null) {
            return false;
        }
        try {
            Authentication auth = getAuthentication();
            if (auth != null) {
                LoginUser user = (LoginUser) auth.getPrincipal();
                return user.getUser().getSubStoreIds().contains(storeId);
            }
        } catch (Exception e) {
            log.error("判断档口管理权限异常", e);
        }
        return false;
    }

    /**
     * 是否有指定档口的管理/子账号权限
     *
     * @param storeId
     * @return
     */
    public static boolean isStoreManagerOrSub(Long storeId) {
        if (storeId == null) {
            return false;
        }
        try {
            Authentication auth = getAuthentication();
            if (auth != null) {
                LoginUser user = (LoginUser) auth.getPrincipal();
                return user.getUser().getManagedStoreIds().contains(storeId)
                        || user.getUser().getSubStoreIds().contains(storeId);
            }
        } catch (Exception e) {
            log.error("判断档口管理权限异常", e);
        }
        return false;
    }

    /**
     * 判断是否有指定角色中的任意一个
     *
     * @param roles
     * @return
     */
    public static boolean hasAnyRole(ESystemRole... roles) {
        if (roles == null || roles.length == 0) {
            return true;
        }
        try {
            Authentication auth = getAuthentication();
            if (auth != null) {
                LoginUser user = (LoginUser) auth.getPrincipal();
                for (ESystemRole role : roles) {
                    if (user.getUser().getRoleIds().contains(role.getId())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("判断档口角色权限异常", e);
        }
        return false;
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户账户
     *
     * @return
     */
    public static String getUsernameSafe() {
        String username = null;
        try {
            Authentication auth = getAuthentication();
            if (auth != null) {
                LoginUser user = (LoginUser) auth.getPrincipal();
                username = Optional.ofNullable(user).map(LoginUser::getUsername).orElse(null);
            }
        } catch (Exception e) {
            log.error("获取用户账户异常", e);
        }
        return username;
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为超级管理员
     *
     * @return 结果
     */
    public static boolean isSuperAdmin() {
        return getLoginUser().getUser().hasSuperAdminRole();
    }

    /**
     * 是否为管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        UserExt user = getLoginUser().getUser();
        return user.hasSuperAdminRole() || user.hasGeneralAdminRole();
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(String permission) {
        return hasPermi(getLoginUser().getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(Collection<String> authorities, String permission) {
//        return authorities.stream().filter(StringUtils::hasText)
//                .anyMatch(x -> Constants.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
        return CollUtil.contains(authorities, permission);
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(String role) {
        return hasRole(getLoginUser().getRoleKeys(), role);
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public static boolean hasRole(Collection<String> roles, String role) {
//        return roles.stream().filter(StringUtils::hasText)
//                .anyMatch(x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
        return CollUtil.contains(roles, role);
    }

}
