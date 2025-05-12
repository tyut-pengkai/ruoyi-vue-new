package com.easycode.cloud.service;

import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;

import java.util.List;

/**
 * 半成品入库单Service接口
 *
 * @author bcp
 * @date 2023-07-22
 */
public interface IStockInSemiFinOrderDetailService
{
    /**
     * 查询半成品入库单
     *
     * @param id 半成品入库单主键
     * @return 半成品入库单
     */
    public StockInSemiFinOrderDetail selectStockInSemiFinOrderDetailById(String id);

    /**
     * 查询半成品入库单列表
     *
     * @param stockInSemiFinOrderDetail 半成品入库单
     * @return 半成品入库单集合
     */
    public List<StockInSemiFinOrderDetailVo> selectStockInSemiFinOrderDetailList(StockInSemiFinOrderDetailVo stockInSemiFinOrderDetail);

    /**
     * 新增半成品入库单
     *
     * @param stockInSemiFinOrderDetail 半成品入库单
     * @return 结果
     */
    public int insertStockInSemiFinOrderDetail(StockInSemiFinOrderDetail stockInSemiFinOrderDetail);

    /**
     * 修改半成品入库单
     *
     * @param stockInSemiFinOrderDetail 半成品入库单
     * @return 结果
     */
    public int updateStockInSemiFinOrderDetail(StockInSemiFinOrderDetail stockInSemiFinOrderDetail);

    /**
     * 批量删除半成品入库单
     *
     * @param ids 需要删除的半成品入库单主键集合
     * @return 结果
     */
    public int deleteStockInSemiFinOrderDetailByIds(Long[] ids);

    /**
     * 删除半成品入库单信息
     *
     * @param id 半成品入库单主键
     * @return 结果
     */
    public int deleteStockInSemiFinOrderDetailById(Long id);
}
