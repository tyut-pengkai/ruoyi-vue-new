package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysAppVersionMapper;
import com.ruoyi.system.service.ISysAppVersionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 软件版本信息Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-19
 */
@Service
public class SysAppVersionServiceImpl implements ISysAppVersionService {

    @Resource
    private SysAppVersionMapper sysAppVersionMapper;

    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    @Override
    public SysAppVersion selectSysAppVersionByAppVersionId(Long appVersionId) {
        return sysAppVersionMapper.selectSysAppVersionByAppVersionId(appVersionId);
    }

    /**
     * 查询软件版本信息
     *
     * @param appId
     * @param appVersion
     * @return 软件版本信息
     */
    @Override
    public SysAppVersion selectSysAppVersionByAppIdAndVersion(Long appId, Long appVersion) {
        return sysAppVersionMapper.selectSysAppVersionByAppIdAndVersion(appId, appVersion);
    }

    /**
     * 查询软件版本信息列表
     *
     * @param sysAppVersion 软件版本信息
     * @return 软件版本信息
     */
    @Override
    public List<SysAppVersion> selectSysAppVersionList(SysAppVersion sysAppVersion) {
        return sysAppVersionMapper.selectSysAppVersionList(sysAppVersion);
    }

    /**
     * 新增软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    @Override
    public int insertSysAppVersion(SysAppVersion sysAppVersion) {
        sysAppVersion.setCreateTime(DateUtils.getNowDate());
        return sysAppVersionMapper.insertSysAppVersion(sysAppVersion);
    }

    /**
     * 修改软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    @Override
    public int updateSysAppVersion(SysAppVersion sysAppVersion) {
        sysAppVersion.setUpdateTime(DateUtils.getNowDate());
        return sysAppVersionMapper.updateSysAppVersion(sysAppVersion);
    }

    /**
     * 批量删除软件版本信息
     *
     * @param appVersionIds 需要删除的软件版本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppVersionByAppVersionIds(Long[] appVersionIds) {
        return sysAppVersionMapper.deleteSysAppVersionByAppVersionIds(appVersionIds);
    }

    /**
     * 删除软件版本信息信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppVersionByAppVersionId(Long appVersionId) {
        return sysAppVersionMapper.deleteSysAppVersionByAppVersionId(appVersionId);
    }

    /**
     * @param appId APP ID
     * @return 最新版本信息
     */
    @Override
    public SysAppVersion selectLatestVersionByAppId(Long appId) {
        return sysAppVersionMapper.selectLatestVersionByAppId(appId);
    }

    /**
     * @param appId APP ID
     * @return 强制更新到的最新版本信息
     */
    @Override
    public SysAppVersion selectLatestVersionForceUpdateByAppId(Long appId) {
        return sysAppVersionMapper.selectLatestVersionForceUpdateByAppId(appId);
    }
}
