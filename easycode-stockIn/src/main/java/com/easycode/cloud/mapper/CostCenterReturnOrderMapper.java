package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成本中心退货单Mapper接口
 *
 * @author fsc
 * @date 2023-03-11
 */
@Repository
public interface CostCenterReturnOrderMapper
{
    /**
     * 查询成本中心退货单
     *
     * @param id 成本中心退货单主键
     * @return 成本中心退货单
     */
    public CostCenterReturnOrder selectCostCenterReturnOrderById(Long id);

    /**
     * 查询成本中心退货单列表
     *
     * @param costCenterReturnOrderVo 成本中心退货单
     * @return 成本中心退货单集合
     */
    public List<CostCenterReturnOrder> selectCostCenterReturnOrderList(CostCenterReturnOrderVo costCenterReturnOrderVo);

    /**
     * 查询成本中心退货单-物料标签信息
     *
     * @param ids 成本中心退货单
     * @return 成本中心退货单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(Long[] ids);

    /**
     * 根据详情id查询成本中心退货单-物料标签信息
     */
    List<PrintInfoVo> getPrintInfoByDetailIds(Long[] ids);

    /**
     * 新增成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    public int insertCostCenterReturnOrder(CostCenterReturnOrder costCenterReturnOrder);

    /**
     * 修改成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    public int updateCostCenterReturnOrder(CostCenterReturnOrder costCenterReturnOrder);

    /**
     * 删除成本中心退货单
     *
     * @param id 成本中心退货单主键
     * @return 结果
     */
    public int deleteCostCenterReturnOrderById(Long id);

    /**
     * 批量删除成本中心退货单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostCenterReturnOrderByIds(Long[] ids);

    CostCenterReturnOrder selectCostCenterReturnOrderByOrderNo(String returnOrderNo);
}
