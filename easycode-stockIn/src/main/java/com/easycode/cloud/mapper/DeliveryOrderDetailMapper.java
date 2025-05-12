package com.easycode.cloud.mapper;

import com.weifu.cloud.domain.DeliveryOrder;
import com.weifu.cloud.domain.DeliveryOrderDetail;
import com.weifu.cloud.domain.vo.DeliveryOrderDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 送货单明细Mapper接口
 *
 * @author weifu
 * @date 2022-11-30
 */
@Repository
public interface DeliveryOrderDetailMapper
{
    /**
     * 查询送货单明细
     *
     * @param id 送货单明细主键
     * @return 送货单明细
     */
    DeliveryOrderDetail selectDeliveryOrderDetailById(Long id);

    /**
     * 查询送货单明细列表
     *
     * @param deliveryOrderDetail 送货单明细
     * @return 送货单明细集合
     */
    List<DeliveryOrderDetailVo> selectDeliveryOrderDetailList(DeliveryOrderDetail deliveryOrderDetail);

    /**
     * 新增送货单明细
     *
     * @param deliveryOrderDetail 送货单明细
     * @return 结果
     */
    int insertDeliveryOrderDetail(DeliveryOrderDetail deliveryOrderDetail);

    /**
     * 修改送货单明细
     *
     * @param deliveryOrderDetail 送货单明细
     * @return 结果
     */
    int updateDeliveryOrderDetail(DeliveryOrderDetail deliveryOrderDetail);

    /**
     * 删除送货单明细
     *
     * @param id 送货单明细主键
     * @return 结果
     */
    int deleteDeliveryOrderDetailById(Long id);

    /**
     * 批量删除送货单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDeliveryOrderDetailByIds(Long[] ids);

    /**
     * 删除送货单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int updateDeliveryDetailStatus(Long[] ids);

    /**
     * 根据ids查询送货单明细
     *
     * @param ids 需要查询的数据主键集合
     * @return 结果
     */
    List<DeliveryOrderDetailVo> selectDeliveryOrderDetailByIds(Long[] ids);

    /**
     * 根据 送货单id 查询送货单明细列表 （返回批次为采购单的物料批次）
     * @param deliveryOrderId
     * @return
     */
    List<DeliveryOrderDetailVo> selectDeliveryOrderDetailByDeliveryOrderId(@Param("deliveryOrderId") Long deliveryOrderId);

    /**
     * 根据采购单明细id 查询送货单明细
     * @param ids
     * @return
     */
    List<DeliveryOrderDetailVo> checkDeliveryOrderDetail(Long[] ids);

    /**
     * 送货单明细与采购单联表查询
     * @param deliveryOrderDetailVo 送货单明细vo
     * @return 送货单明细集合
     */
    List<DeliveryOrderDetailVo> selectDeliveryOrderDetailLkPurchaseOrder(DeliveryOrderDetailVo deliveryOrderDetailVo);

    /**
     * 采购单处理页面送货单详情查询
     * @param deliveryOrderDetailVo 送货单明细vo
     * @return 送货单明细集合
     */
    List<DeliveryOrderDetailVo> getDeliveryOrderDetailByPurchaseOrderNo(DeliveryOrderDetailVo deliveryOrderDetailVo);

    void updateDeliveryOrderDetailByOrderId(DeliveryOrderDetail deliveryOrderDetail);

    /**
     *  查询成品送货单明细
     * @param id 送货单id
     * @return List<DeliveryOrderDetail>
     */
    List<DeliveryOrderDetail> queryDeliveryDetailById(Long id);

    /**
     * 根据id查询送货单明细
     */
    List<DeliveryOrderDetail> queryDeliveryDetailByIdAlt(Long id);

    /**
     * 查询送货单及送货单明细
     */
    List<DeliveryOrderDetailVo> queryDeliveryDetailByDeliveryOrderId(@Param("deliveryOrderId") Long deliveryOrderId);

}
