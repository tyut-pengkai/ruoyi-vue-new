package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.domain.SysApp;
import com.ruoyi.system.service.ISysAppService;

import javax.servlet.http.HttpServletRequest;

/**
 * 软件Service业务层处理
 *
 * @author zwgu
 * @date 2021-11-05
 */
@Service
public class SysAppServiceImpl implements ISysAppService
{
    @Autowired
    private SysAppMapper sysAppMapper;

    /**
     * 查询软件
     *
     * @param appId 软件主键
     * @return 软件
     */
    @Override
    public SysApp selectSysAppByAppId(Long appId)
    {
        return sysAppMapper.selectSysAppByAppId(appId);
    }

    /**
     * 查询软件列表
     *
     * @param sysApp 软件
     * @return 软件
     */
    @Override
    public List<SysApp> selectSysAppList(SysApp sysApp)
    {
        return sysAppMapper.selectSysAppList(sysApp);
    }

    /**
     * 新增软件
     *
     * @param sysApp 软件
     * @return 结果
     */
    @Override
    public int insertSysApp(SysApp sysApp)
    {
        sysApp.setCreateBy(SecurityUtils.getUsername());
        sysApp.setCreateTime(DateUtils.getNowDate());
        sysApp.setAppKey(RandomStringUtils.randomAlphanumeric(32));
        sysApp.setAppSecret(RandomStringUtils.randomAlphanumeric(32));
        HttpServletRequest request = ServletUtils.getRequest();
        sysApp.setApiUrl(request.getScheme() + "://" + request.getServerName()
                + ("80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort())
                + "/api?appid=" + sysApp.getAppKey());
        sysApp.setDelFlag("0");
        return sysAppMapper.insertSysApp(sysApp);
    }

    /**
     * 修改软件
     *
     * @param sysApp 软件
     * @return 结果
     */
    @Override
    public int updateSysApp(SysApp sysApp)
    {
        sysApp.setUpdateTime(DateUtils.getNowDate());
        return sysAppMapper.updateSysApp(sysApp);
    }

    /**
     * 批量删除软件
     *
     * @param appIds 需要删除的软件主键
     * @return 结果
     */
    @Override
    public int deleteSysAppByAppIds(Long[] appIds)
    {
        return sysAppMapper.deleteSysAppByAppIds(appIds);
    }

    /**
     * 删除软件信息
     *
     * @param appId 软件主键
     * @return 结果
     */
    @Override
    public int deleteSysAppByAppId(Long appId)
    {
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
        return sysAppMapper.updateSysApp(app);
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
        return sysAppMapper.updateSysApp(app);
    }
}