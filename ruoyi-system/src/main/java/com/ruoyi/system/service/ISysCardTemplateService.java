package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.vo.BatchReplaceVo;

import java.util.List;

/**
 * 卡密模板Service接口
 *
 * @author zwgu
 * @date 2021-11-28
 */
public interface ISysCardTemplateService
{
    /**
     * 查询卡密模板
     *
     * @param templateId 卡密模板主键
     * @return 卡密模板
     */
    public SysCardTemplate selectSysCardTemplateByTemplateId(Long templateId);

    /**
     * 查询卡密模板列表
     *
     * @param sysCardTemplate 卡密模板
     * @return 卡密模板集合
     */
    public List<SysCardTemplate> selectSysCardTemplateList(SysCardTemplate sysCardTemplate);

    /**
     * 新增卡密模板
     *
     * @param sysCardTemplate 卡密模板
     * @return 结果
     */
    public int insertSysCardTemplate(SysCardTemplate sysCardTemplate);

    /**
     * 修改卡密模板
     *
     * @param sysCardTemplate 卡密模板
     * @return 结果
     */
    public int updateSysCardTemplate(SysCardTemplate sysCardTemplate);

    /**
     * 批量删除卡密模板
     *
     * @param templateIds 需要删除的卡密模板主键集合
     * @return 结果
     */
    public int deleteSysCardTemplateByTemplateIds(Long[] templateIds);

    /**
     * 删除卡密模板信息
     *
     * @param templateId 卡密模板主键
     * @return 结果
     */
    public int deleteSysCardTemplateByTemplateId(Long templateId);

    /**
     * 批量新增卡密
     *
     * @param sysCardTemplate
     * @param genQuantity
     * @param remark
     * @return
     */
    public List<SysCard> genSysCardBatch(SysCardTemplate sysCardTemplate, Integer genQuantity, String onSale, String isAgent, String remark);

    /**
     * 查询卡密模板
     *
     * @param appId        APP主键
     * @param templateName 卡类名称
     * @return 卡密模板
     */
    public SysCardTemplate selectSysCardTemplateByAppIdAndTemplateName(Long appId, String templateName);

    /**
     * 批量换卡
     * @param template
     * @param card
     * @param vo
     * @param batchNo
     * @return
     */
    public SysCard genCardReplace(SysCardTemplate template, SysCard card, BatchReplaceVo vo, String batchNo);
}