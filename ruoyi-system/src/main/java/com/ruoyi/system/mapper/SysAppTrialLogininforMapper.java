package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysAppTrialLogininfor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统访问记录Mapper接口
 *
 * @author zwgu
 * @date 2021-12-29
 */
@Repository
public interface SysAppTrialLogininforMapper {
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
     * @param sysAppLogininfor 系统访问记录
     * @return 结果
     */
    public int updateSysAppTrialLogininfor(SysAppTrialLogininfor sysAppTrialLogininfor);

    /**
     * 删除系统访问记录
     *
     * @param infoId 系统访问记录主键
     * @return 结果
     */
    public int deleteSysAppTrialLogininforByInfoId(Long infoId);

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppTrialLogininforByInfoIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    public int cleanTrialLogininfor();
}
