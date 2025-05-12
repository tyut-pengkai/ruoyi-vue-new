package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成本中心退货单明细Mapper接口
 *
 * @author fsc
 * @date 2023-03-11
 */
@Repository
public interface CostCenterReturnOrderDetailMapper
{
    /**
     * 查询成本中心退货单明细
     *
     * @param id 成本中心退货单明细主键
     * @return 成本中心退货单明细
     */
    public CostCenterReturnOrderDetail selectCostCenterReturnOrderDetailById(Long id);

    /**
     * 查询成本中心退货单明细
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
     * 删除成本中心退货单明细
     *
     * @param id 成本中心退货单明细主键
     * @return 结果
     */
    public int deleteCostCenterReturnOrderDetailById(Long id);

    /**
     * 批量删除成本中心退货单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostCenterReturnOrderDetailByIds(Long[] ids);

    /**
     * 查询未完成的任务数量
     * @param orderNo 退货单据号
     * @param taskType 退货任务类型
     * @return 数量
     */
    public int getBeContinuedNumber(@Param("orderNo") String orderNo, @Param("taskType") String taskType);
}
