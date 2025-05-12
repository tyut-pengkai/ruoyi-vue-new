package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.domain.vo.SaleReturnOrderDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 销售发货退货单明细Mapper接口
 *
 * @author fsc
 * @date 2023-03-11
 */
@Repository
public interface SaleReturnOrderDetailMapper
{
    /**
     * 查询销售发货退货单明细
     *
     * @param id 销售发货退货单明细主键
     * @return 销售发货退货单明细
     */
    public SaleReturnOrderDetail selectSaleReturnOrderDetailById(Long id);

    /**
     * 查询销售发货退货单明细
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
     * 批量新增销售发货退货单明细
     *
     * @param list 销售发货退货单明细
     * @return 结果
     */
    int insertSaleReturnOrderDetailList(List<SaleReturnOrderDetail> list);

    /**
     * 修改销售发货退货单明细
     *
     * @param saleReturnOrderDetail 销售发货退货单明细
     * @return 结果
     */
    public int updateSaleReturnOrderDetail(SaleReturnOrderDetail saleReturnOrderDetail);

    /**
     * 删除销售发货退货单明细
     *
     * @param id 销售发货退货单明细主键
     * @return 结果
     */
    public int deleteSaleReturnOrderDetailById(Long id);

    /**
     * 批量删除销售发货退货单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSaleReturnOrderDetailByIds(Long[] ids);

    /**
     * 删除销售发货退货单明细
     *
     * @param returnOrderNo 单号
     * @return 结果
     */
    int deleteByReturnOrderNo(String returnOrderNo);

    /**
     * 查询未完成的任务数量
     * @param orderNo 退货单据号
     * @param taskType 退货任务类型
     * @return 数量
     */
    public int getBeContinuedNumber(@Param("orderNo") String orderNo, @Param("taskType") String taskType);
}
