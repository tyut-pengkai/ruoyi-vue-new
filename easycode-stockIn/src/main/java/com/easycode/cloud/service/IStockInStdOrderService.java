package com.easycode.cloud.service;

import com.easycode.cloud.domain.vo.StockInStdOrderDetailVo;
import com.weifu.cloud.domain.StockInFinOrder;
import com.weifu.cloud.domain.StockInItemBom;
import com.weifu.cloud.domain.StockInStdOrder;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.dto.StockInOrderCommonDto;
import com.weifu.cloud.domain.dto.StockInStdOrderDetailDto;
import com.weifu.cloud.domain.dto.StockInStdOrderDto;
import com.weifu.cloud.domain.vo.PrintInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 标准入库单Service接口
 *
 * @author weifu
 * @date 2022-12-07
 */
public interface IStockInStdOrderService
{
    /**
     * 查询标准入库单
     *
     * @param id 标准入库单主键
     * @return 标准入库单
     */
    public StockInStdOrder selectStockInStdOrderById(Long id);

    /**
     * 查询标准入库单列表
     *
     * @param stockInStdOrder 标准入库单
     * @return 标准入库单集合
     */
    public List<StockInStdOrder> selectStockInStdOrderList(StockInStdOrder stockInStdOrder);


    /**
     * 打印物料标签
     *
     * @param printInfoVo 标准入库单
     * @return 标准入库单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfoVo);

    /**
     * 新增标准入库单
     *
     * @param stockInStdOrderDtoList
     * @return 结果
     */
    public int insertStockInStdOrder(String stockInStdOrderDtoList);

    /**
     * 关闭标准入库单
     *
     * @param stockInStdOrder 标准入库单
     * @return 结果
     */
    public int closeStockInStdOrder(StockInStdOrder stockInStdOrder);

    /**
     * 查询入标准入口单明细列表
     * @param stockInStdOrderDetailDto
     * @return
     */
    List<StockInStdOrderDetail> selectStockInStdOrderDetailList(StockInStdOrderDetailDto stockInStdOrderDetailDto);

    /**
     * 批量激活标准入库单
     *
     * @param stockInStdOrderDto 标准入库单dto
     * @return 结果
     */
    int activeStockInStdOrder(StockInStdOrderDto stockInStdOrderDto);

    /**
     * 同步标准入库单
     * @param params
     * @return
     */
    String syncStockInStdOrder(Map<String, Object> params);

    /**
     * 同步半成品、成品入库单
     * @param itemList
     * @return
     */
    int syncStockInSemOrFinOrder(List<Map<String, Object>> itemList) throws Exception;
    /**
     * 关闭标准入库任务（入库任务主键id集合）
     * @param ids
     * @return
     */
    int closeStockInTask(Long[] ids);

    /**
     * 根据送货单id 关闭标准入库单
     * @param ids 送货单id集合
     * @return 结果
     */
    int closeStockInByDeliveryOrderIds(Long[] ids);

    /**
     * 查询入标准入口单明细列表(联查主表)
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 标准入库单明细vo集合
     */
    List<StockInStdOrderDetailVo> queryStdOrderAndDetailList(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 修改标准入库单明细
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 结果
     */
    int updateStockInStdOrderDetail(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 原材料退货新增同步更新标准入库单
     * @param stockInStdOrderDto 标准入库单dto
     * @return 结果
     */
    int editDetail(StockInStdOrderDto stockInStdOrderDto);

    /**
     * 批量激活标准入库单
     * @param ids 标准入库单主键id集合
     * @return 结果
     */
    int activeStockInStdByIds(Long[] ids);

    /**
     * 根据物料list、单据状态list查询入库单明细数据，将数据以物料代码分组后返回
     * @param stock 库内Dto
     * @Date 2024/5/13
     * @Author fsc
     * @return 根据物料代码分组后的数据集合
     */
    List<StockInOrderCommonDto> queryOnWayNumGroupByMaterialNo(StockInOrderCommonDto stock);

    /**
     * 查询标准入库明细
     * @param id 入库主单id
     * @return
     */
    List<StockInStdOrderDetail> queryWmsStockinStdOrderDetailById(Long id);

    /**
     * 标准入库单收货单生成选择列表
     * @param orderNos
     * @return
     */
    List<StockInStdOrderDetailDto> stdOrderToDeliveryList(List<String> orderNos);

    /**
     * 标准入库单收货单生成选择列表
     * @param materialNo
     * @param orderNos
     * @return
     */
    List<StockInStdOrderDetailDto> stdOrderToDeliveryList(String materialNo, List<String> orderNos);
    /**
     *
     */
    void timing();

    /**
     * 过账
     * @param deliveryId
     */
    void doPost(Long deliveryId) throws Exception;

    /**
     * 查询bom信息列表
     * @param id
     * @return
     */
    public List<StockInItemBom> getStockInItemBomById(Long id);

    /**
     * 根据任务号查询物料信息
     *
     * @param tastNo 任务号
     * @return 物料信息
     */
    List<StockInFinOrder> getMaterialInfoByTaskNo(String tastNo) throws Exception;

    /**
     * 新增标准入库单
     * @param stockInStdOrder
     * @return
     */
    int addStockInStdOrder(StockInStdOrder stockInStdOrder);

    /**
     * 确认bom提交
     * @param stockInStdOrder
     * @return
     */
    int readBomSubmit(StockInStdOrder stockInStdOrder);

    /**
     * 按bom详情扣减原材料委外库存
     * @param stdOrder
     * @return
     */
    void updateOutSourcedInventory(StockInStdOrder stdOrder);
}
