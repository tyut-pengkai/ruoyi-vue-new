package com.ruoyi.system.service.impl;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.system.mapper.SysAppUserMapper;
import com.ruoyi.system.service.ISysAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 软件用户Service业务层处理
 *
 * @author zwgu
 * @date 2021-11-09
 */
@Service
public class SysAppUserServiceImpl implements ISysAppUserService {
    @Autowired
    private SysAppUserMapper sysAppUserMapper;
    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init() {
        SysAppUser user = new SysAppUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("isVip", "Y");
        user.setParams(map);
        List<SysAppUser> appUserList = sysAppUserMapper.selectSysAppUserList(user);
        for (SysAppUser appUser : appUserList) {
            redisCache.setCacheObject(CacheConstants.SYS_APP_USER_KEY + appUser.getAppUserId(), appUser, 24, TimeUnit.HOURS);
        }
    }

    /**
     * 查询软件用户
     *
     * @param appUserId 软件用户主键
     * @return 软件用户
     */
    @Override
    public SysAppUser selectSysAppUserByAppUserId(Long appUserId) {
        SysAppUser appUser = redisCache.getCacheObject(CacheConstants.SYS_APP_USER_KEY + appUserId);
        if (appUser == null) {
            appUser = sysAppUserMapper.selectSysAppUserByAppUserId(appUserId);
            redisCache.setCacheObject(CacheConstants.SYS_APP_USER_KEY + appUser.getAppUserId(), appUser, 24, TimeUnit.HOURS);
        }
        return appUser;
    }

    /**
     * 查询软件用户列表
     *
     * @param sysAppUser 软件用户
     * @return 软件用户
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysAppUser> selectSysAppUserList(SysAppUser sysAppUser)
    {
        return sysAppUserMapper.selectSysAppUserList(sysAppUser);
    }

    /**
     * 新增软件用户
     *
     * @param sysAppUser 软件用户
     * @return 结果
     */
    @Override
    public int insertSysAppUser(SysAppUser sysAppUser)
    {
        sysAppUser.setCreateTime(DateUtils.getNowDate());
        sysAppUser.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysAppUserMapper.insertSysAppUser(sysAppUser);
    }

    /**
     * 修改软件用户
     *
     * @param sysAppUser 软件用户
     * @return 结果
     */
    @Override
    public int updateSysAppUser(SysAppUser sysAppUser) {
        sysAppUser.setUpdateTime(DateUtils.getNowDate());
        sysAppUser.setUpdateBy(SecurityUtils.getUsernameNoException());
        int i = sysAppUserMapper.updateSysAppUser(sysAppUser);
        if (i > 0) {
            SysAppUser appUser = sysAppUserMapper.selectSysAppUserByAppUserId(sysAppUser.getAppUserId());
            redisCache.setCacheObject(CacheConstants.SYS_APP_USER_KEY + appUser.getAppUserId(), appUser, 24, TimeUnit.HOURS);
        }
        return i;
    }

    /**
     * 批量删除软件用户
     *
     * @param appUserIds 需要删除的软件用户主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserByAppUserIds(Long[] appUserIds) {
        for (Long appUserId : appUserIds) {
            redisCache.deleteObject(CacheConstants.SYS_APP_USER_KEY + appUserId);
        }
        return sysAppUserMapper.deleteSysAppUserByAppUserIds(appUserIds);
    }

    /**
     * 删除软件用户信息
     *
     * @param appUserId 软件用户主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserByAppUserId(Long appUserId)
    {
        redisCache.deleteObject(CacheConstants.SYS_APP_USER_KEY + appUserId);
        return sysAppUserMapper.deleteSysAppUserByAppUserId(appUserId);
    }

    /**
     * 查询软件用户
     *
     * @param appId 软件主键
     * @param userId 账号主键
     * @return 软件用户
     */
    @Override
    public SysAppUser selectSysAppUserByAppIdAndUserId(Long appId, Long userId) {
        return sysAppUserMapper.selectSysAppUserByAppIdAndUserId(appId, userId);
    }

    /**
     * 查询软件用户
     *
     * @param appId     软件主键
     * @param loginCode 单码
     * @return 软件用户
     */
    @Override
    public SysAppUser selectSysAppUserByAppIdAndLoginCode(Long appId, String loginCode) {
        return sysAppUserMapper.selectSysAppUserByAppIdAndLoginCode(appId, loginCode);
    }

    /**
     * 修改状态
     *
     * @param sysAppUser 信息
     * @return 结果
     */
    @Override
    public int updateSysDeviceCodeStatus(SysAppUser sysAppUser) {
        int i = sysAppUserMapper.updateSysAppUser(sysAppUser);
        if (i > 0) {
            SysAppUser appUser = sysAppUserMapper.selectSysAppUserByAppUserId(sysAppUser.getAppUserId());
            redisCache.setCacheObject(CacheConstants.SYS_APP_USER_KEY + appUser.getAppUserId(), appUser, 24, TimeUnit.HOURS);
        }
        return i;
    }


    public Map<String, Object> computeCurrentOnline(Long appUserId) {
        Map<String, Object> result = new HashMap<>();
        // 统计当前在线用户数
        String k = CacheConstants.LOGIN_TOKEN_KEY + "*";
        if(appUserId != null) {
            k += "|" + appUserId;
        }
        Collection<String> keys = redisCache.scan(k);
        Map<Long, Set<LoginUser>> onlineListUMap = new HashMap<>();
        for (String key : keys) {
            LoginUser loginUser = null;
            try {
                loginUser = (LoginUser) SysCache.get(key);
            } catch(Exception ignored) {}
            if(loginUser == null) {
                loginUser = redisCache.getCacheObject(key);
                SysCache.set(key, loginUser);
            }
            if (loginUser != null && loginUser.getIfApp() && !loginUser.getIfTrial()) {
                Long aui = loginUser.getAppUserId();
                if(appUserId == null || appUserId.equals(aui)) {
                    if (!onlineListUMap.containsKey(aui)) {
                        onlineListUMap.put(aui, new HashSet<>());
                    }
                    onlineListUMap.get(aui).add(loginUser);
                }
            }
        }
        result.put("u", onlineListUMap);
        // 统计当前在线设备数
        Map<Long, Set<Long>> onlineListMMap = new HashMap<>();
        for (Map.Entry<Long, Set<LoginUser>> entry : onlineListUMap.entrySet()) {
            Long aui = entry.getKey();
            Set<LoginUser> onlineUList = entry.getValue();
            if (!onlineListMMap.containsKey(aui)) {
                onlineListMMap.put(aui, new HashSet<>());
            }
            for (LoginUser user : onlineUList) {
                if (user.getDeviceCode() != null) {
                    onlineListMMap.get(aui).add(user.getDeviceCode().getDeviceCodeId());
                }
            }
        }
        result.put("m", onlineListMMap);
        return result;
    }

    public Map<String, Object> computeCurrentOnline() {
        return computeCurrentOnline(null);
    }

    public void fillCurrentOnlineInfo(SysAppUser sau, Map<Long, Set<LoginUser>> onlineListUMap, Map<Long, Set<Long>> onlineListMMap) {
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

    public void fillCurrentOnlineInfo(SysAppUser sau) {
        Map<String, Object> map = computeCurrentOnline();
        // 统计当前在线用户数
        Map<Long, Set<LoginUser>> onlineListUMap = (Map<Long, Set<LoginUser>>) map.get("u");
        // 统计当前在线设备数
        Map<Long, Set<Long>> onlineListMMap = (Map<Long, Set<Long>>) map.get("m");
        fillCurrentOnlineInfo(sau, onlineListUMap, onlineListMMap);
    }
}
