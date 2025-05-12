package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.vo.SaleReturnOrderDetailVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.mapper.SaleReturnOrderDetailMapper;
import com.easycode.cloud.service.ISaleReturnOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 销售发货退货单明细Service业务层处理
 *
 * @author fsc
 * @date 2023-03-11
 */
@Service
public class SaleReturnOrderDetailServiceImpl implements ISaleReturnOrderDetailService
{
    @Autowired
    private SaleReturnOrderDetailMapper saleReturnOrderDetailMapper;

    /**
     * 查询销售发货退货单明细
     *
     * @param id 销售发货退货单明细主键
     * @return 销售发货退货单明细
     */
    @Override
    public SaleReturnOrderDetail selectSaleReturnOrderDetailById(Long id)
    {
        return saleReturnOrderDetailMapper.selectSaleReturnOrderDetailById(id);
    }

    /**
     * 查询销售发货退货单明细
     *
     * @param id 销售发货退货单明细主键
     * @return 销售发货退货单明细
     */
    @Override
    public SaleReturnOrderDetailVo getPrintInfoList(Long id)
    {
        return saleReturnOrderDetailMapper.getPrintInfoList(id);
    }

    /**
     * 查询销售发货退货单明细列表
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 销售发货退货单明细
     */
    @Override
    public List<SaleReturnOrderDetail> selectSaleReturnOrderDetailList(SaleReturnOrderDetail saleReturnOrderDetail)
    {
        return saleReturnOrderDetailMapper.selectSaleReturnOrderDetailList(saleReturnOrderDetail);
    }

    /**
     * 新增销售发货退货单明细
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 结果
     */
    @Override
    public int insertSaleReturnOrderDetail(SaleReturnOrderDetail saleReturnOrderDetail)
    {
        saleReturnOrderDetail.setCreateTime(DateUtils.getNowDate());
        return saleReturnOrderDetailMapper.insertSaleReturnOrderDetail(saleReturnOrderDetail);
    }

    /**
     * 修改销售发货退货单明细
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 结果
     */
    @Override
    public int updateSaleReturnOrderDetail(SaleReturnOrderDetail saleReturnOrderDetail)
    {
        saleReturnOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return saleReturnOrderDetailMapper.updateSaleReturnOrderDetail(saleReturnOrderDetail);
    }

    /**
     * 批量删除销售发货退货单明细
     *
     * @param ids 需要删除的销售发货退货单明细主键
     * @return 结果
     */
    @Override
    public int deleteSaleReturnOrderDetailByIds(Long[] ids)
    {
        return saleReturnOrderDetailMapper.deleteSaleReturnOrderDetailByIds(ids);
    }

    /**
     * 删除销售发货退货单明细信息
     *
     * @param id 销售发货退货单明细主键
     * @return 结果
     */
    @Override
    public int deleteSaleReturnOrderDetailById(Long id)
    {
        return saleReturnOrderDetailMapper.deleteSaleReturnOrderDetailById(id);
    }
}
