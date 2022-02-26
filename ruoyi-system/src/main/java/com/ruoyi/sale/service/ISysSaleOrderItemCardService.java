package com.ruoyi.sale.service;

import com.ruoyi.sale.domain.SysSaleOrderItemCard;

import java.util.List;

/**
 * 订单卡密Service接口
 *
 * @author zwgu
 * @date 2022-02-26
 */
public interface ISysSaleOrderItemCardService {
    /**
     * 查询订单卡密
     *
     * @param id 订单卡密主键
     * @return 订单卡密
     */
    public SysSaleOrderItemCard selectSysSaleOrderItemCardById(Long id);

    /**
     * 查询订单卡密列表
     *
     * @param sysSaleOrderItemCard 订单卡密
     * @return 订单卡密集合
     */
    public List<SysSaleOrderItemCard> selectSysSaleOrderItemCardList(SysSaleOrderItemCard sysSaleOrderItemCard);

    /**
     * 新增订单卡密
     *
     * @param sysSaleOrderItemCard 订单卡密
     * @return 结果
     */
    public int insertSysSaleOrderItemCard(SysSaleOrderItemCard sysSaleOrderItemCard);

    /**
     * 修改订单卡密
     *
     * @param sysSaleOrderItemCard 订单卡密
     * @return 结果
     */
    public int updateSysSaleOrderItemCard(SysSaleOrderItemCard sysSaleOrderItemCard);

    /**
     * 批量删除订单卡密
     *
     * @param ids 需要删除的订单卡密主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderItemCardByIds(Long[] ids);

    /**
     * 删除订单卡密信息
     *
     * @param id 订单卡密主键
     * @return 结果
     */
    public int deleteSysSaleOrderItemCardById(Long id);
}
