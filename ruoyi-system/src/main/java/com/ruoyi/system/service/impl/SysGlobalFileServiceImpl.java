package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysGlobalFile;
import com.ruoyi.system.mapper.SysGlobalFileMapper;
import com.ruoyi.system.service.ISysGlobalFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 远程文件Service业务层处理
 *
 * @author zwgu
 * @date 2022-09-30
 */
@Service
public class SysGlobalFileServiceImpl implements ISysGlobalFileService {
    @Autowired
    private SysGlobalFileMapper sysGlobalFileMapper;

    /**
     * 查询远程文件
     *
     * @param id 远程文件主键
     * @return 远程文件
     */
    @Override
    public SysGlobalFile selectSysGlobalFileById(Long id) {
        return sysGlobalFileMapper.selectSysGlobalFileById(id);
    }

    /**
     * 查询全局变量
     *
     * @param name 全局变量主键
     * @return 全局变量
     */
    public SysGlobalFile selectSysGlobalFileByName(String name) {
        return sysGlobalFileMapper.selectSysGlobalFileByName(name);
    }

    /**
     * 查询远程文件列表
     *
     * @param sysGlobalFile 远程文件
     * @return 远程文件
     */
    @Override
    public List<SysGlobalFile> selectSysGlobalFileList(SysGlobalFile sysGlobalFile) {
        return sysGlobalFileMapper.selectSysGlobalFileList(sysGlobalFile);
    }

    /**
     * 新增远程文件
     *
     * @param sysGlobalFile 远程文件
     * @return 结果
     */
    @Override
    public int insertSysGlobalFile(SysGlobalFile sysGlobalFile) {
        sysGlobalFile.setCreateTime(DateUtils.getNowDate());
        sysGlobalFile.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysGlobalFileMapper.insertSysGlobalFile(sysGlobalFile);
    }

    /**
     * 修改远程文件
     *
     * @param sysGlobalFile 远程文件
     * @return 结果
     */
    @Override
    public int updateSysGlobalFile(SysGlobalFile sysGlobalFile) {
        sysGlobalFile.setUpdateTime(DateUtils.getNowDate());
        sysGlobalFile.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysGlobalFileMapper.updateSysGlobalFile(sysGlobalFile);
    }

    /**
     * 批量删除远程文件
     *
     * @param ids 需要删除的远程文件主键
     * @return 结果
     */
    @Override
    public int deleteSysGlobalFileByIds(Long[] ids) {
        return sysGlobalFileMapper.deleteSysGlobalFileByIds(ids);
    }

    /**
     * 删除远程文件信息
     *
     * @param id 远程文件主键
     * @return 结果
     */
    @Override
    public int deleteSysGlobalFileById(Long id) {
        return sysGlobalFileMapper.deleteSysGlobalFileById(id);
    }
}
