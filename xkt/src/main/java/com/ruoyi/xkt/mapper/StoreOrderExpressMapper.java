package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreOrderExpress;

import java.util.List;

/**
 * 档口代发订单快递Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreOrderExpressMapper extends BaseMapper<StoreOrderExpress> {
    /**
     * 查询档口代发订单快递
     *
     * @param id 档口代发订单快递主键
     * @return 档口代发订单快递
     */
    public StoreOrderExpress selectStoreOrderExpressByStoreOrderExprId(Long id);

    /**
     * 查询档口代发订单快递列表
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 档口代发订单快递集合
     */
    public List<StoreOrderExpress> selectStoreOrderExpressList(StoreOrderExpress storeOrderExpress);

    /**
     * 新增档口代发订单快递
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 结果
     */
    public int insertStoreOrderExpress(StoreOrderExpress storeOrderExpress);

    /**
     * 修改档口代发订单快递
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 结果
     */
    public int updateStoreOrderExpress(StoreOrderExpress storeOrderExpress);

    /**
     * 删除档口代发订单快递
     *
     * @param id 档口代发订单快递主键
     * @return 结果
     */
    public int deleteStoreOrderExpressByStoreOrderExprId(Long id);

    /**
     * 批量删除档口代发订单快递
     *
     * @param storeOrderExprIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreOrderExpressByStoreOrderExprIds(Long[] storeOrderExprIds);
}
