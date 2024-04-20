package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BatchReplaceVo;

import java.util.List;

/**
 * 单码类别Service接口
 *
 * @author zwgu
 * @date 2022-01-06
 */
public interface ISysLoginCodeTemplateService {
    /**
     * 查询单码类别
     *
     * @param templateId 单码类别主键
     * @return 单码类别
     */
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByTemplateId(Long templateId);

    /**
     * 查询单码类别列表
     *
     * @param sysLoginCodeTemplate 单码类别
     * @return 单码类别集合
     */
    public List<SysLoginCodeTemplate> selectSysLoginCodeTemplateList(SysLoginCodeTemplate sysLoginCodeTemplate);

    /**
     * 新增单码类别
     *
     * @param sysLoginCodeTemplate 单码类别
     * @return 结果
     */
    public int insertSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate);

    /**
     * 修改单码类别
     *
     * @param sysLoginCodeTemplate 单码类别
     * @return 结果
     */
    public int updateSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate);

    /**
     * 批量删除单码类别
     *
     * @param templateIds 需要删除的单码类别主键集合
     * @return 结果
     */
    public int deleteSysLoginCodeTemplateByTemplateIds(Long[] templateIds);

    /**
     * 删除单码类别信息
     *
     * @param templateId 单码类别主键
     * @return 结果
     */
    public int deleteSysLoginCodeTemplateByTemplateId(Long templateId);

    /**
     * 批量新增单码
     *
     * @param sysLoginCodeTemplate
     * @param genQuantity
     * @param remark
     * @return
     */
    public List<SysLoginCode> genSysLoginCodeBatch(SysLoginCodeTemplate sysLoginCodeTemplate, Integer genQuantity, String onSale, String isAgent, String remark);

    /**
     * 查询卡密模板
     *
     * @param appId        APP主键
     * @param templateName 卡类名称
     * @return 卡密模板
     */
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByAppIdAndTemplateName(Long appId, String templateName);

    /**
     * 批量换卡
     *
     * @param template
     * @param loginCode
     * @param vo
     * @param batchNo
     * @return
     */
    public SysLoginCode genSysLoginCodeReplace(SysLoginCodeTemplate loginCodeTpl, SysLoginCode loginCode, BatchReplaceVo vo, String batchNo);
}
