package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
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
import com.ruoyi.system.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName) {
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
                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
//                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
//                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
//                }
                    if (StringUtils.equals(ipaddr, loginUser.getIpaddr()) && loginUser.getUsername().contains(userName)) {
                        userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
                    }
                } else if (StringUtils.isNotEmpty(ipaddr)) {
//                if (StringUtils.equals(ipaddr, user.getIpaddr())) {
//                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
//                }
                    if (StringUtils.equals(ipaddr, loginUser.getIpaddr())) {
                        userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
                    }
                } else if (StringUtils.isNotEmpty(userName)) {
//                if(StringUtils.isNotNull(user.getUser())) {
//                    if (StringUtils.equals(userName, user.getUsername())) {
//                        userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
//                    }
//                }
                    if (loginUser.getUsername() != null && loginUser.getUsername().contains(userName)) {
                        userOnlineList.add(userOnlineService.loginUserToUserOnline(loginUser));
                    }
                } else {
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

    @SuppressWarnings("unchecked")
    public List<SysUserOnline> getOnlineUserList() {
        TableDataInfo list = list(null, null);
        return (List<SysUserOnline>) list.getRows();
    }

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
