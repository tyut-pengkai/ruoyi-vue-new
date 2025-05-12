package com.easycode.cloud.service;

import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderDetailVo;

import java.util.List;

/**
 * 成本中心退货单明细Service接口
 *
 * @author fsc
 * @date 2023-03-11
 */
public interface ICostCenterReturnOrderDetailService
{
    /**
     * 查询成本中心退货单明细
     *
     * @param id 成本中心退货单明细主键
     * @return 成本中心退货单明细
     */
    public CostCenterReturnOrderDetail selectCostCenterReturnOrderDetailById(Long id);

    /**
     * 打印信息
     *
     * @param id 成本中心退货单明细主键
     * @return 成本中心退货单明细
     */
    public CostCenterReturnOrderDetailVo getPrintInfoList(Long id);

    /**
     * 查询成本中心退货单明细列表
     *
     * @param costCenterReturnOrderDetail 成本中心退货单明细
     * @return 成本中心退货单明细集合
     */
    public List<CostCenterReturnOrderDetail> selectCostCenterReturnOrderDetailList(CostCenterReturnOrderDetail costCenterReturnOrderDetail);

    /**
     * 新增成本中心退货单明细
     *
     * @param costCenterReturnOrderDetail 成本中心退货单明细
     * @return 结果
     */
    public int insertCostCenterReturnOrderDetail(CostCenterReturnOrderDetail costCenterReturnOrderDetail);

    /**
     * 修改成本中心退货单明细
     *
     * @param costCenterReturnOrderDetail 成本中心退货单明细
     * @return 结果
     */
    public int updateCostCenterReturnOrderDetail(CostCenterReturnOrderDetail costCenterReturnOrderDetail);

    /**
     * 批量删除成本中心退货单明细
     *
     * @param ids 需要删除的成本中心退货单明细主键集合
     * @return 结果
     */
    public int deleteCostCenterReturnOrderDetailByIds(Long[] ids);

    /**
     * 删除成本中心退货单明细信息
     *
     * @param id 成本中心退货单明细主键
     * @return 结果
     */
    public int deleteCostCenterReturnOrderDetailById(Long id);
}
