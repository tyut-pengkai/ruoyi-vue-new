package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.CountVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 单码类别Mapper接口
 *
 * @author zwgu
 * @date 2022-01-06
 */
@Repository
public interface SysLoginCodeTemplateMapper {
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
     * 获取某APP下已上架的卡密模板数量
     * @return
     */
    @MapKey("id")
    public Map<Long, CountVo> selectSysLoginCodeTemplateOnSaleCountGroupByAppId();

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
     * 删除单码类别
     *
     * @param templateId 单码类别主键
     * @return 结果
     */
    public int deleteSysLoginCodeTemplateByTemplateId(Long templateId);

    /**
     * 批量删除单码类别
     *
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLoginCodeTemplateByTemplateIds(Long[] templateIds);

    /**
     * 查询卡密模板
     *
     * @param appId        APP主键
     * @param templateName 卡类名称
     * @return 卡密模板
     */
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByAppIdAndTemplateName(@Param("appId") Long appId, @Param("templateName") String templateName);

    public SysLoginCodeTemplate selectSysLoginCodeTemplateByShopUrl(String shopUrl);
}
