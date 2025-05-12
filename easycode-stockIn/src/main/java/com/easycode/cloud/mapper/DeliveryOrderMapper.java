package com.easycode.cloud.mapper;

import com.weifu.cloud.domain.DeliveryOrder;
import com.weifu.cloud.domain.DeliveryOrderDetail;
import com.weifu.cloud.domian.GoodsSourceDef;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 送货单Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-25
 */
@Repository
public interface DeliveryOrderMapper
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
     * @param deliveryOrder 送货单
     * @return 送货单集合
     */
    List<DeliveryOrder> selectDeliveryOrderList(DeliveryOrder deliveryOrder);

    /**
     * 新增送货单
     *
     * @param deliveryOrder 送货单
     * @return 结果
     */
    int insertDeliveryOrder(DeliveryOrder deliveryOrder);

    /**
     * 修改送货单
     *
     * @param deliveryOrder 送货单
     * @return 结果
     */
    int updateDeliveryOrder(DeliveryOrder deliveryOrder);

    /**
     * 删除送货单
     *
     * @param id 送货单主键
     * @return 结果
     */
    int deleteDeliveryOrderById(Long id);

    /**
     * 批量删除送货单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDeliveryOrderByIds(Long[] ids);

    /**
     * 修改送货单状态
     *
     * @param deliveryOrder 送货单
     * @return 结果
     */
    int updateDeliveryOrderStatus(DeliveryOrder deliveryOrder);

    /**
     * 查询送货单
     *
     * @param ids 送货单主键
     * @return 送货单
     */
    List<DeliveryOrder> selectDeliveryOrderByIds(Long[] ids);

    /**
     * 删除送货单 校验
     *
     * @param ids 送货单主键
     * @return 数量
     */
    int selectDeliveryOrderIsNew(Long[] ids);

    List<GoodsSourceDef> selectGoodsSourceDefList(GoodsSourceDef goodsSourceDef);

    List<DeliveryOrder> openQuery(Map<String, Object> paramsMap);
}
