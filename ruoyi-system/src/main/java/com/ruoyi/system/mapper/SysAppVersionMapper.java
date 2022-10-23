package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysAppVersion;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 软件版本信息Mapper接口
 *
 * @author zwgu
 * @date 2021-12-19
 */
@Repository
public interface SysAppVersionMapper {
    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    public SysAppVersion selectSysAppVersionByAppVersionId(Long appVersionId);

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
     * 删除软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 结果
     */
    public int deleteSysAppVersionByAppVersionId(Long appVersionId);

    /**
     * 批量删除软件版本信息
     *
     * @param appVersionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppVersionByAppVersionIds(Long[] appVersionIds);

    /**
     * 查询软件版本信息
     *
     * @param appId
     * @param appVersion
     * @return 软件版本信息
     */
    public SysAppVersion selectSysAppVersionByAppIdAndVersion(@Param("appId") Long appId, @Param("appVersion") Long appVersion);

    /**
     * @param appId APP ID
     * @return 最新版本信息
     */
    public SysAppVersion selectLatestVersionByAppId(Long appId);

    /**
     * @param appId APP ID
     * @return 强制更新到的最新版本信息
     */
    public SysAppVersion selectLatestVersionForceUpdateByAppId(Long appId);

    /**
     * 检查软件名称唯一性
     *
     * @return
     */
    public int checkAppVersionNoUnique(@Param("versionNo") Long versionNo, @Param("appId") Long appId, @Param("appVersionId") Long appVersionId);
}
