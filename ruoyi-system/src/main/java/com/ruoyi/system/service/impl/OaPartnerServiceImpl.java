package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaPartnerMapper;
import com.ruoyi.system.domain.OaPartner;
import com.ruoyi.system.service.IOaPartnerService;

/**
 * 合作方信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaPartnerServiceImpl implements IOaPartnerService 
{
    @Autowired
    private OaPartnerMapper oaPartnerMapper;

    /**
     * 查询合作方信息
     * 
     * @param id 合作方信息主键
     * @return 合作方信息
     */
    @Override
    public OaPartner selectOaPartnerById(Long id)
    {
        return oaPartnerMapper.selectOaPartnerById(id);
    }

    /**
     * 查询合作方信息列表
     * 
     * @param oaPartner 合作方信息
     * @return 合作方信息
     */
    @Override
    public List<OaPartner> selectOaPartnerList(OaPartner oaPartner)
    {
        return oaPartnerMapper.selectOaPartnerList(oaPartner);
    }

    /**
     * 新增合作方信息
     * 
     * @param oaPartner 合作方信息
     * @return 结果
     */
    @Override
    public int insertOaPartner(OaPartner oaPartner)
    {
        return oaPartnerMapper.insertOaPartner(oaPartner);
    }

    /**
     * 修改合作方信息
     * 
     * @param oaPartner 合作方信息
     * @return 结果
     */
    @Override
    public int updateOaPartner(OaPartner oaPartner)
    {
        return oaPartnerMapper.updateOaPartner(oaPartner);
    }

    /**
     * 批量删除合作方信息
     * 
     * @param ids 需要删除的合作方信息主键
     * @return 结果
     */
    @Override
    public int deleteOaPartnerByIds(Long[] ids)
    {
        return oaPartnerMapper.deleteOaPartnerByIds(ids);
    }

    /**
     * 删除合作方信息信息
     * 
     * @param id 合作方信息主键
     * @return 结果
     */
    @Override
    public int deleteOaPartnerById(Long id)
    {
        return oaPartnerMapper.deleteOaPartnerById(id);
    }
}
