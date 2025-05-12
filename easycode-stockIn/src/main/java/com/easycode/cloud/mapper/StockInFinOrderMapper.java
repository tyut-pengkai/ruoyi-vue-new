package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.StockInSemiFinOrder;
import com.weifu.cloud.domain.StockInFinOrder;
import com.weifu.cloud.domain.dto.StockInFinOrderDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成品入库单Mapper接口
 *
 * @author weifu
 * @date 2022-12-07
 */
@Repository
public interface StockInFinOrderMapper {

    /**
     * 查询成品入库单
     *
     * @param id 成品入库单主键
     * @return 成品入库单
     */
    StockInFinOrder selectStockInFinOrderById(Long id);

    /**
     * 查询成品入库单列表
     *
     * @param stockInFinOrder 成品入库单
     * @return 成品入库单集合
     */
    List<StockInFinOrderDto> selectStockInFinOrderList(StockInFinOrder stockInFinOrder);

    /**
     * 新增成品入库单
     *
     * @param stockInFinOrder 成品入库单
     * @return 结果
     */
    int insertStockInFinOrder(StockInFinOrder stockInFinOrder);

    /**
     * 修改成品入库单
     *
     * @param stockInFinOrder 成品入库单
     * @return 结果
     */
    int updateStockInFinOrder(StockInFinOrder stockInFinOrder);

    /**
     * 删除成品入库单
     *
     * @param id 成品入库单主键
     * @return 结果
     */
    int deleteStockInFinOrderById(Long id);

    /**
     * 批量删除成品入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStockInFinOrderByIds(Long[] ids);

    /**
     *
     * @param stockInFinOrder
     * @return
     */
    int closeStockInFinOrder(StockInFinOrder stockInFinOrder);


    /**
     * 查询成品入库单（单表）
     *
     * @param id 成品入库单主键
     * @return 成品入库单
     */
    StockInFinOrder selectStockInFinOrderByIdOne(Long id);

    /**
     * 校验成品入库单
     * @param stockInFinOrder 成品入库单
     * @return 数量
     */
    List<StockInFinOrder> checkStockInFinOrder(StockInFinOrder stockInFinOrder);

    /**
     * 所属成品收货任务均完成时 更新成品收货单据状态为完成
     * @param stockInFinOrder
     * @return 结果
     */
    int updateStockInFinOrderByAllComplete(StockInFinOrder stockInFinOrder);
    /**
     * 所属半成品收货任务均完成时 更新半成品收货单据状态为完成
     * @param stockInSemiFinOrder
     * @return 结果
     */
    int updateStockInSemiFinOrderByAllComplete(StockInSemiFinOrder stockInSemiFinOrder);

    StockInFinOrder selectStockInFinOrderByOrderNo(String stockInOrderNo);
}
