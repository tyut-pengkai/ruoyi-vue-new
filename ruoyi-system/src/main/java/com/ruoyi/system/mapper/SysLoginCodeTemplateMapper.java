package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysLoginCodeTemplate;

import java.util.List;

/**
 * 登录码类别Mapper接口
 *
 * @author zwgu
 * @date 2022-01-06
 */
public interface SysLoginCodeTemplateMapper {
    /**
     * 查询登录码类别
     *
     * @param templateId 登录码类别主键
     * @return 登录码类别
     */
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByTemplateId(Long templateId);

    /**
     * 查询登录码类别列表
     *
     * @param sysLoginCodeTemplate 登录码类别
     * @return 登录码类别集合
     */
    public List<SysLoginCodeTemplate> selectSysLoginCodeTemplateList(SysLoginCodeTemplate sysLoginCodeTemplate);

    /**
     * 新增登录码类别
     *
     * @param sysLoginCodeTemplate 登录码类别
     * @return 结果
     */
    public int insertSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate);

    /**
     * 修改登录码类别
     *
     * @param sysLoginCodeTemplate 登录码类别
     * @return 结果
     */
    public int updateSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate);

    /**
     * 删除登录码类别
     *
     * @param templateId 登录码类别主键
     * @return 结果
     */
    public int deleteSysLoginCodeTemplateByTemplateId(Long templateId);

    /**
     * 批量删除登录码类别
     *
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLoginCodeTemplateByTemplateIds(Long[] templateIds);
}
