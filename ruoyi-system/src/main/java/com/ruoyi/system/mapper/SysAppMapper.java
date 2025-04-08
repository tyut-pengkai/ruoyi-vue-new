package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysApp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 软件Mapper接口
 *
 * @author zwgu
 * @date 2021-11-05
 */
@Repository
public interface SysAppMapper
{
    /**
     * 查询软件
     *
     * @param appId 软件主键
     * @return 软件
     */
    public SysApp selectSysAppByAppId(Long appId);

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
     * 删除软件
     *
     * @param appId 软件主键
     * @return 结果
     */
    public int deleteSysAppByAppId(Long appId);

    /**
     * 批量删除软件
     *
     * @param appIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppByAppIds(Long[] appIds);

    /**
     * 检查软件名称唯一性
     *
     * @param appName 软件名称
     * @return
     */
    public int checkAppNameUnique(@Param("appName") String appName, @Param("appId") Long appId);

    /**
     * 查询软件
     *
     * @param appKey 软件AppKey
     * @return 软件
     */
    public SysApp selectSysAppByAppKey(String appKey);

    /**
     * 查询软件
     *
     * @param appName 软件名称
     * @return 软件
     */
    public SysApp selectSysAppByAppName(String appName);

    public SysApp selectSysAppByShopUrl(String shopUrl);
}