package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysNavigation;
import com.ruoyi.system.mapper.SysNavigationMapper;
import com.ruoyi.system.service.ISysNavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页导航Service业务层处理
 *
 * @author zwgu
 * @date 2022-11-12
 */
@Service
public class SysNavigationServiceImpl implements ISysNavigationService {
    @Autowired
    private SysNavigationMapper sysNavigationMapper;

    /**
     * 查询首页导航
     *
     * @param navId 首页导航主键
     * @return 首页导航
     */
    @Override
    public SysNavigation selectSysNavigationByNavId(Long navId) {
        return sysNavigationMapper.selectSysNavigationByNavId(navId);
    }

    /**
     * 查询首页导航列表
     *
     * @param sysNavigation 首页导航
     * @return 首页导航
     */
    @Override
    public List<SysNavigation> selectSysNavigationList(SysNavigation sysNavigation) {
        return sysNavigationMapper.selectSysNavigationList(sysNavigation);
    }

    /**
     * 新增首页导航
     *
     * @param sysNavigation 首页导航
     * @return 结果
     */
    @Override
    public int insertSysNavigation(SysNavigation sysNavigation) {
        sysNavigation.setCreateTime(DateUtils.getNowDate());
        return sysNavigationMapper.insertSysNavigation(sysNavigation);
    }

    /**
     * 修改首页导航
     *
     * @param sysNavigation 首页导航
     * @return 结果
     */
    @Override
    public int updateSysNavigation(SysNavigation sysNavigation) {
        sysNavigation.setUpdateTime(DateUtils.getNowDate());
        return sysNavigationMapper.updateSysNavigation(sysNavigation);
    }

    /**
     * 批量删除首页导航
     *
     * @param navIds 需要删除的首页导航主键
     * @return 结果
     */
    @Override
    public int deleteSysNavigationByNavIds(Long[] navIds) {
        return sysNavigationMapper.deleteSysNavigationByNavIds(navIds);
    }

    /**
     * 删除首页导航信息
     *
     * @param navId 首页导航主键
     * @return 结果
     */
    @Override
    public int deleteSysNavigationByNavId(Long navId) {
        return sysNavigationMapper.deleteSysNavigationByNavId(navId);
    }
}
