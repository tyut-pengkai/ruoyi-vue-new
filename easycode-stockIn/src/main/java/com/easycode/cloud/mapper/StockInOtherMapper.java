package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.easycode.cloud.domain.dto.StockInOtherParamsDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 其它入库单Mapper接口
 * 
 * @author bcp
 * @date 2023-03-24
 */
@Repository
public interface StockInOtherMapper 
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
     * @param stockInOther 其它入库单
     * @return 结果
     */
    public int insertStockInOther(StockInOther stockInOther);

    /**
     * 修改其它入库单
     * 
     * @param stockInOther 其它入库单
     * @return 结果
     */
    public int updateStockInOther(StockInOther stockInOther);

    /**
     * 删除其它入库单
     * 
     * @param id 其它入库单主键
     * @return 结果
     */
    public int deleteStockInOtherById(Long id);

    /**
     * 批量删除其它入库单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockInOtherByIds(Long[] ids);

    List<StockInOtherParamsDto> stockInOrderList(StockInOtherParamsDto stockInOtherParamsDto);
    List<StockInOtherParamsDto> stockInOrderListByTaskTypeAndOderNo(StockInOtherParamsDto stockInOtherParamsDto);

    StockInOther selectStockInOtherByOrderNo(String orderNo);
}
