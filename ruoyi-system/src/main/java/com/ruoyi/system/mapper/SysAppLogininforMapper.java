package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysAppLogininfor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统访问记录Mapper接口
 *
 * @author zwgu
 * @date 2021-12-29
 */
@Repository
public interface SysAppLogininforMapper {
    /**
     * 查询系统访问记录
     *
     * @param infoId 系统访问记录主键
     * @return 系统访问记录
     */
    public SysAppLogininfor selectSysAppLogininforByInfoId(Long infoId);

    /**
     * 查询系统访问记录列表
     *
     * @param sysAppLogininfor 系统访问记录
     * @return 系统访问记录集合
     */
    public List<SysAppLogininfor> selectSysAppLogininforList(SysAppLogininfor sysAppLogininfor);

    /**
     * 新增系统访问记录
     *
     * @param sysAppLogininfor 系统访问记录
     * @return 结果
     */
    public int insertSysAppLogininfor(SysAppLogininfor sysAppLogininfor);

    /**
     * 修改系统访问记录
     *
     * @param sysAppLogininfor 系统访问记录
     * @return 结果
     */
    public int updateSysAppLogininfor(SysAppLogininfor sysAppLogininfor);

    /**
     * 删除系统访问记录
     *
     * @param infoId 系统访问记录主键
     * @return 结果
     */
    public int deleteSysAppLogininforByInfoId(Long infoId);

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppLogininforByInfoIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    public int cleanLogininfor();
}
