package com.easycode.cloud.service;

import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import com.easycode.cloud.domain.dto.DeliveryRequirementCheckDto;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.domain.DeliveryOrder;
import com.weifu.cloud.domain.DeliveryOrderDetail;
import com.weifu.cloud.domain.dto.DeliveryOrderDto;
import com.weifu.cloud.domain.vo.DeliveryOrderDetailVo;
import com.weifu.cloud.domain.vo.DeliveryOrderVo;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.weifu.cloud.domian.dto.DeliveryRequirementInfoDto;
import com.weifu.cloud.domian.vo.PopupBoxVo;

import java.util.List;

/**
 * 送货单Service接口
 *
 * @author ruoyi
 * @date 2022-11-25
 */
public interface IDeliveryOrderService
{
    /**
     * 查询送货单
     *
     * @param id 送货单主键
     * @return 送货单
     */
    DeliveryOrder selectDeliveryOrderById(Long id);

    /**
     * 查询送货单列表
     *
     * @param deliveryOrderDto 送货单Dto
     * @return 送货单集合
     */
    List<DeliveryOrder> selectDeliveryOrderList(DeliveryOrderDto deliveryOrderDto);

    /**
     * 新增送货单
     *
     * @param deliveryOrderVo 送货单
     * @return 结果
     */
    DeliveryOrder insertDeliveryOrder(DeliveryOrderVo deliveryOrderVo) throws Exception;

    /**
     * 查询送货单列表
     *
     * @param deliveryOrder 送货单
     * @return 送货单集合
     */
    List<PrintInfoVo> getPrintInfoByOrderNo(DeliveryOrderVo deliveryOrder);

    /**
     * 根据送货单详情id查询打印信息
     */
    List<PrintInfoVo> getPrintInfoByIds(DeliveryOrderVo deliveryOrder);

//    /**
//     * 根据送货单号list查询配送单打印信息
//     *
//     * @param deliveryOrder 送货单
//     * @return 送货单集合
//     */
//    List<PrintDeliveryInfoVo> getDeliveryInfoByIds(DeliveryOrderVo deliveryOrder);



    /**
     * 删除送货单信息
     *
     * @param id 送货单主键
     * @return 结果
     */
    int deleteDeliveryOrderById(Long id);

    /**
     * 发布送货单
     *
     * @param deliveryOrderVo 送货单
     * @return 结果
     */
    int updateDeliveryOrderStatus(DeliveryOrderVo deliveryOrderVo) ;

    /**
     * 查询送货单明细列表
     *
     * @param deliveryOrderDetail 送货单明细
     * @return 送货单明细集合
     */
    List<DeliveryOrderDetailVo> selectDeliveryOrderDetailList(DeliveryOrderDetail deliveryOrderDetail);

    /**
     * 根据物料、公司查询是否存在送货要求
     *
     * @param deliveryOrderDetailVos 送货单明细列表
     * @return 送货单明细集合
     */
    List<DeliveryOrderDetailVo> checkDeliveryRequirement(DeliveryRequirementCheckDto deliveryOrderDetailVos);

    /**
     * 根据物料、公司查询送货要求列表
     *
     * @param deliveryRequirementInfoDto 送货单明细列表
     * @return 送货单明细集合
     */
    List<DeliveryRequirementInfoDto> queryDeliveryRequirement(DeliveryRequirementInfoDto deliveryRequirementInfoDto);

    /**
     * 根据采购单号和采购单行号 修改进货单状态 远程调用接口
     * @param deliveryOrderVo
     * @return
     */
    int updateStatusToClose(DeliveryOrderVo deliveryOrderVo);

    /**
     * 校验送货单明细
     * @param ids
     * @return
     * @
     */
    int checkDeliveryOrderDetail(Long[] ids);

    /**
     * 采购单处理页面送货单详情查询
     * @param deliveryOrderDetailVo 送货单明细vo
     * @return 送货单明细集合
     */
    List<DeliveryOrderDetailVo> getDeliveryOrderDetailByPurchaseOrderNo(DeliveryOrderDetailVo deliveryOrderDetailVo);

    int updateDeliveryOrder(DeliveryOrderVo deliveryOrderVo);

    int deleteDeliveryOrderByIds(Long[] ids);

    /**
     * 查询成品入库单明细
     * @param id 送货单id
     * @return List<DeliveryOrderDetail>
     */
    List<DeliveryOrderDetail> queryDeliveryDetailById(Long id);

    /**
     * 发送送检单
     * @param id
     * @throws Exception
     */
    AjaxResult sendMessage(Long id) throws Exception;


    /**
     * 保存送货单明细
     */
    AjaxResult updateDeliveryOrderDetail(DeliveryOrderDetail deliveryOrderDetail);

    List<DeliveryOrder> openQuery(PopupBoxVo popupBoxVo);
}
