package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 半成品入库单Mapper接口
 *
 * @author bcp
 * @date 2023-07-22
 */
@Repository
public interface StockInSemiFinOrderDetailMapper
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
     * 删除半成品入库单
     *
     * @param id 半成品入库单主键
     * @return 结果
     */
    public int deleteStockInSemiFinOrderDetailById(Long id);

    /**
     * 批量删除半成品入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockInSemiFinOrderDetailByIds(Long[] ids);

    /**
     * 校验成品入库单
     * @param stockInSemiFinOrderDetail  半成品收货明细
     * @return 半成品收货明细vo
     */
    List<StockInSemiFinOrderDetailVo> checkStockInSemiFinOrderDetail(StockInSemiFinOrderDetail stockInSemiFinOrderDetail);

    /**
     * 获取成品收货单据最大批次
     * @param prdOrderNo 半成品收货单id
     * @return 最大批次
     */
    String getMaxLot(String prdOrderNo);
}
