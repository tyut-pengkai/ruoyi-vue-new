package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysAppTrialLogininfor;
import com.ruoyi.system.mapper.SysAppTrialLogininforMapper;
import com.ruoyi.system.service.ISysAppTrialLogininforService;
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
public class SysAppTrialLogininforServiceImpl implements ISysAppTrialLogininforService {
    @Autowired
    private SysAppTrialLogininforMapper sysAppTrialLogininforMapper;

    /**
     * 查询系统访问记录
     *
     * @param infoId 系统访问记录主键
     * @return 系统访问记录
     */
    @Override
    public SysAppTrialLogininfor selectSysAppTrialLogininforByInfoId(Long infoId) {
        return sysAppTrialLogininforMapper.selectSysAppTrialLogininforByInfoId(infoId);
    }

    /**
     * 查询系统访问记录列表
     *
     * @param sysAppTrialLogininfor 系统访问记录
     * @return 系统访问记录
     */
    @Override
    public List<SysAppTrialLogininfor> selectSysAppTrialLogininforList(SysAppTrialLogininfor sysAppTrialLogininfor) {
        return sysAppTrialLogininforMapper.selectSysAppTrialLogininforList(sysAppTrialLogininfor);
    }

    /**
     * 新增系统访问记录
     *
     * @param sysAppTrialLogininfor 系统访问记录
     * @return 结果
     */
    @Override
    public int insertSysAppTrialLogininfor(SysAppTrialLogininfor sysAppTrialLogininfor) {
        sysAppTrialLogininfor.setCreateTime(DateUtils.getNowDate());
        sysAppTrialLogininfor.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysAppTrialLogininforMapper.insertSysAppTrialLogininfor(sysAppTrialLogininfor);
    }

    /**
     * 修改系统访问记录
     *
     * @param sysAppTrialLogininfor 系统访问记录
     * @return 结果
     */
    @Override
    public int updateSysAppTrialLogininfor(SysAppTrialLogininfor sysAppTrialLogininfor) {
        sysAppTrialLogininfor.setUpdateTime(DateUtils.getNowDate());
        sysAppTrialLogininfor.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysAppTrialLogininforMapper.updateSysAppTrialLogininfor(sysAppTrialLogininfor);
    }

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的系统访问记录主键
     * @return 结果
     */
    @Override
    public int deleteSysAppTrialLogininforByInfoIds(Long[] infoIds) {
        return sysAppTrialLogininforMapper.deleteSysAppTrialLogininforByInfoIds(infoIds);
    }

    /**
     * 删除系统访问记录信息
     *
     * @param infoId 系统访问记录主键
     * @return 结果
     */
    @Override
    public int deleteSysAppTrialLogininforByInfoId(Long infoId) {
        return sysAppTrialLogininforMapper.deleteSysAppTrialLogininforByInfoId(infoId);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanTrialLogininfor() {
        sysAppTrialLogininforMapper.cleanTrialLogininfor();
    }
}
