package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysAppVersion;

import java.util.List;

/**
 * 软件版本信息Service接口
 *
 * @author zwgu
 * @date 2021-12-19
 */
public interface ISysAppVersionService {
    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    public SysAppVersion selectSysAppVersionByAppVersionId(Long appVersionId);

    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    public SysAppVersion selectSysAppVersionByAppIdAndVersion(Long appId, Long appVersion);

    /**
     * 查询软件版本信息列表
     *
     * @param sysAppVersion 软件版本信息
     * @return 软件版本信息集合
     */
    public List<SysAppVersion> selectSysAppVersionList(SysAppVersion sysAppVersion);

    /**
     * 新增软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    public int insertSysAppVersion(SysAppVersion sysAppVersion);

    /**
     * 修改软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    public int updateSysAppVersion(SysAppVersion sysAppVersion);

    /**
     * 批量删除软件版本信息
     *
     * @param appVersionIds 需要删除的软件版本信息主键集合
     * @return 结果
     */
    public int deleteSysAppVersionByAppVersionIds(Long[] appVersionIds);

    /**
     * 删除软件版本信息信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 结果
     */
    public int deleteSysAppVersionByAppVersionId(Long appVersionId);
}
