package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.dto.PurchaseDto;
import com.easycode.cloud.domain.dto.PurchaseOrderDetailPlanDto;
import com.easycode.cloud.domain.vo.PurchaseVo;
import com.weifu.cloud.domain.PurchaseOrderDetailPlan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购协议计划行Mapper接口
 *
 * @author bcp
 * @date 2023-09-24
 */
@Repository
public interface PurchaseOrderDetailPlanMapper
{
    /**
     * 查询采购协议计划行
     *
     * @param id 采购协议计划行主键
     * @return 采购协议计划行
     */
    public PurchaseOrderDetailPlan selectPurchaseOrderDetailPlanById(Long id);

    /**
     * 查询采购协议计划行列表
     *
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 采购协议计划行集合
     */
    public List<PurchaseOrderDetailPlan> selectPurchaseOrderDetailPlanList(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 新增采购协议计划行
     *
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    public int insertPurchaseOrderDetailPlan(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 修改采购协议计划行
     *
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    public int updatePurchaseOrderDetailPlan(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 修改采购协议计划行已制单数量和时间
     *
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    public int updatePurchaseOrderDetailPlanMadeQty(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 删除采购协议计划行
     *
     * @param id 采购协议计划行主键
     * @return 结果
     */
    public int deletePurchaseOrderDetailPlanById(Long id);

    /**
     * 批量删除采购协议计划行
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePurchaseOrderDetailPlanByIds(Long[] ids);

    /**
     * 获取采购协议计划行 送货时间为上个月一号往后的
     */
    List<PurchaseOrderDetailPlan> selectOrderDetailPlanFirstDay(PurchaseOrderDetailPlan purchaseOrderDetailPlan);

    /**
     * 批量更新采购计划行（删除状态）
     *
     * @param purchaseVo
     * @return 结果
     */
    public int updatePurchaseOrderDetailPlanByIds(PurchaseVo purchaseVo);

    /**
     * 采购单处理页面 查询采购单计划行
     * @param purchaseOrderDetailPlanDto 采购协议计划行
     * @return 结果
     */
    List<PurchaseOrderDetailPlan> queryPurchaseOrderDetailPlan(PurchaseOrderDetailPlanDto purchaseOrderDetailPlanDto);

    /**
     * 从采购单计划行 获取需求数量之和
     * @param purchaseDto 采购协议计划行
     * @return 结果
     */
    List<PurchaseOrderDetailPlan> selectPurchaseOrderDetailPlanListBySupplierPeriod(PurchaseDto purchaseDto);

    /**
     * 从采购单计划行
     * @param list 采购协议计划行
     * @return 结果
     */
    List<PurchaseOrderDetailPlan> getPLanInfoByPurchaseNos(List<String> list);

    /**
     * 清除采购订单相关中间表未处理的数据
     * @return 结果
     */
    public void clearUntreatedPurchaseData();
}
