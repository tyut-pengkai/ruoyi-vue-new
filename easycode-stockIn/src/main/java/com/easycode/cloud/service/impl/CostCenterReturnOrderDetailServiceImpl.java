package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.vo.CostCenterReturnOrderDetailVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.mapper.CostCenterReturnOrderDetailMapper;
import com.easycode.cloud.service.ICostCenterReturnOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 成本中心退货单明细Service业务层处理
 *
 * @author fsc
 * @date 2023-03-11
 */
@Service
public class CostCenterReturnOrderDetailServiceImpl implements ICostCenterReturnOrderDetailService
{
    @Autowired
    private CostCenterReturnOrderDetailMapper costCenterReturnOrderDetailMapper;

    /**
     * 查询成本中心退货单明细
     *
     * @param id 成本中心退货单明细主键
     * @return 成本中心退货单明细
     */
    @Override
    public CostCenterReturnOrderDetail selectCostCenterReturnOrderDetailById(Long id)
    {
        return costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailById(id);
    }

    /**
     * 查询成本中心退货单明细
     *
     * @param id 成本中心退货单明细主键
     * @return 成本中心退货单明细
     */
    @Override
    public CostCenterReturnOrderDetailVo getPrintInfoList(Long id)
    {
        return costCenterReturnOrderDetailMapper.getPrintInfoList(id);
    }

    /**
     * 查询成本中心退货单明细列表
     *
     * @param costCenterReturnOrderDetail 成本中心退货单明细
     * @return 成本中心退货单明细
     */
    @Override
    public List<CostCenterReturnOrderDetail> selectCostCenterReturnOrderDetailList(CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        return costCenterReturnOrderDetailMapper.selectCostCenterReturnOrderDetailList(costCenterReturnOrderDetail);
    }

    /**
     * 新增成本中心退货单明细
     *
     * @param costCenterReturnOrderDetail 成本中心退货单明细
     * @return 结果
     */
    @Override
    public int insertCostCenterReturnOrderDetail(CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        costCenterReturnOrderDetail.setCreateTime(DateUtils.getNowDate());
        return costCenterReturnOrderDetailMapper.insertCostCenterReturnOrderDetail(costCenterReturnOrderDetail);
    }

    /**
     * 修改成本中心退货单明细
     *
     * @param costCenterReturnOrderDetail 成本中心退货单明细
     * @return 结果
     */
    @Override
    public int updateCostCenterReturnOrderDetail(CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        costCenterReturnOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return costCenterReturnOrderDetailMapper.updateCostCenterReturnOrderDetail(costCenterReturnOrderDetail);
    }

    /**
     * 批量删除成本中心退货单明细
     *
     * @param ids 需要删除的成本中心退货单明细主键
     * @return 结果
     */
    @Override
    public int deleteCostCenterReturnOrderDetailByIds(Long[] ids)
    {
        return costCenterReturnOrderDetailMapper.deleteCostCenterReturnOrderDetailByIds(ids);
    }

    /**
     * 删除成本中心退货单明细信息
     *
     * @param id 成本中心退货单明细主键
     * @return 结果
     */
    @Override
    public int deleteCostCenterReturnOrderDetailById(Long id)
    {
        return costCenterReturnOrderDetailMapper.deleteCostCenterReturnOrderDetailById(id);
    }
}
