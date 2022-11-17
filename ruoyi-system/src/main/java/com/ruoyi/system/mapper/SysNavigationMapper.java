package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysNavigation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 首页导航Mapper接口
 *
 * @author zwgu
 * @date 2022-11-12
 */
@Repository
public interface SysNavigationMapper {
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
     * 删除首页导航
     *
     * @param navId 首页导航主键
     * @return 结果
     */
    public int deleteSysNavigationByNavId(Long navId);

    /**
     * 批量删除首页导航
     *
     * @param navIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNavigationByNavIds(Long[] navIds);
}
