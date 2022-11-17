package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysNavigation;

import java.util.List;

/**
 * 首页导航Service接口
 *
 * @author zwgu
 * @date 2022-11-12
 */
public interface ISysNavigationService {
    /**
     * 查询首页导航
     *
     * @param navId 首页导航主键
     * @return 首页导航
     */
    public SysNavigation selectSysNavigationByNavId(Long navId);

    /**
     * 查询首页导航列表
     *
     * @param sysNavigation 首页导航
     * @return 首页导航集合
     */
    public List<SysNavigation> selectSysNavigationList(SysNavigation sysNavigation);

    /**
     * 新增首页导航
     *
     * @param sysNavigation 首页导航
     * @return 结果
     */
    public int insertSysNavigation(SysNavigation sysNavigation);

    /**
     * 修改首页导航
     *
     * @param sysNavigation 首页导航
     * @return 结果
     */
    public int updateSysNavigation(SysNavigation sysNavigation);

    /**
     * 批量删除首页导航
     *
     * @param navIds 需要删除的首页导航主键集合
     * @return 结果
     */
    public int deleteSysNavigationByNavIds(Long[] navIds);

    /**
     * 删除首页导航信息
     *
     * @param navId 首页导航主键
     * @return 结果
     */
    public int deleteSysNavigationByNavId(Long navId);
}
