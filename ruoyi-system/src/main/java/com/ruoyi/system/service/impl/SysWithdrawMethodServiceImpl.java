package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysWithdrawMethod;
import com.ruoyi.system.mapper.SysWithdrawMethodMapper;
import com.ruoyi.system.service.ISysWithdrawMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收款方式Service业务层处理
 *
 * @author zwgu
 * @date 2024-06-03
 */
@Service
public class SysWithdrawMethodServiceImpl implements ISysWithdrawMethodService {
    @Autowired
    private SysWithdrawMethodMapper sysWithdrawMethodMapper;

    /**
     * 查询收款方式
     *
     * @param id 收款方式主键
     * @return 收款方式
     */
    @Override
    public SysWithdrawMethod selectSysWithdrawMethodById(Long id) {
        return sysWithdrawMethodMapper.selectSysWithdrawMethodById(id);
    }

    /**
     * 查询收款方式列表
     *
     * @param sysWithdrawMethod 收款方式
     * @return 收款方式
     */
    @Override
    public List<SysWithdrawMethod> selectSysWithdrawMethodList(SysWithdrawMethod sysWithdrawMethod) {
        return sysWithdrawMethodMapper.selectSysWithdrawMethodList(sysWithdrawMethod);
    }

    /**
     * 新增收款方式
     *
     * @param sysWithdrawMethod 收款方式
     * @return 结果
     */
    @Override
    public int insertSysWithdrawMethod(SysWithdrawMethod sysWithdrawMethod) {
        sysWithdrawMethod.setCreateTime(DateUtils.getNowDate());
        sysWithdrawMethod.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysWithdrawMethodMapper.insertSysWithdrawMethod(sysWithdrawMethod);
    }

    /**
     * 修改收款方式
     *
     * @param sysWithdrawMethod 收款方式
     * @return 结果
     */
    @Override
    public int updateSysWithdrawMethod(SysWithdrawMethod sysWithdrawMethod) {
        sysWithdrawMethod.setUpdateTime(DateUtils.getNowDate());
        sysWithdrawMethod.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysWithdrawMethodMapper.updateSysWithdrawMethod(sysWithdrawMethod);
    }

    /**
     * 批量删除收款方式
     *
     * @param ids 需要删除的收款方式主键
     * @return 结果
     */
    @Override
    public int deleteSysWithdrawMethodByIds(Long[] ids) {
        return sysWithdrawMethodMapper.deleteSysWithdrawMethodByIds(ids);
    }

    /**
     * 删除收款方式信息
     *
     * @param id 收款方式主键
     * @return 结果
     */
    @Override
    public int deleteSysWithdrawMethodById(Long id) {
        return sysWithdrawMethodMapper.deleteSysWithdrawMethodById(id);
    }
}
