package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BalanceChangeType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.BalanceChangeVo;
import com.ruoyi.system.domain.vo.UserBalanceChangeVo;
import com.ruoyi.system.domain.vo.UserBalanceTransferVo;
import com.ruoyi.system.domain.vo.UserBalanceWithdrawVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
    private SysPostMapper postMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysUserPostMapper userPostMapper;
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    protected Validator validator;
    @Resource
    private ISysBalanceLogService sysBalanceLogService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        if(userId == null) {
            return null;
        }
        SysUser sysUser = (SysUser) SysCache.get(CacheConstants.SYS_USER_KEY + userId);
        if (sysUser == null) {
            sysUser = userMapper.selectUserById(userId);
            SysCache.set(CacheConstants.SYS_USER_KEY + userId, sysUser, 86400000);
        }
        return sysUser;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
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
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        user.setCreateTime(DateUtils.getNowDate());
        user.setCreateBy(SecurityUtils.getUsernameNoException());
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        user.setCreateTime(DateUtils.getNowDate());
        user.setCreateBy(SecurityUtils.getUsernameNoException());
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUsernameNoException());
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return updateCache(userMapper.updateUser(user), userId);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUsernameNoException());
        return updateCache(userMapper.updateUser(user), user.getUserId());
    }

    private int updateCache(int i, Long userId) {
        if(i > 0) {
            SysUser sysUser = userMapper.selectUserById(userId);
            SysCache.set(CacheConstants.SYS_USER_KEY + userId, sysUser, 86400000);
        }
        return i;
    }

    private int updateCache(int i, String userName) {
        if(i > 0) {
            SysUser sysUser = userMapper.selectUserByUserName(userName);
            SysCache.set(CacheConstants.SYS_USER_KEY + sysUser.getUserId(), sysUser, 86400000);
        }
        return i;
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUsernameNoException());
        return updateCache(userMapper.updateUser(user), user.getUserId());
    }

    /**
     * 修改用户余额信息
     *
     * @param change 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserBalance(BalanceChangeVo change) {
        // 使用Redis锁来避免多线程并发更新
        String lockKey = Constants.LOCK_USER_CHANGE + change.getUserId();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, "lock", 10, TimeUnit.SECONDS);
        if (lock != null && lock) {
            try {
                // 获取锁后再次检查账户余额，防止其他线程已修改
                SysUser userNow = selectUserById(change.getUserId());
                if (userNow != null) {
                    // 检查余额是否足够
                    if (isNegative(change.getAvailablePayBalance())) {
                        if (isLessThan(userNow.getAvailablePayBalance(), change.getAvailablePayBalance().abs())) {
                            throw new ServiceException("可用余额不足");
                        }
                    }
                    if (isNegative(change.getAvailableFreeBalance())) {
                        if (isLessThan(userNow.getAvailableFreeBalance(), change.getAvailableFreeBalance().abs())) {
                            throw new ServiceException("冻结余额不足");
                        }
                    }
                    if (isNegative(change.getFreezePayBalance())) {
                        if (isLessThan(userNow.getFreezePayBalance(), change.getFreezePayBalance().abs())) {
                            throw new ServiceException("可用赠送余额不足");
                        }
                    }
                    if (isNegative(change.getFreezeFreeBalance())) {
                        if (isLessThan(userNow.getFreezeFreeBalance(), change.getFreezeFreeBalance().abs())) {
                            throw new ServiceException("冻结赠送余额不足");
                        }
                    }
                    // 记录金额变动日志
                    SysBalanceLog balanceLog = new SysBalanceLog();
                    balanceLog.setUserId(change.getUserId());
                    balanceLog.setChangeType(change.getType());
                    balanceLog.setSourceUserId(change.getSourceUserId());
                    balanceLog.setChangeAvailablePayAmount(change.getAvailablePayBalance());
                    balanceLog.setChangeFreezePayAmount(change.getFreezePayBalance());
                    balanceLog.setChangeAvailableFreeAmount(change.getAvailableFreeBalance());
                    balanceLog.setChangeFreezeFreeAmount(change.getFreezeFreeBalance());
                    balanceLog.setAvailablePayBefore(userNow.getAvailablePayBalance());
                    balanceLog.setAvailablePayAfter(userNow.getAvailablePayBalance().add(change.getAvailablePayBalance()));
                    balanceLog.setFreezePayBefore(userNow.getFreezePayBalance());
                    balanceLog.setFreezePayAfter(userNow.getFreezePayBalance().add(change.getFreezePayBalance()));
                    balanceLog.setAvailableFreeBefore(userNow.getAvailableFreeBalance());
                    balanceLog.setAvailableFreeAfter(userNow.getAvailableFreeBalance().add(change.getAvailableFreeBalance()));
                    balanceLog.setFreezeFreeBefore(userNow.getFreezeFreeBalance());
                    balanceLog.setFreezeFreeAfter(userNow.getFreezeFreeBalance().add(change.getFreezeFreeBalance()));
                    balanceLog.setChangeDesc(change.getDescription());
                    balanceLog.setSaleOrderId(change.getSaleOrderId());
                    balanceLog.setWithdrawCashId(change.getWithdrawCashId());
                    balanceLog.setCreateBy(change.getUpdateBy());
                    sysBalanceLogService.insertSysBalanceLog(balanceLog);
                    // 扣除
                    SysUser compute = new SysUser();
                    compute.setUserId(change.getUserId());
                    compute.setUpdateBy(change.getUpdateBy());
                    compute.setAvailablePayBalance(balanceLog.getAvailablePayAfter());
                    compute.setFreezePayBalance(balanceLog.getFreezePayAfter());
                    compute.setAvailableFreeBalance(balanceLog.getAvailableFreeAfter());
                    compute.setFreezeFreeBalance(balanceLog.getFreezeFreeAfter());
                    if (change.getType() == BalanceChangeType.RECHARGE && !isNegative(change.getAvailablePayBalance())) {
                        compute.setPayPayment(userNow.getPayPayment().add(change.getAvailablePayBalance()));
                    }
                    if (!isNegative(change.getAvailableFreeBalance())) {
                        compute.setFreePayment(userNow.getFreePayment().add(change.getAvailableFreeBalance()));
                    }
                    compute.setUpdateTime(DateUtils.getNowDate());
                    compute.setUpdateBy(SecurityUtils.getUsernameNoException());
                    return updateCache(userMapper.updateUserBalance(compute), compute.getUserId());
                }
            } finally {
                // 无论成功与否，最终释放锁
                redisTemplate.delete(lockKey);
            }
        } else {
            // 如果未能获取锁，则等待一段时间后重试或处理错误
            try {
                Thread.sleep(100); // 等待100毫秒后重试
                return updateUserBalance(change);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editBalance(UserBalanceChangeVo vo) {
        if(BigDecimal.ZERO.compareTo(vo.getAmount()) >= 0) {
            throw new ServiceException("调整金额应该大于0");
        }
        checkUserDataScope(vo.getUserId());
        BalanceChangeVo change = new BalanceChangeVo();
        change.setUserId(vo.getUserId());
        change.setUpdateBy(SecurityUtils.getUsername());
        if(Objects.equals(vo.getOperation(), "3")) {
            SysUser sysUser = selectUserById(vo.getUserId());
            if(vo.getAmount().compareTo(sysUser.getAvailablePayBalance()) >= 0) {
                vo.setOperation("1");
                vo.setAmount(vo.getAmount().subtract(sysUser.getAvailablePayBalance()));
            } else {
                vo.setOperation("2");
                vo.setAmount(vo.getAmount().subtract(sysUser.getAvailablePayBalance()).negate());
            }
        }
        change.setType("1".equals(vo.getOperation()) ? BalanceChangeType.OTHOR_IN : BalanceChangeType.OTHOR_OUT);
        change.setDescription("管理员后台" + ("1".equals(vo.getOperation()) ? "加款" : "扣款") + "：" + vo.getAmount() + "，附加信息：" + vo.getRemark());
        change.setAvailablePayBalance("1".equals(vo.getOperation()) ? vo.getAmount() : vo.getAmount().negate());
        return updateUserBalance(change);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transferBalance(UserBalanceTransferVo vo) {
        if(BigDecimal.ZERO.compareTo(vo.getAmount()) >= 0) {
            throw new ServiceException("转账金额应该大于0");
        }
        if(vo.getToUserId() == SecurityUtils.getUserId()) {
            throw new ServiceException("不能给自己转账");
        }
        SysUser toUser = selectUserById(vo.getToUserId());
        if(toUser == null) {
            throw new ServiceException("目标账号不存在");
        }
        BalanceChangeVo changeFrom = new BalanceChangeVo();
        changeFrom.setUserId(SecurityUtils.getUserId());
        changeFrom.setUpdateBy(SecurityUtils.getUsername());
        changeFrom.setType(BalanceChangeType.TRANSFER_OUT);
        changeFrom.setDescription("转账到账号[" + toUser.getUserName() + "]" + "：" + vo.getAmount() + "，附加信息：" + vo.getRemark());
        changeFrom.setAvailablePayBalance(vo.getAmount().negate());
        BalanceChangeVo changeTo = new BalanceChangeVo();
        changeTo.setUserId(vo.getToUserId());
        changeTo.setUpdateBy(SecurityUtils.getUsername());
        changeTo.setType(BalanceChangeType.TRANSFER_IN);
        changeTo.setDescription("收到账号[" + SecurityUtils.getUsername() + "]转账" + "：" + vo.getAmount() + "，附加信息：" + vo.getRemark());
        changeTo.setAvailablePayBalance(vo.getAmount());
        return updateUserBalance(changeFrom) & updateUserBalance(changeTo);
    }

    private boolean isNegative(BigDecimal amount) {
        return BigDecimal.ZERO.compareTo(amount) > 0;
    }

    private boolean isLessThan(BigDecimal amount1, BigDecimal amount2) {
        return amount1.compareTo(amount2) < 0;
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return updateCache(userMapper.updateUserAvatar(userName, avatar), userName) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUsernameNoException());
        return updateCache(userMapper.updateUser(user),user.getUserId());
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return updateCache(userMapper.resetUserPwd(userName, password), userName);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        SysCache.delete(CacheConstants.SYS_USER_KEY + userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
            SysCache.delete(CacheConstants.SYS_USER_KEY + userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList)
        {
            user.setAvailableFreeBalance(BigDecimal.ZERO);
            user.setAvailablePayBalance(BigDecimal.ZERO);
            user.setFreezeFreeBalance(BigDecimal.ZERO);
            user.setFreezePayBalance(BigDecimal.ZERO);
            user.setPayPayment(BigDecimal.ZERO);
            user.setFreePayment(BigDecimal.ZERO);
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    deptService.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号[" + user.getUserName() + "]导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号[" + user.getUserName() + "]更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号[" + user.getUserName() + "]已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号[" + user.getUserName() + "]导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<SysUser> selectUserByExceptAppId(Long appId) {
        return userMapper.selectUserByExceptAppId(appId);
    }

    @Resource
    private ISysWithdrawMethodService withdrawMethodService;
    @Resource
    private ISysConfigWithdrawService configWithdrawService;
    @Resource
    private ISysWithdrawOrderService withdrawOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int withdrawBalance(UserBalanceWithdrawVo vo) {
        if(BigDecimal.ZERO.compareTo(vo.getApplyFee()) >= 0) {
            throw new ServiceException("提现金额应该大于0");
        }
        SysWithdrawMethod withdrawMethod = withdrawMethodService.selectSysWithdrawMethodById(vo.getReceiveAccountId());
        if(withdrawMethod == null) {
            throw new ServiceException("收款账号不存在");
        }
        Long userId = SecurityUtils.getUserId();
        if(!Objects.equals(withdrawMethod.getUserId(), userId)) {
            throw new ServiceException("收款账号所属账号与当前账号不符");
        }
        SysConfigWithdraw configWithdraw = configWithdrawService.getById(1);
        if(configWithdraw == null) {
            throw new ServiceException("提现配置数据缺失");
        }
        // 判断是否开启提现
        if(!configWithdraw.getEnableWithdrawCash().equals("Y")) {
            throw new ServiceException("提现功能未开启");
        }
        // 判断最大最小提现金额
        if(isLessThan(vo.getApplyFee(), configWithdraw.getWithdrawCashMin()) || isLessThan(configWithdraw.getWithdrawCashMax(), vo.getApplyFee())) {
            throw new ServiceException("提现金额应该在" + configWithdraw.getWithdrawCashMin() + "~" + configWithdraw.getWithdrawCashMax() + "之间");
        }
        // 计算手续费及实际提现金额
        BigDecimal handlingFee = vo.getApplyFee().multiply(BigDecimal.valueOf(configWithdraw.getHandlingFeeRate()));
        handlingFee = BigDecimal.valueOf(Math.round(handlingFee.doubleValue()) / 100);
        handlingFee =  isLessThan(handlingFee, configWithdraw.getHandlingFeeMin()) ? configWithdraw.getHandlingFeeMin() : handlingFee;
        handlingFee = isLessThan(configWithdraw.getHandlingFeeMax(), handlingFee) ? configWithdraw.getHandlingFeeMax() : handlingFee;
        SysUser user = userMapper.selectUserById(userId);
        BigDecimal actualFee;
        if(user.getAvailablePayBalance().compareTo(vo.getApplyFee().add(handlingFee)) >= 0) {
            actualFee = vo.getApplyFee();
        } else {
            actualFee = user.getAvailablePayBalance().subtract(handlingFee);
        }
        Long withdrawOrderId = withdrawOrderService.createWithdrawOrder(vo, withdrawMethod, actualFee, handlingFee);
        // 余额变动记录
        String description = "提现到收款账号[" + withdrawMethod.getReceiveMethod() + "|" + withdrawMethod.getReceiveAccount() + "]" + "："
                + "申请：" + vo.getApplyFee() + "，手续费：" + handlingFee + "，实际：" + actualFee + "，附加信息：" + vo.getRemark();
        BalanceChangeVo changeBalance = new BalanceChangeVo();
        changeBalance.setUserId(userId);
        changeBalance.setUpdateBy(SecurityUtils.getUsernameNoException());
        changeBalance.setType(BalanceChangeType.WITHDRAW_CASH_FREEZE);
        changeBalance.setDescription(description);
        changeBalance.setAvailablePayBalance(actualFee.negate());
        changeBalance.setFreezePayBalance(actualFee);
        changeBalance.setWithdrawCashId(withdrawOrderId);
        changeBalance.setSourceUserId(userId);
        return updateUserBalance(changeBalance);
    }
}
