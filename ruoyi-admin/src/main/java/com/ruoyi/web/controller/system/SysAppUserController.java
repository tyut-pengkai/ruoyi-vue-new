package com.ruoyi.web.controller.system;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AppUserExpireChangeType;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 软件用户Controller
 *
 * @author zwgu
 * @date 2021-11-09
 */
@RestController
@RequestMapping("/system/appUser")
public class SysAppUserController extends BaseController {
    @Autowired
    private ISysAppUserService sysAppUserService;
    @Autowired
    private ISysAppService sysAppService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysLoginCodeService sysLoginCodeService;

    /**
     * 查询软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppUser sysAppUser) {
        startPage();
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);

        // 统计当前在线用户数
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*");
        Map<Long, List<LoginUser>> onlineListUMap = new HashMap<>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (user != null && user.getIfApp() && !user.getIfTrial()) {
                Long appUserId = user.getAppUserId();
                if (!onlineListUMap.containsKey(appUserId)) {
                    onlineListUMap.put(appUserId, new ArrayList<>());
                }
                onlineListUMap.get(appUserId).add(user);
            }
        }
        // 统计当前在线设备数
        Map<Long, Set<Long>> onlineListMMap = new HashMap<>();
        for (Map.Entry<Long, List<LoginUser>> entry : onlineListUMap.entrySet()) {
            Long appUserId = entry.getKey();
            List<LoginUser> onlineUList = entry.getValue();
            if (!onlineListMMap.containsKey(appUserId)) {
                onlineListMMap.put(appUserId, new HashSet<>());
            }
            for (LoginUser user : onlineUList) {
                if (user.getDeviceCode() != null) {
                    onlineListMMap.get(appUserId).add(user.getDeviceCode().getDeviceCodeId());
                }
            }
        }

        for (SysAppUser sau : list) {
            sau.setEffectiveLoginLimitU(MyUtils.getEffectiveLoginLimitU(sau.getApp(), sau));
            sau.setEffectiveLoginLimitM(MyUtils.getEffectiveLoginLimitM(sau.getApp(), sau));
            if (onlineListUMap.containsKey(sau.getAppUserId())) {
                sau.setCurrentOnlineU(onlineListUMap.get(sau.getAppUserId()).size());
            } else {
                sau.setCurrentOnlineU(0);
            }
            if (onlineListMMap.containsKey(sau.getAppUserId())) {
                sau.setCurrentOnlineM(onlineListMMap.get(sau.getAppUserId()).size());
            } else {
                sau.setCurrentOnlineM(0);
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:export')")
    @Log(title = "软件用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppUser sysAppUser) {
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);
        ExcelUtil<SysAppUser> util = new ExcelUtil<SysAppUser>(SysAppUser.class);
        return util.exportExcel(list, "软件用户数据");
    }

    /**
     * 获取软件用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:query')")
    @GetMapping(value = "/{appUserId}")
    public AjaxResult getInfo(@PathVariable("appUserId") Long appUserId) {
        return AjaxResult.success(sysAppUserService.selectSysAppUserByAppUserId(appUserId));
    }

    /**
     * 新增软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:add')")
    @Log(title = "软件用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppUser sysAppUser) {
        int i = 0;
        sysAppUser.setCreateBy(getUsername());
        SysApp sysApp = sysAppService.selectSysAppByAppId(sysAppUser.getAppId());
        if (sysApp.getAuthType() == AuthType.ACCOUNT) {
            if(sysAppUser.getUserId() == null) {
                return AjaxResult.error("软件用户所属账号不能为空");
            }
            SysUser sysUser = sysUserService.selectUserById(sysAppUser.getUserId());
            if(sysUser == null) {
                return AjaxResult.error("账号不存在");
            }
            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndUserId(sysAppUser.getAppId(), sysAppUser.getUserId());
            if (appUser != null) {
                return AjaxResult.error("当前软件下该账号已拥有软件用户，可直接登录，无需重复添加");
            }
            SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
            if (sysApp.getBillType() == BillType.TIME) {
                Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(null, sysApp.getFreeQuotaReg());
                sysAppUser.setExpireTime(newExpiredTime);
                sysAppUser.setPoint(null);
                expireLog.setExpireTimeBefore(null);
                expireLog.setExpireTimeAfter(newExpiredTime);
            } else if (sysApp.getBillType() == BillType.POINT) {
                sysAppUser.setExpireTime(null);
                BigDecimal newPoint = MyUtils.getNewPointAdd(null, sysApp.getFreeQuotaReg());
                sysAppUser.setPoint(newPoint);
                expireLog.setPointBefore(null);
                expireLog.setPointAfter(newPoint);
            } else {
                throw new ApiException("软件计费方式有误");
            }
            i = sysAppUserService.insertSysAppUser(sysAppUser);
            if (sysApp.getFreeQuotaReg() > 0) {
                // 记录用户时长变更日志
                expireLog.setAppUserId(sysAppUser.getAppUserId());
                expireLog.setTemplateId(null);
                expireLog.setCardId(null);
                expireLog.setChangeDesc("用户首次登录赠送");
                expireLog.setChangeType(AppUserExpireChangeType.APP_LOGIN);
                expireLog.setChangeAmount(sysApp.getFreeQuotaReg());
                expireLog.setCardNo(null);
                expireLog.setAppId(sysApp.getAppId());
                AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
            }
        } else {
            if(sysAppUser.getLoginCode() == null) {
                return AjaxResult.error("软件用户所属单码不能为空");
            }
            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(sysAppUser.getAppId(), sysAppUser.getLoginCode());
            if (appUser != null) {
                return AjaxResult.error("当前软件下该单码已拥有软件用户，可直接登录，无需重复添加");
            }
            SysLoginCode loginCode = sysLoginCodeService.selectSysLoginCodeByCardNo(sysAppUser.getLoginCode());
            SysAppUserExpireLog expireLog1 = new SysAppUserExpireLog();
            SysAppUserExpireLog expireLog2 = new SysAppUserExpireLog();
            if (sysApp.getBillType() == BillType.TIME) {
                if (sysAppUser.getExpireTime() == null) {
                    sysAppUser.setExpireTime(DateUtils.getNowDate());
                }
                Date newExpiredTime = sysAppUser.getExpireTime();
                Date newExpiredTime2 = MyUtils.getNewExpiredTimeAdd(sysAppUser.getExpireTime(), sysApp.getFreeQuotaReg());
                sysAppUser.setExpireTime(newExpiredTime2);
                sysAppUser.setPoint(null);
                expireLog1.setExpireTimeBefore(null);
                expireLog1.setExpireTimeAfter(newExpiredTime);
                expireLog2.setExpireTimeBefore(newExpiredTime);
                expireLog2.setExpireTimeAfter(newExpiredTime2);
            } else if (sysApp.getBillType() == BillType.POINT) {
                if (sysAppUser.getPoint() == null) {
                    sysAppUser.setPoint(BigDecimal.ZERO);
                }
                sysAppUser.setExpireTime(null);
                BigDecimal newPoint = sysAppUser.getPoint();
                BigDecimal newPoint2 = MyUtils.getNewPointAdd(sysAppUser.getPoint(), sysApp.getFreeQuotaReg());
                sysAppUser.setPoint(newPoint2);
                expireLog1.setPointBefore(null);
                expireLog1.setPointAfter(newPoint);
                expireLog2.setPointBefore(newPoint);
                expireLog2.setPointAfter(newPoint2);
            } else {
                throw new ApiException("软件计费方式有误");
            }
            if (loginCode != null) {
                sysAppUser.setAgentId(loginCode.getAgentId());
                sysAppUser.setLastChargeCardId(loginCode.getCardId());
                sysAppUser.setLastChargeTemplateId(loginCode.getTemplateId());
            }
            i = sysAppUserService.insertSysAppUser(sysAppUser);
            if (sysApp.getFreeQuotaReg() > 0) {
                // 记录用户时长变更日志
                expireLog2.setAppUserId(sysAppUser.getAppUserId());
                expireLog2.setTemplateId(null);
                expireLog2.setCardId(null);
                expireLog2.setChangeDesc("用户首次登录赠送");
                expireLog2.setChangeType(AppUserExpireChangeType.APP_LOGIN);
                expireLog2.setChangeAmount(sysApp.getFreeQuotaReg());
                expireLog2.setCardNo(null);
                expireLog2.setAppId(sysApp.getAppId());
                AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog2));
            }
            // 记录用户时长变更日志
            expireLog1.setAppUserId(sysAppUser.getAppUserId());
            if (loginCode != null) {
                expireLog1.setTemplateId(loginCode.getTemplateId());
                expireLog1.setCardId(loginCode.getCardId());
            }
            expireLog1.setChangeDesc("管理后台添加用户");
            expireLog1.setChangeType(AppUserExpireChangeType.RECHARGE);
            expireLog1.setChangeAmount(0L);
            expireLog1.setCardNo(sysAppUser.getLoginCode());
            expireLog1.setAppId(sysApp.getAppId());
            AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog1));
        }
        return toAjax(i);
    }

    /**
     * 修改软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:edit')")
    @Log(title = "软件用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppUser sysAppUser) {
        sysAppUser.setUpdateBy(getUsername());
        // 记录用户时长变更日志
        if (sysAppUser.getExpireTime() != null || sysAppUser.getPoint() != null) {
            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppUserId(sysAppUser.getAppUserId());
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
            expireLog.setChangeDesc("管理员后台修改：" + getUsername());
            expireLog.setChangeType(AppUserExpireChangeType.ADMIN_UPDATE);
            expireLog.setCardNo(null);
            expireLog.setAppId(appUser.getAppId());
            AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
        }
        return toAjax(sysAppUserService.updateSysAppUser(sysAppUser));
    }

    /**
     * 删除软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:remove')")
    @Log(title = "软件用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appUserIds}")
    public AjaxResult remove(@PathVariable Long[] appUserIds) {
        return toAjax(sysAppUserService.deleteSysAppUserByAppUserIds(appUserIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:edit')")
    @Log(title = "设备码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysAppUser sysAppUser) {
        sysAppUser.setUpdateBy(getUsername());
        return toAjax(sysAppUserService.updateSysDeviceCodeStatus(sysAppUser));
    }
}
