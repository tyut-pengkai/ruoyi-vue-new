package com.easycode.cloud.service;

import com.easycode.cloud.domain.dto.DeliveryDetailRequirementDto;
import com.weifu.cloud.domain.WmsLkDeliveryDetailRequirement;

import java.util.List;

/**
 * 送货单明细-送货要求关系Service接口
 *
 * @author fsc
 * @date 2023-05-10
 */
public interface IDeliveryDetailRequirementService
{
    /**
     * 查询送货单明细-送货要求关系
     *
     * @param id 送货单明细-送货要求关系主键
     * @return 送货单明细-送货要求关系
     */
    public WmsLkDeliveryDetailRequirement selectWmsLkDeliveryDetailRequirementById(Long id);

    /**
     * 查询送货单明细-送货要求关系列表
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 送货单明细-送货要求关系集合
     */
    public List<WmsLkDeliveryDetailRequirement> selectWmsLkDeliveryDetailRequirementList(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement);

    /**
     * 新增送货单明细-送货要求关系
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 结果
     */
    public int insertWmsLkDeliveryDetailRequirement(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement);

    /**
     * 批量新增送货单明细-送货要求关系
     *
     * @param deliveryDetailRequirementDto 送货单明细-送货要求关系
     * @return 结果
     */
    public int batchAdd(DeliveryDetailRequirementDto deliveryDetailRequirementDto);

    int batchAdd(List<WmsLkDeliveryDetailRequirement> list, Long detailId);

    int batchAdd(List<WmsLkDeliveryDetailRequirement> list, Long detailId, Long tenderId);

    /**
     * 修改送货单明细-送货要求关系
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 结果
     */
    public int updateWmsLkDeliveryDetailRequirement(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement);

    /**
     * 批量删除送货单明细-送货要求关系
     *
     * @param ids 需要删除的送货单明细-送货要求关系主键集合
     * @return 结果
     */
    public int deleteWmsLkDeliveryDetailRequirementByIds(Long[] ids);

    /**
     * 批量删除送货单明细-送货要求关系 根据句送货单明细id
     *
     * @param detailId 送货单明细Id
     * @return 结果
     */
    public int deleteRequirementByDetailId(Long detailId);

    /**
     * 删除送货单明细-送货要求关系信息
     *
     * @param id 送货单明细-送货要求关系主键
     * @return 结果
     */
    public int deleteWmsLkDeliveryDetailRequirementById(Long id);
}
