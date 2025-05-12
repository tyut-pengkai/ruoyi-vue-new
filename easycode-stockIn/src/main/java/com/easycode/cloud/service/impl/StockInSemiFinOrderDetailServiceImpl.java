package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import com.easycode.cloud.mapper.StockInSemiFinOrderDetailMapper;
import com.easycode.cloud.service.IStockInSemiFinOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 半成品入库单Service业务层处理
 *
 * @author bcp
 * @date 2023-07-22
 */
@Service
public class StockInSemiFinOrderDetailServiceImpl implements IStockInSemiFinOrderDetailService
{
    @Autowired
    private StockInSemiFinOrderDetailMapper stockInSemiFinOrderDetailMapper;

    /**
     * 查询半成品入库单
     *
     * @param id 半成品入库单主键
     * @return 半成品入库单
     */
    @Override
    public StockInSemiFinOrderDetail selectStockInSemiFinOrderDetailById(String id)
    {
        return stockInSemiFinOrderDetailMapper.selectStockInSemiFinOrderDetailById(id);
    }

    /**
     * 查询半成品入库单列表
     *
     * @param stockInSemiFinOrderDetail 半成品入库单
     * @return 半成品入库单
     */
    @Override
    public List<StockInSemiFinOrderDetailVo> selectStockInSemiFinOrderDetailList(StockInSemiFinOrderDetailVo stockInSemiFinOrderDetail)
    {
        return stockInSemiFinOrderDetailMapper.selectStockInSemiFinOrderDetailList(stockInSemiFinOrderDetail);
    }

    /**
     * 新增半成品入库单
     *
     * @param stockInSemiFinOrderDetail 半成品入库单
     * @return 结果
     */
    @Override
    public int insertStockInSemiFinOrderDetail(StockInSemiFinOrderDetail stockInSemiFinOrderDetail)
    {
        stockInSemiFinOrderDetail.setCreateTime(DateUtils.getNowDate());
        return stockInSemiFinOrderDetailMapper.insertStockInSemiFinOrderDetail(stockInSemiFinOrderDetail);
    }

    /**
     * 修改半成品入库单
     *
     * @param stockInSemiFinOrderDetail 半成品入库单
     * @return 结果
     */
    @Override
    public int updateStockInSemiFinOrderDetail(StockInSemiFinOrderDetail stockInSemiFinOrderDetail)
    {
        stockInSemiFinOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return stockInSemiFinOrderDetailMapper.updateStockInSemiFinOrderDetail(stockInSemiFinOrderDetail);
    }

    /**
     * 批量删除半成品入库单
     *
     * @param ids 需要删除的半成品入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInSemiFinOrderDetailByIds(Long[] ids)
    {
        return stockInSemiFinOrderDetailMapper.deleteStockInSemiFinOrderDetailByIds(ids);
    }

    /**
     * 删除半成品入库单信息
     *
     * @param id 半成品入库单主键
     * @return 结果
     */
    @Override
    public int deleteStockInSemiFinOrderDetailById(Long id)
    {
        return stockInSemiFinOrderDetailMapper.deleteStockInSemiFinOrderDetailById(id);
    }
}
