package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AppUserExpireChangeType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.agent.anno.AgentPermCheck;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 软件用户Controller
 *
 * @author zwgu
 * @date 2021-11-09
 */
@RestController
@RequestMapping("/agent/appUser")
public class SysAgentAppUserController extends BaseController {
    @Autowired
    private ISysAppUserService sysAppUserService;
    @Autowired
    private ISysAppService sysAppService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysAgentUserService sysAgentService;

    /**
     * 查询软件用户列表
     */
    @SuppressWarnings("unchecked")
    @PreAuthorize("@ss.hasPermi('agent:appUser:list')")
    @AgentPermCheck
    @GetMapping("/list")
    public TableDataInfo list(SysAppUser sysAppUser) {
        startPage();
//        if (!permissionService.hasAnyRoles("sadmin,admin")) {
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        if(agent == null) {
            throw new ServiceException("您没有该操作的权限（代理系统）");
        }
        sysAppUser.setAgentId(agent.getAgentId());
//        }
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);

        Map<String, Object> map = sysAppUserService.computeCurrentOnline();
        // 统计当前在线用户数
        Map<Long, Set<LoginUser>> onlineListUMap = (Map<Long, Set<LoginUser>>) map.get("u");
        // 统计当前在线设备数
        Map<Long, Set<Long>> onlineListMMap = (Map<Long, Set<Long>>) map.get("m");

        for (SysAppUser sau : list) {
            sysAppUserService.fillCurrentOnlineInfo(sau, onlineListUMap, onlineListMMap);
        }
        return getDataTable(list);
    }

    /**
     * 导出软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('agent:appUser:export')")
    @AgentPermCheck
    @Log(title = "软件用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppUser sysAppUser) {
//        if (!permissionService.hasAnyRoles("sadmin,admin")) {
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        if(agent == null) {
            throw new ServiceException("您没有该操作的权限（代理系统）");
        }
        sysAppUser.setAgentId(agent.getAgentId());
//        }
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);
        ExcelUtil<SysAppUser> util = new ExcelUtil<SysAppUser>(SysAppUser.class);
        return util.exportExcel(list, "软件用户数据");
    }

    /**
     * 获取软件用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:appUser:query')")
    @AgentPermCheck
    @GetMapping(value = "/{appUserId}")
    public AjaxResult getInfo(@PathVariable("appUserId") Long appUserId) {
        return AjaxResult.success(sysAppUserService.selectSysAppUserByAppUserId(appUserId));
    }

//    /**
//     * 新增软件用户
//     */
//    @PreAuthorize("@ss.hasPermi('agent:appUser:add')")
//    @AgentPermCheck
//    @Log(title = "软件用户", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody SysAppUser sysAppUser) {
//        sysAppUser.setAgentId(getUserId());
//        int i = 0;
//        sysAppUser.setCreateBy(getUsername());
//        SysApp sysApp = sysAppService.selectSysAppByAppId(sysAppUser.getAppId());
//        if (sysApp.getAuthType() == AuthType.ACCOUNT) {
//            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndUserId(sysAppUser.getAppId(), sysAppUser.getUserId());
//            if (appUser != null) {
//                return AjaxResult.error("当前软件下该账号已拥有软件用户，可直接登录，无需重复添加");
//            }
//            SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
//            if (sysApp.getBillType() == BillType.TIME) {
//                Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(null, sysApp.getFreeQuotaReg());
//                sysAppUser.setExpireTime(newExpiredTime);
//                sysAppUser.setPoint(null);
//                expireLog.setExpireTimeBefore(null);
//                expireLog.setExpireTimeAfter(newExpiredTime);
//            } else if (sysApp.getBillType() == BillType.POINT) {
//                sysAppUser.setExpireTime(null);
//                BigDecimal newPoint = MyUtils.getNewPointAdd(null, sysApp.getFreeQuotaReg());
//                sysAppUser.setPoint(newPoint);
//                expireLog.setPointBefore(null);
//                expireLog.setPointAfter(newPoint);
//            } else {
//                throw new ApiException("软件计费方式有误");
//            }
//            i = sysAppUserService.insertSysAppUser(sysAppUser);
//            if (sysApp.getFreeQuotaReg() > 0) {
//                // 记录用户时长变更日志
//                expireLog.setAppUserId(sysAppUser.getAppUserId());
//                expireLog.setTemplateId(null);
//                expireLog.setCardId(null);
//                expireLog.setChangeDesc("用户首次登录赠送");
//                expireLog.setChangeType(AppUserExpireChangeType.APP_LOGIN);
//                expireLog.setChangeAmount(sysApp.getFreeQuotaReg());
//                expireLog.setCardNo(null);
//                expireLog.setAppId(sysApp.getAppId());
//                AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
//            }
//        } else {
//            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(sysAppUser.getAppId(), sysAppUser.getLoginCode());
//            if (appUser != null) {
//                return AjaxResult.error("当前软件下该单码已拥有软件用户，可直接登录，无需重复添加");
//            }
//            SysLoginCode loginCode = sysLoginCodeService.selectSysLoginCodeByCardNo(sysAppUser.getLoginCode());
//            SysAppUserExpireLog expireLog1 = new SysAppUserExpireLog();
//            SysAppUserExpireLog expireLog2 = new SysAppUserExpireLog();
//            if (sysApp.getBillType() == BillType.TIME) {
//                if (sysAppUser.getExpireTime() == null) {
//                    sysAppUser.setExpireTime(DateUtils.getNowDate());
//                }
//                Date newExpiredTime = sysAppUser.getExpireTime();
//                Date newExpiredTime2 = MyUtils.getNewExpiredTimeAdd(sysAppUser.getExpireTime(), sysApp.getFreeQuotaReg());
//                sysAppUser.setExpireTime(newExpiredTime2);
//                sysAppUser.setPoint(null);
//                expireLog1.setExpireTimeBefore(null);
//                expireLog1.setExpireTimeAfter(newExpiredTime);
//                expireLog2.setExpireTimeBefore(newExpiredTime);
//                expireLog2.setExpireTimeAfter(newExpiredTime2);
//            } else if (sysApp.getBillType() == BillType.POINT) {
//                if (sysAppUser.getPoint() == null) {
//                    sysAppUser.setPoint(BigDecimal.ZERO);
//                }
//                sysAppUser.setExpireTime(null);
//                BigDecimal newPoint = sysAppUser.getPoint();
//                BigDecimal newPoint2 = MyUtils.getNewPointAdd(sysAppUser.getPoint(), sysApp.getFreeQuotaReg());
//                sysAppUser.setPoint(newPoint2);
//                expireLog1.setPointBefore(null);
//                expireLog1.setPointAfter(newPoint);
//                expireLog2.setPointBefore(newPoint);
//                expireLog2.setPointAfter(newPoint2);
//            } else {
//                throw new ApiException("软件计费方式有误");
//            }
//            if (loginCode != null) {
//                sysAppUser.setAgentId(loginCode.getAgentId());
//                sysAppUser.setLastChargeCardId(loginCode.getCardId());
//                sysAppUser.setLastChargeTemplateId(loginCode.getTemplateId());
//            }
//            i = sysAppUserService.insertSysAppUser(sysAppUser);
//            if (sysApp.getFreeQuotaReg() > 0) {
//                // 记录用户时长变更日志
//                expireLog2.setAppUserId(sysAppUser.getAppUserId());
//                expireLog2.setTemplateId(null);
//                expireLog2.setCardId(null);
//                expireLog2.setChangeDesc("用户首次登录赠送");
//                expireLog2.setChangeType(AppUserExpireChangeType.APP_LOGIN);
//                expireLog2.setChangeAmount(sysApp.getFreeQuotaReg());
//                expireLog2.setCardNo(null);
//                expireLog2.setAppId(sysApp.getAppId());
//                AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog2));
//            }
//            // 记录用户时长变更日志
//            expireLog1.setAppUserId(sysAppUser.getAppUserId());
//            if (loginCode != null) {
//                expireLog1.setTemplateId(loginCode.getTemplateId());
//                expireLog1.setCardId(loginCode.getCardId());
//
//            }
//            expireLog1.setChangeDesc("代理台添加用户");
//            expireLog1.setChangeType(AppUserExpireChangeType.RECHARGE);
//            expireLog1.setChangeAmount(0L);
//            expireLog1.setCardNo(sysAppUser.getLoginCode());
//            expireLog1.setAppId(sysApp.getAppId());
//            AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog1));
//        }
//        return toAjax(i);
//    }

    /**
     * 修改软件用户
     */
    @PreAuthorize("@ss.hasPermi('agent:appUser:edit')")
    @AgentPermCheck
    @Log(title = "软件用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppUser sysAppUser) {
        SysAppUser appUser = sysAppUserService.selectSysAppUserByAppUserId(sysAppUser.getAppUserId());
        // 检查是否有变更状态权限
        if(sysAppUser.getStatus() != null && !Objects.equals(sysAppUser.getStatus(), appUser.getStatus())) {
            if (Objects.equals(sysAppUser.getStatus(), UserStatus.OK.getCode())) {
                if (!permissionService.hasAgentPermi("enableUpdateAppUserStatus0")) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            }
            if (Objects.equals(sysAppUser.getStatus(), UserStatus.DISABLE.getCode())) {
                if (!permissionService.hasAgentPermi("enableUpdateAppUserStatus1")) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            }
        }
        // 检查是否有变更权限
        checkAgentEditAppUserPerm(sysAppUser, appUser);

        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        if(!Objects.equals(appUser.getAgentId(), agent.getAgentId())) {
            throw new ServiceException("所选软件用户不是您的用户", 400);
        }
        sysAppUser.setUpdateBy(getUsername());
        // 记录用户时长变更日志
        if (sysAppUser.getExpireTime() != null || sysAppUser.getPoint() != null) {
            SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
            if (sysAppUser.getExpireTime() != null) {
                expireLog.setExpireTimeBefore(appUser.getExpireTime());
                expireLog.setExpireTimeAfter(sysAppUser.getExpireTime());
                expireLog.setChangeAmount((sysAppUser.getExpireTime().getTime() - appUser.getExpireTime().getTime()) / 1000);
            } else {
                expireLog.setPointBefore(appUser.getPoint());
                expireLog.setPointAfter(sysAppUser.getPoint());
                expireLog.setChangeAmount(sysAppUser.getPoint().subtract(appUser.getPoint()).longValue());
            }
            expireLog.setAppUserId(appUser.getAppUserId());
            expireLog.setTemplateId(null);
            expireLog.setCardId(null);
            expireLog.setChangeDesc("代理后台修改：" + getUsername());
            expireLog.setChangeType(AppUserExpireChangeType.ADMIN_UPDATE);
            expireLog.setCardNo(null);
            expireLog.setAppId(appUser.getAppId());
            AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
        }
        return toAjax(sysAppUserService.updateSysAppUser(sysAppUser));
    }

    private void checkAgentEditAppUserPerm(SysAppUser appUser, SysAppUser oAppUser) {
        Map<String, String> map =  new HashMap<>();
        map.put("ExpireTime", "enableUpdateAppUserTime");
        map.put("Point", "enableUpdateAppUserPoint");
        map.put("LoginLimitU", "enableUpdateAppUserLoginLimitU");
        map.put("LoginLimitM", "enableUpdateAppUserLoginLimitM");
        map.put("CardLoginLimitU", "enableUpdateCardLoginLimitU");
        map.put("CardLoginLimitM", "enableUpdateCardLoginLimitM");
        map.put("CardCustomParams", "enbaleUpdateAppUserCustomParams");
        map.put("Remark", "enableUpdateAppUserRemark");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                Method declaredMethod = SysAppUser.class.getDeclaredMethod("get" + StringUtils.capitalize(entry.getKey()));
                Object value = declaredMethod.invoke(appUser);
                Object oValue = declaredMethod.invoke(oAppUser);
                if (value != null && !Objects.equals(value, oValue) && !permissionService.hasAgentPermi(entry.getValue())) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * 删除软件用户
     */
    @PreAuthorize("@ss.hasPermi('agent:appUser:remove')")
    @AgentPermCheck("enableDeleteAppUser")
    @Log(title = "软件用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appUserIds}")
    public AjaxResult remove(@PathVariable Long[] appUserIds) {
        return toAjax(sysAppUserService.deleteSysAppUserByAppUserIds(appUserIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('agent:appUser:edit')")
    @AgentPermCheck
    @Log(title = "软件用户", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysAppUser sysAppUser) {
        // 检查是否有变更状态权限
        if(Objects.equals(sysAppUser.getStatus(), UserStatus.OK.getCode())) {
            if(!permissionService.hasAgentPermi("enableUpdateAppUserStatus0")) {
                throw new ServiceException("您没有该操作的权限（代理系统）");
            }
        }
        if(Objects.equals(sysAppUser.getStatus(), UserStatus.DISABLE.getCode())) {
            if(!permissionService.hasAgentPermi("enableUpdateAppUserStatus1")) {
                throw new ServiceException("您没有该操作的权限（代理系统）");
            }
        }
        return toAjax(sysAppUserService.updateSysAppUser(sysAppUser));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:resetPwd')")
    @AgentPermCheck("enableUpdateUserPassword")
    @Log(title = "软件用户", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndUserId(user.getDeptId(), user.getUserId());// 此处使用dept id传递app id
        if(appUser == null) {
            throw new ServiceException("软件用户不存在", 400);
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent myAgent = sysAgentService.selectSysAgentByUserId(SecurityUtils.getUserId());
            if(!Objects.equals(appUser.getAgentId(), myAgent.getAgentId())) {
                throw new ServiceException("所选软件用户不是您的用户", 400);
            }
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(sysUserService.resetPwd(user));
    }
}
