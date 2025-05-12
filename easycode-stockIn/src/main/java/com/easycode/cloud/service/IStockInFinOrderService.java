package com.easycode.cloud.service;

import com.easycode.cloud.domain.vo.FinProductScanVo;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.domain.StockInFinOrder;
import com.weifu.cloud.domain.StockInFinOrderDetail;
import com.weifu.cloud.domain.dto.StockInFinOrderDto;
import com.weifu.cloud.domain.vo.StockInFinOrderDetailVo;

import java.util.List;

/**
 * 成品入库单Service接口
 *
 * @author weifu
 * @date 2022-12-07
 */
public interface IStockInFinOrderService {

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
     * 根据成品收货单号查询打印信息
     *
     * @param stockInFinOrder 成品入库单
     * @return 成品入库单集合
     */
    List<StockInFinOrderDto> getPrintInfoByProductNo(StockInFinOrder stockInFinOrder);

    /**
     * 新增成品入库单
     * @param stockInFinOrderDto
     * @return
     */
    StockInFinOrder insertStockInFinOrder(StockInFinOrderDto stockInFinOrderDto);

    /**
     * 编辑成品入库单
     *
     * @param stockInFinOrderDto 成品入库单
     * @return 结果
     */
    int updateStockInFinOrder(StockInFinOrderDto stockInFinOrderDto);

    /**
     * 批量删除成品入库单
     *
     * @param ids 需要删除的成品入库单主键集合
     * @return 结果
     */
    int deleteStockInFinOrderByIds(Long[] ids);

    /**
     * 删除成品入库单信息
     *
     * @param id 成品入库单主键
     * @return 结果
     */
    int deleteStockInFinOrderById(Long id);

    /**
     * 查询成品收货明细列表
     *
     * @param stockInFinOrderDetail
     * @return
     */
    List<StockInFinOrderDetail> selectStockInFinOrderDetailList(StockInFinOrderDetailVo stockInFinOrderDetail);

    /**
     * 关闭成品入库单
     * @param ids
     * @return
     */
    int closeStockInFinOrder(Long[] ids);

    /**
     * 激活成品入库单
     * @param ids 成品入库单ids
     * @return 结果
     */
    int activeStockInFinOrderByIds(Long[] ids);

    public void mesToWmsProductReceiveTask(String json);

    /**
     * 关闭成品收货任务
     * @param stockInFinOrderDto
     * @return
     */
    int closeStockInFinOrderOne(StockInFinOrderDto stockInFinOrderDto);

    /**
     * 生成成品收货单
     *
     * @param finProductScanVo
     */
    void addFinProductTask(FinProductScanVo finProductScanVo);

    /**
     * 批量确认
     * @param ids
     */
    AjaxResult batchConfirm(Long[] ids);
}
