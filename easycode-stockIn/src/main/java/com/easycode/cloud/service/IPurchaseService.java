package com.easycode.cloud.service;

import com.easycode.cloud.domain.PurchaseOrder;
import com.easycode.cloud.domain.dto.PurchaseDto;
import com.easycode.cloud.domain.vo.PurchaseVo;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import com.weifu.cloud.domain.dto.PurchaseOrderDetailDto;
import com.weifu.cloud.domian.vo.PopupBoxVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 采购单据处理service接口
 * @author hbh
 */
public interface IPurchaseService {



    /**
     * 查询采购单据处理列表(前端显示)
     * @param purchaseVo
     * @return
     */
    List<PurchaseDto> selectWmsPurchaseOrderList(PurchaseVo purchaseVo);

    /**
     * 查询采购单明细
     *
     * @param id 采购单明细主键
     * @return 采购单明细
     */
    PurchaseOrderDetail selectWmsPurchaseOrderDetailById(Long id);

    /**
     * 新增采购单明细
     *
     * @param wmsPurchaseOrderDetail 采购单明细
     * @return 结果
     */
    int insertWmsPurchaseOrderDetail(PurchaseOrderDetail wmsPurchaseOrderDetail);

    /**
     * 修改采购单明细
     *
     * @param wmsPurchaseOrderDetail 采购单明细
     * @return 结果
     */
    int updateWmsPurchaseOrderDetail(PurchaseOrderDetail wmsPurchaseOrderDetail);

    /**
     * 批量删除采购单明细
     *
     * @param ids 需要删除的采购单明细主键集合
     * @return 结果
     */
    int deleteWmsPurchaseOrderDetailByIds(Long[] ids);

    /**
     * 删除采购单明细信息
     *
     * @param id 采购单明细主键
     * @return 结果
     */
    int deleteWmsPurchaseOrderDetailById(Long id);

    /**
     * 根据采购单号和采购单行号 获取采购单明细
     * @param purchaseOrderDetailDto 采购单明细dto
     * @return 采购单集合
     */
    List<PurchaseOrderDetail> getPurchaseOrderDetailByNoAndLine(PurchaseOrderDetailDto purchaseOrderDetailDto);


    /**
     * 修改采购单明细
     * @param purchaseOrderDetailList 采购单明细vo
     * @return 结果
     */
    int updatePurchaseOrderDetail(List<PurchaseOrderDetail> purchaseOrderDetailList);

    /**
     * 更新采购单wms收货数量、sap收货数量(收货)
     * @param purchaseOrderDetailDto 采购单明细dto
     * @return 结果
     */
    int updatePurchaseReceivedQty(PurchaseOrderDetailDto purchaseOrderDetailDto);
    /**
     * 更新采购单wms收货数量、sap收货数量(退货)
     * @param purchaseOrderDetailDto 采购单明细dto
     * @return 结果
     */
    int updatePurchaseReturnQty(PurchaseOrderDetailDto purchaseOrderDetailDto);

    /**
     * 采购单列表开窗查询
     *
     * @param popupBoxVo
     * @return
     */
    public List<PurchaseOrder> openQueryPurchaseOrder(PopupBoxVo popupBoxVo);


    /**
     * 采购单处理详情查询（除去采购协议）
     * @param purchaseOrderDetail
     * @return
     */
    List<PurchaseOrderDetail> selectPurchaseOrderDetailList(PurchaseOrderDetail purchaseOrderDetail);


    /**
     * 根据采购单号、采购单行号、未收数量 回写采购单明细 （入库单关闭）
     * @param list 采购单明细集合
     * @return 结果
     */
    int updatePurchaseOrderDetailBatch(List<PurchaseOrderDetail> list);

    /**
     * 获取wms 已经制单但未收货的数量

     * @param purchaseOrderNo 采购单号
     * @param lineNo 采购单行号
     * @return  已经制单但未收货的数量
     */

    BigDecimal getPurchaseWmsUnmadeQty(String purchaseOrderNo,Long lineNo);

    /**
    * 根据采购订单号查询采购订单明细
    * @param purchaseVo 采购订单Vo
    * @date 2024/05/22
    * @author fsc
    * @return 采购订单明细集合
    */
    List<PurchaseDto> queryPurchaseDetailList(PurchaseVo purchaseVo);

    TableDataInfo selectWmsPurchaseOrderListShow(PurchaseVo purchaseVo);

    /**
     * 销售外向交货单同步
     * @param headList
     * @param itemList
     */

    /**
     * 采购订单, 采购计划同步
     * @param headList
     * @param itemList
     * @return
     */
    String syncPurchasePlanOrder(List<Map<String, Object>> headList, List<Map<String, Object>> itemList);

    /**
     * 非ASN入库
     * @param headList
     * @param itemList
     * @return
     */
    String noAsnStockIn(List<Map<String, Object>> headList, List<Map<String, Object>> itemList);
}
