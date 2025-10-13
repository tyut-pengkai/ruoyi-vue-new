package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysMenuMapper menuMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    protected Validator validator;

    @Override
    public UserInfo getUserById(Long userId) {
        UserInfo userInfo = userMapper.getUserInfoById(userId);
        fillMenus(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getUserByUsername(String userName) {
        UserInfo userInfo = userMapper.getUserInfoByUsername(userName);
        fillMenus(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getUserByPhoneNumber(String phoneNumber) {
        UserInfo userInfo = userMapper.getUserInfoByPhoneNumber(phoneNumber);
        fillMenus(userInfo);
        return userInfo;
    }

    @Override
    public SysUser getBaseUser(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public List<UserListItem> listUser(UserQuery query) {
        return userMapper.listUser(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createUser(UserInfoEdit userEdit) {
        checkRoles(userEdit.getRoleIds());
        checkSupperAdmin(userEdit.getUserId(), userEdit.getRoleIds());
        // 创建用户
        SysUser user = BeanUtil.toBean(userEdit, SysUser.class);
        if (StrUtil.isNotEmpty(userEdit.getPassword())) {
            user.setPassword(SecurityUtils.encryptPassword(userEdit.getPassword()));
        } else {
            String password = configService.selectConfigByKey("sys.user.initPassword");
            user.setPassword(SecurityUtils.encryptPassword(password));
        }
        insertUserBase(user);
        userEdit.setUserId(user.getUserId());
        // 创建用户与角色关联
        insertUserRole(userEdit);
        return userEdit.getUserId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long updateUser(UserInfoEdit userEdit) {
        checkRoles(userEdit.getRoleIds());
        checkSupperAdmin(userEdit.getUserId(), userEdit.getRoleIds());
        // 修改用户信息
        Assert.notNull(userEdit.getUserId());
        SysUser user = userMapper.selectById(userEdit.getUserId());
        Assert.notNull(user);
        user.setUserName(userEdit.getUserName());
        user.setNickName(userEdit.getNickName());
        user.setEmail(userEdit.getEmail());
        user.setPhonenumber(userEdit.getPhonenumber());
        user.setSex(userEdit.getSex());
        user.setAvatar(userEdit.getAvatar());
        if (StrUtil.isNotEmpty(userEdit.getPassword())) {
            user.setPassword(SecurityUtils.encryptPassword(userEdit.getPassword()));
        }
        user.setStatus(userEdit.getStatus());
        user.setRemark(userEdit.getRemark());
        updateUserBase(user);
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userEdit.getUserId());
        // 创建用户与角色关联
        insertUserRole(userEdit);
        return userEdit.getUserId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchUpdateUserStatus(List<Long> userIds, String status) {
        int c = 0;
        if (CollUtil.isEmpty(userIds)) {
            return c;
        }
        List<SysUser> users = userMapper.selectByIds(userIds);
        for (SysUser user : users) {
            checkUserAllowed(user);
            if (user.getStatus().equals(status)) {
                continue;
            }
            user.setStatus(status);
            updateUserBase(user, true);
            c++;
        }
        return c;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteUser(List<Long> userIds) {
        int c = 0;
        if (CollUtil.isEmpty(userIds)) {
            return c;
        }
        List<SysUser> users = userMapper.selectByIds(userIds);
        for (SysUser user : users) {
            checkUserAllowed(user);
            if (Constants.DELETED.equals(user.getDelFlag())) {
                continue;
            }
            user.setDelFlag(Constants.DELETED);
            updateUserBase(user, true);
            // 删除用户与角色关联
            userRoleMapper.deleteUserRoleByUserId(user.getUserId());
            c++;
        }
        return c;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String resetPassword(Long userId, String password) {
        if (userId == null) {
            return null;
        }
        if (StrUtil.isEmpty(password)) {
            password = configService.selectConfigByKey("sys.user.initPassword");
        }
        SysUser user = userMapper.selectById(userId);
        user.setPassword(SecurityUtils.encryptPassword(password));
        updateUserBase(user, true);
        return user.getUserName();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        SysUser user = null;
        if (userId != null) {
            user = userMapper.selectById(userId);
        }
        if (user == null) {
            return;
        }
        user.setLoginIp(loginIp);
        user.setLoginDate(loginDate);
        userMapper.updateById(user);
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateUserProfile(SysUser user) {
        SysUser origin = userMapper.selectById(user.getUserId());
        user.setVersion(origin.getVersion());
        updateUserBase(user);
        return 1;
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        if (StrUtil.isNotEmpty(userName)) {
            SysUser user = userMapper.selectOne(Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getUserName, userName)
                    .eq(SysUser::getDelFlag, Constants.UNDELETED));
            if (user != null) {
                user.setAvatar(avatar);
                updateUserBase(user, true);
                return true;
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserPhoneNumber(Long userId, String phoneNumber) {
        SysUser user = userMapper.selectById(userId);
        Assert.notNull(user);
        if (StrUtil.equals(user.getUserName(), user.getPhonenumber())) {
            user.setUserName(phoneNumber);
        }
//        if (StrUtil.equals(user.getNickName(), user.getPhonenumber())) {
//            user.setNickName(phoneNumber);
//        }
        user.setPhonenumber(phoneNumber);
        updateUserBase(user);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    private void insertUserRole(UserInfoEdit user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    private void insertUserRole(Long userId, List<Long> roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>(roleIds.size());
            for (Long roleId : roleIds) {
                Long storeId;
                if (ESystemRole.SUPPLIER.getId().equals(roleId)) {
                    // 供应商管理员
                    storeId = userMapper.getManageStoreId(userId);
                } else {
                    // 其他
                    SysRole role = roleMapper.selectById(roleId);
                    storeId = Optional.ofNullable(role).map(SysRole::getStoreId).orElse(null);
                }
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                ur.setStoreId(storeId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = null;
                if (StrUtil.isNotEmpty(user.getUserName())) {
                    u = userMapper.selectOne(Wrappers.lambdaQuery(SysUser.class)
                            .eq(SysUser::getUserName, user.getUserName())
                            .eq(SysUser::getDelFlag, Constants.UNDELETED));
                }
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    insertUserBase(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
//                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    user.setVersion(u.getVersion());
                    updateUserBase(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refreshRelStore(Long userId, Long roleId) {
        Assert.notNull(userId);
        Assert.notNull(roleId);
        Long storeId;
        if (ESystemRole.SUPPLIER.getId().equals(roleId)) {
            storeId = userMapper.getManageStoreId(userId);
        } else {
            SysRole role = roleMapper.selectById(roleId);
            storeId = Optional.ofNullable(role).map(SysRole::getStoreId).orElse(null);
        }
        userRoleMapper.deleteUserRoleInfos(roleId, new Long[]{userId});
        SysUserRole ur = new SysUserRole();
        ur.setUserId(userId);
        ur.setRoleId(roleId);
        ur.setStoreId(storeId);
        userRoleMapper.batchUserRole(Collections.singletonList(ur));
        // 检查用户的角色是否合规
        List<Long> roleIds = userRoleMapper.listRelRoleId(userId);
        checkRoles(roleIds);
    }

    /**
     * 新增用户
     *
     * @param user
     */
    private void insertUserBase(SysUser user) {
        checkUserBase(user);
        Assert.isNull(user.getUserId());
        if (StrUtil.isBlank(user.getStatus())) {
            user.setStatus(Constants.SYS_NORMAL_STATUS);
        }
        user.setDelFlag(Constants.UNDELETED);
        user.setVersion(0L);
        String currentUser = SecurityUtils.getUsernameSafe();
        user.setCreateBy(currentUser);
        user.setUpdateBy(currentUser);
        userMapper.insert(user);
    }

    /**
     * 修改用户
     *
     * @param user
     */
    private void updateUserBase(SysUser user) {
        updateUserBase(user, false);
    }

    /**
     * 修改用户
     *
     * @param user
     * @param ignoreBaseCheck
     */
    private void updateUserBase(SysUser user, boolean ignoreBaseCheck) {
        if (!ignoreBaseCheck) {
            checkUserBase(user);
        }
        Assert.notNull(user.getUserId());
        String currentUser = SecurityUtils.getUsernameSafe();
        user.setUpdateBy(currentUser);
        int c = userMapper.updateById(user);
        if (c == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
    }

    /**
     * 用户基础信息校验
     *
     * @param user
     */
    private void checkUserBase(SysUser user) {
        Assert.notNull(user);
        Assert.notEmpty(user.getUserName(), "用户名称不能为空");
        Assert.notEmpty(user.getNickName(), "用户昵称不能为空");
        if (StrUtil.isNotEmpty(user.getPhonenumber())) {
            Assert.isTrue(checkPhoneUnique(user), "手机号已被注册");
        }
        Assert.isTrue(checkUserNameUnique(user), "用户名称已被注册");
        if (StrUtil.isNotEmpty(user.getEmail())) {
            Assert.isTrue(checkEmailUnique(user), "邮箱已被注册");
        }
        if (StrUtil.isNotEmpty(user.getPhonenumber())) {
            Assert.isTrue(checkPhoneUnique(user), "手机号已被注册");
        }
    }

    private void checkRoles(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds) || roleIds.size() == 1) {
            return;
        }
        int sellerCount = 0;
        int otherCount = 0;
        int subCount = 0;
        for (Long roleId : roleIds) {
            if (ESystemRole.SELLER.getId().equals(roleId)) {
                sellerCount++;
            } else if (ESystemRole.isDefaultRole(roleId)) {
                otherCount++;
            } else {
                SysRole r = roleMapper.selectById(roleId);
                if (r.getStoreId() != null) {
                    subCount++;
                } else {
                    otherCount++;
                }
            }
        }
        if (subCount > 0 && otherCount > 0) {
            throw new ServiceException("用户不能同时有子角色与\"电商卖家\"以外的角色");
        }
        if ((sellerCount + otherCount) > 1) {
            throw new ServiceException("用户只能有一个系统角色");
        }
    }

    private void checkSupperAdmin(Long userId, List<Long> roleIds) {
        if (Long.valueOf(1).equals(userId)) {
            Assert.isTrue(CollUtil.emptyIfNull(roleIds).contains(ESystemRole.SUPER_ADMIN.getId()),
                    "不能移除超级管理员");
        }
        if (CollUtil.emptyIfNull(roleIds).contains(ESystemRole.SUPER_ADMIN.getId())) {
            Assert.isTrue(Long.valueOf(1).equals(userId), "不能新增超级管理员");
        }
    }

    /**
     * 菜单
     *
     * @param user
     */
    private void fillMenus(UserInfo user) {
        if (user == null) {
            return;
        }
        List<RoleInfo> roles = CollUtil.emptyIfNull(user.getRoles());
        Set<SysMenu> menus = new HashSet<>();
        for (RoleInfo role : roles) {
            if (ESystemRole.SUPER_ADMIN.getId().equals(role.getRoleId())) {
                //超管有所有菜单
                Set<SysMenu> ms = menuMapper.selectList(Wrappers.emptyWrapper()).stream()
                        .filter(o -> Constants.UNDELETED.equals(o.getDelFlag())
                                && Constants.SYS_NORMAL_STATUS.equals(o.getStatus()))
                        .collect(Collectors.toSet());
                role.setMenus(BeanUtil.copyToList(ms, MenuInfo.class));
            }
            menus.addAll(BeanUtil.copyToList(CollUtil.emptyIfNull(role.getMenus()), SysMenu.class));
        }
        user.setMenus(menus);
    }
}
