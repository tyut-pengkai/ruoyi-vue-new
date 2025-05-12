package com.easycode.cloud.service;

import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.domain.vo.SaleReturnOrderDetailVo;

import java.util.List;

/**
 * 销售发货退货单明细Service接口
 *
 * @author fsc
 * @date 2023-03-11
 */
public interface ISaleReturnOrderDetailService
{
    /**
     * 查询销售发货退货单明细
     *
     * @param id 销售发货退货单明细主键
     * @return 销售发货退货单明细
     */
    public SaleReturnOrderDetail selectSaleReturnOrderDetailById(Long id);

    /**
     * 打印信息
     *
     * @param id 销售发货退货单明细主键
     * @return 销售发货退货单明细
     */
    public SaleReturnOrderDetailVo getPrintInfoList(Long id);

    /**
     * 查询销售发货退货单明细列表
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 销售发货退货单明细集合
     */
    public List<SaleReturnOrderDetail> selectSaleReturnOrderDetailList(SaleReturnOrderDetail saleReturnOrderDetail);

    /**
     * 新增销售发货退货单明细
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 结果
     */
    public int insertSaleReturnOrderDetail(SaleReturnOrderDetail saleReturnOrderDetail);

    /**
     * 修改销售发货退货单明细
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 结果
     */
    public int updateSaleReturnOrderDetail(SaleReturnOrderDetail saleReturnOrderDetail);

    /**
     * 批量删除销售发货退货单明细
     *
     * @param ids 需要删除的销售发货退货单明细主键集合
     * @return 结果
     */
    public int deleteSaleReturnOrderDetailByIds(Long[] ids);

    /**
     * 删除销售发货退货单明细信息
     *
     * @param id 销售发货退货单明细主键
     * @return 结果
     */
    public int deleteSaleReturnOrderDetailById(Long id);
}
