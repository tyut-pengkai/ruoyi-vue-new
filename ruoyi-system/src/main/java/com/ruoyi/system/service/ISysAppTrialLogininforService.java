package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysAppTrialLogininfor;

import java.util.List;

/**
 * 系统访问记录Service接口
 *
 * @author zwgu
 * @date 2021-12-29
 */
public interface ISysAppTrialLogininforService {
    /**
     * 查询系统访问记录
     *
     * @param infoId 系统访问记录主键
     * @return 系统访问记录
     */
    public SysAppTrialLogininfor selectSysAppTrialLogininforByInfoId(Long infoId);

    /**
     * 查询系统访问记录列表
     *
     * @param sysAppTrialLogininfor 系统访问记录
     * @return 系统访问记录集合
     */
    public List<SysAppTrialLogininfor> selectSysAppTrialLogininforList(SysAppTrialLogininfor sysAppTrialLogininfor);

    /**
     * 新增系统访问记录
     *
     * @param sysAppTrialLogininfor 系统访问记录
     * @return 结果
     */
    public int insertSysAppTrialLogininfor(SysAppTrialLogininfor sysAppTrialLogininfor);

    /**
     * 修改系统访问记录
     *
     * @param sysAppTrialLogininfor 系统访问记录
     * @return 结果
     */
    public int updateSysAppTrialLogininfor(SysAppTrialLogininfor sysAppTrialLogininfor);

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的系统访问记录主键集合
     * @return 结果
     */
    public int deleteSysAppTrialLogininforByInfoIds(Long[] infoIds);

    /**
     * 删除系统访问记录信息
     *
     * @param infoId 系统访问记录主键
     * @return 结果
     */
    public int deleteSysAppTrialLogininforByInfoId(Long infoId);

    /**
     * 清空系统登录日志
     */
    public void cleanTrialLogininfor();
}
