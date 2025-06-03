package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaPartner;

/**
 * 合作方信息Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaPartnerService 
{
    /**
     * 查询合作方信息
     * 
     * @param id 合作方信息主键
     * @return 合作方信息
     */
    public OaPartner selectOaPartnerById(Long id);

    /**
     * 查询合作方信息列表
     * 
     * @param oaPartner 合作方信息
     * @return 合作方信息集合
     */
    public List<OaPartner> selectOaPartnerList(OaPartner oaPartner);

    /**
     * 新增合作方信息
     * 
     * @param oaPartner 合作方信息
     * @return 结果
     */
    public int insertOaPartner(OaPartner oaPartner);

    /**
     * 修改合作方信息
     * 
     * @param oaPartner 合作方信息
     * @return 结果
     */
    public int updateOaPartner(OaPartner oaPartner);

    /**
     * 批量删除合作方信息
     * 
     * @param ids 需要删除的合作方信息主键集合
     * @return 结果
     */
    public int deleteOaPartnerByIds(Long[] ids);

    /**
     * 删除合作方信息信息
     * 
     * @param id 合作方信息主键
     * @return 结果
     */
    public int deleteOaPartnerById(Long id);
}
