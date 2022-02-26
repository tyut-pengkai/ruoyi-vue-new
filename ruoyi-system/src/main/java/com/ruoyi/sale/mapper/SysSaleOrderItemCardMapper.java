package com.ruoyi.sale.mapper;

import com.ruoyi.sale.domain.SysSaleOrderItemCard;
import com.ruoyi.system.domain.SysCard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单卡密Mapper接口
 *
 * @author zwgu
 * @date 2022-02-26
 */
@Repository
public interface SysSaleOrderItemCardMapper {
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
     * 删除订单卡密
     *
     * @param id 订单卡密主键
     * @return 结果
     */
    public int deleteSysSaleOrderItemCardById(Long id);

    /**
     * 批量删除订单卡密
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderItemCardByIds(Long[] ids);

    /**
     * 批量删除卡密
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCardByCardIds(Long[] ids);

    /**
     * 批量新增卡密
     *
     * @param sysCardList 卡密列表
     * @return 结果
     */
    public int batchSysCard(List<SysCard> sysCardList);


    /**
     * 通过订单卡密主键删除卡密信息
     *
     * @param id 订单卡密ID
     * @return 结果
     */
    public int deleteSysCardByCardId(Long id);
}
