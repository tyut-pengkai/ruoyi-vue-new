package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.system.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.ruoyi.framework.aspectj.DataScopeAspect.DATA_SCOPE_ALL;
import static com.ruoyi.framework.aspectj.DataScopeAspect.DATA_SCOPE_SELF;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Resource
    private ISysDeviceCodeService deviceCodeService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUserOnline user) {
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<>();
        for (String key : keys) {
            LoginUser loginUser = null;
            try {
                loginUser = (LoginUser) SysCache.get(key);
            } catch(Exception ignored) {}
            if(loginUser == null) {
                loginUser = redisCache.getCacheObject(key);
                SysCache.set(key, loginUser, redisCache.getExpire(key));
            }
            if (loginUser != null) {
//                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
////                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
////                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
////                }
//                    if (StringUtils.equals(ipaddr, loginUser.getIpaddr()) && loginUser.getUsername().contains(userName)) {
//                        userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
//                    }
//                } else if (StringUtils.isNotEmpty(ipaddr)) {
////                if (StringUtils.equals(ipaddr, user.getIpaddr())) {
////                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
////                }
//                    if (StringUtils.equals(ipaddr, loginUser.getIpaddr())) {
//                        userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
//                    }
//                } else if (StringUtils.isNotEmpty(userName)) {
////                if(StringUtils.isNotNull(user.getUser())) {
////                    if (StringUtils.equals(userName, user.getUsername())) {
////                        userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
////                    }
////                }
//                    if (loginUser.getUsername() != null && loginUser.getUsername().contains(userName)) {
//                        userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
//                    }
//                } else {
//                    userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
//                }
                boolean flag = true;
                if(StringUtils.isNotEmpty(user.getIpaddr())) {
                    if (!StringUtils.equals(user.getIpaddr(), loginUser.getIpaddr())) {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getUserName())) {
                    if (loginUser.getUsername() == null || !loginUser.getUsername().contains(user.getUserName())) {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getDeptName())) {
                    if (loginUser.getUser() != null && StringUtils.isNotNull(loginUser.getUser().getDept())) {
                        if (loginUser.getUser().getDept().getDeptName() == null || !loginUser.getUser().getDept().getDeptName().contains(user.getDeptName())) {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getLoginLocation())) {
                    if (loginUser.getLoginLocation() == null || !loginUser.getLoginLocation().contains(user.getLoginLocation())) {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getBrowser())) {
                    if (loginUser.getBrowser() == null || !loginUser.getBrowser().contains(user.getBrowser())) {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getOs())) {
                    if (loginUser.getOs() == null || !loginUser.getOs().contains(user.getOs())) {
                        flag = false;
                    }
                }
                if(user.getIfApp() == 'Y' || user.getIfApp() == 'N') {
                    if (!Objects.equals(loginUser.getIfApp(), UserConstants.YES.equals(String.valueOf(user.getIfApp())))) {
                        flag = false;
                    }
                }
                if(user.getIfTrial() == 'Y' || user.getIfTrial() == 'N') {
                    if (!Objects.equals(loginUser.getIfTrial(), UserConstants.YES.equals(String.valueOf(user.getIfTrial())))) {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getAppDesc())) {
                    SysUserOnline online = new SysUserOnline();
                    userOnlineService.fillSysUserOnline(loginUser, online);
                    if (online.getAppDesc() == null || !online.getAppDesc().contains(user.getAppDesc())) {
                        flag = false;
                    }
                }
                if(StringUtils.isNotEmpty(user.getDeviceCode())) {
                    SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCodeId(loginUser.getDeviceCodeId());
                    if (deviceCode == null || !deviceCode.getDeviceCode().contains(user.getDeviceCode())) {
                        flag = false;
                    }
                }
                if(flag) {
                    userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
                }
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));

        List<SysUserOnline> userOnlineListFilted = new ArrayList<>();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser)) {
                if (currentUser.isSAdmin()) {
                    userOnlineListFilted = userOnlineList;
                } else {
                    for (SysRole role : currentUser.getRoles()) {
                        String dataScope = role.getDataScope();
                        if (DATA_SCOPE_ALL.equals(dataScope)) {
                            userOnlineListFilted = userOnlineList;
                            break;
                        } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                            for (SysUserOnline online : userOnlineList) {
                                if (online.getUserName().equals(currentUser.getUserName()) ||
                                        (online.getIfApp() == 'Y' &&
                                                online.getAppAuthor().equals(currentUser.getUserName()))) {
                                    userOnlineListFilted.add(online);
                                }
                            }
                        }
                    }
                }
            }
        }
        return getDataTable(userOnlineListFilted);
    }

//    @SuppressWarnings("unchecked")
//    public List<SysUserOnline> getOnlineUserList() {
//        TableDataInfo list = list(null, null);
//        return (List<SysUserOnline>) list.getRows();
//    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + tokenId + "*");
        for (String key : keys) {
            redisCache.deleteObject(key);
            SysCache.delete(key);
            Constants.LAST_ERROR_REASON_MAP.put(tokenId, "您已被系统强制下线");
        }
        return AjaxResult.success();
    }
}
