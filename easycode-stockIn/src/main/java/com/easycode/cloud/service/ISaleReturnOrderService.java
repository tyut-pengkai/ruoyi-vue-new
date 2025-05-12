package com.easycode.cloud.service;

import com.easycode.cloud.domain.SaleReturnOrder;
import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.domain.vo.SaleReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.SaleReturnOrderVo;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.domain.vo.PrintInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 销售发货退货单Service接口
 *
 * @author fsc
 * @date 2023-03-11
 */
public interface ISaleReturnOrderService
{
    /**
     * 查询销售发货退货单
     *
     * @param id 销售发货退货单主键
     * @return 销售发货退货单
     */
    public SaleReturnOrder selectSaleReturnOrderById(Long id);

    /**
     * 查询销售发货退货单列表
     *
     * @param saleReturnOrderVo 销售发货退货单
     * @return 销售发货退货单集合
     */
    public List<SaleReturnOrder> selectSaleReturnOrderList(SaleReturnOrderVo saleReturnOrderVo);

    /**
     * 查询销售发货退货单列表
     *
     * @param printInfoVo 销售发货退货单
     * @return 销售发货退货单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfoVo);

    /**
     * 新增销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    public int insertSaleReturnOrder(SaleReturnOrder saleReturnOrder);

    /**
     * 关闭销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    public int closeSaleOrder(SaleReturnOrder saleReturnOrder);

    /**
     * 新增销售发货退货单及明细
     *
     * @param saleReturnOrderVo 销售发货退货单
     * @return 结果
     */
    public SaleReturnOrder addSaleReturn(SaleReturnOrderVo saleReturnOrderVo);

    /**
     * 销售外向交货单自动同步，类型销售退的数据
     * @param headList 头数据
     * @param itemList 明细数据
     * @return 状态
     */
    String sapSalesReturn(List<Map<String, Object>> headList, List<Map<String, Object>> itemList);

    /**
     * 修改销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    public int updateSaleReturnOrder(SaleReturnOrder saleReturnOrder);

    /**
     * 批量删除销售发货退货单
     *
     * @param ids 需要删除的销售发货退货单主键集合
     * @return 结果
     */
    public int deleteSaleReturnOrderByIds(Long[] ids);

    /**
     * 删除销售发货退货单信息
     *
     * @param id 销售发货退货单主键
     * @return 结果
     */
    public int deleteSaleReturnOrderById(Long id);

    /**
     * 激活销售发货退货单
     *
     * @param ids 销售发货退货单主键集合
     * @return 结果
     */
    int activeSaleOrderReturnByIds(Long[] ids);


    /**
     * 查找是否存在导入数据
     *
     * @param stockList 销售发货
     * @return 结果
     */
    public AjaxResult importData(List<SaleReturnOrderDetailVo> stockList);

    /**
     * 新增退货入库任务
     * @param detail 单据明细
     */
    int addRetTask(SaleReturnOrderDetail detail, String taskType);
}
