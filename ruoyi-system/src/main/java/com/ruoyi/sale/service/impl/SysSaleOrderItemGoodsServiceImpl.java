package com.ruoyi.sale.service.impl;

import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import com.ruoyi.sale.mapper.SysSaleOrderItemGoodsMapper;
import com.ruoyi.sale.service.ISysSaleOrderItemGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单商品Service业务层处理
 *
 * @author zwgu
 * @date 2022-03-01
 */
@Service
public class SysSaleOrderItemGoodsServiceImpl implements ISysSaleOrderItemGoodsService {
    @Autowired
    private SysSaleOrderItemGoodsMapper sysSaleOrderItemGoodsMapper;

    /**
     * 查询订单商品
     *
     * @param id 订单商品主键
     * @return 订单商品
     */
    @Override
    public SysSaleOrderItemGoods selectSysSaleOrderItemGoodsById(Long id) {
        return sysSaleOrderItemGoodsMapper.selectSysSaleOrderItemGoodsById(id);
    }

    /**
     * 查询订单商品
     *
     * @param itemId 订单商品主键
     * @return 订单商品
     */
    @Override
    public List<SysSaleOrderItemGoods> selectSysSaleOrderItemGoodsByItemId(Long itemId) {
        return sysSaleOrderItemGoodsMapper.selectSysSaleOrderItemGoodsByItemId(itemId);
    }

    /**
     * 查询订单商品列表
     *
     * @param sysSaleOrderItemGoods 订单商品
     * @return 订单商品
     */
    @Override
    public List<SysSaleOrderItemGoods> selectSysSaleOrderItemGoodsList(SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        return sysSaleOrderItemGoodsMapper.selectSysSaleOrderItemGoodsList(sysSaleOrderItemGoods);
    }

    /**
     * 新增订单商品
     *
     * @param sysSaleOrderItemGoods 订单商品
     * @return 结果
     */
    @Override
    public int insertSysSaleOrderItemGoods(SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        return sysSaleOrderItemGoodsMapper.insertSysSaleOrderItemGoods(sysSaleOrderItemGoods);
    }

    /**
     * 修改订单商品
     *
     * @param sysSaleOrderItemGoods 订单商品
     * @return 结果
     */
    @Override
    public int updateSysSaleOrderItemGoods(SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        return sysSaleOrderItemGoodsMapper.updateSysSaleOrderItemGoods(sysSaleOrderItemGoods);
    }

    /**
     * 批量删除订单商品
     *
     * @param ids 需要删除的订单商品主键
     * @return 结果
     */
    @Override
    public int deleteSysSaleOrderItemGoodsByIds(Long[] ids) {
        return sysSaleOrderItemGoodsMapper.deleteSysSaleOrderItemGoodsByIds(ids);
    }

    /**
     * 删除订单商品信息
     *
     * @param id 订单商品主键
     * @return 结果
     */
    @Override
    public int deleteSysSaleOrderItemGoodsById(Long id) {
        return sysSaleOrderItemGoodsMapper.deleteSysSaleOrderItemGoodsById(id);
    }
}
