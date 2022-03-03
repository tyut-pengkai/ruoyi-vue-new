package com.ruoyi.sale.service;

import com.ruoyi.sale.domain.SysSaleOrderItemGoods;

import java.util.List;

/**
 * 订单商品Service接口
 *
 * @author zwgu
 * @date 2022-03-01
 */
public interface ISysSaleOrderItemGoodsService {
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
     * 批量删除订单商品
     *
     * @param ids 需要删除的订单商品主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderItemGoodsByIds(Long[] ids);

    /**
     * 删除订单商品信息
     *
     * @param id 订单商品主键
     * @return 结果
     */
    public int deleteSysSaleOrderItemGoodsById(Long id);
}
