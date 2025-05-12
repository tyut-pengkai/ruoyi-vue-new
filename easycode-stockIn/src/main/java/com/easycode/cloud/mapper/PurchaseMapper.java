package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.PurchaseOrder;
import com.easycode.cloud.domain.dto.PurchaseDto;
import com.easycode.cloud.domain.vo.PurchaseVo;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import com.easycode.cloud.domain.WmsPurchaseOrderDetailRaw;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 采购单据处理mapper接口
 * @author HBH
 */
@Repository
public interface PurchaseMapper {

    /**
     * 查询采购单据处理列表
     * @param purchaseVo
     * @return
     */
    List<PurchaseDto> selectWmsPurchaseOrderList(PurchaseVo purchaseVo);

    /**
     *
     * @param purchaseVo
     * @return
     */

    /**
     * 采购订单目前使用（货源）
     * @param purchaseVo
     * @return
     */
    List<PurchaseDto> selectPurchaseOrderList(PurchaseVo purchaseVo);


    /**
     * 查询采购单明细
     *
     * @param id 采购单明细主键
     * @return 采购单明细
     */
    PurchaseOrderDetail selectWmsPurchaseOrderDetailById(Long id);

    /**
     * @desc
     * @author yangSen
     * @date 2022/11/30
     * @param purchaseOrderDetail
     */
    List<PurchaseOrderDetail> selectPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 新增采购单明细
     *
     * @param purchaseOrder 采购单明细
     * @return 结果
     */
    int insertPurchaseOrder(PurchaseOrder purchaseOrder);

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
     * 删除采购单明细
     *
     * @param id 采购单明细主键
     * @return 结果
     */
    int deleteWmsPurchaseOrderDetailById(Long id);

    /**
     * 根据订单号删除采购单明细
     * @param orderNo
     * @return
     */
    int deleteWmsPurchaseOrderDetailByOrderNo(String orderNo);

    /**
     * 批量删除采购单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteWmsPurchaseOrderDetailByIds(Long[] ids);

    /**
     * 根据采购单号 采购单行号 查询采购单明细
     * @param list
     * @return
     */
    List<PurchaseOrderDetail> selectPurchaseOrderDetailByNoAndLine(List<PurchaseOrderDetail> list);

    /**
     * 根据交货日期获取采购订单明细
     * @param purchaseOrderDetail
     * @return
     */
    List<PurchaseOrderDetail> selectDetailByDeliveryDate(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 根据交货计划行计数器获取采购订单明细
     * @param wmsPurchaseOrderDetailRaw
     * @return
     */
    List<PurchaseOrderDetail> selectDetailByLineNum(WmsPurchaseOrderDetailRaw wmsPurchaseOrderDetailRaw);

    /**
     * 获取交货计划行计数器
     */
    List<PurchaseVo> selectPurOrderDtAgByLineNum(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 删除采购明细信息
     */
    int deleteOrderDetailById(PurchaseVo purchaseVo);

    /**
     * 获取采购订单明细信息
     */
    List<PurchaseOrderDetail> selectOrderDetail(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 根据项目行号获取已制单数量总和
     */
    List<PurchaseOrderDetail> selectSumByPurchaseLineNo(PurchaseOrderDetail purchaseOrderDetail);

    int updateMadeQty(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 采购单列表开窗查询
     *
     * @param paramsMap
     * @return
     */
    List<PurchaseOrder> openQueryPurchaseOrder(Map<String, Object> paramsMap);

    /**
     * 获取需要同步对比的采购协议明细
     */
    List<PurchaseOrderDetail> getPurchaseOrderDetailBySync();

    /**
     * 获取需要同步对比的采购协议明细
     */
    List<PurchaseOrderDetail> getPurchaseOrderDetailBySyncFirstDay();

    /**
     * 获取采购订单明细信息 送货时间为上个月一号往后的
     */
    List<PurchaseOrderDetail> selectOrderDetailFirstDay(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 获取 wms 未激活的送货数量

     * @param purchaseOrderNo
     * @param lineNo
     * @return
     */
    BigDecimal getPurchaseWmsMmadeUnActiveQty(@Param("purchaseOrderNo") String purchaseOrderNo, @Param("purchaseOrderLineNo") Long lineNo);

    /**
     * 获取已经激活的送货单但未收货的数量
       => 标准入库单明细中 激活 + 部分完成 的 总需要数 -总已经收的数量
     * @param purchaseOrderNo 采购单号
     * @param lineNo 采购间秸
     * @return 获取已经激活的送货单但wms未收货的数量
     */
    BigDecimal getPurchaseActiveUnReceivedQty(@Param("purchaseOrderNo") String purchaseOrderNo,@Param("purchaseOrderLineNo") Long lineNo);

    /**
     * 清除采购订单相关中间表未处理的数据
     * @return 结果
     */
    public void clearUntreatedPurchaseData();
}
