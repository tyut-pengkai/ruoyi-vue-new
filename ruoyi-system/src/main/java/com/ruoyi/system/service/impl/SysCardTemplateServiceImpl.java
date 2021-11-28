package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.system.service.ISysCardTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卡密模板Service业务层处理
 *
 * @author zwgu
 * @date 2021-11-28
 */
@Service
public class SysCardTemplateServiceImpl implements ISysCardTemplateService
{
    @Autowired
    private SysCardTemplateMapper sysCardTemplateMapper;

    /**
     * 查询卡密模板
     *
     * @param templateId 卡密模板主键
     * @return 卡密模板
     */
    @Override
    public SysCardTemplate selectSysCardTemplateByTemplateId(Long templateId)
    {
        return sysCardTemplateMapper.selectSysCardTemplateByTemplateId(templateId);
    }

    /**
     * 查询卡密模板列表
     *
     * @param sysCardTemplate 卡密模板
     * @return 卡密模板
     */
    @Override
    public List<SysCardTemplate> selectSysCardTemplateList(SysCardTemplate sysCardTemplate)
    {
        return sysCardTemplateMapper.selectSysCardTemplateList(sysCardTemplate);
    }

    /**
     * 新增卡密模板
     *
     * @param sysCardTemplate 卡密模板
     * @return 结果
     */
    @Override
    public int insertSysCardTemplate(SysCardTemplate sysCardTemplate)
    {
        sysCardTemplate.setCreateTime(DateUtils.getNowDate());
        return sysCardTemplateMapper.insertSysCardTemplate(sysCardTemplate);
    }

    /**
     * 修改卡密模板
     *
     * @param sysCardTemplate 卡密模板
     * @return 结果
     */
    @Override
    public int updateSysCardTemplate(SysCardTemplate sysCardTemplate)
    {
        sysCardTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysCardTemplateMapper.updateSysCardTemplate(sysCardTemplate);
    }

    /**
     * 批量删除卡密模板
     *
     * @param templateIds 需要删除的卡密模板主键
     * @return 结果
     */
    @Override
    public int deleteSysCardTemplateByTemplateIds(Long[] templateIds)
    {
        return sysCardTemplateMapper.deleteSysCardTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除卡密模板信息
     *
     * @param templateId 卡密模板主键
     * @return 结果
     */
    @Override
    public int deleteSysCardTemplateByTemplateId(Long templateId)
    {
        return sysCardTemplateMapper.deleteSysCardTemplateByTemplateId(templateId);
    }
}