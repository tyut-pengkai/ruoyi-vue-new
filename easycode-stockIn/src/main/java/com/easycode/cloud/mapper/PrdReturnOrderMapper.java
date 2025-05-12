package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.PrdReturnOrder;
import com.easycode.cloud.domain.dto.PrdReturnOrderDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 生产订单发货退料单Mapper接口
 * 
 * @author bcp
 * @date 2023-03-11
 */
@Repository
public interface PrdReturnOrderMapper 
{
    /**
     * 查询生产订单发货退料单
     * 
     * @param id 生产订单发货退料单主键
     * @return 生产订单发货退料单
     */
    public PrdReturnOrder selectPrdReturnOrderById(Long id);

    /**
     * 查询生产订单发货退料单列表
     * 
     * @param prdReturnOrderDto 生产订单发货退料单
     * @return 生产订单发货退料单集合
     */
    public List<PrdReturnOrder> selectPrdReturnOrderList(PrdReturnOrderDto prdReturnOrderDto);

    /**
     * 新增生产订单发货退料单
     * 
     * @param prdReturnOrder 生产订单发货退料单
     * @return 结果
     */
    public int insertPrdReturnOrder(PrdReturnOrder prdReturnOrder);

    /**
     * 修改生产订单发货退料单
     * 
     * @param prdReturnOrder 生产订单发货退料单
     * @return 结果
     */
    public int updatePrdReturnOrder(PrdReturnOrder prdReturnOrder);

    /**
     * 删除生产订单发货退料单
     * 
     * @param id 生产订单发货退料单主键
     * @return 结果
     */
    public int deletePrdReturnOrderById(Long id);

    /**
     * 批量删除生产订单发货退料单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePrdReturnOrderByIds(Long[] ids);
}
