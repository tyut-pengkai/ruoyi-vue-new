package com.ruoyi.sale.mapper;

import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单商品Mapper接口
 *
 * @author zwgu
 * @date 2022-03-01
 */
@Repository
public interface SysSaleOrderItemGoodsMapper {
    /**
     * 查询订单商品
     *
     * @param id 订单商品主键
     * @return 订单商品
     */
    public SysSaleOrderItemGoods selectSysSaleOrderItemGoodsById(Long id);

    /**
     * 查询订单商品
     *
     * @param itemId 订单商品主键
     * @return 订单商品
     */
    public List<SysSaleOrderItemGoods> selectSysSaleOrderItemGoodsByItemId(Long itemId);

    /**
     * 查询订单商品列表
     *
     * @param sysSaleOrderItemGoods 订单商品
     * @return 订单商品集合
     */
    public List<SysSaleOrderItemGoods> selectSysSaleOrderItemGoodsList(SysSaleOrderItemGoods sysSaleOrderItemGoods);

    /**
     * 新增订单商品
     *
     * @param sysSaleOrderItemGoods 订单商品
     * @return 结果
     */
    public int insertSysSaleOrderItemGoods(SysSaleOrderItemGoods sysSaleOrderItemGoods);

    /**
     * 新增订单商品
     *
     * @param sysSaleOrderItemGoodsList 订单商品
     * @return 结果
     */
    public int insertSysSaleOrderItemGoodsBatch(List<SysSaleOrderItemGoods> sysSaleOrderItemGoodsList);

    /**
     * 修改订单商品
     *
     * @param sysSaleOrderItemGoods 订单商品
     * @return 结果
     */
    public int updateSysSaleOrderItemGoods(SysSaleOrderItemGoods sysSaleOrderItemGoods);

    /**
     * 删除订单商品
     *
     * @param id 订单商品主键
     * @return 结果
     */
    public int deleteSysSaleOrderItemGoodsById(Long id);

    /**
     * 批量删除订单商品
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderItemGoodsByIds(Long[] ids);
}
