package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysApp;

import java.util.List;

/**
 * 软件Service接口
 *
 * @author zwgu
 * @date 2021-11-05
 */
public interface ISysAppService {
    /**
     * 查询软件
     *
     * @param appId 软件主键
     * @return 软件
     */
    public SysApp selectSysAppByAppId(Long appId);

    /**
     * 查询软件
     *
     * @param appKey 软件AppKey
     * @return 软件
     */
    public SysApp selectSysAppByAppKey(String appKey);

    /**
     * 查询软件列表
     *
     * @param sysApp 软件
     * @return 软件集合
     */
    public List<SysApp> selectSysAppList(SysApp sysApp);

    /**
     * 新增软件
     *
     * @param sysApp 软件
     * @return 结果
     */
    public int insertSysApp(SysApp sysApp);

    /**
     * 修改软件
     *
     * @param sysApp 软件
     * @return 结果
     */
    public int updateSysApp(SysApp sysApp);

    /**
     * 批量删除软件
     *
     * @param appIds 需要删除的软件主键集合
     * @return 结果
     */
    public int deleteSysAppByAppIds(Long[] appIds);

    /**
     * 删除软件信息
     *
     * @param appId 软件主键
     * @return 结果
     */
    public int deleteSysAppByAppId(Long appId);

    /**
     * 修改软件状态
     *
     * @param app 软件信息
     * @return 结果
     */
    int updateSysAppStatus(SysApp app);

    /**
     * 修改软件计费状态
     *
     * @param app 软件信息
     * @return 结果
     */
    int updateSysAppChargeStatus(SysApp app);

    /**
     * 检查软件名称唯一性
     *
     * @param appName
     * @return
     */
    String checkAppNameUnique(String appName, Long appId);

    /**
     * 设置ApiUrl
     *
     * @param app
     */
    public void setApiUrl(SysApp app);

    /**
     * 查询软件
     *
     * @param appName 软件名称
     * @return 软件
     */
    public SysApp selectSysAppByAppName(String appName);
}