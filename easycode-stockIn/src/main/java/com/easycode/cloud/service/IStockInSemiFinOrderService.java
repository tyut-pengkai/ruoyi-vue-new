package com.easycode.cloud.service;

import com.easycode.cloud.domain.StockInSemiFinOrder;
import com.easycode.cloud.domain.dto.StockInSemiFinOrderDto;
import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;

import java.util.List;

/**
 * 半成品入库单Service接口
 * 
 * @author bcp
 * @date 2023-07-22
 */
public interface IStockInSemiFinOrderService 
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
     * @param stockInSemiFinOrderDto 半成品入库单
     * @return 结果
     */
    public StockInSemiFinOrder insertStockInSemiFinOrder(StockInSemiFinOrderDto stockInSemiFinOrderDto);

    /**
     * 修改半成品入库单
     * 
     * @param stockInSemiFinOrderDto 半成品入库单
     * @return 结果
     */
    public int updateStockInSemiFinOrder(StockInSemiFinOrderDto stockInSemiFinOrderDto);

    /**
     * 批量删除半成品入库单
     * 
     * @param ids 需要删除的半成品入库单主键集合
     * @return 结果
     */
    public int deleteStockInSemiFinOrderByIds(Long[] ids);

    /**
     * 删除半成品入库单信息
     * 
     * @param id 半成品入库单主键
     * @return 结果
     */
    public int deleteStockInSemiFinOrderById(Long id);

    /**
     * 关闭半成品入库单
     * @param ids 半成品入库单主键集合
     * @return 结果
     */
    int closeStockInSemiFinOrder(Long[] ids);

    /**
     * 获取半成品标签打印
     * @param stockInSemiFinOrderDto 半成品入库单dto
     * @return 半成品入库明细
     */
    List<StockInSemiFinOrderDetailVo> getStockInSemiFinOrderPrint(StockInSemiFinOrderDto stockInSemiFinOrderDto);

    public List<StockInSemiFinOrderDto> selectStockInSemiList(StockInSemiFinOrderDto dto);

}
