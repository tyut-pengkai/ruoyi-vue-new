package com.easycode.cloud.service;

import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.domain.vo.PrintInfoVo;

import java.util.List;

/**
 * 成本中心退货单Service接口
 *
 * @author fsc
 * @date 2023-03-11
 */
public interface ICostCenterReturnOrderService
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
     * 查询成本中心退货单列表
     *
     * @param printInfoVo 成本中心退货单
     * @return 成本中心退货单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfoVo);

    /**
     * 新增成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    public int insertCostCenterReturnOrder(CostCenterReturnOrder costCenterReturnOrder);

    /**
     * 关闭成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    public int closeCostCenterOrder(CostCenterReturnOrder costCenterReturnOrder);

    /**
     * 新增成本中心退货单及明细
     *
     * @param costCenterReturnOrderVo 成本中心退货单
     * @return 结果
     */
    public CostCenterReturnOrder addCostCenterReturn(CostCenterReturnOrderVo costCenterReturnOrderVo);

    /**
     * 修改成本中心退货单
     *
     * @param costCenterReturnOrder 成本中心退货单
     * @return 结果
     */
    public int updateCostCenterReturnOrder(CostCenterReturnOrder costCenterReturnOrder);

    /**
     * 批量删除成本中心退货单
     *
     * @param ids 需要删除的成本中心退货单主键集合
     * @return 结果
     */
    public int deleteCostCenterReturnOrderByIds(Long[] ids);

    /**
     * 删除成本中心退货单信息
     *
     * @param id 成本中心退货单主键
     * @return 结果
     */
    public int deleteCostCenterReturnOrderById(Long id);

    /**
     * 激活成本中心退货单
     *
     * @param ids 成本中心退货单主键集合
     * @return 结果
     */
    int activeCostCenterOrderReturnByIds(Long[] ids);


    /**
     * 查找是否存在导入数据
     *
     * @param stockList 成本中心
     * @return 结果
     */
    public AjaxResult importData(List<CostCenterReturnOrderDetailVo> stockList);

    /**
     * PDA成本中心退货列表查询
     * @param retTaskDto
     * @return
     */
    public List<RetTaskVo> costCenterTaskList(RetTaskDto retTaskDto);

    /**
     * 成本中心任务
     * @param id
     * @return
     */
    RetTaskVo getTaskInfo(Long id);

    /**
     * PDA提交成本中心退货任务
     * @param retTaskDto
     * @return
     */
    String submitCostCenterTask(RetTaskDto retTaskDto)  throws Exception ;
}
