package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysAppLogininfor;
import com.ruoyi.system.mapper.SysAppLogininforMapper;
import com.ruoyi.system.service.ISysAppLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问记录Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-29
 */
@Service
public class SysAppLogininforServiceImpl implements ISysAppLogininforService {
    @Autowired
    private SysAppLogininforMapper sysAppLogininforMapper;

    /**
     * 查询系统访问记录
     *
     * @param infoId 系统访问记录主键
     * @return 系统访问记录
     */
    @Override
    public SysAppLogininfor selectSysAppLogininforByInfoId(Long infoId) {
        return sysAppLogininforMapper.selectSysAppLogininforByInfoId(infoId);
    }

    /**
     * 查询系统访问记录列表
     *
     * @param sysAppLogininfor 系统访问记录
     * @return 系统访问记录
     */
    @Override
    public List<SysAppLogininfor> selectSysAppLogininforList(SysAppLogininfor sysAppLogininfor) {
        return sysAppLogininforMapper.selectSysAppLogininforList(sysAppLogininfor);
    }

    /**
     * 新增系统访问记录
     *
     * @param sysAppLogininfor 系统访问记录
     * @return 结果
     */
    @Override
    public int insertSysAppLogininfor(SysAppLogininfor sysAppLogininfor) {
        sysAppLogininfor.setCreateTime(DateUtils.getNowDate());
        sysAppLogininfor.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysAppLogininforMapper.insertSysAppLogininfor(sysAppLogininfor);
    }

    /**
     * 修改系统访问记录
     *
     * @param sysAppLogininfor 系统访问记录
     * @return 结果
     */
    @Override
    public int updateSysAppLogininfor(SysAppLogininfor sysAppLogininfor) {
        sysAppLogininfor.setUpdateTime(DateUtils.getNowDate());
        sysAppLogininfor.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysAppLogininforMapper.updateSysAppLogininfor(sysAppLogininfor);
    }

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的系统访问记录主键
     * @return 结果
     */
    @Override
    public int deleteSysAppLogininforByInfoIds(Long[] infoIds) {
        return sysAppLogininforMapper.deleteSysAppLogininforByInfoIds(infoIds);
    }

    /**
     * 删除系统访问记录信息
     *
     * @param infoId 系统访问记录主键
     * @return 结果
     */
    @Override
    public int deleteSysAppLogininforByInfoId(Long infoId) {
        return sysAppLogininforMapper.deleteSysAppLogininforByInfoId(infoId);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        sysAppLogininforMapper.cleanLogininfor();
    }
}
