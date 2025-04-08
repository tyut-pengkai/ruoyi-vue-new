package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.vo.CountVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 卡密模板Mapper接口
 *
 * @author zwgu
 * @date 2021-11-28
 */
@Repository
public interface SysCardTemplateMapper
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
     * 删除卡密模板
     *
     * @param templateId 卡密模板主键
     * @return 结果
     */
    public int deleteSysCardTemplateByTemplateId(Long templateId);

    /**
     * 批量删除卡密模板
     *
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCardTemplateByTemplateIds(Long[] templateIds);

    /**
     * 查询卡密模板
     *
     * @param appId        APP主键
     * @param templateName 卡类名称
     * @return 卡密模板
     */
    public SysCardTemplate selectSysCardTemplateByAppIdAndTemplateName(@Param("appId") Long appId, @Param("templateName") String templateName);

    /**
     * 获取某APP下已上架的卡密模板数量
     * @return
     */
    @MapKey("id")
    public Map<Long, CountVo> selectSysCardTemplateOnSaleCountGroupByAppId();

    public SysCardTemplate selectSysCardTemplateByShopUrl(String shopUrl);
}