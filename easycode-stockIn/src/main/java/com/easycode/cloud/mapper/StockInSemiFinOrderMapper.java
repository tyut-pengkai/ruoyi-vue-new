package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.StockInSemiFinOrder;
import com.easycode.cloud.domain.dto.StockInSemiFinOrderDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 半成品入库单Mapper接口
 * 
 * @author bcp
 * @date 2023-07-22
 */
@Repository
public interface StockInSemiFinOrderMapper 
{
    /**
     * 查询半成品入库单
     * 
     * @param id 半成品入库单主键
     * @return 半成品入库单
     */
    public StockInSemiFinOrder selectStockInSemiFinOrderById(Long id);

    /**
     * 查询半成品入库单列表
     * 
     * @param stockInSemiFinOrder 半成品入库单
     * @return 半成品入库单集合
     */
    public List<StockInSemiFinOrder> selectStockInSemiFinOrderList(StockInSemiFinOrder stockInSemiFinOrder);

    /**
     * 新增半成品入库单
     * 
     * @param stockInSemiFinOrder 半成品入库单
     * @return 结果
     */
    public int insertStockInSemiFinOrder(StockInSemiFinOrder stockInSemiFinOrder);

    /**
     * 修改半成品入库单
     * 
     * @param stockInSemiFinOrder 半成品入库单
     * @return 结果
     */
    public int updateStockInSemiFinOrder(StockInSemiFinOrder stockInSemiFinOrder);

    /**
     * 删除半成品入库单
     * 
     * @param id 半成品入库单主键
     * @return 结果
     */
    public int deleteStockInSemiFinOrderById(Long id);

    /**
     * 批量删除半成品入库单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockInSemiFinOrderByIds(Long[] ids);

    public List<StockInSemiFinOrderDto> selectStockInSemiList(StockInSemiFinOrderDto dto);
}
