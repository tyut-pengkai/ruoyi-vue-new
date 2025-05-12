package com.easycode.cloud.service;

import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.easycode.cloud.domain.dto.StockInOtherParamsDto;
import com.easycode.cloud.domain.dto.StockinOtherDetailDto;
import com.weifu.cloud.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 其它入库单Service接口
 * 
 * @author bcp
 * @date 2023-03-24
 */
public interface IStockInOtherService 
{
    /**
     * 查询其它入库单
     * 
     * @param id 其它入库单主键
     * @return 其它入库单
     */
    public StockInOther selectStockInOtherById(Long id);

    /**
     * 查询其它入库单列表
     * 
     * @param stockInOtherDto 其它入库单
     * @return 其它入库单集合
     */
    public List<StockInOther> selectStockInOtherList(StockInOtherDto stockInOtherDto);

    /**
     * 新增其它入库单
     * 
     * @param stockInOtherDto 其它入库单
     * @return 结果
     */
    public StockInOther insertStockInOther(StockInOtherDto stockInOtherDto);

    /**
     * 修改其它入库单
     * 
     * @param stockInOther 其它入库单
     * @return 结果
     */
    public int updateStockInOther(StockInOther stockInOther);

    /**
     * 批量删除其它入库单
     * 
     * @param ids 需要删除的其它入库单主键集合
     * @return 结果
     */
    public int deleteStockInOtherByIds(Long[] ids);

    /**
     * 删除其它入库单信息
     * 
     * @param id 其它入库单主键
     * @return 结果
     */
    public int deleteStockInOtherById(Long id);

    /**
     * 关闭其它入库单
     * @param id 其它入库主键id
     * @return 结果
     */
    int close(Long id);

    /**
     * 批量激活其它入库单
     * @param ids 其它入库主键ids
     * @return 结果
     */
    int activeStockInOtherByIds(Long[] ids);

    StockinOtherDetailDto stockInOtherOrderDetailList(Long detailId);

    List<StockInOtherParamsDto> stockInOrderList(StockInOtherParamsDto stockInOtherParamsDto);

    AjaxResult submitStockInOrderTask(StockinOtherDetailDto stockinOtherDetailDto) throws Exception;

    StockinOtherDetailDto stockInOtherOrderDetailListByTaskNo(String taskNo);
}
