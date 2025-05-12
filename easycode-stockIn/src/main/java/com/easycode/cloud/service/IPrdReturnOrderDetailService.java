package com.easycode.cloud.service;

import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.weifu.cloud.domain.vo.PrintInfoVo;

import java.util.List;

/**
 * 生产订单发货退货单明细Service接口
 *
 * @author bcp
 * @date 2023-03-11
 */
public interface IPrdReturnOrderDetailService
{
    /**
     * 查询生产订单发货退货单明细
     *
     * @param id 生产订单发货退货单明细主键
     * @return 生产订单发货退货单明细
     */
    public PrdReturnOrderDetail selectPrdReturnOrderDetailById(Long id);

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 生产订单发货退货单明细集合
     */
    public List<PrdReturnOrderDetail> selectPrdReturnOrderDetailList(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 退料单物料标签打印
     *
     * @param printInfoVo 生产订单发货退货单明细
     * @return 生产订单发货退货单明细集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfoVo);

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 生产订单发货退货单明细集合
     */
    public List<PrdReturnOrderDetail> getPrintInfoByProductNo(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 新增生产订单发货退货单明细
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 结果
     */
    public int insertPrdReturnOrderDetail(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 修改生产订单发货退货单明细
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 结果
     */
    public int updatePrdReturnOrderDetail(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 批量删除生产订单发货退货单明细
     *
     * @param ids 需要删除的生产订单发货退货单明细主键集合
     * @return 结果
     */
    public int deletePrdReturnOrderDetailByIds(Long[] ids);

    /**
     * 删除生产订单发货退货单明细信息
     *
     * @param id 生产订单发货退货单明细主键
     * @return 结果
     */
    public int deletePrdReturnOrderDetailById(Long id);
}
