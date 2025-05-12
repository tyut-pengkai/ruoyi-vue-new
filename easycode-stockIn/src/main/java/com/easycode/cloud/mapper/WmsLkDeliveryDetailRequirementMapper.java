package com.easycode.cloud.mapper;

import com.weifu.cloud.domain.WmsLkDeliveryDetailRequirement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 送货单明细-送货要求关系Mapper接口
 *
 * @author fsc
 * @date 2023-05-10
 */
@Repository
public interface WmsLkDeliveryDetailRequirementMapper
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
     * 新增送货单明细-送货要求关系
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 结果
     */
    public int batchInsertRequirement(List<WmsLkDeliveryDetailRequirement> wmsLkDeliveryDetailRequirement);

    /**
     * 修改送货单明细-送货要求关系
     *
     * @param wmsLkDeliveryDetailRequirement 送货单明细-送货要求关系
     * @return 结果
     */
    public int updateWmsLkDeliveryDetailRequirement(WmsLkDeliveryDetailRequirement wmsLkDeliveryDetailRequirement);

    /**
     * 删除送货单明细-送货要求关系
     *
     * @param id 送货单明细-送货要求关系主键
     * @return 结果
     */
    public int deleteWmsLkDeliveryDetailRequirementById(Long id);

    /**
     * 批量删除送货单明细-送货要求关系
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsLkDeliveryDetailRequirementByIds(Long[] ids);

    /**
     * 批量删除送货单明细-送货要求关系
     *
     * @param id 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRequirementByDetailId(Long id);
}
