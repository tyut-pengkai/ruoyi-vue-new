package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.SysConfigWebsite;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysConfigWebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 软件Service业务层处理
 *
 * @author zwgu
 * @date 2021-11-05
 */
@Service
public class SysAppServiceImpl implements ISysAppService {
    @Autowired
    private SysAppMapper sysAppMapper;
//    @Resource
//    private RedisCache redisCache;
    @Resource
    private ISysConfigWebsiteService sysConfigWebsiteService;
    /**
     * 设置请求的统一前缀
     */
    @Value("${swagger.pathMapping}")
    private String pathMapping;

    @PostConstruct
    public void init() {
        List<SysApp> appList = sysAppMapper.selectSysAppList(new SysApp());
        for (SysApp app : appList) {
            if (StringUtils.isNotBlank(app.getAppKey())) {
                SysCache.set(CacheConstants.SYS_APP_KEY + app.getAppKey(), app, 86400000);
            }
        }
    }

    /**
     * 查询软件
     *
     * @param appId 软件主键
     * @return 软件
     */
    @Override
    public SysApp selectSysAppByAppId(Long appId) {
        return sysAppMapper.selectSysAppByAppId(appId);
    }

    /**
     * 查询软件
     *
     * @param appKey 软件AppKey
     * @return 软件
     */
    @Override
    public SysApp selectSysAppByAppKey(String appKey) {
        SysApp app = (SysApp) SysCache.get(CacheConstants.SYS_APP_KEY + appKey);
        if (app == null) {
            app = sysAppMapper.selectSysAppByAppKey(appKey);
            SysCache.set(CacheConstants.SYS_APP_KEY + appKey, app, 86400000);
        }
        return app;
    }

    /**
     * 查询软件列表
     *
     * @param sysApp 软件
     * @return 软件
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysApp> selectSysAppList(SysApp sysApp) {
        return sysAppMapper.selectSysAppList(sysApp);
    }

    /**
     * 新增软件
     *
     * @param sysApp 软件
     * @return 结果
     */
    @Override
    public int insertSysApp(SysApp sysApp) {
        sysApp.setCreateTime(DateUtils.getNowDate());
        sysApp.setCreateBy(SecurityUtils.getUsernameNoException());
        // 检查软件位限制
        List<SysApp> appList = sysAppMapper.selectSysAppList(new SysApp());
        // -1 表示不限制
        Integer appLimit = ((LicenseCheckModel) Constants.LICENSE_CONTENT.getExtra()).getAppLimit();
        if (appLimit != -1 && appList != null && appList.size() >= appLimit) {
            throw new ServiceException("当前创建的软件数量已超出授权上限，请升级授权或删除部分软件后再试");
        }
        return sysAppMapper.insertSysApp(sysApp);
    }

    /**
     * 修改软件
     *
     * @param sysApp 软件
     * @return 结果
     */
    @Override
    public int updateSysApp(SysApp sysApp) {
        sysApp.setUpdateTime(DateUtils.getNowDate());
        sysApp.setUpdateBy(SecurityUtils.getUsernameNoException());
        sysApp.setAuthType(null);
        sysApp.setBillType(null);
        int i = sysAppMapper.updateSysApp(sysApp);
        if (i > 0) {
            SysApp app = sysAppMapper.selectSysAppByAppId(sysApp.getAppId());
            SysCache.set(CacheConstants.SYS_APP_KEY + app.getAppKey(), app, 86400000);
        }
        return i;
    }

    /**
     * 批量删除软件
     *
     * @param appIds 需要删除的软件主键
     * @return 结果
     */
    @Override
    public int deleteSysAppByAppIds(Long[] appIds) {
        for (Long appId : appIds) {
            SysApp app = selectSysAppByAppId(appId);
            SysCache.delete(CacheConstants.SYS_APP_KEY + app.getAppKey());
        }
        return sysAppMapper.deleteSysAppByAppIds(appIds);
    }

    /**
     * 删除软件信息
     *
     * @param appId 软件主键
     * @return 结果
     */
    @Override
    public int deleteSysAppByAppId(Long appId) {
        SysApp app = selectSysAppByAppId(appId);
        SysCache.delete(CacheConstants.SYS_APP_KEY + app.getAppKey());
        return sysAppMapper.deleteSysAppByAppId(appId);
    }

    /**
     * 修改软件状态
     *
     * @param app 软件信息
     * @return 结果
     */
    @Override
    public int updateSysAppStatus(SysApp app)
    {
        return updateSysApp(app);
    }

    /**
     * 修改软件计费状态
     *
     * @param app 软件信息
     * @return 结果
     */
    @Override
    public int updateSysAppChargeStatus(SysApp app)
    {
        return updateSysApp(app);
    }

    /**
     * 检查软件名称唯一性
     *
     * @param appName 软件名称
     * @return
     */
    @Override
    public boolean checkAppNameUnique(String appName, Long appId) {
        int count = sysAppMapper.checkAppNameUnique(appName, appId);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置ApiUrl
     *
     * @param app
     */
    public void setApiUrl(SysApp app) {
        HttpServletRequest request = ServletUtils.getRequest();
        String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
        port = pathMapping.contains("dev") ? "" : port;
        String url = "";
        SysConfigWebsite website = sysConfigWebsiteService.getById(1);
        if (website != null && StringUtils.isNotBlank(website.getDomain())) {
            url = website.getDomain();
        } else {
            url = request.getScheme() + "://" + request.getServerName() + port;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        app.setApiUrl(url + pathMapping + "/api/v1/" + app.getAppKey());
    }

    /**
     * 查询软件
     *
     * @param appName 软件名称
     * @return 软件
     */
    @Override
    public SysApp selectSysAppByAppName(String appName) {
        return sysAppMapper.selectSysAppByAppName(appName);
    }
}
