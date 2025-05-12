package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.dto.DeliveryDetailRequirementDto;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.domain.WmsLkDeliveryDetailRequirement;
import com.easycode.cloud.mapper.WmsLkDeliveryDetailRequirementMapper;
import com.easycode.cloud.service.IDeliveryDetailRequirementService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 送货单明细-送货要求关系Service业务层处理
 *
 * @author fsc
 * @date 2023-05-10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryDetailRequirementServiceImpl implements IDeliveryDetailRequirementService
{
    @Autowired
    private WmsLkDeliveryDetailRequirementMapper wmsLkDeliveryDetailRequirementMapper;

    /**
     * 查询送货单明细-送货要求关系
     *
     * @param id 送货单明细-送货要求关系主键
     * @return 送货单明细-送货要求关系
     */
    @Override
    public WmsLkDeliveryDetailRequirement selectWmsLkDeliveryDetailRequirementById(Long id)
    {
        return wmsLkDeliveryDetailRequirementMapper.selectWmsLkDeliveryDetailRequirementById(id);
    }

    /**
     * 查询送货单明细-送货要求关系列表
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 送货单明细-送货要求关系
     */
    @Override
    public List<WmsLkDeliveryDetailRequirement> selectWmsLkDeliveryDetailRequirementList(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement)
    {
        return wmsLkDeliveryDetailRequirementMapper.selectWmsLkDeliveryDetailRequirementList(wmsLkDeliveryDetailRequirement);
    }

    /**
     * 新增送货单明细-送货要求关系
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 结果
     */
    @Override
    public int insertWmsLkDeliveryDetailRequirement(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement)
    {
        wmsLkDeliveryDetailRequirement.setCreateTime(DateUtils.getNowDate());
        return wmsLkDeliveryDetailRequirementMapper.insertWmsLkDeliveryDetailRequirement(wmsLkDeliveryDetailRequirement);
    }

    /**
     * 批量新增送货单明细-送货要求关系
     *
     * @param deliveryDetailRequirementDto 送货单明细-送货要求关系
     * @return 结果
     */
    @Override
    public int batchAdd(DeliveryDetailRequirementDto deliveryDetailRequirementDto)
    {
        List<WmsLkDeliveryDetailRequirement> requirementList = deliveryDetailRequirementDto.getRequirementList();
        for (WmsLkDeliveryDetailRequirement requirement : requirementList){
            wmsLkDeliveryDetailRequirementMapper.insertWmsLkDeliveryDetailRequirement(requirement);
        }
        return HttpStatus.SC_OK;
    }


    /**
     * 批量新增送货单明细-送货要求关系
     *
     * @param list 送货要求列表
     * @param detailId 送货单明细id
     * @return 结果
     */
    @Override
    public int batchAdd(List<WmsLkDeliveryDetailRequirement> list, Long detailId)
    {
        list = Optional.ofNullable(list).orElse(new ArrayList<WmsLkDeliveryDetailRequirement>());
        for (WmsLkDeliveryDetailRequirement requirement : list){
            requirement.setDeliveryDetailId(detailId);
        }
        wmsLkDeliveryDetailRequirementMapper.batchInsertRequirement(list);
        return HttpStatus.SC_OK;
    }

    @Override
    public int batchAdd(List<WmsLkDeliveryDetailRequirement> list, Long detailId, Long tenantId)
    {
        list = Optional.ofNullable(list).orElse(new ArrayList<WmsLkDeliveryDetailRequirement>());
        for (WmsLkDeliveryDetailRequirement requirement : list){
            requirement.setDeliveryDetailId(detailId);
            requirement.setTenantId(tenantId);
        }
        wmsLkDeliveryDetailRequirementMapper.batchInsertRequirement(list);
        return HttpStatus.SC_OK;
    }

    /**
     * 修改送货单明细-送货要求关系
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 结果
     */
    @Override
    public int updateWmsLkDeliveryDetailRequirement(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement)
    {
        wmsLkDeliveryDetailRequirement.setUpdateTime(DateUtils.getNowDate());
        return wmsLkDeliveryDetailRequirementMapper.updateWmsLkDeliveryDetailRequirement(wmsLkDeliveryDetailRequirement);
    }

    /**
     * 批量删除送货单明细-送货要求关系
     *
     * @param ids 需要删除的送货单明细-送货要求关系主键
     * @return 结果
     */
    @Override
    public int deleteWmsLkDeliveryDetailRequirementByIds(Long[] ids)
    {
        return wmsLkDeliveryDetailRequirementMapper.deleteWmsLkDeliveryDetailRequirementByIds(ids);
    }

    /**
     * 批量删除送货单明细-送货要求关系 根据句送货单明细id
     *
     * @param detailId 送货单明细Id
     * @return 结果
     */
    @Override
    public int deleteRequirementByDetailId(Long detailId) {
        return wmsLkDeliveryDetailRequirementMapper.deleteRequirementByDetailId(detailId);
    }

    /**
     * 删除送货单明细-送货要求关系信息
     *
     * @param id 送货单明细-送货要求关系主键
     * @return 结果
     */
    @Override
    public int deleteWmsLkDeliveryDetailRequirementById(Long id)
    {
        return wmsLkDeliveryDetailRequirementMapper.deleteWmsLkDeliveryDetailRequirementById(id);
    }
}
